package com.gsafety.xseed.system1.bz1.contract.service;



import com.gsafety.xseed.system1.bz1.contract.model.AppUserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */
public interface UserService {

    AppUserInfo save(AppUserInfo appUserEntity);
    List<AppUserInfo> findAll();
    boolean testTrans();

}
