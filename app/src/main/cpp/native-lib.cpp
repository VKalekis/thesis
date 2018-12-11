#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_bill_androidredblacktree_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */,
        jstring temp) {
    std::string hello = "Hello from C++";
    const char *nativeString = env->GetStringUTFChars(temp , 0);
    return env->NewStringUTF(nativeString);
}


