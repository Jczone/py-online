package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.ExperimentMapper;
import com.czj.pojo.Experiment;
import com.czj.service.ExperimentService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ExperimentServiceImpl extends ServiceImpl<ExperimentMapper, Experiment> implements ExperimentService {


}
