package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.entity.Student;

public interface AdjustRoomService extends IService<AdjustRoom> {

    int updateRoom(AdjustRoom adjustRoom);

    int updateStu(AdjustRoom adjustRoom);

    //查询调宿申请
    Page find(Integer pageNum, Integer pageSize, String search);

    //删除调宿申请
    int deleteAdjustment(Integer id);

    //更新
    int updateApply(AdjustRoom adjustRoom);

    // 添加
    int addApply(AdjustRoom adjustRoom);

    // 按名字查询调宿申请
    Page findByName(Integer pageNum, Integer pageSize, String name);

    //判断是否为学生
    Boolean ifStu(String username);


}
