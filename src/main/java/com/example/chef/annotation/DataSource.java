package com.example.chef.annotation;

/**
 * description: TODO
 * create: 2019/1/29 15:21
 *
 * @author NieMingXin
 */

import com.example.chef.common.ContextConst;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    ContextConst.DataSourceType value() default ContextConst.DataSourceType.SLAVE;

}
