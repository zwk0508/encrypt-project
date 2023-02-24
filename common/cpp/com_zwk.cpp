#include "com_zwk.h"
JNIEXPORT jbyteArray JNICALL Java_com_zwk_Encryptor_encrypt
(JNIEnv* env, jclass c, jbyteArray bs)
{
	long size = env->GetArrayLength(bs);
	signed char* buf = env->GetByteArrayElements(bs, NULL);
	long mid = size / 2;
	buf[0] = 0x19;
	buf[1] = 0x93;
	buf[2] = 0x05;
	buf[3] = 0x08;
	for (int i = 0; i < mid; i++)
	{
		char tmp = buf[i]+1;
		buf[i] = buf[size - 1 - i] -1;
		buf[size - 1 - i] = tmp;
	}
	env->ReleaseByteArrayElements(bs, buf, 0);
	return bs;
}

JNIEXPORT jclass JNICALL Java_com_zwk_ClassDecryptor_defineClass
(JNIEnv* env, jclass c, jstring s,jobject loader, jbyteArray bs, jint start, jint length)
{
	const char * className= env->GetStringUTFChars(s, NULL);
	long size = env->GetArrayLength(bs);
	signed char* buf = env->GetByteArrayElements(bs, NULL);
	long mid = size / 2;
	for (int i = 0; i < mid; i++)
	{
		char tmp = buf[i] + 1;
		buf[i] = buf[size - 1 - i] - 1;
		buf[size - 1 - i] = tmp;
	}
	buf[0] = 0xca;
	buf[1] = 0xfe;
	buf[2] = 0xba;
	buf[3] = 0xbe;
	jclass ret= env->DefineClass(className, loader, buf, length);
	env->ReleaseByteArrayElements(bs, buf, 0);
	return ret;
}