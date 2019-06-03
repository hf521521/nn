package com.lilingyan.shiro.session;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:25
 */
public class UnknownSessionException extends InvalidSessionException{

    public UnknownSessionException(String message) {
        super(message);
    }

}
