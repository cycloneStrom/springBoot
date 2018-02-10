package org.poem.core.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by poem on 2016/4/15.
 * 基类
 *  1、基于代码复用和模型分离的思想，在项目开发中使用JPA的@MappedSuperclass注解将实体类的多个属性分别封装到不同的非实体类中。
 *  2、被MappedSuperclass注册的类不会映射到数据表中
 *  3、标注为@MappedSuperclass的类不能再标注@Entity或@Table注解，也无需实现序列化接口。
 *  4、但是如果一个标注为@MappedSuperclass的类继承了另外一个实体类或者另外一个同样标注了@MappedSuperclass的类的话，他将可以使用@AttributeOverride或@AttributeOverrides注解重定义其父类(无论是否是实体类)的属性映射到数据库表中的字段。
 */
@MappedSuperclass
public class IdEntity implements Serializable {

    /**
     * 记录的id
     */
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id;


    public String getId() {
        return id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     * #Id 标注这个是主键，在数据库中满足唯一性约束
     * #GeneratedValue 自增长值，这个在插入的时候如果为空，会设置一个新的
     * #GenericGenerator自增
     * #Column 数据列，在数据库中的列
     */

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdEntity)) return false;

        IdEntity idEntity = (IdEntity) o;

        if (id != null ? !id.equals(idEntity.id) : idEntity.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
