package org.poem.common.hibernat;


import org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy;

/**
 * Created by lyw on 2016/4/13.
 * hibernat配置
 */
public class NamingStrategy extends SpringNamingStrategy {

    /**
     * 表明
     * @param tableName 表明
     * @return
     */
    @Override
    public String tableName(String tableName) {
        return super.tableName(tableName).toUpperCase();
    }

    @Override
    public String columnName(String columnName) {
        return super.columnName(columnName).toUpperCase();
    }
}
