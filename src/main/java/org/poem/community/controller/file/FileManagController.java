package org.poem.community.controller.file;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.poem.api.exception.PlatformException;
import org.poem.api.file.FileDataSourceService;
import org.poem.api.file.vo.FileDataSourceVO;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.common.utils.JsonBean;
import org.poem.common.utils.RedisUtils;
import org.poem.common.utils.StorageUtils;
import org.poem.common.utils.URLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yineng on 2016/12/19.
 * 文件上传controller管理
 */
@Controller
@RequestMapping("/fileManger")
public class FileManagController {

    /**
     * 日志记录
     */
    private static Logger logger = LoggerFactory.getLogger( FileManagController.class );

    /**
     * 数据管理
     */
    @Autowired
    private FileDataSourceService fileDataSourceService;

    /**
     * The prefix_path.
     */
    public static String prefix_path;

    static {
        try {
            String classPath = FileManagController.class.getResource("/").toURI().getPath();
            File classesDir = new File(classPath);
            prefix_path = classesDir.getParent() + File.separator + "upload";
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
    }
    /**
     * 文件下载
     *
     * @param fastDFSId 文件的id
     * @return
     */
    @RequestMapping("download")
    public String downLoad(String fastDFSId, HttpServletResponse response, HttpServletRequest request) {
        if (StringUtils.isEmpty( fastDFSId )) {
            throw new RuntimeException( "file fastDFSId is not null" );
        }
        logger.info( "down file (" + fastDFSId + ") from storage." );
        ServletOutputStream out = null;
        FileDataSourceVO fileDataSourceVO = new FileDataSourceVO();
        try {
            fileDataSourceVO = this.fileDataSourceService.getFileInfoById( fastDFSId );
            String groupName = fileDataSourceVO.getGoupName();
            String filename = fileDataSourceVO.getFileName();
            byte[] bytes = StorageUtils.downStorage( groupName, fastDFSId );
            if (StringUtils.isNotEmpty( fileDataSourceVO.getBasePath() )) {
                String allFileName = fileDataSourceVO.getBasePath();
//                String showFileName = fileDataSourceVO.getBasePath().substring( allFileName.lastIndexOf( File.separator ) + 1 );
                String ext = fileDataSourceVO.getBasePath().substring( allFileName.lastIndexOf( "." ) + 1, allFileName.length() );
                if (ext.toLowerCase().endsWith( "zip" )) {
                    response.setContentType( "application/x-zip-compressed" );
                } else if (ext.toLowerCase().endsWith( "rar" )) {
                    response.setContentType( "application/octet-stream" );
                } else if (ext.toLowerCase().endsWith( "doc" )) {
                    response.setContentType( "application/msword" );
                } else if (ext.toLowerCase().endsWith( "xls" ) || ext.toLowerCase().endsWith( "csv" )) {
                    response.setContentType( "application/ms-excel" );
                } else if (ext.toLowerCase().endsWith( "pdf" )) {
                    response.setContentType( "application/pdf" );
                } else {
                    response.setContentType( "application/x-msdownload" );
                }
                String userAgent = request.getHeader( "User-Agent" ).toLowerCase();
                if (userAgent.contains( "firefox" )) {
                    response.addHeader( "Content-Disposition", "attachment;filename=\"" + new String( filename.getBytes( "gb2312" ), "iso-8859-1" )+ "\"" );
                } else {
                    response.addHeader( "Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode( filename, "UTF-8" ) + "\"" );
                }
            }
            out = response.getOutputStream();
            out.write( bytes );
        } catch (IOException e) {
            logger.error( e.getMessage(), e );
        } catch (PlatformException e) {
            logger.error( e.getMessage(), e );
        } finally {
            IOUtils.closeQuietly( out );
        }
        return  fileDataSourceVO.getBasePath();
    }

    /**
     * 上传文件
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request){
         /*模拟表单提交*/
        if(!ServletFileUpload.isMultipartContent(request)) {
            logger.error( "请模拟表单提交文件！" );
        }
        print( request );
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 得到上传的文件map集合对象
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        InputStream inputStream = null;
        String fileName = null;
        FileDataSourceVO fileDataSourceVO = null; ;
        Map<String,Object> resultMap ;
        try {
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                inputStream = entity.getValue().getInputStream();
                fileName = entity.getValue().getOriginalFilename();
                resultMap = StorageUtils.uploadFile(entity.getValue().getInputStream(), fileName);
                fileDataSourceVO = new FileDataSourceVO();
                fileDataSourceVO.setFileName(fileName);
                fileDataSourceVO.setFileSize(resultMap.get("fileSize").toString());
                fileDataSourceVO.setFastdfsId(resultMap.get("storageFileName").toString());
                fileDataSourceVO.setGoupName(resultMap.get("groupName").toString());
                fileDataSourceVO.setResource("FileManagController");
                fileDataSourceVO.setBasePath(URLUtil.getBasePath());
                this.fileDataSourceService.saveFileInfo(fileDataSourceVO);
            }
        }catch (IOException e){
            logger.info( e.getMessage(),e );
        }catch (PlatformException e){
            logger.info( e.getMessage(),e );
        } catch (Exception e) {
            logger.info( e.getMessage(),e );
        } finally {
            IOUtils.closeQuietly( inputStream );
        }
        if(null != fileDataSourceVO){
            return fileDataSourceVO.getFastdfsId();
        }
        return  "-1";
    }

    /**
     * 下载文件流
     * @param fastDFSId 文件的fastdsid
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/downStream")
    @ResponseBody
    public String downStream(String fastDFSId, HttpServletResponse response, HttpServletRequest request){
        logger.info( "down file (" + fastDFSId + ") from storage." );
        ServletOutputStream out = null;
        FileDataSourceVO fileDataSourceVO = new FileDataSourceVO();
        byte[] bytes;
        try {
            bytes = (byte[])RedisUtils.get( fastDFSId );
            if(null == bytes || bytes.length == 0){
                fileDataSourceVO = this.fileDataSourceService.getFileInfoById( fastDFSId );
                String groupName = fileDataSourceVO.getGoupName();
                bytes = StorageUtils.downStorage( groupName, fileDataSourceVO.getFastdfsId() );
            }
            if(null != bytes && bytes.length > 0){
                RedisUtils.set( fastDFSId,bytes,10,TimeUnit.MINUTES );
            }
            response.setContentType("application/octet-stream");
            if (StringUtils.isNotEmpty( fileDataSourceVO.getBasePath() )) {
                String showfileName = fileDataSourceVO.getBasePath().substring(fileDataSourceVO.getBasePath().lastIndexOf(File.separator)+1);
                response.addHeader("Content-Disposition", "attachment;filename="+ new String(showfileName.getBytes("gb2312"), "iso-8859-1"));
            }
            out = response.getOutputStream();
            out.write( bytes );
        } catch (IOException e) {
            logger.error( e.getMessage(), e );
        } catch (PlatformException e) {
            logger.error( e.getMessage(), e );
        } finally {
            IOUtils.closeQuietly( out );
        }
        return  fileDataSourceVO.getBasePath();
    }

    /**
     * 删除
     * @param fastDFSId
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public JsonBean deleteFile(String fastDFSId){
        logger.info( "deletes file (" + fastDFSId + ") from storage." );
        FileDataSourceVO fileDataSourceVO  ;
        try{
            fileDataSourceVO = this.fileDataSourceService.deleteFile( fastDFSId );
            if(null != fileDataSourceVO){
                StorageUtils.deteleStorageFile( fileDataSourceVO.getGoupName(), fileDataSourceVO.getFastdfsId() );
            }
            return  new JsonBean(  );
        } catch (PlatformException e) {
            logger.error( e.getMessage(), e );
            return new JsonBean(e.getMessage(),-1);
        }
    }
    /**
     * Prints the.
     *
     * @param req the req
     */
    public void print(HttpServletRequest req) {
        logger.info("====para" + req.getParameterMap());
        logger.info("chunks=" + req.getParameter("chunks"));
        logger.info("chunk=" + req.getParameter("chunk"));
        logger.info("id=" + req.getParameter("id"));
        logger.info("lastModifiedDate=" + req.getParameter("lastModifiedDate"));
        logger.info("type=" + req.getParameter("type"));
        logger.info("size=" + req.getParameter("size"));
        logger.info("uid=" + req.getParameter("uid"));
        logger.info("====epara");
        logger.info("--------------------------------------->>>");
    }


    /**
     * 获取用户的头像
     * @param fastDFSId
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/getUserHeadUrl")
    @ResponseBody
    public String getUserHeadUrl(String fastDFSId,String userId, HttpServletResponse response, HttpServletRequest request){
        logger.info( "down file (" + fastDFSId + ") from storage." );
        ServletOutputStream out = null;
        /**获取当前的登录人的信息*/
        String key = userId + fastDFSId ;
        FileDataSourceVO fileDataSourceVO = new FileDataSourceVO();
        byte[] bytes;
        try {
            bytes = (byte[]) RedisUtils.get( key );
            if(null == bytes){
                fileDataSourceVO = this.fileDataSourceService.getFileInfoById( fastDFSId );
                String groupName = fileDataSourceVO.getGoupName();
                bytes = StorageUtils.downStorage( groupName, fileDataSourceVO.getFastdfsId() );
            }
            if (null != bytes && bytes.length >0 ){
                RedisUtils.set( key, bytes,7, TimeUnit.DAYS );
            }
            response.setContentType("application/octet-stream");
            if (StringUtils.isNotEmpty( fileDataSourceVO.getBasePath() )) {
                String showfileName = fileDataSourceVO.getBasePath().substring(fileDataSourceVO.getBasePath().lastIndexOf(File.separator)+1);
                response.addHeader("Content-Disposition", "attachment;filename="+ new String(showfileName.getBytes("gb2312"), "iso-8859-1"));
            }
            out = response.getOutputStream();
            out.write( bytes );
        } catch (IOException e) {
            logger.error( e.getMessage(), e );
        } catch (PlatformException e) {
            logger.error( e.getMessage(), e );
        } finally {
            IOUtils.closeQuietly( out );
        }
        return  fileDataSourceVO.getBasePath();
    }
}
