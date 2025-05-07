package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormBuild;
import com.example.springboot.service.DormBuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "宿舍楼管理")
@RestController
@RequestMapping("/building")
public class DormBuildController {

    @Resource
    private DormBuildService dormBuildService;

    @Operation(summary = "添加宿舍楼")
    @PostMapping("/add")
    public Result<?> add(@Parameter(description = "宿舍楼信息") @RequestBody DormBuild dormBuild) {
        int result = dormBuildService.addNewBuilding(dormBuild);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    @Operation(summary = "更新宿舍楼信息")
    @PutMapping("/update")
    public Result<?> update(@Parameter(description = "宿舍楼信息") @RequestBody DormBuild dormBuild) {
        int result = dormBuildService.updateNewBuilding(dormBuild);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    @Operation(summary = "删除宿舍楼")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@Parameter(description = "宿舍楼ID") @PathVariable Integer id) {
        int result = dormBuildService.deleteBuilding(id);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    @Operation(summary = "分页查询宿舍楼")
    @GetMapping("/find")
    public Result<?> findPage(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                             @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                             @Parameter(description = "搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = dormBuildService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    @Operation(summary = "获取楼宇信息")
    @GetMapping("/getBuildingName")
    public Result<?> getBuildingName() {
        List<DormBuild> buildingName = dormBuildService.getBuildingId();
        List<String> buildingId = buildingName.stream().map(dormBuildId -> dormBuildId.getDormBuildId()).collect(Collectors.toList());

        if (!buildingId.isEmpty()) {
            return Result.success(buildingId);
        } else {
            return Result.error("-1", "查询失败");
        }
    }
}
