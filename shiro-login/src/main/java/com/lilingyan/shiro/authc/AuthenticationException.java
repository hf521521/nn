package com.lilingyan.shiro.authc;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:22
 */
public class AuthenticationException extends RuntimeException{

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
