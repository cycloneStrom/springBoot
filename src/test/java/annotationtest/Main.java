package annotationtest;

/**
 * Created by YN on 2016/8/23.
 */
public class Main {

    /**
     * 名称
     */
    @MyAnnotation(score = "路延文")
    private String name;

    /**
     * 年龄
     */
    @MyAnnotation(score = "25")
    private String age;

    /**
     * 我的最爱
     */
    private String myLover;


    public Main() {
    }

    public Main(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public void setMyLover(String lover){
        this.myLover = lover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMyLover() {
        return myLover;
    }
}
