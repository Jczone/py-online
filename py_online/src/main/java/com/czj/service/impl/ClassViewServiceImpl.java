package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.ClassViewMapper;
import com.czj.pojo.view.ClassView;
import com.czj.service.ClassViewService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ClassViewServiceImpl extends ServiceImpl<ClassViewMapper, ClassView> implements ClassViewService {

}
