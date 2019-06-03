package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.subject.Subject;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:57
 */
public interface SessionStorageEvaluator {

    boolean isSessionStorageEnabled(Subject subject);

}
