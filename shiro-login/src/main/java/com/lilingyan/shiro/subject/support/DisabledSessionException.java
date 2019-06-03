package com.lilingyan.shiro.subject.support;

import com.lilingyan.shiro.session.SessionException;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 15:41
 */
public class DisabledSessionException extends SessionException {

    public DisabledSessionException(String message) {
        super(message);
    }

}
