package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.AdminMapper;
import com.wxj.pojo.Admin;
import com.wxj.service.AdminService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}