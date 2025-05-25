package com.example.busik.other;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.InputStream;
import java.security.spec.ECField;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static byte[] key;
    private static byte[] iv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void loadConfig(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("properties.keys");
            Properties props = new Properties();
            props.load(inputStream);

            // Декодирование из Base64
            key = Base64.getDecoder().decode(props.getProperty("aes.key"));
            iv = Base64.getDecoder().decode(props.getProperty("aes.iv"));

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }catch (Exception e){
            Log.e("DDD1", "Encrypt error", e);
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        }catch (Exception e){
            Log.e("DDD1", "Decrypt error", e);
            return null;
        }
    }
}
