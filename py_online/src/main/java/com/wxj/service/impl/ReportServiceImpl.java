package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.ReportMapper;
import com.wxj.pojo.Report;
import com.wxj.service.ReportService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

}
