package com.numsg.system1.biz1.contract.service;


import com.numsg.system1.biz1.contract.model.AppUserInfo;

import java.util.List;

/**
 * Created by numsg on 2017/3/3.
 */
public interface UserService {

    AppUserInfo save(AppUserInfo appUserEntity);
    List<AppUserInfo> findAll();
    boolean testTrans();

}
