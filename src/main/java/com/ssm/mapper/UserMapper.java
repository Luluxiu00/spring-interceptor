package com.ssm.mapper;

import com.ssm.pojo.User;

/**
 * Created by 326944 on 2017/7/12.
 */
public interface UserMapper {
    User login(String username, String password);
}
