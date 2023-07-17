package com.czj.controller.com;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czj.controller.util.DEA;
import com.czj.pojo.Notice;
import com.czj.pojo.view.NoticeView;
import com.czj.service.NoticeService;
import com.czj.service.NoticeViewService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/Notice")
public class NoticeCon {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NoticeViewService noticeViewService;

    // 分页查询数据结构
    @ToString
    static class SearchForm{
        public String title;
        public int pageSize;
        public int currentPage;
    }

    /**
     * 分页条件查询课程中的通知
     * @return 数据集
     */
    @PostMapping("/getNoticeByTitle")
    public DEA getNoticeByTitle(@RequestBody SearchForm searchNotice, HttpServletRequest request){
        // 获取课程id
        HttpSession session = request.getSession();
        int classId = (int) session.getAttribute("classId");
        String title = searchNotice.title;
        // 封装查询类
        QueryWrapper<NoticeView> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.eq("class_id", classId);
        queryWrapper.orderByDesc("create_time");
        // 查询页面总数
        int count = noticeViewService.count(queryWrapper);
        int currentPage = searchNotice.currentPage;
        int pageSize = searchNotice.pageSize;
        // 超界检测-默认归零
        if(count < (currentPage - 1) * pageSize){
            currentPage = 1;
        }
        // 封装分页信息
        IPage<NoticeView> page = new Page<>(currentPage,pageSize);
        return  new DEA(noticeViewService.page(page,queryWrapper));
    }

    /**
     * 添加一条通知到数据库
     * @param notice 包含标题和内容
     * @return 成功标记
     */
    @PostMapping("/createNotice")
    public DEA createNotice(@RequestBody Notice notice, HttpServletRequest request){
        // 获取课程id
        HttpSession session = request.getSession();
        int classId = (int) session.getAttribute("classId");
        // 获取当前时间
        Date currentTime = new Date();
        notice.setCreateTime(currentTime);
        notice.setClassId(classId);
        notice.setHits(0);
        return  new DEA(noticeService.save(notice));
    }

    /**
     * 通知点击量增加
     * @param notice 讨论数据
     */
    @PostMapping("/addHits")
    public DEA addHits(@RequestBody Notice notice){
        Notice noticeCurrent = noticeService.getById(notice.getNoticeId());
        // 封装更新对象
        Notice noticeUpdate = new Notice();
        noticeUpdate.setHits(noticeCurrent.getHits()+1);
        // 封装查询类
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", noticeCurrent.getNoticeId());
        // 调用接口
        return new DEA(noticeService.update(noticeUpdate,queryWrapper));
    }

    /**
     * 更新通知
     * @param notice 包含标题、内容和通知id
     * @return 成功标记
     */
    @PostMapping("/updateNotice")
    public DEA updateNotice(@RequestBody Notice notice){
        int noticeId = notice.getNoticeId();
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id",noticeId);
        return  new DEA(noticeService.update(notice,queryWrapper));
    }

    /**
     * 删除一条通知
     * @param notice 包含id
     */
    @PostMapping("/deleteNoticeById")
    public DEA deleteNoticeById(@RequestBody Notice notice){
        return new DEA(noticeService.removeById(notice.getNoticeId()));
    }

}
