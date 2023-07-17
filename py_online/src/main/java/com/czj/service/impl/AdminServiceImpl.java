package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.AdminMapper;
import com.czj.pojo.Admin;
import com.czj.service.AdminService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}