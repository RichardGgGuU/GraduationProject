package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Visitor;
import com.example.springboot.service.VisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "访客管理")
@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Resource
    private VisitorService visitorService;

    @ApiOperation("添加访客")
    @PostMapping("/add")
    public Result<?> add(@ApiParam("访客信息") @RequestBody Visitor visitor) {
        int result = visitorService.addNewVisitor(visitor);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    @ApiOperation("更新访客")
    @PutMapping("/update")
    public Result<?> update(@ApiParam("访客信息") @RequestBody Visitor visitor) {
        int result = visitorService.updateNewVisitor(visitor);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    @ApiOperation("删除访客")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@ApiParam("访客ID") @PathVariable Integer id) {
        int result = visitorService.deleteVisitor(id);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    @ApiOperation("分页查询访客")
    @GetMapping("/find")
    public Result<?> findPage(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
                             @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                             @ApiParam("搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = visitorService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }
}
