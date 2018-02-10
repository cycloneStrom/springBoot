package reflecttest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by poem on 2016/8/16.
 */
public class ClassTest {
    public static void main(String... args) {
        try {
            Class clazz = Class.forName("reflecttest.MyObject");
            System.err.println("获取类的信息：" + clazz.getName());
            System.err.println("获取类的包名：" + clazz.getPackage().getName());
            int mod = clazz.getModifiers();
            System.err.println("获取类的修饰符：" + mod + " >>>>> " + Modifier.toString(mod));
            System.err.println("获取类的全限定名:" + clazz.getName());
            System.err.println("获取类的父类:" + clazz.getSuperclass().getName());
            System.err.println("获取类实现的接口:" + clazz.getInterfaces().getClass().getName());
            System.err.println("\n\n");
            /**
             * 获取类的成员变量
             */
        System.err.println("\n获取类的成员变量:");
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                String modifier = Modifier.toString(field.getModifiers());//权限访问修饰符
                Class type = field.getType();//数据类型
                String name  = field.getName();//字段名
                if(type.isArray()){
                    String arrType = type.getComponentType().getTypeName() + "[]";
                    System.err.println( "   " + modifier + "  " + arrType + "  " + name + ";");
                }else{
                    System.err.println( "   " + modifier + "  "  + "  " + name + ";");
                }
            }

            /**
             * 获取构造函数
             */
        System.err.println(" \n获取构造函数:");
            Constructor[] constructors = clazz.getConstructors();
            for(Constructor constructor : constructors){
                String name = constructor.getName();//获取构造该方法名称
                String modifier = Modifier.toString(constructor.getModifiers());
                //得到修饰符
                System.err.print("  "  + modifier + " " + name + " (");
                Class[] paramTypes = constructor.getParameterTypes();//获取参数列表
                for(int i=0;i<paramTypes.length;i++){
                    if(i > 0){
                        System.err.print(", ");
                    }
                    if(paramTypes[i].isArray()){
                        System.err.println("" + paramTypes[i].getComponentType().getName()+"[]");
                    }else{
                        System.err.print(paramTypes[i].getName());
                    }
                }
                System.err.println(");");
            }
            /**
             * 獲取成員消息
             */
            System.err.println(" \n成员方法 :");
            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                String modifier = Modifier.toString(method.getModifiers());
                Class returnType = method.getReturnType();
                if(returnType.isArray()){
                    String arrType = returnType.getComponentType().getTypeName();
                    System.err.print("  " + modifier + "  " + "  "  + arrType + " " + method.getName() + "(");
                }else {
                    System.err.print("  " + modifier + "  " + "  "  + returnType.getName() + " " + method.getName() + "(");
                }
                Class[] paramTypes = method.getParameterTypes();//获取参数列表
                for(int i=0;i<paramTypes.length;i++){
                    if(i > 0){
                        System.err.print(", ");
                    }
                    if(paramTypes[i].isArray()){
                        System.err.println("" + paramTypes[i].getComponentType().getName()+"[]");
                    }else{
                        System.err.print(paramTypes[i].getName());
                    }
                }
                System.err.println(");");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class MyObject{

    private String name;

    private String  number;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public MyObject(String name, String number, String age) {
        this.name = name;
        this.number = number;
        this.age = age;
    }

    public MyObject(String name, String number) {

        this.name = name;
        this.number = number;
    }

    public MyObject() {
    }

    public void myLover(String lover){
       System.out.println(lover);
   }

    public MyObject(String name) {
        this.name = name;
    }
}
