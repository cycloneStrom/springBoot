package overloadtest;

import java.io.Serializable;

/**
 * Created by YN on 2016/8/21.
 */
public class OverLoadTest {

// 6、   public static void sayHello(Object arg){
//        System.err.println("Hello Object");
//    }

// 2、   public static void sayHello(int arg){
//        System.err.println("Hello int");
//    }

// 3、   public static void sayHello(long arg){
//        System.err.println("Hello long");
//    }

// 4、   public static void sayHello(Character arg){
//        System.err.println("Hello Character");
//    }

//  1、  public static void sayHello(char  arg){
//        System.err.println("Hello char");
//    }

    public static void sayHello(char ... arg){
        System.err.println("Hello char ... ");
    }

// 5、   public static void sayHello(Serializable arg){
//        System.err.println("Hello Serializable");
//    }

    public static void main(String  ... arg){
        sayHello('a');
    }
}
