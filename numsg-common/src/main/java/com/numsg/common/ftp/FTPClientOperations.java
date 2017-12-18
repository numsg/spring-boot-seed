package com.numsg.common.ftp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.net.ftp.FTPFile;

/**
 * FTP操作的客户端操作上传下载
 * Created by nusmg 2017-12-18
 */
public interface FTPClientOperations {
    /**
     * 文件上传的方法
     *
     * @param localFilePath  本地文件路径
     * @param ftpFilePath 远端文件路径
     * @return upload status
     * @throws IOException the io exception
     */
    public UploadStatus upload(String localFilePath, String ftpFilePath) throws IOException;

    /**
     * 文件上传的方法
     *
     * @param localFile 本地文件
     * @param remoteFilePath    远端文件路径
     * @return upload status
     * @throws IOException the io exception
     */
    public UploadStatus uploadCover(File localFile, String remoteFilePath) throws IOException;

    /**
     * 文件上传的方法
     *
     * @param localFilePath  本地文件路径
     * @param ftpFilePath 远端文件路径
     * @return upload status
     * @throws IOException the io exception
     */
    public UploadStatus uploadCover(String localFilePath, String ftpFilePath) throws IOException;

    /**
     * 文件上传的方法
     *
     * @param localFile 本地文件
     * @param remoteFilePath    远端文件路径
     * @return upload status
     * @throws IOException the io exception
     */
    public UploadStatus upload(File localFile, String remoteFilePath) throws IOException;


    /**
     * 从远程服务器目录下载文件到本地服务器目录中
     *
     * @param localdir     本地保存目录
     * @param remotedir    FTP下载服务器目录
     * @param localTmpFile 临时下载记录文件
     * @return 成功返回true ，否则返回false
     * @throws IOException the io exception
     */
    public Collection<String> downloadList(final String localdir, final String remotedir, final String localTmpFile) throws IOException;

    /**
     * 文件下载的方法
     *
     * @param ftpFilePath 远端文件路径
     * @param localFilePath  本地文件路径
     * @return download status
     * @throws IOException the io exception
     */
    public DownloadStatus downloadFile(String ftpFilePath, String localFilePath) throws IOException;

    /**
     * 查看服务器上文件列表方法
     *
     * @param remotedir the remotedir
     * @return ftp file [ ]
     * @throws IOException the io exception
     */
    public FTPFile[] list(final String remotedir) throws IOException;


    /**
     * Issue the FTP MDTM command (not supported by all servers) to retrieve the last
     * modification time of a file. The modification string should be in the
     * ISO 3077 form "YYYYMMDDhhmmss(.xxx)?". The timestamp represented should also be in
     * GMT, but not all FTP servers honour this.
     *
     * @param pathname The file path to query.
     * @return A string representing the last file modification time in <code>YYYYMMDDhhmmss</code> format
     * @throws IOException the io exception
     */
    public String getModificationTime(String pathname) throws IOException;


    /**
     * 文件下载的方法,比对本地版本文件存放的version-time与远端文件的version-time(updateTime),
     * 如果一致，则不下载，如果远端大于本地，则下载，同时更新本地版本
     *
     * @param ftpFilePath           远端文件路径
     * @param localFilePath            本地文件路径
     * @param localVersionFile 本地版本文件路径
     * @return download status
     * @throws IOException the io exception
     */
    public DownloadVersionStatus downloadFileWithVersion(String ftpFilePath, String localFilePath, String localVersionFile) throws IOException;

    /**
     * 文件夹下载的方法,比对本地版本文件中的version-time与远端文件夹中的版本文件的version-time(updateTime),
     * 如果一致，则不下载，如果远端大于本地，则下载，同时更新本地版本
     *
     * @param remoteFolder      远端文件夹
     * @param remoteVersionFile 远端版本文件
     * @param localFolder       本地文件夹
     * @param localVersionFile  本地版本文件路径
     * @return download status
     * @throws IOException the io exception
     */
    public DownloadVersionStatus downloadFolderWithVersion(String remoteFolder, String remoteVersionFile, String localFolder, String localVersionFile) throws IOException;
}
