package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.Result;
import com.example.springboot.common.UID;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.DormManager;
import com.example.springboot.entity.Student;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.DormManagerService;
import com.example.springboot.service.StudentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final String ip = "http://localhost";
    static String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/files/";
    private String port = "9090";
    @Resource
    private StudentService studentService;

    @Resource
    private AdminService adminService;

    @Resource
    private DormManagerService dormManagerService;

    /**
     * 将上传的头像写入本地 rootFilePath
     */
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        //获取文件尾缀
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

        //重命名
        String uid = new UID().produceUID();
        String newFilename = uid + fileType;
        System.out.println(newFilename);
        //存储位置
        String targetPath = rootFilePath + newFilename;
        System.out.println(targetPath);
        //获取字节流
        FileUtil.writeBytes(file.getBytes(), targetPath);

        return Result.success(newFilename);
    }

    /**
     * 将头像名称更新到数据库中
     */
    @PostMapping("/uploadAvatar/stu")
    public Result<?> uploadStuAvatar(@RequestBody Student student) {
        if (student.getAvatar() != null) {
            int i = studentService.updateNewStudent(student);
            if (i == 1) {
                return Result.success(student.getAvatar());
            }
        } else {
            return Result.error("-1", "头像文件名为空");
        }
        return Result.error("-1", "设置头像失败");
    }

    @PostMapping("/uploadAvatar/admin")
    public Result<?> uploadAdminAvatar(@RequestBody Admin admin) {
        if (admin.getAvatar() != null) {
            int i = adminService.updateAdmin(admin);
            if (i == 1) {
                return Result.success(admin.getAvatar());
            }
        } else {
            return Result.error("-1", "头像文件名为空");
        }
        return Result.error("-1", "设置头像失败");
    }

    @PostMapping("/uploadAvatar/dormManager")
    public Result<?> uploadDormManagerAvatar(@RequestBody DormManager dormManager) {
        if (dormManager.getAvatar() != null) {
            int i = dormManagerService.updateNewDormManager(dormManager);
            if (i == 1) {
                return Result.success(dormManager.getAvatar());
            }
        } else {
            return Result.error("-1", "头像文件名为空");
        }
        return Result.error("-1", "设置头像失败");
    }

    /**
     * 前端调用接口，后端查询存储与本地的头像，进行Base64编码 发送到前端
     */
    @GetMapping("/initAvatar/{filename}")
    public Result<?> initAvatar(@PathVariable String filename) throws IOException {
        System.out.println(filename);
        String path = rootFilePath + filename;
        System.out.println(path);
        return Result.success(getImage(path));
    }

    private Result<?> getImage(String path) throws IOException {

        //读取图片变成字节数组
        FileInputStream fileInputStream = new FileInputStream(path);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = fileInputStream.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        byte[] fileByte = bos.toByteArray();

        //进行base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(fileByte);

        fileInputStream.close();
        bos.close();
        return Result.success(data);
    }

}
