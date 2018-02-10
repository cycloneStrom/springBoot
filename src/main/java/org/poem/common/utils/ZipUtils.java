package org.poem.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * 压缩/解压缩zip包处理类
 */
public class ZipUtils {

    ZipUtils() {
    }

    /**
     * zip编码方式
     */
    public final static Charset charset = Charset.forName("GBK");

    private static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩
     *
     * @param zipFileName  压缩产生的zip包文件名--带路径,如果为null或空则默认按文件名生产压缩文件名
     * @param relativePath 相对路径，默认为空
     * @param directory    文件或目录的绝对路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zip(String zipFileName, String relativePath,
                           String directory) throws IOException {
        String fileName = zipFileName;
        if (fileName == null || fileName.trim().equals("")) {
            File temp = new File(directory);
            if (temp.isDirectory()) {
                fileName = directory + ".zip";
            } else {
                if (directory.indexOf(".") > 0) {
                    fileName = directory.substring(0, directory
                            .lastIndexOf("."))
                            + "zip";
                } else {
                    fileName = directory + ".zip";
                }
            }
        }
        ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(fileName));
        try {
            zip(zos, relativePath, directory);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != zos) {
                zos.close();
            }
        }
    }

    /**
     * 压缩
     *
     * @param zos          压缩输出流
     * @param relativePath 相对路径
     * @param absolutPath  文件或文件夹绝对路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zos, String relativePath, String absolutPath) throws IOException {
        File file = new File(absolutPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File tempFile = files[i];
                    if (tempFile.isDirectory()) {
                        String newRelativePath = relativePath + tempFile.getName()
                                + File.separator;
                        createZipNode(zos, newRelativePath);
                        zip(zos, newRelativePath, tempFile.getPath());
                    } else {
                        zipFile(zos, tempFile, relativePath);
                    }
                }
            }
        } else {
            zipFile(zos, file, relativePath);
        }
    }

    /**
     * 压缩文件
     *
     * @param zos          压缩输出流
     * @param file         文件对象
     * @param relativePath 相对路径
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zos, File file, String relativePath) throws IOException {
        ZipEntry entry = null;
        if (relativePath != null) {
            entry = new ZipEntry(relativePath + file.getName());
        } else {
            entry = new ZipEntry(file.getName());
        }
        zos.putNextEntry(entry);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int BUFFERSIZE = 2 << 10;
            int length = 0;
            byte[] buffer = new byte[BUFFERSIZE];
            while ((length = is.read(buffer, 0, BUFFERSIZE)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.flush();
            zos.closeEntry();
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    /**
     * 创建目录
     *
     * @param zos          zip输出流
     * @param relativePath 相对路径
     * @throws IOException
     */
    private static void createZipNode(ZipOutputStream zos, String relativePath)
            throws IOException {
        ZipEntry zipEntry = new ZipEntry(relativePath);
        zos.putNextEntry(zipEntry);
        zos.closeEntry();
    }

    /**
     * 解压缩zip包
     *
     * @param zipFilePath zip文件路径
     * @param targetPath  解压缩到的位置，如果为null或空字符串则默认解压缩到跟zip包同目录跟zip包同名的文件夹下
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String targetPath) throws IOException, IllegalArgumentException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath, charset);
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
            } else {
                directoryPath = targetPath;
            }
            Enumeration entryEnum = zipFile.entries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {
                        directoryPath = directoryPath + File.separator + zipEntry.getName();
                        continue;
                    }
                    if (zipEntry.getSize() > 0) {
                        // 文件
                        File targetFile = buildFile(directoryPath + File.separator + zipEntry.getName(), false);
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }

                        os.flush();
                        os.close();
                    } else {
                        // 空目录
                        buildFile(directoryPath + File.separator
                                + zipEntry.getName(), true);
                    }
                }
            }
        } catch (IOException ex) {
            throw ex;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } finally {
            if (null != zipFile) {
                zipFile.close();
                zipFile = null;
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
    }

    /**
     * 创建目录
     *
     * @param fileName
     * @param isDirectory
     * @return
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }

    /**
     * 转换图片格式
     *
     * @param imgFile
     * @param format
     * @param formatFile
     * @return
     */
    public static boolean convertImage(File imgFile, String format, File formatFile) {
        try {
            BufferedImage bIMG = ImageIO.read(imgFile);
            ImageIO.write(bIMG, format, formatFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static int[] getResolution(File file) {
        int[] resolution = new int[2];
        try {
            BufferedImage image = ImageIO.read(file);
            resolution[0] = image.getWidth();
            resolution[1] = image.getHeight();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            resolution[0] = 0;
            resolution[1] = 0;
        }
        return resolution;
    }

    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                //关闭流
                try {
                    if (null != bis) bis.close();
                    if (null != zos) zos.close();
                    if (null != fis) fis.close();
                    if (null != fos) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }
}