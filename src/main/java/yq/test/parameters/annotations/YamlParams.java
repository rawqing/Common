package yq.test.parameters.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * 默认从 resources 为根目录读取 ， 不便于环境化
 */
public @interface YamlParams {
    String value() default "";
}
