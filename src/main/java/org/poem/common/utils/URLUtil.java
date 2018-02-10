package org.poem.common.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Created by poem on 2016/4/18.
 * 文件管理
 */
public class URLUtil {

    /**
     * 取得当前类所在的文件
     * @param clazz
     * @return
     */
    private static File getClassFile(Class clazz) {
        URL path = clazz.getResource(clazz.getName().substring(
                clazz.getName().lastIndexOf(".") + 1)
                + ".class");
        if (path == null) {
            String name = clazz.getName().replaceAll("[.]", "/");
            path = clazz.getResource("/" + name + ".class");
        }
        return new File(path.getFile());
    }

    /**
     * 同getClassFile 解决中文编码问题
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class clazz) {
        try {
            return java.net.URLDecoder.decode(getClassFile(clazz)
                    .getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * 取得当前类所在的ClassPath目录
     * @param clazz
     * @return
     */
    private static File getClassPathFile(Class clazz) {
        File file = getClassFile(clazz);
        for (int i = 0, count = clazz.getName().split("[.]").length; i < count; i++)
            file = file.getParentFile();
        if (file.getName().toUpperCase().endsWith(".JAR!")) {
            file = file.getParentFile();
        }
        return file;
    }

    /**
     * 同getClassPathFile 解决中文编码问题
     * @param clazz
     * @return
     */
    public static String getClassPath(Class clazz) {
        try {
            return java.net.URLDecoder.decode(getClassPathFile(clazz)
                    .getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取文件的路径
     * @return
     * @throws Exception
     */
    public static String getBasePath() throws Exception {
        return new File(URLUtil.class.getResource("/").toURI().getPath()).getParent()+File.separator+"upload";
    }

}
