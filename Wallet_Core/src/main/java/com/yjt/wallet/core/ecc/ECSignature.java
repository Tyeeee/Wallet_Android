package com.yjt.wallet.core.ecc;

import com.yjt.wallet.core.crypto.ECKey;
import com.yjt.wallet.core.crypto.KeyCrypterException;
import com.yjt.wallet.core.utils.Sha256Hash;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;

import java.math.BigInteger;

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

    /**
     * 签名过程如下：
     * 1、选择一条椭圆曲线Ep(a,b)，和基点G；
     * 2、选择私有密钥k（k<n，n为G的阶），利用基点G计算公开密钥K=kG；
     * 3、产生一个随机整数r（r<n），计算点R=rG；
     * 4、将原数据和点R的坐标值x,y作为参数，计算SHA1做为hash，即Hash=SHA1(原数据,x,y)；
     * 5、计算s≡r - Hash * k (mod n)
     * 6、r和s做为签名值，如果r和s其中一个为0，重新从第3步开始执行
     *
     * @return
     */
    public static ECSignature createSignature(BigInteger privateKey, Sha256Hash sha256Hash) {
        if (privateKey != null) {
            ECDSASigner ecdsaSigner = new ECDSASigner();
            ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey, ecDomainParameters);
            ecdsaSigner.init(true, ecPrivateKeyParameters);
//            BigInteger[] bigIntegers = ecdsaSigner.generateSignature(sha256Hash.getBytes());
            BigInteger[] bigIntegers = ecdsaSigner.generateSignature(sha256Hash.getBytes());
            final ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(bigIntegers[0], bigIntegers[1]);
            ecdsaSignature.ensureCanonical();
            return new ECSignature(ecdsaSignature.r, ecdsaSignature.s);
        } else {
            throw new KeyCrypterException("This ECKey does not have the private key necessary for signing.");
        }
    }

//    public static String createSignature(BigInteger privateKey, byte[] publicKey, String data, boolean compressed) throws KeyCrypterException {
//        if (privateKey != null) {
//            ECDSASigner ecdsaSigner = new ECDSASigner();
//            ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey, ecDomainParameters);
//            ecdsaSigner.init(true, ecPrivateKeyParameters);
//            Sha256Hash sha256Hash = Sha256Hash.createDouble(formatData(data));
//            BigInteger[] bigIntegers = ecdsaSigner.generateSignature(sha256Hash.getBytes());
//            final ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(bigIntegers[0], bigIntegers[1]);
//            ecdsaSignature.ensureCanonical();
//            int recId = -1;
//            for (int i = 0; i < 4; i++) {
//                ECKey ecKey = recoverFromSignature(i, ecdsaSignature, sha256Hash, compressed);
//                if (ecKey != null && Arrays.equals(ecKey.getPubKey(), publicKey)) {
//                    recId = i;
//                    break;
//                }
//            }
//            if (recId != -1) {
//                int headerByte = recId + 27 + (compressed ? 4 : 0);
//                byte[] bytes = new byte[65];  // 1 header + 32 bytes for R + 32 bytes for S
//                bytes[0] = (byte) headerByte;
//                System.arraycopy(bigIntegerToBytes(ecdsaSignature.r, 32), 0, bytes, 1, 32);
//                System.arraycopy(bigIntegerToBytes(ecdsaSignature.s, 32), 0, bytes, 33, 32);
////                return new String(Base64.encode(sigData), Charset.forName(Charsets.UTF_8.name()));
//                return bytes.toString();
//            } else {
//                throw new RuntimeException("Could not construct a recoverable key. This should never happen.");
//            }
//        } else {
//            throw new KeyCrypterException("This ECKey does not have the private key necessary for signing.");
//        }
//
//    }

    /**
     * 验证过程如下：
     * 1、接受方在收到消息(m)和签名值(r,s)后，进行以下运算
     * 2、计算：sG+H(m)P=(x1,y1), r1≡ x1 mod p。
     * 3、验证等式：r1 ≡ r mod p。
     * 4、如果等式成立，接受签名，否则签名无效。
     *
     * @return
     */
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

//    private static ECKey recoverFromSignature(int recId, ECKey.ECDSASignature ecdsaSignature, Sha256Hash sha256Hash, boolean compressed) {
//        Preconditions.checkArgument(recId >= 0, "recId must be positive");
//        Preconditions.checkArgument(ecdsaSignature.r.compareTo(BigInteger.ZERO) >= 0, "r must be positive");
//        Preconditions.checkArgument(ecdsaSignature.s.compareTo(BigInteger.ZERO) >= 0, "s must be positive");
//        Preconditions.checkNotNull(sha256Hash);
//        BigInteger n = ecDomainParameters.getN();
//        BigInteger i = BigInteger.valueOf((long) recId / 2);
//        BigInteger x = ecdsaSignature.r.add(i.multiply(n));
//        ECCurve.Fp curve = (ECCurve.Fp) ecDomainParameters.getCurve();
//        BigInteger prime = curve.getQ();
//        if (x.compareTo(prime) >= 0) {
//            return null;
//        }
//        X9IntegerConverter x9IntegerConverter = new X9IntegerConverter();
//        byte[] compressEncode = x9IntegerConverter.integerToBytes(x, 1 + x9IntegerConverter.getByteLength(ecDomainParameters.getCurve()));
//        compressEncode[0] = (byte) ((recId & 1) == 1 ? 0x03 : 0x02);
//        ECPoint ecPoint = ecDomainParameters.getCurve().decodePoint(compressEncode);
//        if (!ecPoint.multiply(n).isInfinity()) {
//            return null;
//        }
//        BigInteger e = sha256Hash.toBigInteger();
//        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
//        BigInteger rInv = ecdsaSignature.r.modInverse(n);
//        BigInteger srInv = rInv.multiply(ecdsaSignature.s).mod(n);
//        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
//        ECPoint.Fp q = (ECPoint.Fp) ECAlgorithms.sumOfTwoMultiplies(ecDomainParameters.getG(), eInvrInv, ecPoint, srInv);
//        if (compressed) {
//            q = new ECPoint.Fp(curve, q.getX(), q.getY(), true);
//        }
//        return new ECKey(null, q.getEncoded());
//    }

//    private static byte[] bigIntegerToBytes(BigInteger bigInteger, int length) {
//        if (bigInteger == null) {
//            return null;
//        }
//        byte[] bytes = bigInteger.toByteArray();
//        byte[] temp = new byte[length];
//        int start = (bytes.length == length + 1) ? 1 : 0;
//        System.arraycopy(bytes, start, temp, length - Math.min(bytes.length, length), Math.min(bytes.length, length));
//        return temp;
//    }

//    private static final String BITCOIN_SIGNED_MESSAGE_HEADER = "Bitcoin Signed Message:\n";

//    public static byte[] formatData(String data) {
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bos.write(BITCOIN_SIGNED_MESSAGE_HEADER.getBytes(Charsets.UTF_8).length);
//            bos.write(BITCOIN_SIGNED_MESSAGE_HEADER.getBytes(Charsets.UTF_8));
//            byte[] bytes = data.getBytes(Charsets.UTF_8);
//            VarInt varInt = new VarInt(bytes.length);
//            bos.write(varInt.encode());
//            bos.write(bytes);
//            return bos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
