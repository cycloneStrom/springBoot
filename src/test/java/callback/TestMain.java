package callback;

/**
 * Created by poem on 2016/7/31.
 * JAVA callback test
 */
public class TestMain {

    public static  void main(String ... arg){
        new MiddlewareClass().transfer(10, new CallBackMainClass() {
            @Override
            public void onMemeryPrintln(int sum) {
                System.err.println("THIS IS TEST JAVA CallBack FILEDS");
                System.err.println("the number of sum is :" + sum);
            }
        });
    }
}

/**
 * 中间件调用
 */
class MiddlewareClass {
    public void  transfer(int index,CallBackMainClass callBackMainClass){
        int sum = 0;
        for(int i=0;i<index;i++){
            System.err.println("THIS IS INDEX :" + i);
            sum += i;
        }
        callBackMainClass.onMemeryPrintln(sum);
    }
}

/**
 * 回调的借口
 */
interface CallBackMainClass {

    /**
     * 调用
     */
    public void  onMemeryPrintln(int sum);
}