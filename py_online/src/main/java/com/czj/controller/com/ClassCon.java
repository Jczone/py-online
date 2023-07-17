package com.czj.controller.com;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.czj.controller.util.DEA;
import com.czj.pojo.ClassGroup;
import com.czj.pojo.ClassUser;
import com.czj.pojo.view.ClassView;
import com.czj.service.ClassGroupService;
import com.czj.service.ClassUserService;
import com.czj.service.ClassViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ClassCon")
public class ClassCon {

    @Autowired
    private ClassGroupService classGroupService;
    @Autowired
    private ClassUserService classUserService;
    @Autowired
    private ClassViewService classViewService;

    /**
     * 查询用户创建的班级
     * @param request 提供用户id
     * @return List
     */
    @GetMapping("/getClassGroup")
    public DEA getClassGroup(HttpServletRequest request){

        String userId = (String)request.getSession().getAttribute("userId");
        QueryWrapper<ClassGroup> classGroupQueryWrapper = new QueryWrapper<>();
        classGroupQueryWrapper.eq("user_id",userId);
        classGroupQueryWrapper.orderByDesc("create_time");
        return new DEA(classGroupService.list(classGroupQueryWrapper));
    }

    /**
     * 查询用户加入的班级
     * @param request 提供用户id
     * @return List
     */
    @GetMapping("/getJoinClass")
    public DEA getJoinClass(HttpServletRequest request){

        String userId = (String)request.getSession().getAttribute("userId");
        // 先查询用户加入的班级ids
        QueryWrapper<ClassUser> classUserQueryWrapper = new QueryWrapper<>();
        classUserQueryWrapper.eq("user_id",userId);
        classUserQueryWrapper.orderByAsc("create_time");
        List<ClassUser> classUsers = classUserService.list(classUserQueryWrapper);
        // 封装为Id列表
        List<Integer> classIds = new ArrayList<>();
        if(classUsers.size() > 0){
            for(ClassUser classUser : classUsers){
                classIds.add(classUser.getClassId());
            }
            return new DEA(classViewService.listByIds(classIds));
        }else{
            return new DEA(new ArrayList<>());
        }
    }

    /**
     * 创建新班级
     * @param classGroup 班级信息
     * @param request 用户id
     * @return List
     */
    @PostMapping("/createClassGroup")
    public DEA createClassGroup(@RequestBody ClassGroup classGroup, HttpServletRequest request){

        String userId = (String)request.getSession().getAttribute("userId");
        classGroup.setUserId(userId);
        classGroup.setIsPublic(1);
        classGroup.setIsStart(0);
        classGroup.setCreateTime(new Date());
        classGroup.setStuNum(0);
        return new DEA(classGroupService.save(classGroup));
    }

    /**
     * 修改班级数据
     * @param classGroup 班级信息-班级标题和简介-id
     * @return List
     */
    @PostMapping("/updateClassGroup")
    public DEA updateClassGroup(@RequestBody ClassGroup classGroup){
        UpdateWrapper<ClassGroup> classGroupUpdateWrapper = new UpdateWrapper<>();
        classGroupUpdateWrapper.eq("class_id",classGroup.getClassId()); // where class_id = 33
        classGroupUpdateWrapper.set("class_name",classGroup.getClassName());
        classGroupUpdateWrapper.set("describes",classGroup.getDescribes());

        return new DEA(classGroupService.update(classGroupUpdateWrapper));
    }

    /**
     * 删除班级及其所有数据
     * @param classGroup 班级信息
     * @return List
     */
    @PostMapping("/deleteClassGroup")
    public DEA deleteClassGroup(@RequestBody ClassGroup classGroup){
        return new DEA(classGroupService.removeById(classGroup.getClassId()));
    }

    /**
     * 加入班级
     * @param classGroup 班级ID
     * @param request 用户id
     * @return List
     */
    @PostMapping("/joinClassGroup")
    public DEA joinClassGroup(@RequestBody ClassGroup classGroup, HttpServletRequest request){
        String userId = (String)request.getSession().getAttribute("userId");
        ClassUser classUser = new ClassUser(null,classGroup.getClassId(),userId,0,new Date());
        return new DEA(classUserService.save(classUser));
    }


    /**
     * 退出班级
     * @param classView 班级ID
     * @param request 用户id
     * @return List
     */
    @PostMapping("/quitClass")
    public DEA quitClass(@RequestBody ClassView classView, HttpServletRequest request){
        String userId = (String)request.getSession().getAttribute("userId");
        QueryWrapper<ClassUser> classUserQueryWrapper = new QueryWrapper<>();
        classUserQueryWrapper.eq("user_id",userId);
        classUserQueryWrapper.eq("class_id",classView.getClassId());
        return new DEA(classUserService.remove(classUserQueryWrapper));
    }

    /**
     * 将当前操作的班级id存储到session
     * @param classGroup 提供classId 33
     * @param request 操作session
     * @return true
     */
    @PostMapping("/setClassIdToSession")
    public DEA setClassIdToSession(@RequestBody ClassGroup classGroup, HttpServletRequest request){
        request.getSession().setAttribute("classId",classGroup.getClassId());
        return new DEA(true);
    }

    /**
     * 查询当前session中记录的班级数据
     * @param request 提供session
     * @return classGroup
     */
    @GetMapping("/getClassGroupBySession")
    public DEA getClassGroupBySession(HttpServletRequest request){
        Integer classId = (Integer) request.getSession().getAttribute("classId");
        return new DEA(classGroupService.getById(classId));
    }
}
