package com.gsafety.xseed.system1.bz1.service.serviceimpl;


import com.gsafety.xseed.system1.bz1.contract.model.AppUserInfo;

import com.gsafety.xseed.system1.bz1.contract.service.UserService;
import com.gsafety.xseed.system1.bz1.service.datamappers.UserMapper;
import com.gsafety.xseed.system1.bz1.service.entity.AppUserEntity;
import com.gsafety.xseed.system1.bz1.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/3/3.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public AppUserInfo save(AppUserInfo appUserInfo) {
        AppUserEntity userEntity = userMapper.modelToEntity(appUserInfo);
        return userMapper.entityToModel( userRepository.save( userEntity));
    }

    @Override
    public List<AppUserInfo> findAll() {

        final List<AppUserEntity> resultList = new ArrayList<>();
        final Iterable<AppUserEntity> all = userRepository.findAll();
        all.forEach(appUser -> resultList.add(appUser));
        return userMapper.entitiestoModels(resultList);
    }

    @Override
    public boolean testTrans() {

        userRepository.save(new AppUserEntity("nm11"));
        userRepository.save(new AppUserEntity("nm21"));
        userRepository.save(new AppUserEntity("namelengthmorethen10"));
        userRepository.save(new AppUserEntity("nm31"));
        userRepository.save(new AppUserEntity("nm41"));
        return true;
    }
}
