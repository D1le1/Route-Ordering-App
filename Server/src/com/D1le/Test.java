package com.D1le;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        System.out.println(hashPassword("111"));
    }

    public static String hashPassword(String password) {
        if(!password.equals("")) {
            try {
                // Получаем экземпляр класса MessageDigest с алгоритмом SHA-256
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

                // Преобразуем пароль в байтовый массив
                byte[] passwordBytes = password.getBytes();

                // Вычисляем хэш-код пароля
                byte[] hashBytes = messageDigest.digest(passwordBytes);

                // Преобразуем хэш-код в шестнадцатеричную строку
                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : hashBytes) {
                    stringBuilder.append(String.format("%02x", b));
                }
                return stringBuilder.toString();
            } catch (NoSuchAlgorithmException e) {
                // Обработка исключения
                e.printStackTrace();
                return null;
            }
        }else
        {
            return " ";
        }
    }
}