package yq.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static yq.test.utils.Armory.getTypesArray;

/**
 * Created by king on 16/11/23.
 */

public class MReflect {

    /**
     * 获取对象的所有定义的属性
     * @param object
     * @return
     */
    public static Field[] getDecFields(Object object){
        Class clz = object.getClass();
        return getDecFields(clz);
    }
    public static Field[] getDecFields(Class clz){
        Field [] fields = clz.getDeclaredFields();
        Field.setAccessible(fields , true);
        return fields;
    }
    public static List<Field> getDecFieldsWithSuper(Class clz ){
        List<Field> list = new ArrayList<>();
        while (true) {
            if (clz == null || clz == Object.class) {
                return list;
            }
            List<Field> fields = Arrays.asList(getDecFields(clz));
            list.addAll(fields);
            clz = clz.getSuperclass();
        }
    }
    /**
     * 获取指定名称的 Field
     * @param clz
     * @param fieldName
     * @return
     */
    public static Field getDecField(Class clz ,String fieldName){
        try {
            return clz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Field getDecFieldWithSuper(Class clz ,String fieldName){
        Field field = getDecField(clz, fieldName);
        if (field != null || clz == Object.class) {
            return field;
        }
        return getDecFieldWithSuper(clz.getSuperclass(), fieldName);
    }
    /**
     * 匹配指定属性
     * @param fields
     * @param fieldName
     * @return
     */
    public static Field matcherField(Field[] fields , String fieldName){
        for(Field field : fields){
            if(field.getName().equals(fieldName))
                return field ;
        }
        return null;
    }

    /**
     * 获得匹配的属性 , 只匹配第一个被注解的属性
     * @param fields
     * @param annotation
     * @return
     */
    public static Field matcherField(Field[] fields , Class<? extends Annotation> annotation){
        Field.setAccessible(fields , true);
        for(Field field : fields){
            // 这里只获取使用了annotation注解过的的属性.
            if (field.isAnnotationPresent(annotation)) {
                return field;
            }
        }
        return null;
    }
    public static Field matcherField(List<Field> fields , Class<? extends Annotation> annotation){
        if (fields.size() < 1)  return null;
        Field[] fs = new Field[fields.size()];
        Field[] fieldsArray = fields.toArray(fs);
        return matcherField(fieldsArray, annotation);
    }
    /**
     * 获取属性的值
     * @param field
     * @param object
     * @return
     */
    public static Object getFieldObject(Field field , Object object){
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object getFieldObject(String fieldName , Object object){
        return getFieldObject(Objects.requireNonNull(matcherField(getDecFields(object), fieldName)),object);
    }

    /**
     * 获取特定注解的属性的值
     *      只获取第一个被注解的属性值
     * @param fields
     * @param object
     * @param annotation
     * @return
     */
    public static Object getFieldObject(Field[] fields ,Object object ,Class<? extends Annotation> annotation){
        Field.setAccessible(fields , true);
        for(Field field : fields){
            try {
                // 这里只获取使用了annotation注解过的的属性.
                if (field.isAnnotationPresent(annotation)) {
                    return field.get(object);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Object getFieldObject(List<Field> fields ,Object object ,Class<? extends Annotation> annotation){
        if (fields.size() < 1)  return null;
        Field[] fs = new Field[fields.size()];
        Field[] fieldsArray = fields.toArray(fs);
        return getFieldObject(fieldsArray, object, annotation);
    }
    public static Object getFieldObject(Class<?> clz ,Object object ,Class<? extends Annotation> annotation){
        Field[] declaredFields = clz.getDeclaredFields();
        return getFieldObject(declaredFields, object, annotation);
    }
    /**
     * 通过 Class 获取对象
     * @param clz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T getObject(Class<T> clz) throws IllegalAccessException, InstantiationException {
        return clz.newInstance();
    }
    public static <T> T getObject(Class<T> clz ,Object ...obj) throws IllegalAccessException, InstantiationException {
        return null;
    }

    /**
     * 反射设置 属性的值
     * @param field
     * @param o
     * @param val
     */
    public static void setFieldValue(Field field, Object o, Object val) {
        field.setAccessible(true);
        try {
            field.set(o, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得指定class 的实例
     * @param clz
     * @param objects
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> clz ,Object... objects){
        Class[] clzs = getTypesArray(objects);
        try {
            if (clzs == null) {
                return clz.newInstance();
            }
            Constructor<T> constructor = clz.getConstructor(clzs);
            return constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
