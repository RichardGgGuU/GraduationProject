package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.AdjustRoom;

public interface AdjustRoomMapper extends BaseMapper<AdjustRoom> {
    Page selectall(Page page);
}
