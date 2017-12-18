package com.gsafety.xseed.system1.bz1.service.datamappers;


import com.gsafety.xseed.system1.bz1.contract.model.AppUserInfo;
import com.gsafety.xseed.system1.bz1.service.entity.AppUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by xiaodiming on 2017/3/8.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    AppUserInfo entityToModel(AppUserEntity appUserEntity);

    @Mapping(ignore = true, target = "id")
    AppUserEntity modelToEntity(AppUserInfo appUserInfo);

    List<AppUserInfo> entitiestoModels(List<AppUserEntity> appUserEntity);

    List<AppUserEntity> modelstoEntities(List<AppUserInfo> appUserEntity);
}
