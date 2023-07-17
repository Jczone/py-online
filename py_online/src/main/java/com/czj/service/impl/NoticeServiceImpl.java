package com.czj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czj.mapper.NoticeMapper;
import com.czj.pojo.Notice;
import com.czj.service.NoticeService;
import org.springframework.stereotype.Service;

@Service // 此处使用了mp提供的通用service模板
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
