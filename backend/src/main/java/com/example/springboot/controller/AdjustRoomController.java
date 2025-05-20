package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.service.AdjustRoomService;
import com.example.springboot.service.DormRoomService;
import com.example.springboot.service.RedisLockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@Api(tags = "调宿申请管理")
@RestController
@RequestMapping("/adjustRoom")
public class AdjustRoomController {

    @Resource
    private AdjustRoomService adjustRoomService;

    @Resource
    private DormRoomService dormRoomService;

    @Resource
    private RedisLockService redisLockService;

    @ApiOperation("添加调宿申请")
    @PostMapping("/add")
    public Result<?> add(@ApiParam("调宿申请信息") @RequestBody AdjustRoom adjustRoom) {
        String lockKey = "adjustRoomAddLock";
        String requestId = UUID.randomUUID().toString();
        boolean locked = redisLockService.tryLock(lockKey, requestId, 10); // 10秒过期

        if (locked) {
            try {
                // 执行业务逻辑
                int result = adjustRoomService.addApply(adjustRoom);
                if (result > 0) {
                    return Result.success();
                } else {
                    return Result.error("-1", "添加失败");
                }
            } finally {
                // 释放锁
                redisLockService.releaseLock(lockKey, requestId);
            }
        }
        return Result.error("-1", "添加失败");
    }

    @ApiOperation("更新调宿申请")
    @PutMapping("/update/{state}")
    public Result<?> update(@ApiParam("调宿申请信息") @RequestBody AdjustRoom adjustRoom, 
                          @ApiParam("是否通过") @PathVariable Boolean state) {
        String lockKey = "adjustRoomUpdateLock";
        String requestId = UUID.randomUUID().toString();
        boolean locked = redisLockService.tryLock(lockKey, requestId, 10); // 10秒过期

        if (locked) {
            try {
                // 执行业务逻辑
                if (state) {
                    // 更新房间表信息
                    int i = dormRoomService.adjustRoomUpdate(adjustRoom);
                    if (i == -2) {
                        return Result.error("-1", "重复操作");
                    }
                }
                //更新调宿表信息
                int i = adjustRoomService.updateApply(adjustRoom);
                adjustRoomService.updateRoom(adjustRoom);
                adjustRoomService.updateStu(adjustRoom);
                if (i == 1) {
                    return Result.success();
                } else {
                    return Result.error("-1", "更新失败");
                }
            } finally {
                // 释放锁
                redisLockService.releaseLock(lockKey, requestId);
            }
        }
        return Result.error("-1", "更新失败");
    }

    @ApiOperation("删除调宿申请")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@ApiParam("申请ID") @PathVariable Integer id) {
        String lockKey = "adjustRoomDeleteLock";
        String requestId = UUID.randomUUID().toString();
        boolean locked = redisLockService.tryLock(lockKey, requestId, 10); // 10秒过期

        if (locked) {
            try {
                // 执行业务逻辑
                int i = adjustRoomService.deleteAdjustment(id);
                if (i == 1) {
                    return Result.success();
                } else {
                    return Result.error("-1", "删除失败");
                }
            } finally {
                // 释放锁
                redisLockService.releaseLock(lockKey, requestId);
            }
        }
        return Result.error("-1", "删除失败");
    }

    @ApiOperation("分页查询调宿申请")
    @GetMapping("/find")
    public Result<?> findPage(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
                             @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                             @ApiParam("搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = adjustRoomService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }
}
