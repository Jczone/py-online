package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.ReportMapper;
import com.czj.pojo.Report;
import com.czj.service.ReportService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

}
