package com.yjt.wallet.core.ecc;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import javax.annotation.Nullable;

/**
 * ECC加密过程：
 * K = k * G， 大K是公钥，小k是私钥；
 * 把明文编码成曲线上的点M；
 * 生成一个随机数r；
 * 计算密文C1=M + r*K, C2 = r*G，其中大K是公钥；
 * 对方收到密文后，可以计算C1 - kC2 = M，其中小k是私钥；
 * 攻击者得到C1、 C2，公钥K以及基点G，没有私钥是无法计算出M的。
 * 
 * 1.生成随机私钥
 * 2.椭圆曲线算公钥
 * 3.计算公钥的SHA-256哈希值
 * 4.计算 RIPEMD-160哈希值
 * 5.加入地址版本号（比特币主网版本号“0x00”）
 * 6.计算SHA-256哈希值
 * 7.取6.结果的前4个字节（8位十六进制）
 * 8.把这4个字节加在第五步的结果后面
 * 9.用Base58编码变换一下地址
 * https://www.zhihu.com/question/22399196
 */
public class ECKeyPair {

    private BigInteger privateKey;
    private byte[] publicKey;

    private ECKeyPair(@Nullable BigInteger privateKey, @Nullable byte[] publicKey, boolean compressed) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        if (privateKey == null) {
            throw new IllegalArgumentException("ECKey requires at least private or public key");
        }
        this.privateKey = privateKey;
        if (publicKey != null) {
            this.publicKey = publicKey;
        } else {
            this.publicKey = createPublicKey(privateKey, compressed);
        }
    }

    public String getPrivateKey() {
        return Hex.toHexString(privateKey.toByteArray());
    }

    public String getPublicKey() {
        return Hex.toHexString(publicKey);
    }

    private static byte[] createPublicKey(BigInteger privateKey, boolean compressed) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (privateKey != null) {
            X9ECParameters x9ECParameters = SECNamedCurves.getByName("secp256k1");
            ECDomainParameters ecDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH());
            ECPoint ecPoint = ecDomainParameters.getG().multiply(privateKey);
            return new ECPoint.Fp(ecDomainParameters.getCurve(), ecPoint.getX(), ecPoint.getY(), compressed).getEncoded();
        }
        return null;
    }

    public static ECKeyPair createECKeyPair(boolean compressed) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        //Add bouncy castle as key pair gen provider
//        Security.addProvider(new BouncyCastleProvider());
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        //Generate key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        //Convert KeyPair to ECKeyPair, to store keys as BigIntegers
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new ECKeyPair(((BCECPrivateKey) keyPair.getPrivate()).getD(), ((BCECPublicKey) keyPair.getPublic()).getQ().getEncoded(compressed), compressed);
    }

    public static ECKeyPair createECKeyPair(BigInteger privateKey, boolean compressed) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return new ECKeyPair(privateKey, null, compressed);
    }
}
