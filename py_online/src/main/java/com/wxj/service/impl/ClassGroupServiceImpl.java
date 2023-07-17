package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.ClassGroupMapper;
import com.wxj.pojo.ClassGroup;
import com.wxj.service.ClassGroupService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ClassGroupServiceImpl extends ServiceImpl<ClassGroupMapper, ClassGroup> implements ClassGroupService {

}
