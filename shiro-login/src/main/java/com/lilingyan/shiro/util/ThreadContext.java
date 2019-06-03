package com.lilingyan.shiro.util;

import com.lilingyan.shiro.mgt.SecurityManager;
import com.lilingyan.shiro.subject.Subject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 14:06
 */
@Slf4j
public class ThreadContext {

    public static final String SECURITY_MANAGER_KEY = ThreadContext.class.getName() + "_SECURITY_MANAGER_KEY";
    public static final String SUBJECT_KEY = ThreadContext.class.getName() + "_SUBJECT_KEY";

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<Map<Object, Object>>();

    public static SecurityManager getSecurityManager() {
        return (SecurityManager) get(SECURITY_MANAGER_KEY);
    }

    public static void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        if (value == null) {
            remove(key);
            return;
        }

        ensureResourcesInitialized();
        resources.get().put(key, value);

        if (log.isTraceEnabled()) {
            String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] to thread " + "[" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
    }

    private static void ensureResourcesInitialized(){
        if (resources.get() == null){
            resources.set(new HashMap<Object, Object>());
        }
    }

    public static Object get(Object key) {
        if (log.isTraceEnabled()) {
            String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }

        Object value = getValue(key);
        if ((value != null) && log.isTraceEnabled()) {
            String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] " + "bound to thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
        return value;
    }

    public static Object remove(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        Object value = perThreadResources != null ? perThreadResources.remove(key) : null;

        if ((value != null) && log.isTraceEnabled()) {
            String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" +
                    key + "]" + "from thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }

        return value;
    }

    private static Object getValue(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        return perThreadResources != null ? perThreadResources.get(key) : null;
    }

    /**
     * shiro中重要的
     * 创建子线程时，自动扩散认证信息 机制
     * @param <T>
     */
    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {

        /**
         * 认证信息 传递的不是引用 而是clone过去的
         * 需要注意
         * @param parentValue
         * @return
         */
        @SuppressWarnings({"unchecked"})
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            } else {
                return null;
            }
        }
    }

    public static void bind(Subject subject) {
        if (subject != null) {
            put(SUBJECT_KEY, subject);
        }
    }

    public static Subject getSubject() {
        return (Subject) get(SUBJECT_KEY);
    }

}
