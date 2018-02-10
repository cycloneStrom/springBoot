package annotationtest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by YN on 2016/8/23.
 * 自定义注解类
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})//Annotation限定范围
@Retention(RetentionPolicy.RUNTIME)//Annotation保留的时间的长度
public @interface MyAnnotation {
    /**
     * 定义一个类型
     * @return
     */
    String score() default "";

}
