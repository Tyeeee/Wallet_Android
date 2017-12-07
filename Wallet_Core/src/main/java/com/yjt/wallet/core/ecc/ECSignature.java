package com.yjt.wallet.core.ecc;

import com.google.common.base.Preconditions;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.core.crypto.ECKey;
import com.yjt.wallet.core.crypto.KeyCrypterException;
import com.yjt.wallet.core.utils.Sha256Hash;
import com.yjt.wallet.core.utils.Utils;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.asn1.x9.X9IntegerConverter;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;
import org.spongycastle.math.ec.ECAlgorithms;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Arrays;

public class ECSignature {

    private static final ECDomainParameters ecDomainParameters;
    private BigInteger r;
    private BigInteger s;

    static {
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        ecDomainParameters = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
    }

    private ECSignature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getS() {
        return s;
    }

    public static ECSignature generateSignature(BigInteger privateKey, Sha256Hash sha256Hash, boolean isRandom) {
        if (privateKey != null) {
            ECDSASigner ecdsaSigner;
            LogUtil.getInstance().print(String.format("Signature random: %s", isRandom));
            if (isRandom) {
                ecdsaSigner = new ECDSASigner();
            } else {
                ecdsaSigner = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
            }
            ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey, ecDomainParameters);
            ecdsaSigner.init(true, ecPrivateKeyParameters);
            BigInteger[] bigIntegers = ecdsaSigner.generateSignature(sha256Hash.getBytes());
            final ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(bigIntegers[0], bigIntegers[1]);
            ecdsaSignature.ensureCanonical();
            return new ECSignature(ecdsaSignature.r, ecdsaSignature.s);
        } else {
            throw new KeyCrypterException("This ECKey does not have the private key necessary for signing.");
        }
    }

    public static boolean verifySignature(Sha256Hash sha256Hash, BigInteger r, BigInteger s, byte[] pubicKey) {
        try {
            ECDSASigner ecdsaSigner = new ECDSASigner();
            ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(ecDomainParameters.getCurve().decodePoint(pubicKey), ecDomainParameters);
            ecdsaSigner.init(false, ecPublicKeyParameters);
            return ecdsaSigner.verifySignature(sha256Hash.getBytes(), r, s);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateSignature(BigInteger privateKey, byte[] publicKey, String data, boolean compressed, boolean isRandom) throws KeyCrypterException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        if (privateKey != null) {
            ECDSASigner ecdsaSigner;
            LogUtil.getInstance().print(String.format("Signature random: %s", isRandom));
            if (isRandom) {
                ecdsaSigner = new ECDSASigner();
            } else {
                ecdsaSigner = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
            }
            ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey, ecDomainParameters);
            ecdsaSigner.init(true, ecPrivateKeyParameters);
            if(isRandom){
                ECDomainParameters ecDomainParameters = ecPrivateKeyParameters.getParameters();
                LogUtil.getInstance().print(String.format("Random Parameters[N]: %s", ecDomainParameters.getN()));
            }else{
                ECDomainParameters ecDomainParameters = ecPrivateKeyParameters.getParameters();
                LogUtil.getInstance().print(String.format("HMac Parameters[N]: %s", ecDomainParameters.getN()));
                LogUtil.getInstance().print(String.format("HMac Parameters[D]: %s", ecPrivateKeyParameters.getD()));
            }
//            Sha256Hash sha256Hash = Sha256Hash.create(Utils.formatMessageForSigning(data));
            Sha256Hash sha256Hash = Sha256Hash.create(data.getBytes());
            LogUtil.getInstance().print(String.format("Signature Data: %s", data));
            LogUtil.getInstance().print(String.format("Signature Hashed Data: %s", Hex.toHexString(sha256Hash.getBytes())));
            BigInteger[] bigIntegers = ecdsaSigner.generateSignature(sha256Hash.getBytes());
            LogUtil.getInstance().print(String.format("Signature[r]: %s", Hex.toHexString(bigIntegers[0].toByteArray())));
            LogUtil.getInstance().print(String.format("Signature[s]: %s", Hex.toHexString(bigIntegers[1].toByteArray())));
            final ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(bigIntegers[0], bigIntegers[1]);
            ecdsaSignature.ensureCanonical();
            int recoverId = -1;
            for (int i = 0; i < 4; i++) {
                ECKeyPair ecKeyPair = recoverFromSignature(i, ecdsaSignature, sha256Hash, compressed);
                if(ecKeyPair != null){
                    LogUtil.getInstance().print(String.format("PublicKey[%s]: %s", i, Hex.toHexString(ecKeyPair.getPublicKey())));
                    if (Arrays.equals(ecKeyPair.getPublicKey(), publicKey)) {
                        recoverId = i;
                        break;
                    }
                }
            }
            LogUtil.getInstance().print(String.format("Signature RecoverId: %s", recoverId));
            if (recoverId != -1) {
                int header = recoverId + 27 + (compressed ? 4 : 0);
                // 1 header + 32 bytes for R + 32 bytes for S
                byte[] signature = new byte[65];
                signature[0] = (byte) header;
                System.arraycopy(Utils.bigIntegerToBytes(ecdsaSignature.r, 32), 0, signature, 1, 32);
                System.arraycopy(Utils.bigIntegerToBytes(ecdsaSignature.s, 32), 0, signature, 33, 32);
//                return new String(Base64.encode(sigData), Charset.forName(Charsets.UTF_8.name()));
                return Hex.toHexString(signature);
            } else {
                throw new RuntimeException("Could not construct a recoverable key. This should never happen.");
            }
        } else {
            throw new KeyCrypterException("This ECKey does not have the private key necessary for signing.");
        }
    }

    private static ECKeyPair generateSignature(String data, String signature) throws SignatureException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] bytes;
        bytes = Hex.decode(signature);
        if (bytes.length < 65) {
            throw new SignatureException("Signature truncated, expected 65 bytes and got " + bytes.length);
        }
        int header = bytes[0] & 0xFF;
        if (header < 27 || header > 34) {
            throw new SignatureException("Header byte out of range: " + header);
        }
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(bytes, 1, 33));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(bytes, 33, 65));
        ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(r, s);
//        Sha256Hash sha256Hash = Sha256Hash.createDouble(Utils.formatMessageForSigning(data));
        Sha256Hash sha256Hash = Sha256Hash.create(data.getBytes());
        boolean compressed = false;
        if (header >= 31) {
            compressed = true;
            header -= 4;
        }
        int recId = header - 27;
        ECKeyPair ecKeyPair = recoverFromSignature(recId, ecdsaSignature, sha256Hash, compressed);
        if (ecKeyPair == null) {
            throw new SignatureException("Could not recover public key from signature");
        }
        return ecKeyPair;
    }

    public static boolean verifySignature(String data, String signature, byte[] publicKey) throws SignatureException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = generateSignature(data, signature);
        if (Arrays.equals(ecKeyPair.getPublicKey(), publicKey)) {
            return true;
        } else {
            return false;
        }
    }

    private static ECKeyPair recoverFromSignature(int recoverId, ECKey.ECDSASignature ecdsaSignature, Sha256Hash sha256Hash, boolean compressed) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Preconditions.checkArgument(recoverId >= 0, "recId must be positive");
        Preconditions.checkArgument(ecdsaSignature.r.compareTo(BigInteger.ZERO) >= 0, "r must be positive");
        Preconditions.checkArgument(ecdsaSignature.s.compareTo(BigInteger.ZERO) >= 0, "s must be positive");
        Preconditions.checkNotNull(sha256Hash);
        //1.1 x = (n * i) + r
        BigInteger n = ecDomainParameters.getN();
        BigInteger i = BigInteger.valueOf((long) recoverId / 2);
        BigInteger x = ecdsaSignature.r.add(i.multiply(n));
        //1.2&1.3convert 02<Rx> to point R. (step 1.2 and 1.3)
        ECCurve.Fp fp = (ECCurve.Fp) ecDomainParameters.getCurve();
        BigInteger prime = fp.getQ();
        if (x.compareTo(prime) >= 0) {
            return null;
        } else {
            //1.4 Check n*R is point at infinity
            ECPoint R = decompressPoint(x, (recoverId & 1) == 1);
            if (!R.multiply(n).isInfinity()) {
                return null;
            } else {
                //1.5 calculate e from message using the same algorithm as ecdsa signature calculation.
                BigInteger e = sha256Hash.toBigInteger();
                //1.6 We calculate the two terms sR and eG separately multiplied by the inverse of r (from the signature). We then add them to calculate Q = r^-1(sR-eG)
                BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
                BigInteger rInv = ecdsaSignature.r.modInverse(n);
                BigInteger srInv = rInv.multiply(ecdsaSignature.s).mod(n);
                BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
                ECPoint.Fp q = (ECPoint.Fp) ECAlgorithms.sumOfTwoMultiplies(ecDomainParameters.getG(), eInvrInv, R, srInv);
                if (compressed) {
                    q = new ECPoint.Fp(fp, q.getX(), q.getY(), true);
                }
                return new ECKeyPair(null, q.getEncoded());
            }
        }
    }

    private static ECPoint decompressPoint(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9IntegerConverter = new X9IntegerConverter();
        byte[] compressEncode = x9IntegerConverter.integerToBytes(xBN, 1 + x9IntegerConverter.getByteLength(ecDomainParameters.getCurve()));
        compressEncode[0] = (byte) (yBit ? 0x03 : 0x02);
        return ecDomainParameters.getCurve().decodePoint(compressEncode);
    }
}
