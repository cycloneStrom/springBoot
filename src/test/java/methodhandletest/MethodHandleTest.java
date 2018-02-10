package methodhandletest;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created by poem on 2016/8/23.
 * Java 语言动态代理实现
 * 动态类型
 */
public class MethodHandleTest {
    static class ClassA{
        public void println(String s){
            System.err.println(s);
        }
    }

    public static void main(String ...  args) throws Throwable {

        Object obj = System.currentTimeMillis() % 2 == 0 ? System.out:new ClassA();

        getPrintlnMH(obj).invokeExact("1245452");
    }

    public static MethodHandle getPrintlnMH(Object reveiver) throws Throwable{
        /**
         * MethodType 代表方法类型
         * 包含方法的返回值 第一个参数
         * 参数的具体类型   第二个参数
         */
        MethodType methodType = MethodType.methodType(void.class,String.class);

        /**
         * 指定类中查找复合给定的方法名，方法类型，并且符合调用权限的方法句柄
         * 这里调用的是一个虚拟方法，按照Java语言规范，方法第一个参数是隐试
         * 代表方法的接受者，就是this指向的对象
         * 这个参数以前是放在参数列表中进行传递，现在提供bindTo
         */
        return MethodHandles.lookup().findVirtual(reveiver.getClass(),"println",methodType).bindTo(reveiver);
    }
}
