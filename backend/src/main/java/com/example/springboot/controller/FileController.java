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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Tag(name = "文件管理")
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

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public Result<?> upload(@Parameter(description = "文件") @RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

        String uid = new UID().produceUID();
        String newFilename = uid + fileType;
        System.out.println(newFilename);
        String targetPath = rootFilePath + newFilename;
        System.out.println(targetPath);
        FileUtil.writeBytes(file.getBytes(), targetPath);

        return Result.success(newFilename);
    }

    @Operation(summary = "更新学生头像")
    @PostMapping("/uploadAvatar/stu")
    public Result<?> uploadStuAvatar(@Parameter(description = "学生信息") @RequestBody Student student) {
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

    @Operation(summary = "更新管理员头像")
    @PostMapping("/uploadAvatar/admin")
    public Result<?> uploadAdminAvatar(@Parameter(description = "管理员信息") @RequestBody Admin admin) {
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

    @Operation(summary = "更新宿管头像")
    @PostMapping("/uploadAvatar/dormManager")
    public Result<?> uploadDormManagerAvatar(@Parameter(description = "宿管信息") @RequestBody DormManager dormManager) {
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

    @Operation(summary = "获取头像")
    @GetMapping("/initAvatar/{filename}")
    public Result<?> initAvatar(@Parameter(description = "文件名") @PathVariable String filename) throws IOException {
        System.out.println(filename);
        String path = rootFilePath + filename;
        System.out.println(path);
        return Result.success(getImage(path));
    }

    private Result<?> getImage(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = fileInputStream.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        byte[] fileByte = bos.toByteArray();

        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(fileByte);

        fileInputStream.close();
        bos.close();
        return Result.success(data);
    }
}
