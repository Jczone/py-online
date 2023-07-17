package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.ClassUserMapper;
import com.wxj.pojo.ClassUser;
import com.wxj.service.ClassUserService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ClassUserServiceImpl extends ServiceImpl<ClassUserMapper, ClassUser> implements ClassUserService {

}
