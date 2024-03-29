package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.NoticeViewMapper;
import com.czj.pojo.view.NoticeView;
import com.czj.service.NoticeViewService;
import org.springframework.stereotype.Service;


@Service // 此处使用了mp提供的通用service模板
public class NoticeViewServiceImpl extends ServiceImpl<NoticeViewMapper, NoticeView> implements NoticeViewService {

}
