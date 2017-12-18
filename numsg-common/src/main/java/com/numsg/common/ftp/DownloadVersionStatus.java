package com.numsg.common.ftp;

public class DownloadVersionStatus {

    private DownloadStatus downloadStatus;
    private String localVersionStr;
    private String ftpVersionStr;

    /**
     * 获取下载状态.
     *
     * @return the download status
     */
    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    /**
     * 设置下载状态.
     *
     * @param downloadStatus the download status
     */
    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    /**
     * 获取本地文件版本号
     *
     * @return the local version str
     */
    public String getLocalVersionStr() {
        return localVersionStr;
    }

    /**
     * 设置本地文件版本号
     *
     * @param localVersionStr the local version str
     */
    public void setLocalVersionStr(String localVersionStr) {
        this.localVersionStr = localVersionStr;
    }

    /**
     * 获取ftp文件版本号
     *
     * @return the ftp version str
     */
    public String getFtpVersionStr() {
        return ftpVersionStr;
    }

    /**
     * 设置ftp文件版本号
     *
     * @param ftpVersionStr the ftp version str
     */
    public void setFtpVersionStr(String ftpVersionStr) {
        this.ftpVersionStr = ftpVersionStr;
    }
}
