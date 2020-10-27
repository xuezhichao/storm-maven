package com.byxf.storm.hbase;

import java.lang.annotation.*;

/**
 * @Descriptions: 自定义注解，用于获取table
 * @Date: Created in 2018/3/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
@Inherited
public @interface HbaseTable {
    String tableName() default "";
}
