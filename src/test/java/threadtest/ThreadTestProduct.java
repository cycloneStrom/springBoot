package threadtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by poem on 2016/8/16.
 * 多线程测试
 */
public class ThreadTestProduct {
    public static void main(String ... args){
        Product product = new Product();
        Thread productorThread = new Thread(new Productor(product));
        Thread cusumerThread = new Thread(new Cusumer(product));
        productorThread.start();
        cusumerThread.start();
    }

    public void main(){
        while (true){
            new Thread().start();
        }
    }
}

/**
 * 产品
 */
class Product{

    /**
     * 日志记录
     */
    public static Logger logger = LoggerFactory.getLogger(Product.class);
    /**
     * 产品的数量
     */
    public Integer productNum =0;

    /**
     * 增加产品的数量
     */
    public synchronized  void addProduct(){
        if(this.productNum >= 20){
            try {
                wait();//是线程进入到等待状态
            } catch (InterruptedException e) {
                logger.info(e.getMessage(),e);
                e.printStackTrace();
            }
        }else{
            System.err.println("生产了" + (++this.productNum )  + "个产品.");
            notifyAll();//等待状态转为可运行状态
        }
    }

    /**
     * 实现消费
     */
    public synchronized void getProduct(){
        if(this.productNum <= 0){
            try {
                wait();//是线程进入到等待状态
            } catch (InterruptedException e) {
                logger.info(e.getMessage(),e);
                e.printStackTrace();
            }
        }else{
            System.err.println("取走第" + (-- this.productNum )  + "个产品.");
            notifyAll();
        }
    }
}

/**
 * 生产者
 */
class Productor implements Runnable{
    /**
     * 生产者
     */
    private Product product;

    /**
     * 构造函数
     * @param product
     */
    Productor(Product product){
        this.product = product;
    }

    public void run(){
        System.err.println("生产者开始大量生产产品");
        while (true){
            try {
                Thread.sleep((int)(Math.random()*10)*100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            product.addProduct();
        }
    }
}

 class Cusumer implements Runnable{
     /**
      * 生产者
      */
     private Product product;

     /**
      * 构造函数
      * @param product
      */
     Cusumer(Product product){
         this.product = product;
     }

     public void run(){
         System.err.println("消费者开始消耗产品！");
         while (true){
             try {
                 Thread.sleep((int)(Math.random()*10)*100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             product.getProduct();
         }
     }
 }

