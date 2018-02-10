package file;

import com.google.common.collect.Lists;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.poem.SpringBootTestApplication;
import org.poem.api.file.FileDataSourceService;
import org.poem.api.file.upload.FormFieldKeyValue;
import org.poem.api.file.upload.UploadFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by yineng on 2016/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootTestApplication.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class FileUploadDataSourceTest {

    @Autowired
    private FileDataSourceService fileDataSourceService;

    // 每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
    private static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH-lyw";

    private static String path = "G:\\02-myFolder\\01-BlogDemo\\src\\main\\java\\org\\poem\\common\\utils\\ZipUtils.java";
    /**
     * 本地静态资源
     */
    private static File file;

    /**
     * 在所有测试方法前执行一次
     * 在其中写上整体初始化的代码
     */
    @AfterClass
    public static void initFile() {
        file = new File( path );
    }

    /**
     * 在所有测试方法后执行一次，
     * 一般在其中写上销毁和释放资源的代码
     */
    @AfterClass
    public static void close() {
        System.out.println( "执行完成！" );
    }


    @Test(timeout = 3000)
    @Rollback(false)
    public void fileUpload() {
        String url = "http://10.6.22.216:8089/blog/fileManger/upload";
        List<FormFieldKeyValue> generalFormFields = Lists.newArrayList();
        List<UploadFileItem> uploadFileItems = Lists.newArrayList();
        UploadFileItem uploadFileItem = new UploadFileItem( "file", path );
        uploadFileItems.add( uploadFileItem );
        try {
            String result = sendHttpPostRequest( url, generalFormFields, uploadFileItems );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取链接
     *
     * @param serverUrl
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getConnection(String serverUrl) throws IOException {
        // 向服务器发送post请求
        URL url = new URL( serverUrl );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 发送POST请求必须设置如下两行
        connection.setDoOutput( true );
        connection.setDoInput( true );
        connection.setUseCaches( false );
        connection.setRequestMethod( "POST" );
        connection.setRequestProperty( "Connection", "Keep-Alive" );
        connection.setRequestProperty( "Charset", "UTF-8" );
        connection.setRequestProperty( "Content-Type", "multipart/form-data; boundary=" + BOUNDARY );
        connection.setRequestProperty( "X-Requested-With","XMLHttpRequest" );
        return connection;
    }

    /**
     * 文件上传服务
     *
     * @param serverUrl
     * @param generalFormFields
     * @param filesToBeUploaded
     * @return
     * @throws Exception
     */
    public static String sendHttpPostRequest(String serverUrl, List<FormFieldKeyValue> generalFormFields, List<UploadFileItem> filesToBeUploaded) throws Exception {
        HttpURLConnection connection = getConnection( serverUrl );
        // 传输内容
        StringBuffer contentBody = new StringBuffer( "--" + BOUNDARY );
        // 尾
        String endBoundary = "\r\n--" + BOUNDARY + "--\r\n";
        OutputStream out = connection.getOutputStream();
        // 1. 处理文字形式的POST请求
        for (FormFieldKeyValue formFieldKeyValue : generalFormFields) {
            contentBody.append( "\r\n" ).append( "Content-Disposition: form-data; name=\"" )
                    .append( formFieldKeyValue.getKey() + "\"" ).append( "\r\n" )
                    .append( "\r\n" ).append( formFieldKeyValue.getValue() )
                    .append( "\r\n" ).append( "--" ).append( BOUNDARY );
        }
        String boundaryMessage1 = contentBody.toString();
        out.write( boundaryMessage1.getBytes( "utf-8" ) );
        // 2. 处理文件上传
        for (UploadFileItem uploadFileItem : filesToBeUploaded) {
            contentBody = new StringBuffer();
            contentBody.append( "\r\n" ).append( "Content-Disposition:form-data; name=\"" ).append( uploadFileItem.getFormFieldName() + "\"; " ) // form中field的名称
                    .append( "filename=\"" ).append( uploadFileItem.getFileName() + "\"" ) // 上传文件的文件名，包括目录
                    .append( "\r\n" ).append( "Content-Type:application/octet-stream" ).append( "\r\n\r\n" );
            String boundaryMessage2 = contentBody.toString();
            out.write( boundaryMessage2.getBytes( "utf-8" ) );
            // 开始真正向服务器写文件
            File file = new File( uploadFileItem.getFileName() );
            DataInputStream dis = new DataInputStream( new FileInputStream( file ) );
            int bytes = 0;
            byte[] bufferOut = new byte[(int) file.length()];
            bytes = dis.read( bufferOut );
            out.write( bufferOut, 0, bytes );
            dis.close();
            contentBody.append( "------------" + BOUNDARY );
            String boundaryMessage = contentBody.toString();
            out.write( boundaryMessage.getBytes( "utf-8" ) );
        }
        out.write( ("------------" + BOUNDARY + "--\r\n").getBytes( "UTF-8" ) );
        // 3. 写结尾
        out.write( endBoundary.getBytes( "utf-8" ) );
        out.flush();
        out.close();
        // 4. 从服务器获得回答的内容
        String strLine = "";
        String strResponse = "";
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
        while ((strLine = reader.readLine()) != null) {
            strResponse += strLine + "\n";
        }
        return strResponse;
    }
}
