package com.example.studentservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.exception.ResourceNotFoundException;
import com.example.studentservice.dao.StudentDao;
import com.example.studentservice.dto.request.StudentCreateRequest;
import com.example.studentservice.dto.request.StudentQueryRequest;
import com.example.studentservice.dto.request.StudentUpdateRequest;
import com.example.studentservice.entity.EduStudent;
import com.example.studentservice.service.StudentService;
import com.example.studentservice.vo.StudentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStudent(StudentCreateRequest request) {
        log.info("创建学生，请求参数：{}", request);

        // 检查学号是否已存在
        LambdaQueryWrapper<EduStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduStudent::getStudentNo, request.getStudentNo());
        Long count = studentDao.selectCount(wrapper);
        if (count > 0) {
            throw new IllegalArgumentException("学号已存在: " + request.getStudentNo());
        }

        // 创建学生实体
        EduStudent student = new EduStudent();
        BeanUtils.copyProperties(request, student);

        // 插入数据库
        int rows = studentDao.insert(student);
        if (rows <= 0) {
            throw new RuntimeException("创建学生失败");
        }

        log.info("学生创建成功，学生ID：{}", student.getId());
        return student.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStudent(Long id, StudentUpdateRequest request) {
        log.info("更新学生信息，学生ID：{}，请求参数：{}", id, request);

        // 检查学生是否存在
        EduStudent student = studentDao.selectById(id);
        if (student == null) {
            throw new ResourceNotFoundException("学生", id);
        }

        // 更新学生信息
        EduStudent updateStudent = new EduStudent();
        BeanUtils.copyProperties(request, updateStudent);
        updateStudent.setId(id);

        int rows = studentDao.updateById(updateStudent);
        if (rows <= 0) {
            log.warn("更新学生信息失败，学生ID：{}", id);
            return false;
        }

        log.info("学生信息更新成功，学生ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteStudent(Long id) {
        log.info("删除学生，学生ID：{}", id);

        // 检查学生是否存在
        EduStudent student = studentDao.selectById(id);
        if (student == null) {
            throw new ResourceNotFoundException("学生", id);
        }

        // 软删除
        int rows = studentDao.deleteById(id);
        if (rows <= 0) {
            log.warn("删除学生失败，学生ID：{}", id);
            return false;
        }

        log.info("学生删除成功，学生ID：{}", id);
        return true;
    }

    @Override
    public StudentVO getStudentDetail(Long id) {
        log.info("获取学生详情，学生ID：{}", id);

        StudentVO studentVO = studentDao.selectStudentDetailById(id);
        if (studentVO == null) {
            throw new ResourceNotFoundException("学生", id);
        }

        // 设置性别描述
        if (studentVO.getGender() != null) {
            studentVO.setGenderDesc(studentVO.getGender() == 1 ? "男" : "女");
        }

        return studentVO;
    }

    @Override
    public StudentVO getStudentByNo(String studentNo) {
        log.info("根据学号查询学生，学号：{}", studentNo);

        StudentVO studentVO = studentDao.selectStudentByNo(studentNo);
        if (studentVO == null) {
            throw new ResourceNotFoundException("学生不存在，学号: " + studentNo);
        }

        // 设置性别描述
        if (studentVO.getGender() != null) {
            studentVO.setGenderDesc(studentVO.getGender() == 1 ? "男" : "女");
        }

        return studentVO;
    }

    @Override
    public IPage<StudentVO> getStudentList(StudentQueryRequest request) {
        log.info("分页查询学生列表，请求参数：{}", request);

        // 创建分页对象
        Page<StudentVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 查询
        IPage<StudentVO> result = studentDao.selectStudentPage(page, request);

        // 设置性别描述
        result.getRecords().forEach(studentVO -> {
            if (studentVO.getGender() != null) {
                studentVO.setGenderDesc(studentVO.getGender() == 1 ? "男" : "女");
            }
        });

        return result;
    }
}
