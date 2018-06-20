package yq.test.parameters.paramHandle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import yq.test.constant.ConfSet;
import yq.test.parameters.IParametersProvider;
import yq.test.parameters.annotations.YamlFilePath;
import yq.test.parameters.annotations.YamlParams;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static yq.test.utils.DoIt.allList2array;
import static yq.test.utils.DoIt.castObject;
import static yq.test.utils.Judge.notNone;
import static yq.test.utils.YamlRW.readYamlFile;


public class YamlProviderImp implements IParametersProvider {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private YamlParams yamlParams;
    private Class<?>[] parameterTypes;
    private Class<?> testClass;
    private String testMethodName;
    private final String class_key = "class";
    private final String method_key = "method";

    private String ymlRootDir = null;
    private String ymlFilePath = null;

    public YamlProviderImp(){
        if (notNone(ConfSet.ymlRootDir)) {
            this.ymlRootDir = ConfSet.ymlRootDir;
        }
    }
    public YamlProviderImp(String rootDir ,String ymlFilePath){
        ymlRootDir = rootDir;
        this.ymlFilePath = ymlFilePath;
    }
    @Override
    public void initialize(Annotation parametersAnnotation) {

    }

    @Override
    public Object[][] getParameters(ITestContext context, Method method) {
        this.yamlParams = method.getAnnotation(YamlParams.class);
        this.parameterTypes = method.getParameterTypes();
        this.testClass = method.getDeclaringClass();
        this.testMethodName = method.getName();

        Object[][] parameters = getParameters();
        log.info("yaml 文件参数 : {}" , Arrays.deepToString(parameters));
        return parameters;
    }

    /**
     * 获取yml文件的绝对路径 ( 通过@YmlParameter方法注解 或 @YamlFilePath类注解 )
     * @return 绝对路径
     */
    private String getYmlPath(){
        String value = "";
        if (yamlParams != null) {
            value = yamlParams.value();
        }
        if (this.ymlFilePath != null) {
            value = this.ymlFilePath;
        }
        if ("".equals(value)) {
            YamlFilePath yamlAnnot = testClass.getAnnotation(YamlFilePath.class);
            if (yamlAnnot != null)  value = yamlAnnot.value();
        }
        if (ymlRootDir != null) {
            value = ymlRootDir + File.separator + value;
        }
        System.out.println(String.format("yml file path : %s", value));
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource(value)).getPath();
    }

    /**
     * 获取最终参数
     * @return
     */
    private Object[][] getParameters() {
        List params = getMethodParams(getYmlPath());
        if (params == null) {
            log.error("{} : {} Empty parameters ." ,testClass.getName() ,testMethodName);
            return null;
        }
        List res = new ArrayList();
        for (Object po : params) {
            List data = null;
            // 将每行数据封装成 List
            if (po instanceof List) {
                data = (List) po;
            } else {
                data = new ArrayList();
                data.add(po);
            }

            res.add(createParams(data ,parameterTypes));
        }
        return allList2array(res);
    }
    /**
     * 创建参数
     * @param datas
     * @param types
     * @return
     */
    private Object[] createParams(List datas ,Class[] types){
        int dataSize = datas.size();
        int typeLen = types.length;
        // 判断参数个数与参数类型个数是否一致
        if (dataSize != typeLen) {
            log.error("参数个数({})与参数类型个数({})不匹配", dataSize, typeLen);
            return null;
        }
        Object[] o = new Object[typeLen];
        for(int i=0;i<typeLen; i++) {
            Object data = datas.get(i);
            o[i] = castObject(types[i], data);
        }
        return o;
    }
    /**
     * 捕获 yml 文档中当前 test class 的所有 method 的数据
     *      每个文档必须包含 "class" , "method" 这两个key
     * @return method 这个 key 的value
     *      一般为 ArrayList
     */
    private Object getYamlMethods(String path){
        List list = readYamlFile(path);
        for (Object obj : list) {
            Map map = (Map) obj;
            Object aClass = map.get(class_key);
            if (this.testClass.getSimpleName().equals(aClass)) {
                return map.get(method_key);
            }
        }
        return null;
    }

    /**
     * 获取当前方法在 yml 文件中定义的参数
     * @return
     */
    private List getMethodParams(String path){
        Object yamlObject = this.getYamlMethods(path);
        if (yamlObject == null) {
            log.error("为获取到相应的 methods。yml：{} ，class：{} 。 ",path ,testClass.getName());
            return null;
        }
        List datas = (List) yamlObject;
        for (Object o : datas) {
            Map map = (Map)o;
            if (testMethodName.equals(map.get("name"))) {
                return (List) map.get("data");
            }
        }
        return null;
    }
}
