package com.numsg.common.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
/**
 * FTPCLient回调
 * Created by nusmg 2017-12-18
 *
 * @param <T>
 */
@FunctionalInterface
public interface FTPClientCallback<T> {

    public T doTransfer(FTPClient ftp)throws IOException;

}
