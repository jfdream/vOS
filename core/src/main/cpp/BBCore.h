//
// Created by Milk on 4/9/21.
//

#ifndef VIRTUALM_VMCORE_H
#define VIRTUALM_VMCORE_H

#include <jni.h>

#define VMCORE_CLASS "top/niunaijun/blackbox/core/NativeCore"

class BBCore {
public:
    static JavaVM *GetJavaVM();
    static int GetApiLevel();
    static int GetCallingUid(JNIEnv *env, int orig);
    static jstring Redirect(JNIEnv *env, jstring path);
    static jobject Redirect(JNIEnv *env, jobject path);
};


#endif //VIRTUALM_VMCORE_H
