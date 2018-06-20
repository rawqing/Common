package yq.test.parameters.paramHandle;

import org.testng.ITestContext;
import yq.test.parameters.IParametersProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class NoneProviderImp implements IParametersProvider {
    @Override
    public void initialize(Annotation parametersAnnotation) {

    }

    @Override
    public Object[][] getParameters(ITestContext context, Method method) {
        return new Object[0][];
    }
}
