package com.lilingyan.shiro.authc.pam;

import com.lilingyan.shiro.authc.AuthenticationException;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:12
 */
public class UnsupportedTokenException extends AuthenticationException {

    public UnsupportedTokenException(String message) {
        super(message);
    }

}
