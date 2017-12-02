package com.yjt.wallet.ecc;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Test2 {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException {
        //Get secp256k1 pair - which we can use for both addresses
        ECKeyPair keyPair = ECKeyPair.createECKeyPair();
        System.out.println("Private key: " + keyPair.getPrivateKey() + " - " + keyPair.getPrivateKey().length());
        System.out.println("Public key: " + keyPair.getPublicKey() + " - " + keyPair.getPublicKey().length());
        //Calculate Bitcoin Address
        BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey());
        //Calculate Ethereum Address
        EthAddressGen.genEthereumAddress(keyPair.getPublicKey());
    }
}
