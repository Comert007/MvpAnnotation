package com.coder.mvpframe.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by feng on 2017/10/23.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface MVAnnotation {
    Class getModelClz();

    Class getViewClz();
}
