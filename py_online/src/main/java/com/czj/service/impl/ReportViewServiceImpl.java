package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.ReportViewMapper;
import com.czj.pojo.view.ReportView;
import com.czj.service.ReportViewService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class ReportViewServiceImpl extends ServiceImpl<ReportViewMapper, ReportView> implements ReportViewService {

}
