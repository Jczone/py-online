package com.wxj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxj.mapper.NoticeViewMapper;
import com.wxj.pojo.view.NoticeView;
import com.wxj.service.NoticeViewService;
import org.springframework.stereotype.Service;


@Service // 此处使用了mp提供的通用service模板
public class NoticeViewServiceImpl extends ServiceImpl<NoticeViewMapper, NoticeView> implements NoticeViewService {

}
