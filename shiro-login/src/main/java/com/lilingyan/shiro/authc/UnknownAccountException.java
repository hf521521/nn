package com.lilingyan.shiro.authc;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:13
 */
public class UnknownAccountException extends AccountException{

    public UnknownAccountException(String message) {
        super(message);
    }

}
