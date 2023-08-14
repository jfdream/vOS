#include <jni.h>
#include <android/log.h>
#include <future>
#include "art.h"

static JavaVM *_vm;

extern "C"
JNIEXPORT jint JNICALL
Java_me_weishu_reflection_Reflection_unsealNative(JNIEnv *env, jclass type, jint targetSdkVersion) {
    return unseal(env, targetSdkVersion);
}


JNIEnv *attachCurrentThread() {
    JNIEnv *env;
    int res = _vm->AttachCurrentThread(&env, nullptr);
    __android_log_print(ANDROID_LOG_DEBUG, "native", "Found attached %d", res);
    return env;
}

void detachCurrentThread() {
    _vm->DetachCurrentThread();
}


jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    // save JavaVM
    _vm = vm;
    if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}


static jobject getDeclaredField_internal(jobject object, jstring field_name) {

    JNIEnv *env = attachCurrentThread();//这里是重点

    jclass clazz_class = env->GetObjectClass(object);
    jmethodID methodId = env->GetMethodID(clazz_class, "getDeclaredField",
                                          "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
    jobject res = env->CallObjectMethod(object, methodId, field_name);
    jobject global_res = nullptr;
    if (res != nullptr) {
        global_res = env->NewGlobalRef(res);
    }

    detachCurrentThread();
    return global_res;
}


extern "C"
JNIEXPORT jobject JNICALL Java_me_weishu_reflection_BootstrapClass_getDeclaredField(JNIEnv *env,
                                                                                        jclass t,
                                                                                        jclass clz,
                                                                                        jstring fieldName) {
    auto global_clazz = env->NewGlobalRef(clz);
    jstring global_method_name = static_cast<jstring>(env->NewGlobalRef(fieldName)) ;
//  通过 async 来创建线程，因为 async 可以返回 future 来把异步同步化，线程内执行 getDeclaredField_internal
    auto future = std::async(&getDeclaredField_internal, global_clazz, global_method_name);
    auto result = future.get();

    env->DeleteGlobalRef(global_clazz) ;
    env->DeleteGlobalRef(global_method_name) ;
    return result ;
}


static jobject getDeclaredMethod_internal(jobject clazz, jstring method_name, jobjectArray params) {
    JNIEnv *env = attachCurrentThread();
    jclass clazz_class = env->GetObjectClass(clazz);
    jmethodID get_declared_method_id = env->GetMethodID(clazz_class, "getDeclaredMethod",
                                                        "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
    jobject res = env->CallObjectMethod(clazz, get_declared_method_id, method_name, params);

    jobject globalRes = env->NewGlobalRef(res);
    env->DeleteGlobalRef(clazz) ;
    env->DeleteGlobalRef(method_name) ;

    detachCurrentThread() ;

    return globalRes ;
}


extern "C"
JNIEXPORT jobject JNICALL Java_me_weishu_reflection_BootstrapClass_getDeclaredMethod(JNIEnv *env, jclass clazz, jclass  c, jstring method_name, jobjectArray params) {
    std::thread test_thread ;
    jobject global_clazz = env->NewGlobalRef(c) ;
    jstring global_method_name = static_cast<jstring>(env->NewGlobalRef(method_name)) ;
    jobjectArray global_params = nullptr;
    int arg_length = env->GetArrayLength(params);
    if (params != nullptr) {
        for (int i = 0; i < arg_length; i++) {
            jobject element = static_cast<jobject> (env->GetObjectArrayElement(params, i));
            jobject global_element = env->NewGlobalRef(element);
            env->SetObjectArrayElement(params, i, global_element);
        }
        global_params = (jobjectArray) env->NewGlobalRef(params);
    }

    auto future = std::async(&getDeclaredMethod_internal, global_clazz,
                             global_method_name,
                             global_params);
    auto result = future.get();

    if (global_params != nullptr) {
        env->DeleteGlobalRef(global_params) ;
    }
    return result ;
}
