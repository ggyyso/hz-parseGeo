package com.common.annotations;

/**
 * description: 不检查Session注解，可配合触发器、AOP使用 <br>
 * date: 2019/2/13 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoCheckSession {
}
