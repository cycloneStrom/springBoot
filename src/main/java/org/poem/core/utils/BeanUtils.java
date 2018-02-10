package org.poem.core.utils;

import org.dozer.DozerBeanMapper;

/**
 * Created by poem on 2016/4/15.
 * BeanUtils Bean
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils{

    private static DozerBeanMapper dozerMapper= new DozerBeanMapper();

    public static <T> T copyProperties(Class<T> destClass, Object source) {
        Object target = null;
        try {
            target = destClass.newInstance();
            copyProperties(source, target);
            return (T) target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyProperties(Object source, Object dest) {
        dozerMapper.map(source,dest);
    }

}
