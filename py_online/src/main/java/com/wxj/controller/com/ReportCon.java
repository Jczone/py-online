package com.wxj.controller.com;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxj.controller.util.DEA;
import com.wxj.pojo.Experiment;
import com.wxj.pojo.Report;
import com.wxj.pojo.view.NoticeView;
import com.wxj.pojo.view.ReportView;
import com.wxj.service.ExperimentService;
import com.wxj.service.ReportService;
import com.wxj.service.ReportViewService;
import com.wxj.util.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/ReportCon")
public class ReportCon {

    @Value("${myProject.static.userFiles}")
    private String userFiles;

    // 项目静态资源绝对路径-斜杠
    @Value("${myProject.static.userFilesR}")
    private String userFilesR;

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportViewService reportViewService;
    @Autowired
    private ExperimentService experimentService;

    static class Form{
        public String code;
        public String params;
        public String title;
        public int pageSize;
        public int currentPage;
    }

    /**
     * python脚本运行
     * @param form  提供py代码和参数
     * @param request  提供用户ID
     * @return  运行结果或者报错信息
     */
    @PostMapping("/runPython")
    public DEA runPython(@RequestBody Form form, HttpServletRequest request){

        String userId = (String) request.getSession().getAttribute("userId");
        // py文件路径组织-反斜杠:相对路径+用户id+code目录+run.py
        String pyFileUrl = userFiles + "\\" + userId + "\\" + "code" + "\\" + "run.py";
        String paramsFileUrl = userFiles + "\\" + userId + "\\" + "code" + "\\" + "run_params.txt";
        // py文件路径组织-斜杠:相对路径+用户id+code目录+实验id.py
        String pyFileUrlR = userFilesR + "/" + userId + "/" + "code" + "/" + "run.py";
        String paramsFileUrlR = userFilesR + "/" + userId + "/" + "code" + "/" + "run_params.txt";
        // 目录创建
        if(makeDir(pyFileUrl) && makeDir(paramsFileUrl)){
            // 运行python脚本
            String res = codeRun(form.code,form.params,pyFileUrlR,paramsFileUrlR,pyFileUrl,paramsFileUrl);
            return new DEA(true, res,"操作成功！");
        }else{
            return new DEA("文件创建失败！请重试");
        }
    }

    /**
     * 提交代码报告-运行代码生成报告文件、参数文件、结果文件
     * @param form  提供py代码和参数
     * @param request  提供用户ID
     * @return boolean
     */
    @PostMapping("/submitReport")
    public DEA submitReport(@RequestBody Form form, HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute("userId");
        Integer experimentId = (Integer) request.getSession().getAttribute("experimentId");
        // 生成随机文件名
        String fileName = UploadUtils.getPhotoName(experimentId + "_code",".py");
        String paramsFileName = UploadUtils.getPhotoName(experimentId + "_params",".txt");
        String resultFileName = UploadUtils.getPhotoName(experimentId + "_result",".txt");
        // 路径组织-反斜杠-目录创建、文件运行
        String pyFileUrl     = userFiles + "\\" + userId + "\\" + "code" + "\\" + experimentId + "\\" + fileName;
        String paramsFileUrl = userFiles + "\\" + userId + "\\" + "code" + "\\" + experimentId + "\\" + paramsFileName;
        String resultFileUrl = userFiles + "\\" + userId + "\\" + "code" + "\\" + experimentId + "\\" + resultFileName;
        // 路径组织-斜杠-写入文件
        String pyFileUrlR     = userFilesR + "/" + userId + "/" + "code" + "/" + experimentId + "/" + fileName;
        String paramsFileUrlR = userFilesR + "/" + userId + "/" + "code" + "/" + experimentId + "/" + paramsFileName;
        String resultFileUrlR = userFilesR + "/" + userId + "/" + "code" + "/" + experimentId + "/" + resultFileName;
        // 相对路径组织-数据库
        String pyFileRel =     "/" + userId + "/" + "code" + "/" + experimentId + "/" + fileName;
        String paramsFileRel = "/" + userId + "/" + "code" + "/" + experimentId + "/" + paramsFileName;
        String resultFileRel = "/" + userId + "/" + "code" + "/" + experimentId + "/" + resultFileName;
        // 目录创建
        if(makeDir(pyFileUrl) && makeDir(paramsFileUrl) && makeDir(resultFileUrl)){
            // 保存运行结果到文件
            String res = codeRun(form.code,form.params,pyFileUrlR,paramsFileUrlR,pyFileUrl,paramsFileUrl);
            // 定义含特殊字符的正则表达式
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(res);
            res = m.replaceAll("");
            try{
                OutputStream outputStream = Files.newOutputStream(Paths.get(resultFileUrlR));
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                writer.append(res);   // 写入运行结果
                writer.close();
                outputStream.close();
            }catch (IOException e){
                e.printStackTrace();
                return new DEA("文件创建失败！请重试");
            }
            // 判分
            Experiment experiment = experimentService.getById(experimentId);
            int score = 0;
            if(experiment.getScore() > 0){
                score = res.equals(experiment.getResult()) ? experiment.getScore() : 0;
            }
            // 写入数据库
            Report report = new Report(null,experimentId,userId,pyFileRel,paramsFileRel,resultFileRel,score,new Date());
            reportService.save(report);
            return new DEA(true,"操作成功！");
        }else{
            return new DEA("文件创建失败！请重试");
        }
    }

    /**
     * 分页查询当前用户的所有实验报告
     * @param form 标题
     * @param request userId
     * @return Page
     */
    @PostMapping("/getReportByTitle")
    public DEA getReportByTitle(@RequestBody Form form, HttpServletRequest request){

        // 获取用户id
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        // 封装查询类
        QueryWrapper<ReportView> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", form.title);
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        // 查询页面总数
        int count = reportViewService.count(queryWrapper);
        int currentPage = form.currentPage;
        int pageSize = form.pageSize;
        // 超界检测-默认归零
        if(count < (currentPage - 1) * pageSize){
            currentPage = 1;
        }
        // 封装分页信息
        IPage<ReportView> page = new Page<>(currentPage,pageSize);
        return new DEA(reportViewService.page(page,queryWrapper));
    }

    /**
     * 分页查询当前实验中提交的所有报告
     * @param form 标题/分页条件
     * @param request experimentId
     * @return Page
     */
    @PostMapping("/getTeaReportByTitle")
    public DEA getTeaReportByTitle(@RequestBody Form form, HttpServletRequest request){

        // 获取用户id
        HttpSession session = request.getSession();
        Integer experimentId = (Integer) session.getAttribute("experimentId");
        // 封装查询类
        QueryWrapper<ReportView> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", form.title);
        queryWrapper.eq("experiment_id", experimentId);
        queryWrapper.orderByDesc("create_time");
        // 查询页面总数
        int count = reportViewService.count(queryWrapper);
        int currentPage = form.currentPage;
        int pageSize = form.pageSize;
        // 超界检测-默认归零
        if(count < (currentPage - 1) * pageSize){
            currentPage = 1;
        }
        // 封装分页信息
        IPage<ReportView> page = new Page<>(currentPage,pageSize);
        return new DEA(reportViewService.page(page,queryWrapper));
    }

    /**
     * 文件目录创建
     * @param file 代码绝对路径-反斜杠
     * @return Boolean
     */
    public static boolean makeDir(String file){
        // 创建文件路径
        File pyFile = new File(file);
        if(!pyFile.getParentFile().exists()){ // 目录不存在则创建
            return pyFile.getParentFile().mkdirs();
        }
        return true;
    }

    /**
     * 运行python代码并返回运行结果
     * @param code 代码
     * @param params 参数
     * @param pyFileUrlR 代码绝对路径-斜杠
     * @param paramsFileUrlR 参数文件绝对路径-斜杠
     * @param pyFileUrl 代码绝对路径-反斜杠
     * @param paramsFileUrl 参数文件绝对路径-反斜杠
     * @return String运行结果
     */
    public static String codeRun(String code, String params, String pyFileUrlR, String paramsFileUrlR, String pyFileUrl, String paramsFileUrl){

        StringBuilder res = new StringBuilder();
        try {
            // 创建py文件
            OutputStream pyOutputStream = Files.newOutputStream(Paths.get(pyFileUrlR));
            OutputStreamWriter pyWriter = new OutputStreamWriter(pyOutputStream, StandardCharsets.UTF_8);
            pyWriter.append(code);   // 写入.py文件
            pyWriter.close();
            pyOutputStream.close();
            // 创建txt文件-输入流重定向文件
            OutputStream fileOutputStream = Files.newOutputStream(Paths.get(paramsFileUrlR));
            OutputStreamWriter fileWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            params = params.replace(" ","\n");
            fileWriter.append(params);   // 写入.txt文件
            fileWriter.close();
            fileOutputStream.close();
            // Java运行python脚本
            ProcessBuilder processBuilder = new ProcessBuilder("python",pyFileUrl);
            processBuilder.redirectInput(new File(paramsFileUrl));
            Process process = processBuilder.start();
            // 获取输入流
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
            // 获取输入流读取
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"GBK");
            InputStreamReader errInputStreamReader = new InputStreamReader(errorStream,"GBK");
            // 获取缓存读取
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedReader errBufferedReader = new BufferedReader(errInputStreamReader);
            String content = bufferedReader.readLine();
            if(content != null){    // 正常运行读取流
                while (content != null) {
                    res.append(content).append("\n");
                    content = bufferedReader.readLine();
                }
            }else{  // 异常读取流
                content = errBufferedReader.readLine();
                while (content != null) {
                    res.append(content).append("\n");
                    content = errBufferedReader.readLine();
                }
            }
            process.destroy();  // 需要手动回收
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

}
