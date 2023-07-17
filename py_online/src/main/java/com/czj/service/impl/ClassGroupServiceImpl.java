package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.ClassGroupMapper;
import com.czj.pojo.ClassGroup;
import com.czj.service.ClassGroupService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ClassGroupServiceImpl extends ServiceImpl<ClassGroupMapper, ClassGroup> implements ClassGroupService {

}
