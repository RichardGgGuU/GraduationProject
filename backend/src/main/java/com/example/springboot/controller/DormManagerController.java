package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormManager;
import com.example.springboot.entity.User;
import com.example.springboot.service.DormManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Tag(name = "宿管管理")
@RestController
@RequestMapping("/dormManager")
public class DormManagerController {

    @Resource
    private DormManagerService dormManagerService;

    /**
     * 宿管添加
     */
    @Operation(summary = "添加宿管")
    @PostMapping("/add")
    public Result<?> add(@Parameter(description = "宿管信息") @RequestBody DormManager dormManager) {
        int result = dormManagerService.addNewDormManager(dormManager);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    /**
     * 宿管信息更新
     */
    @Operation(summary = "更新宿管信息")
    @PutMapping("/update")
    public Result<?> update(@Parameter(description = "宿管信息") @RequestBody DormManager dormManager) {
        int result = dormManagerService.updateNewDormManager(dormManager);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 宿管删除
     */
    @Operation(summary = "删除宿管")
    @DeleteMapping("/delete/{username}")
    public Result<?> delete(@Parameter(description = "宿管ID") @PathVariable String username) {
        int result = dormManagerService.deleteDormManager(username);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 宿管查找
     */
    @Operation(summary = "分页查询宿管")
    @GetMapping("/find")
    public Result<?> findPage(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                              @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                              @Parameter(description = "搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = dormManagerService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 获取宿管个人信息
     */
    @Operation(summary = "获取宿管个人信息")
    @GetMapping("/self")
    public Result<?> getSelfInfo(HttpSession session) {
        DormManager dormManager = (DormManager) session.getAttribute("User");
        if (dormManager != null) {
            return Result.success(dormManager);
        } else {
            return Result.error("-1", "未登录或会话已过期");
        }
    }

    /**
     * 宿管登录
     */
    @Operation(summary = "宿管登录")
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user, HttpSession session) {

        Object o = dormManagerService.dormManagerLogin(user.getUsername(), user.getPassword());
        if (o != null) {
            System.out.println(o);
            //存入session
            session.setAttribute("Identity", "dormManager");
            session.setAttribute("User", o);
            return Result.success(o);
        } else {
            return Result.error("-1", "用户名或密码错误");
        }
    }
}
