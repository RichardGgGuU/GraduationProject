package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Notice;
import com.example.springboot.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "通知管理")
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Operation(summary = "添加通知")
    @PostMapping("/add")
    public Result<?> add(@Parameter(description = "通知信息") @RequestBody Notice notice) {
        int result = noticeService.addNewNotice(notice);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    @Operation(summary = "更新通知")
    @PutMapping("/update")
    public Result<?> update(@Parameter(description = "通知信息") @RequestBody Notice notice) {
        int result = noticeService.updateNewNotice(notice);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    @Operation(summary = "删除通知")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@Parameter(description = "通知ID") @PathVariable Integer id) {
        int result = noticeService.deleteNotice(id);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    @Operation(summary = "分页查询通知")
    @GetMapping("/find")
    public Result<?> findPage(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                             @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                             @Parameter(description = "搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = noticeService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 首页公告展示
     */
    @GetMapping("/homePageNotice")
    public Result<?> homePageNotice() {
        List<?> list = noticeService.homePageNotice();
        if (list != null) {
            return Result.success(list);
        } else {
            return Result.error("-1", "首页公告查询失败");
        }
    }
}
