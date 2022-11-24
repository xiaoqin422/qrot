package cn.stuxx.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)//注解不仅被保存到class文件中，jvm加载class文件之后，仍存在
@Target(ElementType.METHOD) //注解添加的位置
@Documented
public @interface LogPrint {
    String description() default "";
}