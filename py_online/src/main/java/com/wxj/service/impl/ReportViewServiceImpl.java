package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.ReportViewMapper;
import com.wxj.pojo.view.ReportView;
import com.wxj.service.ReportViewService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ReportViewServiceImpl extends ServiceImpl<ReportViewMapper, ReportView> implements ReportViewService {

}
