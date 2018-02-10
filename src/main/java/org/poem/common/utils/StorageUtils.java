package org.poem.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yineng on 2016/12/19.
 * 文件上传 文件的单列模式
 */
public class StorageUtils {

    /**
     * 默认的文件上传的大小
     * 超过大小把文件切割上传
     */
    private static final Integer DEFAULT_FILE_LENGTH = 8 * 1024 * 1024;
    /**
     * 当前对象示例
     */
    private static final StorageUtils storageUtils = new StorageUtils();

    /**
     * 日志记录
     */
    private static  Logger logger =  LoggerFactory.getLogger( StorageUtils.class ) ;

    /**
     * 在启动的时候就创建一个示例
     */
    private StorageUtils() {
        if(null == logger){
            logger =  LoggerFactory.getLogger( StorageUtils.class ) ;
        }
        String configFilePath = getClientConfFile();
        logger.info( "FastdfsUtils.init加载配置文件:" + configFilePath );
        try {
            ClientGlobal.init( configFilePath );
        } catch (IOException e) {
            logger.error( "获取配置文件失败！", e.getMessage() );
            e.printStackTrace();
        } catch (MyException e) {
            logger.error( "获取配置文件失败！", e.getMessage() );
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件的路径
     *
     * @return 获取文件的配置文件的路径
     */
    private static String getClientConfFile() {
        String classPath = URLUtil.getClassPath( StorageUtils.class );
        int index = classPath.indexOf( "file" );
        classPath = classPath.substring( 0, index == -1 ? classPath.length() : index );
        return classPath.lastIndexOf( File.separator ) != (classPath.length() - 1) ? classPath + File.separator + "client.conf" : classPath + "client.conf";
    }
    /**
     * 获取文件上传的客户端
     * 均衡文件上传服务客户端
     *
     * @return
     */
    private static TrackerClient getTrackerClient() {
        return new TrackerClient();
    }

    /**
     * 获取文件上传的均衡服务 实现文件的上传
     *
     * @return
     */
    private static TrackerServer getTrackerServer() throws IOException {
        return getTrackerClient().getConnection();
    }

    /**
     * 获取文件的上传文件存错信息
     *
     * @return
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient( trackerServer, storageServer );
        return storageClient;
    }

    /**
     * 是否是图片
     */
    private static boolean isImage(String fileName) {
        return FileTypeUtil.isImageByExtension( fileName );
    }

    /**
     * 打印文件的上传的信息
     *
     * @param file
     * @param fileName
     */
    private static void loggerFileInfo(File file, String fileName) {
        if(null == logger){
            logger =  LoggerFactory.getLogger( StorageUtils.class ) ;
        }
        logger.info( "fileName:" + fileName );
        String originalFileName = FilenameUtils.getName( fileName );// 文件名
        logger.info( "originalFileName:" + originalFileName );
        String baseName = FilenameUtils.getBaseName( fileName );// 不含后缀名
        logger.info( "baseName:" + baseName );
        String fileExtName = FilenameUtils.getExtension( originalFileName );// 文件后缀名
        logger.info( "fileExtName:" + fileExtName );

        long length = file.length();
        logger.info( "length:" + length );
        boolean isImage = isImage( originalFileName );
        logger.info( "isImage:" + isImage );
        String mimeType = FileUtil.getMimeType( fileName );
        logger.info( "mimeType:" + mimeType );
    }

    /**
     * 上传文件到fastdfs
     *
     * @param file     上传的文件
     * @param fileName 上传的文件名
     * @return 文件的上传的信息
     */
    public static Map<String, Object> uploadFile(File file, String fileName) {
        if(null == logger){
            logger =  LoggerFactory.getLogger( StorageUtils.class ) ;
        }
        loggerFileInfo(file, fileName);
        long fileLength = file.length();
        BufferedInputStream buffer = null;
        FileInputStream fis = null;
        StorageClient storageClient = null;
        int length , count = 1;
        boolean status = false;
        TrackerServer trackerServer = null;
        Map<String,Object> resultMap = Maps.newHashMap();
        String[] uploadStrArr = null;
        byte[] fileByteArr  = new byte[fileLength > DEFAULT_FILE_LENGTH ? DEFAULT_FILE_LENGTH : (int) fileLength];
        NameValuePair[] vars = new NameValuePair[]{new NameValuePair("fileName", fileName)};
        try {
            fis = new FileInputStream(file);
            buffer = new BufferedInputStream(fis);
            storageClient = getStorageClient();
            trackerServer = getTrackerServer();
            while ((length = buffer.read( fileByteArr )) != -1){
                if(count == 1){
                    uploadStrArr = storageClient.upload_appender_file(fileByteArr, 0, length, FilenameUtils.getExtension(fileName), vars);
                    resultMap.put("groupName", uploadStrArr[0]);
                    resultMap.put("storageFileName", uploadStrArr[1]);
                }else{
                    storageClient.append_file(uploadStrArr[0], uploadStrArr[1], fileByteArr, 0, length);
                }
                count ++;
            }
            if (resultMap.size() != 0) {
                status = true;
                logger.info("group_name: " + resultMap.get( "groupName" ) + ", remote_filename: " + resultMap.get("storageFileName"));
                logger.info(storageClient.get_file_info(resultMap.get( "groupName" ).toString(), resultMap.get("storageFileName").toString()).toString());
            }else{
                logger.info("upload file: " + fileName + " to FastFS fail,  please check");
            }
            resultMap.put("status", status);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (MyException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly( buffer );
            IOUtils.closeQuietly( fis );
            try {
                if (null != trackerServer) {
                    trackerServer.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return  resultMap;
    }

    /**
     * 文件上传信息
     * @param fileBuff
     * @param fileExtName
     * @param metaList
     * @return
     */
    private static String[] storeFile(byte[] fileBuff, String fileExtName, NameValuePair[] metaList) {
        String[] responseData = null;
        try {
            StorageClient storageClient = getStorageClient();
            responseData = storageClient.upload_file(fileBuff, fileExtName.toLowerCase(), metaList);
        } catch (Exception e) {
            logger.error("StorageUtils.storeFile时发生异常:", e);
        }
        return responseData;
    }

    /**
     * 获取文件的信息
     * @param fileBuff
     * @return
     */
    private static int[] getImageInfo(byte[] fileBuff) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(fileBuff);// 将byte[]作为输入流；
            BufferedImage image = ImageIO.read(in);// 将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
            int width = image.getWidth();
            int height = image.getHeight();
            return new int[]{width, height};
        } catch (Exception e) {
            logger.error("StorageUtils.getImageInfo时发生异常:", e);
        }
        return new int[]{0, 0};
    }

    /**
     * 字节流方式上传
     * @param inputStream 文件的字节流
     * @param fileName 文件的名称
     * @return 文件的上传信息
     */
    public static Map<String, Object> uploadFile(InputStream inputStream, String fileName) {
        if(null == logger){
            logger =  LoggerFactory.getLogger( StorageUtils.class ) ;
        }
        logger.info("fileName:" + fileName);
        String originalFileName = FilenameUtils.getName(fileName);// 文件名
        logger.info("originalFileName:" + originalFileName);
        String baseName = FilenameUtils.getBaseName(fileName);// 不含后缀名
        logger.info("baseName:" + baseName);
        String fileExtName = FilenameUtils.getExtension(originalFileName);// 文件后缀名
        logger.info("fileExtName:" + fileExtName);

        boolean isImage = isImage(originalFileName);
        logger.info("isImage:" + isImage);
        String mimeType = FileUtil.getMimeType(fileName);
        logger.info("mimeType:" + mimeType);

        int width = 0;
        int height = 0;

        boolean status = false;
        String message = "文件上传失败！";
        Map<String, Object> uploadResult = new HashMap<String, Object>();
        NameValuePair[] metaList = new NameValuePair[]{new NameValuePair("fileName", fileName), new NameValuePair("isImage", isImage + ""), new NameValuePair("mimeType", mimeType), new NameValuePair("width", width + ""), new NameValuePair("height", height + ""), new NameValuePair("author", "StorageUtils")};
        byte[] fileBuff = null;
        try {
            fileBuff = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        if (isImage) {
            int[] imageInfo = getImageInfo(fileBuff);
            if (imageInfo != null) {
                width = imageInfo[0];
                height = imageInfo[1];
            }
        }
        uploadResult.put("fileSize", fileBuff.length);
        String[] responseData = storeFile(fileBuff, fileExtName, metaList);

        if (responseData != null) {
            status = true;
            message = "文件上传成功！";
            uploadResult.put("groupName", responseData[0]);
            uploadResult.put("storageFileName", responseData[1]);
            uploadResult.put("link", responseData[0] + "/" + responseData[1]);// 文件访问链接
        }
        uploadResult.put("status", status);
        uploadResult.put("message", message);
        uploadResult.put("fileName", fileName);
        uploadResult.put("mimeType", mimeType);
        logger.info("length:" + uploadResult.get("length"));
        uploadResult.put("isImage", isImage);
        if (isImage) {
            uploadResult.put("width", width);
            uploadResult.put("height", height);
        }

        return uploadResult;
    }
    /**
     * 下载文件 返回文件的字节流
     * @param group 文件的group
     * @param fastdfsId 文件的fasefs
     * @return
     */
    public static byte[] downStorage(String group, String fastdfsId){
        if(null == logger){
            logger =  LoggerFactory.getLogger( StorageUtils.class ) ;
        }
        Validate.notEmpty(group, "groupName can\'t be empty.", new Object[0]);
        Validate.notEmpty(fastdfsId, "FastFS_Id can\'t be empty.", new Object[0]);
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        byte[] fileByte = null;
        try{
            storageClient = getStorageClient();
            trackerServer = getTrackerServer();
            long e = System.currentTimeMillis();
            fileByte = storageClient.download_file( group, fastdfsId);
            logger.info("download fileName:" + fastdfsId + " speed time: " + (System.currentTimeMillis() - e) + " ms");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (MyException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }finally {
            if(null != trackerServer) {
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
        return fileByte;
    }

    /**
     * 删除文件
     * @param group 文件的分组ID
     * @param fastdfsId 文件的fastdfsID
     * @return
     */
    public static int deteleStorageFile(String group,String fastdfsId){
        if(null == logger){
            logger =  LoggerFactory.getLogger( StorageUtils.class ) ;
        }
        Validate.notEmpty(group, "groupName can\'t be empty.", new Object[0]);
        Validate.notEmpty(fastdfsId, "FastFS_Id can\'t be empty.", new Object[0]);
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        int deleteFileCount = 0;
        try{
            storageClient = getStorageClient();
            trackerServer = getTrackerServer();
            deleteFileCount = storageClient.delete_file(  group ,fastdfsId );
            logger.info( "delete file("+ fastdfsId +") from FastFS");
        }catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (MyException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }finally {
            if(null != trackerServer) {
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
        return deleteFileCount;
    }
}
