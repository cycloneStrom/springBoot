package org.poem.api.file.upload;

import java.io.Serializable;

/**
 * Created by yineng
 * name:01-BlogDemo
 * User:lyw
 * Date 2016/12/21
 */
public class FormFieldKeyValue implements Serializable {

    /**
     * The form field used for receivinguser's input
     * such as "username" in "<inputtype="text" name="username"/>"
     */
    private String key;

    /**
     * The value entered by user in thecorresponding form field,
     *  such as "Patrick" the abovementioned formfield "username"
     */
    private String value;


    public FormFieldKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
