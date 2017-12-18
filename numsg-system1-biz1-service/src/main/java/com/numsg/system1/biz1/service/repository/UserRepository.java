package com.gsafety.xseed.system1.bz1.service.repository;

import com.gsafety.xseed.system1.bz1.service.entity.AppUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/3/3.
 */
@Repository
public interface UserRepository extends CrudRepository<AppUserEntity,Long> {
}
