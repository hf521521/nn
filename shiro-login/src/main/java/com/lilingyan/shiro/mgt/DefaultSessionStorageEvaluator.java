package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.subject.Subject;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:57
 */
public class DefaultSessionStorageEvaluator implements SessionStorageEvaluator{

    private boolean sessionStorageEnabled = true;

    public boolean isSessionStorageEnabled(Subject subject) {
        return (subject != null && subject.getSession(false) != null) || isSessionStorageEnabled();
    }

    public boolean isSessionStorageEnabled() {
        return sessionStorageEnabled;
    }

}
