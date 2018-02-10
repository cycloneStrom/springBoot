package annotationtest;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YN on 2016/8/23.
 * 测试
 */
public class MainTest {

    /**
     * 执行的方法区域
     * @param args
     */
    public static void main(String ... args){
        Main main = new Main();
        main.setName("敏");
        main.setAge("29");
        try {
            AnnotationInstall.processAnnotations(Main.class,main);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
