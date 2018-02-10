package annotationtest;

/**
 * Created by YN on 2016/8/23.
 * 获取的类的注解的名称
 */
public class MyAnnotationBean {

    public String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    MyAnnotationBean(){

    }

    MyAnnotationBean(String score){
        this.score = score;
    }
}
