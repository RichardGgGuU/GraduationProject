package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.entity.Student;
import com.example.springboot.mapper.AdjustRoomMapper;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.mapper.StudentMapper;
import com.example.springboot.service.AdjustRoomService;
import com.example.springboot.service.DormRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdjustRoomServiceImpl extends ServiceImpl<AdjustRoomMapper, AdjustRoom> implements AdjustRoomService {


    @Resource
    private AdjustRoomMapper adjustRoomMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private DormRoomMapper dormRoomMapper;

    @Resource
    private DormRoomService dormRoomService;

    /**
     * 添加调宿申请
     */
    @Override
    public int addApply(AdjustRoom adjustRoom) {
        int insert = adjustRoomMapper.insert(adjustRoom);
        return insert;
    }

    @Override
    public int updateRoom(AdjustRoom adjustRoom){
        DormRoom currentRoom = dormRoomMapper.selectById(adjustRoom.getCurrentRoomId());
        DormRoom towardsRoom = dormRoomMapper.selectById(adjustRoom.getTowardsRoomId());
        switch (adjustRoom.getCurrentBedId()){
            case 1:
                currentRoom.setFirstBed(null);
                break;
            case 2:
                currentRoom.setSecondBed(null);
                break;
            case 3:
                currentRoom.setThirdBed(null);
                break;
            case 4:
                currentRoom.setFourthBed(null);
                break;
        }
        switch (adjustRoom.getTowardsBedId()){
            case 1:
                towardsRoom.setFirstBed(adjustRoom.getUsername());
                break;
            case 2:
                towardsRoom.setSecondBed(adjustRoom.getUsername());
                break;
            case 3:
                towardsRoom.setThirdBed(adjustRoom.getUsername());
                break;
            case 4:
                towardsRoom.setFourthBed(adjustRoom.getUsername());
                break;
        }
        dormRoomService.updateById(currentRoom);
        dormRoomService.updateById(towardsRoom);
        return 0;
    }

    /**
     * 查找调宿申请
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        if(ifStu(search)){
            qw.like("username", search);
            Page orderPage = adjustRoomMapper.selectPage(page, qw);
            return orderPage;
        }
        else {
            search="";
            qw.like("username", search);
            Page orderPage = adjustRoomMapper.selectPage(page, qw);
            //Page orderPage = adjustRoomMapper.selectall(page);
            return orderPage;

        }
        //qw.like("username", search);
        //Page orderPage = adjustRoomMapper.selectPage(page, qw);
        //return orderPage;
    }

    /**
     * 删除调宿申请
     */
    @Override
    public int deleteAdjustment(Integer id) {
        int i = adjustRoomMapper.deleteById(id);
        return i;
    }


    /**
     * 更新调宿申请
     */
    @Override
    public int updateApply(AdjustRoom adjustRoom) {
        int i = adjustRoomMapper.updateById(adjustRoom);
        return i;
    }

    /**
     * 按名字查找调宿申请
     */
    @Override
    public Page findByName(Integer pageNum, Integer pageSize, String name) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        qw.eq("name", name);  // 使用精确匹配而不是模糊匹配
        Page orderPage = adjustRoomMapper.selectPage(page, qw);
        return orderPage;
    }

    @Override
    public Boolean ifStu(String username) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Student student = studentMapper.selectOne(qw);
        return student != null;
    }

}
