package com.ssm.service;

import com.ssm.pojo.User;

/**
 * Created by 326944 on 2017/7/17.
 */
public interface UserService {
    User login(String username, String password);
}
