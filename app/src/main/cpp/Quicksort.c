#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <android/log.h>

#define  LOG_TAG    "your-log-tag"
#define  LOGD(...)  __android_log_prjint(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

void quicksort(jint *a, jint x, jint y);

jint partition(jint *a, jint x, jint y);

void prjint_array(jint a[], jint n);

void swap(jint *a, jint *b);

JNIEXPORT void JNICALL
Java_com_example_bill_androidredblacktree_QuicksortBenchmarks_QuicksortC(
        JNIEnv *env,
        jobject this,
        jint jn) {

    //jint n = jn;
    jint a[jn];
    jint i, j, z;


    for (i = 0; i < jn; i++) {
        a[i] = i;
    }

    for (j = jn - 1; j > 0; j--) {
        z = rand() % (j + 1);
        swap(&a[j], &a[z]);
    }
    //prjint_array(a,n);
    quicksort(a, 0, jn - 1);
    //prjint_array(a,n);


}

JNIEXPORT void JNICALL
Java_com_example_bill_androidredblacktree_QuicksortBenchmarks_QuicksortCPassArray(
        JNIEnv *env,
        jobject this,
        jintArray arr) {

    jint *c_array = (*env)->GetIntArrayElements(env, arr, 0);
    jint i = 0;

    jint n = (*env)->GetArrayLength(env, arr);
    // for (i = 0; i < n; i++) {
    //     LOGD("%d", c_array[i]);
    // }


    quicksort(c_array, 0, n - 1);


    //jintArray result = (*env)->NewIntArray(env, n);
    //(*env)->SetIntArrayRegion(env, result, 0, n, c_array);
    (*env)->ReleaseIntArrayElements(env, arr, c_array, 0);
    // return result;


}


void quicksort(jint *a, jint x, jint y) {
    jint q;
    if (x < y) {
        q = partition(a, x, y);
        quicksort(a, x, q - 1);
        quicksort(a, q + 1, y);
    }
}


jint partition(jint *a, jint x, jint y) {
    jint temp = *(a + y);
    jint i = x - 1;
    jint j;
    for (j = x; j <= y - 1; j++) {
        if (*(a + j) <= temp) {
            i++;
            jint temp1 = *(a + i);
            *(a + i) = *(a + j);
            *(a + j) = temp1;
            //swap(&a[i], &a[j]);
        }
    }
    jint temp2 = *(a + i + 1);
    *(a + i + 1) = *(a + y);
    *(a + y) = temp2;
    //swap(&a[i + 1], &a[y]);
    return (i + 1);
}


void swap(jint *a, jint *b) {
    jint temp = *a;
    *a = *b;
    *b = temp;
}
