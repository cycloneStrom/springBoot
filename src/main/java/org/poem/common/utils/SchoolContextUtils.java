package org.poem.common.utils;

/**
 * Created by poem on 2016/6/18.
 */
public final class SchoolContextUtils {

    private static final ThreadLocal<School> MAP = new ThreadLocal();

    public static String currentSchoolSchemaSql(String sql) {
        String schema = currentSchoolSchema();
        if (StringUtils.isNotEmpty(schema) && StringUtils.isNotEmpty(sql) && !hasSchema(sql)) {
            StringBuilder sb = new StringBuilder("/*!mycat:schema=");
            sb.append(schema);
            sb.append("*/ ");
            sb.append(sql);
            return sb.toString();
        }
        return sql;
    }

    public static boolean hasSchema(String sql) {
        if (StringUtils.isNotEmpty(sql) && sql.contains("/*")) {
            return true;
        }
        return false;
    }

    public static String currentSchoolSchema() {
        School school = currentSchool();
        if(school==null){
            return null;
        }
        return school.getSchema();
    }

    public static School currentSchool() {
        return MAP.get();

    }
    public static void setCurrentSchoolSchema(String schema) {
        setCurrentSchoolSchema(null, schema);
    }

    public static void setCurrentSchoolSchema(String id, String schema) {
        if (null==schema||schema.isEmpty()) {
            return;
        }
        School school = new School();
        school.setId(id);
        school.setSchema(schema);
        MAP.set(school);
    }

    public static void setCurrentSchool(School school) {
        if (null!=school) {
            MAP.set(school);
        }
    }

    /**
     *  清除value
     */
    public static void clear() {
        MAP.remove();
    }

    public static class School {
        String id;
        String schema;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }
    }
}
