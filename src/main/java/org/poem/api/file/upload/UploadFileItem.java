package org.poem.api.file.upload;

import java.io.Serializable;

/**
 * Created by yineng
 * name:01-BlogDemo
 * User:lyw
 * Date 2016/12/21
 */
public class UploadFileItem implements Serializable {
    //
    /**
     * he form field name in a form used foruploading a file
     * such as "upload1" in "<input type="file" name="upload1"/>"
     */
    private String formFieldName;

    /**
     * File name to be uploaded, thefileName contains path,
     * such as "E:\\some_file.jpg"
     */
    private String fileName;


    public UploadFileItem(String formFieldName, String fileName) {
        this.formFieldName = formFieldName;
        this.fileName = fileName;
    }

    public String getFormFieldName() {
        return formFieldName;
    }

    public void setFormFieldName(String formFieldName) {
        this.formFieldName = formFieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
