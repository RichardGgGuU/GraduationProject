package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Repair;
import com.example.springboot.service.RepairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "报修管理")
@RestController
@RequestMapping("/repair")
public class RepairController {

    @Resource
    private RepairService repairService;

    @Operation(summary = "添加报修")
    @PostMapping("/add")
    public Result<?> add(@Parameter(description = "报修信息") @RequestBody Repair repair) {
        int result = repairService.addNewOrder(repair);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    @Operation(summary = "更新报修")
    @PutMapping("/update")
    public Result<?> update(@Parameter(description = "报修信息") @RequestBody Repair repair) {
        int result = repairService.updateNewOrder(repair);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    @Operation(summary = "删除报修")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@Parameter(description = "报修ID") @PathVariable Integer id) {
        int result = repairService.deleteOrder(id);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    @Operation(summary = "分页查询报修")
    @GetMapping("/find")
    public Result<?> findPage(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                             @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                             @Parameter(description = "搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = repairService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 个人申报报修 分页查询
     */
    @GetMapping("/find/{name}")
    public Result<?> individualFind(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "") String search,
                                    @PathVariable String name) {
        System.out.println(name);
        Page page = repairService.individualFind(pageNum, pageSize, search, name);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 首页顶部：报修统计
     */
    @GetMapping("/orderNum")
    public Result<?> orderNum() {
        int num = repairService.showOrderNum();
        if (num >= 0) {
            return Result.success(num);
        } else {
            return Result.error("-1", "报修统计查询失败");
        }
    }
}
