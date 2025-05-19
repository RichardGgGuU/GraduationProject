package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.entity.Student;
import com.example.springboot.entity.User;
import com.example.springboot.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Tag(name = "学生管理")
@RestController
@RequestMapping("/stu")
public class StudentController {

    @Resource
    private StudentService studentService;

    /**
     * 添加学生信息
     */
    @Operation(summary = "添加学生")
    @PostMapping("/add")
    public Result<?> add(@Parameter(description = "学生信息") @RequestBody Student student) {
        int result = studentService.addNewStudent(student);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    /**
     * 更新学生信息
     */
    @Operation(summary = "更新学生信息")
    @PutMapping("/update")
    public Result<?> update(@Parameter(description = "学生信息") @RequestBody Student student) {
        int result = studentService.updateNewStudent(student);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 删除学生信息
     */
    @Operation(summary = "删除学生")
    @DeleteMapping("/delete/{username}")
    public Result<?> delete(@Parameter(description = "学生ID") @PathVariable String username) {
        int result = studentService.deleteStudent(username);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 查找学生信息
     */
    @Operation(summary = "分页查询学生")
    @GetMapping("/find")
    public Result<?> findPage(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                              @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                              @Parameter(description = "搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = studentService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 获取学生个人信息
     */
    @Operation(summary = "获取学生个人信息")
    @GetMapping("/self")
    public Result<?> getSelfInfo(HttpSession session) {
        Student student = (Student) session.getAttribute("User");
        if (student != null) {
            return Result.success(student);
        } else {
            return Result.error("-1", "未登录或会话已过期");
        }
    }

    /**
     * 学生登录
     */
    @Operation(summary = "学生登录")
    @PostMapping("/login")
    public Result<?> login(@Parameter(description = "登录信息") @RequestBody Student student) {
        Student res = studentService.stuLogin(student.getUsername(), student.getPassword());
        if (res != null) {
            return Result.success(res);
        } else {
            return Result.error("-1", "用户名或密码错误");
        }
    }

    /**
     * 主页顶部：学生统计
     */
    @Operation(summary = "获取学生统计")
    @GetMapping("/stuNum")
    public Result<?> stuNum() {
        int num = studentService.stuNum();
        if (num > 0) {
            return Result.success(num);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 床位信息，查询是否存在该学生
     * 床位信息，查询床位上的学生信息
     */
    @Operation(summary = "查询学生信息")
    @GetMapping("/exist/{value}")
    public Result<?> exist(@Parameter(description = "学生信息") @PathVariable String value) {
        Student student = studentService.stuInfo(value);
        if (student != null) {
            return Result.success(student);
        } else {
            return Result.error("-1", "未分配宿舍");
        }
    }
}
