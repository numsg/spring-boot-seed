package com.numsg.common.ftp;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类
 * <p>
 * Created by nusmg 2017-12-18
 */
public class FileOperateUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileOperateUtils.class);

    private FileOperateUtils() {
    }

    /**
     * 查找需要合并文件的临时文件信息
     *
     * @param directory       临时文件所在的目录
     * @param prefix          临时文件的前缀
     * @param caseSensitivity 临时文件的大小写敏感
     * @return collection
     */
    public static Collection<File> searchPrefixFile(File directory, String prefix, boolean
            caseSensitivity) {
        IOCase iocase = IOCase.INSENSITIVE;
        if (caseSensitivity) {
            iocase = IOCase.SENSITIVE;
        }
        //创建相关的过滤器
        IOFileFilter fileFilter = FileFilterUtils.prefixFileFilter(prefix, iocase);
        //检查相关的过滤信息
        return FileUtils.listFiles(directory, fileFilter, FalseFileFilter.INSTANCE);
    }

    /**
     * 查找目录下特定后缀的文件
     *
     * @param directory  特定的目录
     * @param extensions 临时文件的后缀扩展
     * @param recursive  是否查询临时文件所在的目录下的子目录
     * @return collection
     */
    public static Collection<File> searchExtensionFile(File directory, String[]
            extensions, boolean recursive) {
        return FileUtils.listFiles(directory, extensions, recursive);
    }

    /**
     * 文件追加功能
     *
     * @param lines    要写入的内容行
     * @param filePath 文件路径
     * @param append   是否追加
     */
    public static void writeLinesToFile(Collection<String> lines, String filePath, boolean append) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(filePath, append);
            IOUtils.writeLines(lines, "", output, "UTF-8");
        } catch (Exception e) {
            logger.error(filePath + "追加文件失败" + e.getMessage(), e);
        }
    }


    /**
     * 读取文件内容首行
     *
     * @param filePath 文件路径
     * @return the string
     */
    public static String readFirstLineFromFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        return readFirstLineFromFile(new File(filePath));

    }


    /**
     * 读取文件内容首行
     *
     * @param file 文件对象
     * @return the string
     */
    public static String readFirstLineFromFile(File file) {
        List<String> lines = readLinesFromFile(file);
        if (!lines.isEmpty()) {
            return lines.get(0);
        } else {
            return null;
        }

    }

    /**
     * 读取文件内容行
     *
     * @param filePath 文件路径
     * @return the list
     */
    public static List<String> readLinesFromFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return new ArrayList<>();
        }
        return readLinesFromFile(new File(filePath));
    }

    /**
     * 读取文件内容行
     *
     * @param file 文件对象
     * @return the list
     */
    public static List<String> readLinesFromFile(File file) {
        List<String> rltStr = new ArrayList<>();
        if (file == null || !file.exists()) {
            return rltStr;
        }
        try (InputStream in = new FileInputStream(file)) {
            rltStr = IOUtils.readLines(in, "UTF-8");
        } catch (Exception e) {
            logger.error(file.getName() + "读取文件信息失败：" + e.getMessage(), e);
        }
        return rltStr;
    }

    /**
     * 通过递归调用删除一个文件夹下面的所有文件(不删除自己)
     *
     * @param folderPath 本地文件夹路径
     */
    public static void deleteFolderDeep(String folderPath) {
        File folder = new File(folderPath);
        deleteFolderDeep(folder);
    }

    /**
     * 通过递归调用删除一个文件夹下面的所有文件(不删除自己)
     *
     * @param file the file
     */
    public static void deleteFolderDeep(File file) {
        if (file.isFile()) {//表示该文件不是文件夹
            file.delete();
        } else {
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                File childFile = new File(file.getAbsolutePath() + "\\" + childFilePath);
                deleteDeep(childFile);
            }
        }
    }

    /**
     * 通过递归调用删除一个文件夹及下面的所有文件
     *
     * @param file the file
     */
    public static void deleteDeep(File file) {
        if (file.isFile()) {//表示该文件不是文件夹
            file.delete();
        } else {
            //首先得到当前的路径
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                File childFile = new File(file.getAbsolutePath() + "\\" + childFilePath);
                deleteDeep(childFile);
            }
            file.delete();
        }
    }


}
