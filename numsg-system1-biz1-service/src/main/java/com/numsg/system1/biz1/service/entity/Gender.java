package com.gsafety.xseed.system1.bz1.service.entity;


import com.gsafety.xseed.system1.bz1.service.entity.metadata.EnumValueContract;

/**
 * Created by chenwei on 2016/5/11.
 */
public enum Gender implements EnumValueContract<Gender> {
    /**
     *    male
     */
    MALE(0),

    /**
     *    female
     */
    FEMALE(1),

    /**
     *    unknown
     */
    UNKNOWN(2);

    Gender(int paramType) {
        this.paramType = paramType;
    }


    /**
     * Gets param type.
     *
     * @return the param type
     */
    @Override
    public int getParamType() {
        return paramType;
    }


    private int paramType;
}
