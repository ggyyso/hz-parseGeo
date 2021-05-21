package com.common.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * description: RecursiveZipUtils <br>
 * date: 2020/3/23 14:16 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class RecursiveZipUtils {
    /**
     * 解压到指定目录
     *
     * @param zipPath
     * @param descDir
     * @author isea533
     */
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * @param path          要压缩的路径, 可以是目录, 也可以是文件.
     * @param basePath      如果path是目录,它一般为new File(path), 作用是:使输出的zip文件以此目录为根目录,
     *                      如果为null它只压缩文件, 不解压目录.
     * @param zo            压缩输出流
     * @param isRecursive   是否递归
     * @param isOutBlankDir 是否输出空目录, 要使输出空目录为true,同时baseFile不为null.
     * @throws IOException
     */
    public static void zip(String path, File basePath, ZipOutputStream zo,
                           boolean isRecursive, boolean isOutBlankDir)
            throws IOException {
        try {

            File inFile = new File(path);

            File[] files = new File[0];
            if (inFile.isDirectory()) { // 是目录
                files = inFile.listFiles();
            } else if (inFile.isFile()) { // 是文件
                files = new File[1];
                files[0] = inFile;
            }
            byte[] buf = new byte[1024];
            int len;
            // System.out.println("baseFile: "+baseFile.getPath());
            for (int i = 0; i < files.length; i++) {
                String pathName = "";
                if (basePath != null) {
                    if (basePath.isDirectory()) {
                        pathName = files[i].getPath().substring(basePath.getPath().length() + 1);
                    } else {// 文件
                        pathName = files[i].getPath().substring(basePath.getParent().length() + 1);
                    }
                } else {
                    pathName = files[i].getName();
                }
                System.out.println(pathName);
                if (files[i].isDirectory()) {
                    if (isOutBlankDir && basePath != null) {
                        zo.putNextEntry(new ZipEntry(pathName + "/")); // 可以使空目录也放进去
                    }
                    if (isRecursive) { // 递归
                        zip(files[i].getPath(), basePath, zo, isRecursive, isOutBlankDir);
                    }
                } else {
                    FileInputStream fin = new FileInputStream(files[i]);
                    zo.putNextEntry(new ZipEntry(pathName));
                    while ((len = fin.read(buf)) > 0) {
                        zo.write(buf, 0, len);
                    }
                    fin.close();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 采用apache ant 解压文件到指定目录
     *
     * @param zipFile
     * @param descDir
     * @author isea533
     */

    public static boolean unZipFiles(File zipFile, String descDir) throws IOException {
        // 创建文件路径对象
        try {
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            // 创建zip文件对象
            ZipFile zip = new ZipFile(zipFile);
            // 得到zip文件条目枚举对象
            Enumeration<?> zipEnum = zip.getEntries();
            while (zipEnum.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEnum.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                // 输出文件路径信息
                System.out.println(outPath);

                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            zip.close();
            System.out.println("******************解压完毕********************");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println("====开始====");
        try {
            OutputStream os = new FileOutputStream("C:/doc-recursive.zip");
            BufferedOutputStream bs = new BufferedOutputStream(os);
            ZipOutputStream zo = new ZipOutputStream(bs);
            zo.setEncoding("GBK");

            // recursiveZip.zip("D:/recursive-zip/新建文本文档.txt", new
            // File("D:/recursive-zip"), zo, true, true);
            RecursiveZipUtils.zip("D:\\javaIo", new File("D:\\javaIo"), zo, true, true);

            zo.closeEntry();
            zo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("====完成====");
    }
}
