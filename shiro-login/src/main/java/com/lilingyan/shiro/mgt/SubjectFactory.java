package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.subject.SubjectContext;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:47
 */
public interface SubjectFactory {

    Subject createSubject(SubjectContext context);

}
