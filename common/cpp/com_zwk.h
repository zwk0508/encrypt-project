#pragma once
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_zwk_Encryptor */

#ifndef _Included_com_zwk_Encryptor
#define _Included_com_zwk_Encryptor
#ifdef __cplusplus
extern "C" {
#endif
	/*
	 * Class:     com_zwk_Encryptor
	 * Method:    encrypt
	 * Signature: ([B)[B
	 */
	JNIEXPORT jbyteArray JNICALL Java_com_zwk_Encryptor_encrypt
	(JNIEnv*, jclass, jbyteArray);
/*
 * Class:     com_zwk_ClassDecryptor
 * Method:    defineClass
 * Signature: (Ljava/lang/String;Ljava/lang/ClassLoader;[BII)Ljava/lang/Class;
 */
	JNIEXPORT jclass JNICALL Java_com_zwk_ClassDecryptor_defineClass
	(JNIEnv*, jclass, jstring, jobject, jbyteArray, jint, jint);
#ifdef __cplusplus
}
#endif
#endif
