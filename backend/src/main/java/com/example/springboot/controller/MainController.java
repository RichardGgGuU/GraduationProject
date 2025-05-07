package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.DormManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Tag(name = "主页管理")
@RestController
@RequestMapping("/main")
public class MainController {

    /**
     * 获取身份信息
     */
    @Operation(summary = "获取身份信息")
    @GetMapping("/loadIdentity")
    public Result<?> loadIdentity(HttpSession session) {
        Object identity = session.getAttribute("Identity");

        if (identity != null) {
            return Result.success(identity);
        } else {
            return Result.error("-1", "加载失败");
        }
    }

    /**
     * 获取个人信息
     */
    @Operation(summary = "获取个人信息")
    @GetMapping("/loadUserInfo")
    public Result<?> loadUserInfo(HttpSession session) {
        Object User = session.getAttribute("User");

        if (User != null) {
            return Result.success(User);
        } else {
            return Result.error("-1", "加载失败");
        }
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录")
    @GetMapping("/signOut")
    public Result<?> signOut(HttpSession session) {
        session.removeAttribute("User");
        session.removeAttribute("Identity");
        return Result.success();
    }

    @Operation(summary = "获取主页信息")
    @GetMapping("/info")
    public Result<?> info() {
        return Result.success();
    }
}
