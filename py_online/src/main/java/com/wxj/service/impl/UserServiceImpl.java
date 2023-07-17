package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.UserMapper;
import com.wxj.pojo.User;
import com.wxj.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
