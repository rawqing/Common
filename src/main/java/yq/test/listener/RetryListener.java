package yq.test.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import yq.test.exception.AcceptableException;
import yq.test.listener.feature.Retry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer ,IInvokedMethodListener {
    private Logger log = LoggerFactory.getLogger(RetryListener.class);

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (retry == null) {
            annotation.setRetryAnalyzer(Retry.class);
        }
    }


    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    /**
     * 若为自定义异常"可接受异常"则标注测试为成功的状态
     * 截获异常并处理需要在 afterInvocation 中执行 , 在 onTestFailure 中若监听是加在xml文件中则在run的时候无法截获,
     * 而debug的时候却可以成功 ,若监听加在 class 上则没有问题.
     * @param method
     * @param testResult
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Throwable throwable = testResult.getThrowable();
        if (throwable instanceof AcceptableException) {
            log.debug("test exception instanceof AcceptableException: \n\t{} \n\tset test status to SUCCESS ."
                ,throwable.toString());
            testResult.setStatus(1);
        }
    }
}
