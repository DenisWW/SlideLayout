#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_rainbell_simple_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "点击跳转到title";
    return env->NewStringUTF(hello.c_str());
}
