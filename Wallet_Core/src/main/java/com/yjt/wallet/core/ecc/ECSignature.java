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


    private static ECSignature eCSignature;
    private static ECDomainParameters ecDomainParameters;

    static {
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        ecDomainParameters = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
    }

    private ECSignature() {
        // cannot be instantiated
    }

    public static synchronized ECSignature getInstance() {
        if (eCSignature == null) {
            eCSignature = new ECSignature();
        }
        return eCSignature;
    }

    public static void releaseInstance() {
        if (eCSignature != null) {
            eCSignature = null;
        }
        ecDomainParameters = null;
    }

    public ECKey.ECDSASignature generateSignature(BigInteger privateKey, String data, boolean isRandom) {
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
//            Sha256Hash sha256Hash = Sha256Hash.create(Utils.formatMessageForSigning(data));
            LogUtil.getInstance().print(String.format("Signature Data: %s", data));
            LogUtil.getInstance().print(String.format("Signature Hashed Data: %s", Hex.toHexString(Sha256Hash.create(data.getBytes()).getBytes())));
            BigInteger[] bigIntegers = ecdsaSigner.generateSignature(Sha256Hash.create(data.getBytes()).getBytes());
            LogUtil.getInstance().print(String.format("Signature[r]: %s", Hex.toHexString(bigIntegers[0].toByteArray())));
            LogUtil.getInstance().print(String.format("Signature[s]: %s", Hex.toHexString(bigIntegers[1].toByteArray())));
            ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(bigIntegers[0], bigIntegers[1]);
            ecdsaSignature.ensureCanonical();
            return ecdsaSignature;
        } else {
            throw new KeyCrypterException("This ECKey does not have the private key necessary for signing.");
        }
    }

    public boolean verifySignature(Sha256Hash sha256Hash, BigInteger r, BigInteger s, byte[] pubicKey) {
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

    public byte[] generateSignature(BigInteger privateKey, byte[] publicKey, String data, boolean compressed, boolean isRandom) throws KeyCrypterException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        if (privateKey != null) {
            ECKey.ECDSASignature ecdsaSignature = generateSignature(privateKey, data, isRandom);
            int recoverId = -1;
            for (int i = 0; i < 4; i++) {
                ECKeyPair ecKeyPair = recoverFromSignature(i, ecdsaSignature, Sha256Hash.create(data.getBytes()), compressed);
                if (ecKeyPair != null) {
                    LogUtil.getInstance().print(String.format("Public Key[%s]: %s", i + 1, Hex.toHexString(ecKeyPair.getPublicKey())));
                    LogUtil.getInstance().print(String.format("Public Key: %s", Hex.toHexString(publicKey)));
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
                return signature;
            } else {
                throw new RuntimeException("Could not construct a recoverable key. This should never happen.");
            }
        } else {
            throw new KeyCrypterException("This ECKey does not have the private key necessary for signing.");
        }
    }

    private ECKeyPair generateSignature(String data, String signature) throws SignatureException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        LogUtil.getInstance().print(String.format("Signature: %s", signature));
        byte[] bytes = Hex.decode(signature);
        if (bytes.length < 65) {
            throw new SignatureException("Signature truncated, expected 65 bytes and got " + bytes.length);
        }
        int header = bytes[0] & 0xFF;
        LogUtil.getInstance().print(String.format("Header: %s", header));
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
        int recoverId = header - 27;
        ECKeyPair ecKeyPair = recoverFromSignature(recoverId, ecdsaSignature, sha256Hash, compressed);
        if (ecKeyPair == null) {
            throw new SignatureException("Could not recover public key from signature");
        }
        return ecKeyPair;
    }

    public boolean verifySignature(String data, String signature, byte[] publicKey) throws SignatureException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = generateSignature(data, signature);
        return ecKeyPair != null && Arrays.equals(ecKeyPair.getPublicKey(), publicKey);
    }

    private ECKeyPair recoverFromSignature(int recoverId, ECKey.ECDSASignature ecdsaSignature, Sha256Hash sha256Hash, boolean compressed) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Preconditions.checkArgument(recoverId >= 0, "recoverId must be positive");
        Preconditions.checkArgument(ecdsaSignature.r.compareTo(BigInteger.ZERO) >= 0, "r must be positive");
        Preconditions.checkArgument(ecdsaSignature.s.compareTo(BigInteger.ZERO) >= 0, "s must be positive");
        Preconditions.checkNotNull(sha256Hash);
        //1.1 x = (n * i) + r
        BigInteger n = ecDomainParameters.getN();
        LogUtil.getInstance().print(String.format("Parameters[N]: %s", n));
        BigInteger i = BigInteger.valueOf((long) recoverId / 2L);
        LogUtil.getInstance().print(String.format("Parameters[i]: %s", i));
        BigInteger x = ecdsaSignature.r.add(i.multiply(n));
        LogUtil.getInstance().print(String.format("Parameters[x]: %s", x));
        //1.2&1.3convert 02<Rx> to point R. (step 1.2 and 1.3)
        ECCurve.Fp fp = (ECCurve.Fp) ecDomainParameters.getCurve();
        BigInteger prime = fp.getQ();
        LogUtil.getInstance().print(String.format("Parameters[Q]: %s", prime.toString(16)));
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
                LogUtil.getInstance().print(String.format("Parameters[e]: %s", e.toString(16)));
                //1.6 We calculate the two terms sR and eG separately multiplied by the inverse of r (from the signature). We then add them to calculate Q = r^-1(sR-eG)
                BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
                LogUtil.getInstance().print(String.format("Parameters[eInv]: %s", eInv.toString(16)));
                BigInteger rInv = ecdsaSignature.r.modInverse(n);
                LogUtil.getInstance().print(String.format("Parameters[rInv]: %s", rInv.toString(16)));
                BigInteger srInv = rInv.multiply(ecdsaSignature.s).mod(n);
                LogUtil.getInstance().print(String.format("Parameters[srInv]: %s", srInv.toString(16)));
                BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
                LogUtil.getInstance().print(String.format("Parameters[eInvrInv]: %s", eInvrInv.toString(16)));
                ECPoint.Fp q = (ECPoint.Fp) ECAlgorithms.sumOfTwoMultiplies(ecDomainParameters.getG(), eInvrInv, R, srInv);
                LogUtil.getInstance().print(String.format("ECPoint[X]: %s", Hex.toHexString(q.getX().getEncoded())));
                LogUtil.getInstance().print(String.format("ECPoint[Y]: %s", Hex.toHexString(q.getY().getEncoded())));
                LogUtil.getInstance().print(String.format("compressed: %s", compressed));
                byte[] publicKey = new ECPoint.Fp(fp, q.getX(), q.getY(), compressed).getEncoded();
                LogUtil.getInstance().print(String.format("Public Key: %s", Hex.toHexString(publicKey)));
                return new ECKeyPair(null, publicKey, compressed);
            }
        }
    }

    private ECPoint decompressPoint(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9IntegerConverter = new X9IntegerConverter();
        byte[] compressEncode = x9IntegerConverter.integerToBytes(xBN, 1 + x9IntegerConverter.getByteLength(ecDomainParameters.getCurve()));
        LogUtil.getInstance().print(String.format("Parameters[xBN]: %s", xBN));
        LogUtil.getInstance().print(String.format("Parameters[yBit]: %s", yBit));
        compressEncode[0] = (byte) (yBit ? 0x03 : 0x02);
        LogUtil.getInstance().print(String.format("Compress Encode: %s", Hex.toHexString(compressEncode)));
        return ecDomainParameters.getCurve().decodePoint(compressEncode);
    }
}
