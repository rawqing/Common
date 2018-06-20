package yq.test.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.Test;
import yq.test.parameters.DataFeed;
import yq.test.parameters.annotations.CustomParameter;
import yq.test.parameters.annotations.Params;
import yq.test.parameters.annotations.YamlParams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ParamsListener implements IInvokedMethodListener, ITestListener {
    private Logger log = LoggerFactory.getLogger(ParamsListener.class);
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        log.info("[{}]{}.{} before invocation ." ,
                Thread.currentThread().getId(),
                method.getTestMethod().getTestClass().getName(),
                method.getTestMethod().getMethodName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        log.info("[{}]{}.{} after invocation ." ,
                Thread.currentThread().getId(),
                method.getTestMethod().getTestClass().getName(),
                method.getTestMethod().getMethodName());
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.debug("test success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.debug("test failure");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    /**
     * 参数化过程
     * @param context
     */
    @Override
    public void onStart(ITestContext context) {
        ITestNGMethod[] am = context.getAllTestMethods();
        for (ITestNGMethod method : am) {
            Method m = method.getConstructorOrMethod().getMethod();
            if(m.isAnnotationPresent(YamlParams.class)
                    || m.isAnnotationPresent(Params.class)
                    || isCustomParameters(m)) {
                setPod(m);
            }
        }
    }

    /**
     * 是否自定义的参数注解 ,由于
     * @param method
     * @return
     */
    private boolean isCustomParameters(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation an : annotations) {
            CustomParameter targetAnnotation = an.annotationType().getAnnotation(CustomParameter.class);
            if (targetAnnotation!=null) {
                return true;
            }
        }
        return false;
    }
    private void setPod(Method method){
        try {
            Test test = method.getAnnotation(Test.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(test);
            Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
            declaredField.setAccessible(true);
            Map memberValues = (Map) declaredField.get(invocationHandler);
            // 修改 value 属性值
            memberValues.put("dataProviderClass", DataFeed.class);
            memberValues.put("dataProvider", "getData");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onFinish(ITestContext context) {
    }
}
