package com.zwk;

/**
 * @author zwk
 * @version 1.0
 * @date 2023/2/24 11:06
 */

public class ClassDecryptor {
    static {
        System.loadLibrary("libclassencrypt");
    }
    public static native Class<?> defineClass(String className,ClassLoader loader, byte[] bytes, int start, int length);
}
