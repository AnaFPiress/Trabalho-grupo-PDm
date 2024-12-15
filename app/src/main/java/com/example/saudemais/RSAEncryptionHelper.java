package com.example.saudemais;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAEncryptionHelper {

    private static final String KEY_ALIAS = "RSA_KEY";
    private static final String PREFS_NAME = "RSA_KEY_PREFS";
    private static final String PUBLIC_KEY_PREF = "PUBLIC_KEY";
    private static final String PRIVATE_KEY_PREF = "PRIVATE_KEY";

    private SharedPreferences sharedPreferences;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSAEncryptionHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadKeys();
    }

    // Carrega as chaves do SharedPreferences ou gera um novo par de chaves se não existirem
    private void loadKeys() {
        try {
            String publicKeyString = sharedPreferences.getString(PUBLIC_KEY_PREF, null);
            String privateKeyString = sharedPreferences.getString(PRIVATE_KEY_PREF, null);

            if (publicKeyString != null && privateKeyString != null) {
                // Converte as chaves armazenadas em String para PublicKey e PrivateKey
                byte[] publicBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
                byte[] privateBytes = Base64.decode(privateKeyString, Base64.DEFAULT);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
                privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
            } else {
                // Gera um novo par de chaves
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair keyPair = keyGen.generateKeyPair();

                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();

                // Armazena as chaves no SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PUBLIC_KEY_PREF, Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT));
                editor.putString(PRIVATE_KEY_PREF, Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT));
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para criptografar uma string usando a chave pública
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT).replace('\n','a').replace('\r','a');
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para descriptografar uma string usando a chave privada
    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            return new String(decryptedBytes, "UTF-8").replace('\n','a').replace('\r','a');
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}