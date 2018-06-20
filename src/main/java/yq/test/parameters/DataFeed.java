package yq.test.parameters;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import yq.test.parameters.annotations.CustomParameter;
import yq.test.parameters.annotations.Params;
import yq.test.parameters.annotations.YamlParams;
import yq.test.parameters.paramHandle.CustomParametersImp;
import yq.test.parameters.paramHandle.NoneProviderImp;
import yq.test.parameters.paramHandle.ParamsProviderImp;
import yq.test.parameters.paramHandle.YamlProviderImp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DataFeed {

    @DataProvider
    public  Object[][] getData(ITestContext context,Method method) {
        return getProvider(method).getParameters(context, method);
    }

    /**
     * 分发至不同的 provider 去处理数据
     * @param method
     * @return
     */
    private IParametersProvider getProvider(Method method) {
        if(method.isAnnotationPresent(YamlParams.class)){
            return new YamlProviderImp();
        }
        if (method.isAnnotationPresent(Params.class)) {
            return new ParamsProviderImp();
        }
        return customParameters(method);
    }

    /**
     * 使用自定义的参数解析方式
     * @param m
     * @return
     */
    private IParametersProvider customParameters(Method m){
        Annotation[] annotations = m.getAnnotations();

        for (Annotation an : annotations) {
            CustomParameter targetAnnotation = an.annotationType().getAnnotation(CustomParameter.class);
            if (targetAnnotation!=null) {
                Class<? extends IParametersProvider> provider = targetAnnotation.provider();
                return new CustomParametersImp(an, provider);
            }
        }
        return new NoneProviderImp();
    }
}
