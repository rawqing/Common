package yq.test.parameters.annotations;


import yq.test.parameters.IParametersProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.ANNOTATION_TYPE)
public @interface CustomParameter {

    /**
     * 具体的目标实现类
     * @return
     */
    Class<? extends IParametersProvider> provider();
}
