package org.poem.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by poem on 2016/6/18.
 */
public class PropertyUtils {
    /** The Constant LOG. */
    private static final Logger LOG  = LoggerFactory.getLogger(PropertyUtils.class);

    /** 加载配置文件的对象. */
    private static Properties prop = new Properties();
    static{
        try {
            List<URL> fileUrls = FileUtils.scanFileByPath(PropertyUtils.class.getResource("/").toURI().getPath(), new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return true;
                    }
                    return file.getName().endsWith(".properties");
                }
            });
            for(URL url : fileUrls) {
                prop.load(url.openStream());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } catch (URISyntaxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 禁止创建工具类的实例.
     */
    private PropertyUtils(){
    }

    /**
     * 获取系统当前运行的黑白名单模式.
     *
     * @return Ip过滤模式配置
     */
    public static String getRunMode(){
        return getString("SYS_RUN_MODE", "N");
    }

    /**
     * 获取系统的黑名单列表表达式.
     *
     * @return 系统黑名单配置
     */
    public static String getBlackList(){
        return prop.getProperty("BLACK_LIST");
    }

    /**
     * 获取系统的黑名单列表表达式.
     *
     * @return 系统白名单配置
     */
    public static String getWhiteList(){
        return prop.getProperty("WHITE_LIST");
    }

    /**
     * 获取资源文件中指定属性.
     *
     * @param name 属性名称
     * @return 配置文件中name对应的值
     */
    public static String getString(String name){
        return prop.getProperty( name );
    }

    /**
     * 获取资源文件中指定属性.
     *
     * @param name 属性名称
     * @param def 默认值
     * @return 配置文件中name对应的值
     */
    public static int getInt(String name, int def){
        String value = prop.getProperty( name );
        if(isNumber(value)){
            return Integer.parseInt( value );
        }
        return def;
    }

    /**
     * 获取资源文件中指定属性.
     *
     * @param name 属性名称
     * @param def 默认值
     * @return 配置文件中name对应的值
     */
    public static String getString(String name, String def){
        String value = prop.getProperty( name );
        if(org.apache.commons.lang3.StringUtils.isNotBlank(value)){
            return value;
        }
        return def;
    }

    /**
     * 判断传入的字符串是否为数字.
     *
     * @param str 字符串表达式
     * @return 字符串是否数字
     */
    private static boolean isNumber(String str) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\d]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
