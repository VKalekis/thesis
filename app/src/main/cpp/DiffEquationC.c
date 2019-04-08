#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

jdouble f1(jdouble t, jdouble y);

JNIEXPORT jdoubleArray JNICALL
    Java_com_example_bill_androidredblacktree_DiffEquationBenchmarks_DiffEquationC(
            JNIEnv *env,
            jobject this,
            jint jn) {


        jint i;
        jdouble t = 0.0, h = 5.0 / jn, k1, k2, k3, k4, y[jn];
        y[0]=5.0;
        for (i = 0; i < jn - 1; i++) {
            t += h;
            k1 = h * f1(t, y[i]);
            k2 = h * f1(t + h / 2, y[i] + k1 / 2);
            k3 = h * f1(t + h / 2, y[i] + k2 / 2);
            k4 = h * f1(t + h, y[i] + k3);
            y[i + 1] = y[i] + 1.0 / 6.0 * (k1 + 2 * k2 + 2 * k3 + k4);
        }


        jdoubleArray result = (*env)->NewDoubleArray(env, jn);
        (*env)->SetDoubleArrayRegion(env, result, 0, jn, y);
        return result;

}

jdouble f1(jdouble t, jdouble y) {
    return (cos(t) * y + exp(t));
}