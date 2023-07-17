package com.czj.controller.util;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.czj.pojo.User;
import com.czj.util.UploadUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/Files")
public class FileCon {

    // 项目静态资源绝对路径
    @Value("${myProject.static.userFiles}")
    private String userFiles;

    static class FileU{
        public String fileName;
        public String userId;
    }

    // 对ckeditor的返回结果-特别适配
    @AllArgsConstructor
    class UploadImageResult {
        /**
         * 1成功，0失败
         */
        public Integer uploaded;
        public String fileName;
        public String url;
    }

    /**
     * 上传用户头像
     * @param file MultipartFile file为接收图片参数
     */
    @PostMapping("/userAvatarUpload")
    public String userAvatarUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // userId获取
        String userId = (String)request.getSession().getAttribute("userId");
        // 项目内部路径组织
        String fileLocation = "/user/" + userId + "/avatar/";
        String originalFilename = file.getOriginalFilename();
        String fileName = UploadUtils.getPhotoName("avatar",originalFilename);  // 根据时间生成文件名
        return upload(file,userFiles + userId + "\\avatar\\", fileLocation,fileName);
    }

    /**
     * Ckeditor上传图片
     * @param file MultipartFile file为接收图片参数
     * @return 相对路径
     */
    @PostMapping("/ckeditorImgUpload")
    public UploadImageResult ckeditorImgUpload(@RequestParam("upload") MultipartFile file, HttpServletRequest request) {
        // userId获取
        String userId = (String)request.getSession().getAttribute("userId");
        // 项目内部路径组织
        String fileLocation = "/user/" + userId + "/ckeditor/";
        String originalFilename = file.getOriginalFilename();
        String fileName = UploadUtils.getPhotoName("ckeditor",originalFilename);  // 根据时间生成文件名
        String url = upload(file, userFiles + userId + "\\ckeditor\\", fileLocation, fileName);
        return new UploadImageResult(1,fileName,url);
    }

    /**
     * 删除被替换的头像资源
     * @param user 删除头像
     */
    @PostMapping("/deleteUserAvatar")
    public boolean deleteUserAvatar(@RequestBody User user) {
        String fileName = user.getAvatarUrl();
        // userId获取
        String userId = user.getUserId();
        // 截取图片名称
        int begin = fileName.lastIndexOf("avatar");
        if(begin == -1){return true;}
        String name = fileName.substring(begin);
        // 获取文件绝对路径
        String fileLocation =userFiles + userId + "\\avatar\\" + name;
        File file = new File(fileLocation);
        boolean delete = file.delete();
        return delete;
    }

    /**
     * 文件下载
     * @param fileu 文件路径
     */
    @JsonManagedReference
    @PostMapping("/reportDownload")
    public void reportDownload(@RequestBody FileU fileu, HttpServletResponse response){
        try {
            String fileName = fileu.fileName;
            // 获取文件绝对路径
            String fileLocation =userFiles + fileName;
            // 返回文件的二进制流
            File file = new File(fileLocation);
            if(file.exists()){
                // 设置信息给客户端不解析
                String type = new MimetypesFileTypeMap().getContentType(fileName);
                // 设置contentType，即告诉客户端所发送的数据属于什么类型
                response.setHeader("Content-type",type);
                // 设置编码
                String code = new String(fileName.getBytes("utf-8"), "iso-8859-1");
                // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
                response.setHeader("Content-Disposition", "attachment;filename=" + code);
                // 设置文件大小 下载下来的excel文件才不会在打开前提示修复
                response.addHeader("Content-Length",String.valueOf(file.length()));
                // 发送给客户端的数据
                OutputStream outputStream = response.getOutputStream();
                byte[] buff = new byte[1024];
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                int i = bufferedInputStream.read(buff);
                while (i != -1) {
                    outputStream.write(buff, 0, buff.length);
                    outputStream.flush();
                    i = bufferedInputStream.read(buff);
                }
                // 关闭流
                outputStream.close();
                fileInputStream.close();
                bufferedInputStream.close();
            }else{
                System.out.println("文件不存在!");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 文件上传
     * @param file 文件
     * @param pathname 存入路径-绝对路径
     * @param fileLocation 读取路径-相对路径
     * @param fileName  文件名
     * @return 文件读取路径
     */
    private String upload(MultipartFile file, String pathname, String fileLocation, String fileName){
        try {
            byte[] bytes = file.getBytes();
            // 创建文件路径
            File dest = new File(pathname + fileName);
            // 目录不存在则创建
            if(!dest.getParentFile().exists()){
                boolean success = dest.getParentFile().mkdirs();
                if(!success)throw new IOException();
            }
            // 文件已存在
            if(dest.exists()){
                return null;
            }
            // 创建文件
            Path path = Paths.get(pathname + fileName);
            Files.write(path, bytes);    // 写入文件
            return fileLocation + fileName;  //返回文件路径
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
