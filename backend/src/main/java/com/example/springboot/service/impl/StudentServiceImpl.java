package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.entity.Student;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.mapper.StudentMapper;
import com.example.springboot.service.DormRoomService;
import com.example.springboot.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private StudentMapper studentMapper;

    @Resource
    private DormRoomMapper dormRoomMapper;

    @Resource
    private DormRoomService dormRoomService;

    /**
     * 学生登陆
     */
    @Override
    public Student stuLogin(String username, String password) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("username", username);
        qw.eq("password", password);
        Student student = studentMapper.selectOne(qw);
        if (student != null) {
            return student;
        } else {
            return null;
        }
    }

    /**
     * 学生新增
     */
    @Override
    public int addNewStudent(Student student) {
        int insert = studentMapper.insert(findlastRoom(student));
        return insert;
    }

    //分配房间
    public Student findlastRoom(Student student){
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        qw.like("dormroom_id", "");
        List<DormRoom> dormRoomList= dormRoomMapper.selectList(qw);
        for(DormRoom dormRoom:dormRoomList){
            if (dormRoom.getFirstBed()==null){
                student.setDormroomid(dormRoom.getDormRoomId());
                dormRoom.setThirdBed(student.getUsername());
                dormRoomService.updateById(dormRoom);
                break;
            }
            if (dormRoom.getSecondBed()==null){
                student.setDormroomid(dormRoom.getDormRoomId());
                dormRoom.setSecondBed(student.getUsername());
                dormRoomService.updateById(dormRoom);
                break;
            }
            if (dormRoom.getThirdBed()==null){
                student.setDormroomid(dormRoom.getDormRoomId());
                dormRoom.setThirdBed(student.getUsername());
                dormRoomService.updateById(dormRoom);
                break;
            }
            if (dormRoom.getFourthBed()==null){
                student.setDormroomid(dormRoom.getDormRoomId());
                dormRoom.setFourthBed(student.getUsername());
                dormRoomService.updateById(dormRoom);
                break;
            }
        }
        return student;
    }



    /**
     * 分页查询学生
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.like("name", search);
        Page studentPage = studentMapper.selectPage(page, qw);
        //QueryWrapper<DormRoom> dr = new QueryWrapper<>();
        //DormRoom dormRoom = dormRoomMapper.selectById()
        //DormRoom dormRoom = dormRoomService.judgeHadBed(search);

        //studentPage.addOrder("dormRoomId",dormRoom.getDormRoomId());
        return studentPage;
    }


    /**
     * 更新学生信息
     */
    @Override
    public int updateNewStudent(Student student) {
        int i = studentMapper.updateById(student);
        return i;
    }

    /**
     * 删除学生信息
     */
    @Override
    public int deleteStudent(String username) {
        int i = studentMapper.deleteById(username);
        return i;
    }


    /**
     * 主页顶部：学生统计
     */
    @Override
    public int stuNum() {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.isNotNull("username");
        int stuNum = Math.toIntExact(studentMapper.selectCount(qw));
        return stuNum;
    }

    /**
     * 床位信息，查询该学生信息
     */
    @Override
    public Student stuInfo(String username) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Student student = studentMapper.selectOne(qw);
        return student;
    }
}
