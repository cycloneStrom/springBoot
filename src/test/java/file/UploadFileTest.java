package file;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.poem.common.utils.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yineng on 2016/12/19.
 */
public class UploadFileTest {

    @Test
    public void uploadTest() {
        String path = "G:\\02-myFolder\\01-BlogDemo\\src\\main\\java\\org\\poem\\common\\utils\\ZipUtils.java";
        File file = new File( path );
        StorageUtils.uploadFile( file, file.getName() );
    }


    @Test
    public void downFileTest() {
        byte[] bytes = StorageUtils.downStorage( "group1", "M00/0D/4E/CgYAwVhXlkyEWAlJAAAAAApZtr816.java" );
        if (null != bytes && bytes.length > 0) {
            File file = new File( "G:\\02-myFolder\\ZipUtils.java" );
            BufferedOutputStream bufferedOutputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream( file );
                bufferedOutputStream = new BufferedOutputStream( fileOutputStream );
                bufferedOutputStream.write( bytes, 0, bytes.length );
                bufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly( fileOutputStream );
                IOUtils.closeQuietly( bufferedOutputStream );
            }
        }
    }

    @Test
    public void deletFile(){
        StorageUtils.deteleStorageFile( "group1", "M00/0D/4E/CgYAwVhXlkyEWAlJAAAAAApZtr816.java" );
    }
}
