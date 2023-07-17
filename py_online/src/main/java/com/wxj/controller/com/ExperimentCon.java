package com.wxj.controller.com;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wxj.controller.util.CLog;
import com.wxj.controller.util.DEA;
import com.wxj.pojo.Experiment;
import com.wxj.pojo.Section;
import com.wxj.pojo.view.SectionSuperSet;
import com.wxj.service.ExperimentService;
import com.wxj.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ExperimentCon")
public class ExperimentCon {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private ExperimentService experimentService;

    /**
     * 查找当前班级所含章节数据
     * @param section 提供title
     * @param request 提供classId
     * @return List<SectionSuperSet>
     */
    @PostMapping("/seaSectionOnSubmit")
    public DEA seaSectionOnSubmit(@RequestBody Section section, HttpServletRequest request){

        // 查找对应Title的章节数据
        Integer classId = (Integer) request.getSession().getAttribute("classId");
        QueryWrapper<Section> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("class_id",classId);
        sectionQueryWrapper.like("title",section.getTitle());
        List<Section> sections = sectionService.list(sectionQueryWrapper);
        // 根据sections查找对应的实验数据，封装为章节列表
        List<SectionSuperSet> sectionSuperSets = new ArrayList<>();
        for(Section nowSection : sections){
            // 初始化当前章节
            SectionSuperSet sectionSuperSet = new SectionSuperSet(
                    nowSection.getSectionId(),
                    nowSection.getClassId(),
                    nowSection.getTitle(),
                    nowSection.getExpNum(),
                    nowSection.getCreateTime());
            // 查找对应实验列表
            QueryWrapper<Experiment> experimentQueryWrapper = new QueryWrapper<>();
            experimentQueryWrapper.eq("section_id",nowSection.getSectionId());
            List<Experiment> experiments = experimentService.list(experimentQueryWrapper);
            // 初始化当前章节的实验列表
            sectionSuperSet.setExperiments(experiments);
            // 添加到章节列表中
            sectionSuperSets.add(sectionSuperSet);
        }
        return new DEA(sectionSuperSets);
    }

    /**
     * 发布新章节
     * @param section 提供title
     * @param request 提供classId
     * @return Boolean
     */
    @PostMapping("/createSection")
    public DEA createSection(@RequestBody Section section, HttpServletRequest request){

        Integer classId = (Integer) request.getSession().getAttribute("classId");
        section.setClassId(classId);
        section.setCreateTime(new Date());
        return new DEA(sectionService.save(section));
    }

    /**
     * 编辑章节
     * @param section 提供title和sectionId
     * @return Boolean
     */
    @PostMapping("/updateSection")
    public DEA updateSection(@RequestBody Section section){

        UpdateWrapper<Section> sectionUpdateWrapper = new UpdateWrapper<>();
        sectionUpdateWrapper.eq("section_id",section.getSectionId());
        sectionUpdateWrapper.set("title",section.getTitle());
        return new DEA(sectionService.update(sectionUpdateWrapper));
    }

    /**
     * 删除章节
     * @param section sectionId
     * @return Boolean
     */
    @PostMapping("/deleteSection")
    public DEA deleteSection(@RequestBody Section section){
        return new DEA(sectionService.removeById(section.getSectionId()));
    }

    /**
     * 发布新实验
     * @param experiment 提供title
     * @return Boolean
     */
    @PostMapping("/createExperiment")
    public DEA createExperiment(@RequestBody Experiment experiment){
        CLog.log(experiment);
        experiment.setCreateTime(new Date());
        return new DEA(experimentService.save(experiment));
    }

    /**
     * 编辑实验
     * @param experiment 提供编辑后的数据
     * @return Boolean
     */
    @PostMapping("/updateExperiment")
    public DEA updateExperiment(@RequestBody Experiment experiment){
        return new DEA(experimentService.updateById(experiment));
    }

    /**
     * 删除实验
     * @param experiment 提供experimentId
     * @return Boolean
     */
    @PostMapping("/deleteExperiment")
    public DEA deleteExperiment(@RequestBody Experiment experiment){
        return new DEA(experimentService.removeById(experiment.getExperimentId()));
    }

    /**
     * 将当前操作的experimentId存储到session
     * @param experiment 提供experimentId
     * @param request 操作session
     * @return true
     */
    @PostMapping("/setExperimentIdToSession")
    public DEA setExperimentIdToSession(@RequestBody Experiment experiment, HttpServletRequest request){
        request.getSession().setAttribute("experimentId",experiment.getExperimentId());
        return new DEA(true);
    }

    /**
     * 查询当前session中记录的experiment
     * @param request 提供session
     * @return experimentId
     */
    @GetMapping("/getExperimentBySession")
    public DEA getExperimentBySession(HttpServletRequest request){
        Integer experimentId = (Integer) request.getSession().getAttribute("experimentId");
        return new DEA(experimentService.getById(experimentId));
    }

}
