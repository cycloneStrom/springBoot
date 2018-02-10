package annotationtest;

import com.google.common.collect.Maps;
import org.poem.common.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by YN on 2016/8/23.
 * 注解的处理类
 */
public class AnnotationInstall {

    /**
     * 当前处理
     * @param clazz
     */
    public static void processAnnotations(Class<?> clazz,Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        /*获取类的列 方法 属性 等等信息*/
        Set<Field> fields = new HashSet<>(Arrays.asList(clazz.getDeclaredFields()));
         /*获取类的父级类的列 方法 属性 等等信息*/
        fields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        /*遍历 获取注解中的内容*/
        Map<String , MyAnnotationBean> myAnnotationBeanMap = Maps.newHashMap();

        /*方法信息*/
        Map<String,Field> fieldMap = Maps.newHashMap();
        for (Field field : fields) {
            MyAnnotation myAnnotation = field.getAnnotation(MyAnnotation.class);
            if(field.isAnnotationPresent(MyAnnotation.class) && StringUtils.isNotBlank(myAnnotation.score())){
                myAnnotationBeanMap.put(field.getName(),new MyAnnotationBean(myAnnotation.score()));
            }else {
                myAnnotationBeanMap.put(field.getName(),null);
            }
            fieldMap.put(field.getName(),field);
        }
        /*注释的名称和值*/
        for (String fieldName : myAnnotationBeanMap.keySet()) {
            String annotationValue =  null != myAnnotationBeanMap.get(fieldName) ? myAnnotationBeanMap.get(fieldName).score:null;
            String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            Object fieldValue = clazz.getMethod(methodName, fieldMap.get(fieldName).getType().getClasses()).invoke(obj);
            System.err.println(fieldValue + " >>> "  + annotationValue);
        }

        /*方法名称*/
        Set<Method> methods = new HashSet<>(Arrays.asList(clazz.getMethods()));
        methods.addAll(Arrays.asList(clazz.getSuperclass().getMethods()));
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }
}
