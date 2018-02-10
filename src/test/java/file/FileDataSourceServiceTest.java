package file;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.poem.SpringBootTestApplication;
import org.poem.api.exception.PlatformException;
import org.poem.api.file.FileDataSourceService;
import org.poem.api.file.vo.FileDataSourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * Created by yineng on 2016/12/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootTestApplication.class)//工程启动的Application类
@WebAppConfiguration
@IntegrationTest
@Transactional
public class FileDataSourceServiceTest   {


    @Autowired
    private FileDataSourceService fileDataSourceService;

    @Test
    @Rollback(false)
    public void testSaveFileInfo(){
        FileDataSourceVO fileDataSourceVO = new FileDataSourceVO();
        try {
            String path = "G:\\02-myFolder\\01-BlogDemo\\src\\main\\java\\org\\poem\\common\\utils\\ZipUtils.java";
            File file = new File( path );
            fileDataSourceVO.setGoupName( "group1" );
            fileDataSourceVO.setFastdfsId( "M00/0D/4E/CgYAwVhY5V-EXLC2AAAAAHOY8v046.java" );
            fileDataSourceVO.setFileName( file.getName() );
            fileDataSourceVO.setBasePath( file.getPath() );
            fileDataSourceVO.setFileSize( String.valueOf( file.length() ) );
            fileDataSourceVO.setResource( "FileDataSourceServiceTest" );
            fileDataSourceVO.setRelativePath( file.getAbsolutePath() );
            fileDataSourceService.saveFileInfo( fileDataSourceVO );
        } catch (PlatformException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback(false)
    public void getFileInfoById(){
        String fastdFs = "M00/0D/4E/CgYAwVhY5V-EXLC2AAAAAHOY8v0461.java";
        FileDataSourceVO fileDataSourceVO;
        try {
            fileDataSourceVO = fileDataSourceService.getFileInfoById( fastdFs );
            System.err.print( fileDataSourceVO.getFileName() );
        } catch (PlatformException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback(false)
    public void deleteDataSource(){
        String fastdFs = "M00/0D/4E/CgYAwVhY5V-EXLC2AAAAAHOY8v046.java";
        try{
            fileDataSourceService.deleteFile( fastdFs );
        } catch (PlatformException e) {
            e.printStackTrace();
        }
    }
}
