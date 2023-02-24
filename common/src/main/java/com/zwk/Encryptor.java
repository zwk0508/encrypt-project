package com.zwk;

/**
 * @author zwk
 * @version 1.0
 * @date 2023/2/24 11:04
 */

public class Encryptor {
    static {
        System.loadLibrary("libclassencrypt");
    }
    public static native byte[] encrypt(byte[] bytes);
}
