package com.numsg.common.ftp;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Ftp回调模板
 * <p>
 * Created by nusmg 2017-12-18
 */
public class FTPClientImpl implements FTPClientOperations {
    private static final Logger logger = LoggerFactory.getLogger(FTPClientImpl.class);

    private static final String DEAFULT_REMOTE_CHARSET = "UTF-8";
    private static final int DEAFULT_REMOTE_PORT = 21;
    private static String separator = File.separator;
    private FTPClientConfig ftpClientConfig;
    private String host;
    private String username;
    private String password;
    private String port;

    /**
     * Instantiates a new Ftp client.
     *
     * @param host the host
     * @param user the user
     * @param pwd  the pwd
     * @param port the port
     */
    public FTPClientImpl(String host, String user, String pwd, String port) {
        this.host = host;
        this.username = user;
        this.password = pwd;
        this.port = port;
    }

    /**
     * 查看服务器上文件列表方法
     *
     * @param remotedir
     * @return
     * @throws IOException
     */
    @Override
    public FTPFile[] list(final String remotedir) throws IOException {
        return execute((FTPClient ftp) -> ftp.listFiles(remotedir));
    }

    @Override
    public String getModificationTime(String pathname) throws IOException {
        return execute((FTPClient ftp) -> ftp.getModificationTime(pathname));
    }

    /**
     * 文件下载的方法,比对本地版本文件存放的version-time与远端文件的version-time(updateTime),
     * 如果一致，则不下载，如果远端大于本地，则下载，同时更新本地版本
     *
     * @param ftpFilePath          远端文件路径
     * @param localFilePath        本地文件路径
     * @param localVersionFilePath 本地版本文件路径
     * @return
     * @throws IOException
     */
    @Override
    public DownloadVersionStatus downloadFileWithVersion(String ftpFilePath, String localFilePath, String localVersionFilePath) throws IOException {
        return execute((FTPClient ftpClient) -> {
                    DownloadVersionStatus versionStatus = new DownloadVersionStatus();
                    DownloadStatus downloadStatus = DownloadStatus.Download_New_Failed;
                    boolean needDown = needUpdate(ftpClient, localVersionFilePath, ftpFilePath);
                    String ftpVersion = ftpClient.getModificationTime(ftpFilePath);
                    if (needDown) {
                        //delete old local file
                        File localFIle = new File(localFilePath);
                        if (localFIle.exists()) {
                            localFIle.delete();
                        }
                        //download ftp file
                        downloadStatus = FtpHelper.getInstance().download(ftpClient, ftpFilePath, localFilePath);
                        //write local version
                        List<String> stringVersion = new ArrayList<>();
                        stringVersion.add(ftpVersion);
                        FileOperateUtils.writeLinesToFile(stringVersion, localVersionFilePath, false);
                    } else {
                        downloadStatus = DownloadStatus.No_Need_Download;
                    }

                    versionStatus.setDownloadStatus(downloadStatus);
                    versionStatus.setFtpVersionStr(ftpVersion);
                    //更新完成后，本地与ftp版本一致
                    versionStatus.setLocalVersionStr(ftpVersion);
                    return versionStatus;
                }
        );
    }

    /**
     * 文件夹下载的方法,比对本地版本文件中的version-time与远端文件夹中的版本文件的version-time(updateTime),
     * 如果一致，则不下载，如果远端大于本地，则下载，同时更新本地版本
     *
     * @param remoteFolder         远端文件夹
     * @param remoteVersionFile    远端版本文件
     * @param localFolder          本地文件夹
     * @param localVersionFilePath 本地版本文件路径
     * @return
     * @throws IOException
     */
    @Override
    public DownloadVersionStatus downloadFolderWithVersion(String remoteFolder, String remoteVersionFile, String
            localFolder, String localVersionFilePath) throws IOException {
        return execute((FTPClient ftpClient) -> {
                    DownloadVersionStatus versionStatus = new DownloadVersionStatus();

                    DownloadStatus downloadStatus = DownloadStatus.Download_New_Failed;
                    boolean needDown = needUpdate(ftpClient, localVersionFilePath, remoteVersionFile);
                    String ftpVersion = ftpClient.getModificationTime(remoteVersionFile);
                    if (needDown) {
                        //delete old folder file
                        File localFile = new File(localFolder);
                        if (!localFile.exists() || !localFile.isDirectory()) {
                            localFile.mkdirs();
                        }

                        //localFile.delete()
                        FileOperateUtils.deleteFolderDeep(localFolder);
                        //download ftp file
                        Collection<String> downloadStrs = downloadFolderFiles(ftpClient, localFolder, remoteFolder, "");
                        if (!downloadStrs.isEmpty()) {
                            downloadStatus = DownloadStatus.Download_New_Success;
                        }
                        //write local version
                        List<String> stringVersion = new ArrayList<>();
                        stringVersion.add(ftpVersion);
                        FileOperateUtils.writeLinesToFile(stringVersion, localVersionFilePath, false);
                    } else {
                        downloadStatus = DownloadStatus.No_Need_Download;
                    }
                    versionStatus.setDownloadStatus(downloadStatus);
                    versionStatus.setFtpVersionStr(ftpVersion);
                    //更新完成后，本地与ftp版本一致
                    versionStatus.setLocalVersionStr(ftpVersion);
                    return versionStatus;
                }
        );
    }

    /**
     * 文件上传的方法
     *
     * @param ftpFilePath
     * @param localFilePath
     * @return
     * @throws IOException
     */
    @Override
    public UploadStatus upload(final String localFilePath, final String ftpFilePath) throws IOException {
        return execute((FTPClient ftpClient) ->
                FtpHelper.getInstance().upload(ftpClient, localFilePath, ftpFilePath)
        );
    }

    @Override
    public UploadStatus upload(File localFile, String remoteFilePath) throws IOException {
        return execute((FTPClient ftpClient) ->
                FtpHelper.getInstance().upload(ftpClient, localFile, remoteFilePath)
        );
    }

    @Override
    public UploadStatus uploadCover(File localFile, String remoteFilePath) throws IOException {
        return execute((FTPClient ftpClient) -> {
                    boolean deleteRemote = ftpClient.deleteFile(remoteFilePath);
                    if (deleteRemote) {
                        return FtpHelper.getInstance().upload(ftpClient, localFile, remoteFilePath);
                    } else {
                        return UploadStatus.Upload_New_File_Failed;
                    }
                }
        );
    }

    @Override
    public UploadStatus uploadCover(String localFilePath, String ftpFilePath) throws IOException {
        return execute((FTPClient ftpClient) -> {
                    boolean deleteRemote = ftpClient.deleteFile(ftpFilePath);
                    if (deleteRemote) {
                        return FtpHelper.getInstance().upload(ftpClient, localFilePath, ftpFilePath);
                    } else {
                        return UploadStatus.Upload_New_File_Failed;
                    }
                }
        );
    }

    /**
     * 上传文件到服务器,新上传和断点续传
     *
     * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile  本地文件File句柄，绝对路径
     * @param ftpClient  FTPClient引用
     * @param remoteSize 需要显示的处理进度步进值
     * @return upload status
     * @throws IOException the io exception
     */

    public UploadStatus uploadFile(String remoteFile, File localFile,
                                   FTPClient ftpClient, long remoteSize) throws IOException {
        return FtpHelper.getInstance().uploadFile(remoteFile, localFile, ftpClient, remoteSize);
    }


    /**
     * 从远程服务器目录下载文件到本地服务器目录中
     *
     * @param localdir     本地保存目录
     * @param remotedir    FTP下载服务器目录
     * @param localTmpFile 临时下载记录文件
     * @return 成功下载记录
     */
    @Override
    public Collection<String> downloadList(final String localdir, final String remotedir, final String localTmpFile) throws IOException {
        return execute((final FTPClient ftp) ->
                downloadFolderFiles(ftp, localdir, remotedir, localTmpFile)
        );
    }

    /**
     * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
     *
     * @param remote        远程文件路径
     * @param localFilePath 本地文件路径
     * @return 上传的状态
     * @throws IOException
     */
    @Override
    public DownloadStatus downloadFile(final String remote, final String localFilePath) throws IOException {
        return execute((FTPClient ftpClient) -> FtpHelper.getInstance().download(ftpClient, remote, localFilePath)
        );
    }

    /**
     * 执行FTP回调操作的方法
     *
     * @param <T>      the type parameter
     * @param callback 回调的函数
     * @return the t
     * @throws IOException the io exception
     */
    public <T> T execute(FTPClientCallback<T> callback) throws IOException {
        FTPClient ftp = new FTPClient();
        try {
            //登录FTP服务器
            try {
                //设置超时时间
                ftp.setDataTimeout(7200);
                //设置默认编码
                ftp.setControlEncoding(DEAFULT_REMOTE_CHARSET);
                //设置默认端口
                ftp.setDefaultPort(DEAFULT_REMOTE_PORT);
                //设置是否显示隐藏文件
                ftp.setListHiddenFiles(false);
                //连接ftp服务器
                if (StringUtils.isNotEmpty(port) && NumberUtils.isDigits(port)) {
                    ftp.connect(host, Integer.valueOf(port));
                } else {
                    ftp.connect(host);
                }
            } catch (ConnectException e) {
                logger.error("连接FTP服务器失败：" + ftp.getReplyString() + ftp.getReplyCode());
                throw new IOException("Problem connecting the FTP-server fail", e);
            }
            //得到连接的返回编码
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            //登录失败权限验证失败
            if (!ftp.login(getUsername(), getPassword())) {
                ftp.quit();
                ftp.disconnect();
                logger.error("连接FTP服务器用户或者密码失败：：" + ftp.getReplyString());
                throw new IOException("Cant Authentificate to FTP-Server");
            }
            if (logger.isDebugEnabled()) {
                logger.info("成功登录FTP服务器：" + host + " 端口：" + port);
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //回调FTP的操作
            return callback.doTransfer(ftp);
        } finally {
            //FTP退出
            ftp.logout();
            //断开FTP连接
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
        }
    }

    /**
     * Resolve file string.
     *
     * @param file the file
     * @return the string
     */
    protected String resolveFile(String file) {
        return null;
        //return file.replace(System.getProperty("file.separator").charAt(0), this.remoteFileSep.charAt(0))
    }

    /**
     * 获取FTP的配置操作系统
     *
     * @return ftp client config
     */
    public FTPClientConfig getFtpClientConfig() {
        //获得系统属性集
        Properties props = System.getProperties();
        //操作系统名称
        String osname = props.getProperty("os.name");
        //针对window系统
        if (osname.toLowerCase().contains("windows")) {
            ftpClientConfig = new FTPClientConfig(FTPClientConfig.SYST_NT);
            //针对linux系统
        } else if (osname.toLowerCase().contains("linux")) {
            ftpClientConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        }
        if (logger.isDebugEnabled()) {
            logger.info("the ftp client system os Name " + osname);
        }
        return ftpClientConfig;
    }


    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * 是否需要更新
     *
     * @param ftpClient         ftp client实例
     * @param localVerisonPath  本地版本文件路径（文件中第一行存储的版本号:yyyyMMddHHmmss）
     * @param remoteVersionPath 远程文件路径(使用最后更新时间作为版本号)
     * @return
     * @throws IOException
     */
    public boolean needUpdate(FTPClient ftpClient, String localVerisonPath, String remoteVersionPath) throws IOException {
        File localVersionFile = new File(localVerisonPath);
        if (!localVersionFile.exists()) {
            localVersionFile.createNewFile();
            return true;
        }
        String localVersionStr = FileOperateUtils.readFirstLineFromFile(localVersionFile);
        if (StringUtils.isEmpty(localVersionStr)) {
            return true;
        }
        String remoteVersionStr = ftpClient.getModificationTime(remoteVersionPath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date remoteVersion = dateFormat.parse(remoteVersionStr);
            if (remoteVersion.after(dateFormat.parse(localVersionStr))) {
                return true;
            }
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }


    /**
     * 从远程服务器目录下载文件到本地服务器目录中
     *
     * @param ftp          ftp对象
     * @param localdir     本地文件夹路径
     * @param remotedir    远程文件夹路径
     * @param localTmpFile 本地下载文件名记录
     * @return
     * @throws IOException
     */
    private Collection<String> downloadFolderFiles(FTPClient ftp, final String localdir, final String remotedir, final String localTmpFile) throws IOException {
        //切换到下载目录的中
        ftp.changeWorkingDirectory(remotedir);
        //获取目录中所有的文件信息
        FTPFile[] ftpfiles = ftp.listFiles();
        Collection<String> fileNamesCol = new ArrayList<>();
        //判断文件目录是否为空
        if (!ArrayUtils.isEmpty(ftpfiles)) {
            for (FTPFile ftpfile : ftpfiles) {
                String remoteFilePath = ftpfile.getName();
                String localFilePath = localdir + separator + ftpfile.getName();
                //System.out.println("remoteFilePath ="+remoteFilePath +" localFilePath="+localFilePath)
                //单个文件下载状态
                DownloadStatus downStatus = FtpHelper.getInstance().download(ftp, remoteFilePath, localFilePath);
                if (downStatus == DownloadStatus.Download_New_Success) {
                    //临时目录中添加记录信息
                    fileNamesCol.add(remoteFilePath);
                }
            }
        }
        if (!fileNamesCol.isEmpty() && !StringUtils.isEmpty(localTmpFile)) {
            FileOperateUtils.writeLinesToFile(fileNamesCol, localTmpFile, false);
        }
        return fileNamesCol;
    }

}