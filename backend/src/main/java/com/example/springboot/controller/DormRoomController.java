package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.service.DormRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@Tag(name = "宿舍房间管理")
@RestController
@RequestMapping("/room")
public class DormRoomController {

    @Resource
    private DormRoomService dormRoomService;

    /**
     * 添加房间
     */
    @Operation(summary = "添加房间")
    @PostMapping("/add")
    public Result<?> add(@Parameter(description = "房间信息") @RequestBody DormRoom dormRoom) {
        int i = dormRoomService.addNewRoom(dormRoom);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    /**
     * 更新房间
     */
    @Operation(summary = "更新房间信息")
    @PutMapping("/update")
    public Result<?> update(@Parameter(description = "房间信息") @RequestBody DormRoom dormRoom) {
        int i = dormRoomService.updateNewRoom(dormRoom);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 删除房间
     */
    @Operation(summary = "删除房间")
    @DeleteMapping("/delete/{dormRoomId}")
    public Result<?> delete(@Parameter(description = "房间ID") @PathVariable Integer dormRoomId) {
        int i = dormRoomService.deleteRoom(dormRoomId);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 查找房间
     */
    @Operation(summary = "分页查询房间")
    @GetMapping("/find")
    public Result<?> findPage(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                              @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                              @Parameter(description = "搜索关键字") @RequestParam(defaultValue = "") String search) {
        Page page = dormRoomService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 首页顶部：空宿舍统计
     */
    @Operation(summary = "空宿舍统计")
    @GetMapping("/noFullRoom")
    public Result<?> noFullRoom() {
        int num = dormRoomService.notFullRoom();
        if (num >= 0) {
            return Result.success(num);
        } else {
            return Result.error("-1", "空宿舍查询失败");
        }
    }

    /**
     * 删除床位学生信息
     */
    @Operation(summary = "删除床位学生信息")
    @DeleteMapping("/delete/{bedName}/{dormRoomId}/{calCurrentNum}")
    public Result<?> deleteBedInfo(@Parameter(description = "床位名称") @PathVariable String bedName, 
                                  @Parameter(description = "房间ID") @PathVariable Integer dormRoomId, 
                                  @Parameter(description = "当前人数") @PathVariable int calCurrentNum) {
        int i = dormRoomService.deleteBedInfo(bedName, dormRoomId, calCurrentNum);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 床位信息，查询该学生是否已由床位
     */
    @Operation(summary = "检查学生是否已有床位")
    @GetMapping("/judgeHadBed/{value}")
    public Result<?> judgeHadBed(@Parameter(description = "学生信息") @PathVariable String value) {
        DormRoom dormRoom = dormRoomService.judgeHadBed(value);
        if (dormRoom == null) {
            return Result.success();
        } else {
            return Result.error("-1", "该学生已有宿舍");
        }
    }

    /**
     * 主页 住宿人数
     */
    @Operation(summary = "获取住宿人数")
    @GetMapping("/selectHaveRoomStuNum")
    public Result<?> selectHaveRoomStuNum() {
        Long count = dormRoomService.selectHaveRoomStuNum();
        if (count >= 0) {
            return Result.success(count);
        } else {
            return Result.error("-1", "查询首页住宿人数失败");
        }
    }

    /**
     * 住宿分布人数
     */
    @Operation(summary = "获取各楼宇住宿人数")
    @GetMapping("/getEachBuildingStuNum/{num}")
    public Result<?> getEachBuildingStuNum(@Parameter(description = "楼宇数量") @PathVariable int num) {
        ArrayList<Long> arrayList = new ArrayList();
        for (int i = 1; i <= num; i++) {
            Long eachBuildingStuNum = dormRoomService.getEachBuildingStuNum(i);
            arrayList.add(eachBuildingStuNum);
        }

        if (!arrayList.isEmpty()) {
            return Result.success(arrayList);
        } else {
            return Result.error("-1", "获取人数失败");
        }
    }

    /**
     * 学生功能： 我的宿舍
     */
    @Operation(summary = "获取我的房间信息")
    @GetMapping("/getMyRoom/{name}")
    public Result<?> getMyRoom(@Parameter(description = "用户名") @PathVariable String name) {
        DormRoom dormRoom = dormRoomService.judgeHadBed(name);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "未分配宿舍");
        }
    }

    /**
     * 检查房间是否满员
     */
    @Operation(summary = "检查房间状态")
    @GetMapping("/checkRoomState/{dormRoomId}")
    public Result<?> checkRoomState(@Parameter(description = "房间ID") @PathVariable Integer dormRoomId) {
        DormRoom dormRoom = dormRoomService.checkRoomState(dormRoomId);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "该房间人满了");
        }
    }

    /**
     * 检查床位是否已经有人
     */
    @Operation(summary = "检查床位状态")
    @GetMapping("/checkBedState/{dormRoomId}/{bedNum}")
    public Result<?> getMyRoom(@Parameter(description = "房间ID") @PathVariable Integer dormRoomId, 
                              @Parameter(description = "床位ID") @PathVariable int bedNum) {
        DormRoom dormRoom = dormRoomService.checkBedState(dormRoomId, bedNum);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "该床位已有人");
        }
    }

    /**
     * 检查房间是否满员
     */
    @Operation(summary = "检查房间是否存在")
    @GetMapping("/checkRoomExist/{dormRoomId}")
    public Result<?> checkRoomExist(@Parameter(description = "房间ID") @PathVariable Integer dormRoomId) {
        DormRoom dormRoom = dormRoomService.checkRoomExist(dormRoomId);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "不存在该房间");
        }
    }
}
