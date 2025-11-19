package com.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.exception.ResourceNotFoundException;
import com.example.teacherservice.dao.TeacherDao;
import com.example.teacherservice.dto.request.TeacherCreateRequest;
import com.example.teacherservice.dto.request.TeacherQueryRequest;
import com.example.teacherservice.dto.request.TeacherUpdateRequest;
import com.example.teacherservice.entity.EduTeacher;
import com.example.teacherservice.service.TeacherService;
import com.example.teacherservice.vo.TeacherVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 教师服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDao teacherDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTeacher(TeacherCreateRequest request) {
        log.info("创建教师，请求参数：{}", request);

        // 检查教师编号是否已存在
        LambdaQueryWrapper<EduTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduTeacher::getTeacherNo, request.getTeacherNo());
        Long count = teacherDao.selectCount(wrapper);
        if (count > 0) {
            throw new IllegalArgumentException("教师编号已存在: " + request.getTeacherNo());
        }

        // 创建教师实体
        EduTeacher teacher = new EduTeacher();
        BeanUtils.copyProperties(request, teacher);

        // 插入数据库
        int rows = teacherDao.insert(teacher);
        if (rows <= 0) {
            throw new RuntimeException("创建教师失败");
        }

        log.info("教师创建成功，教师ID：{}", teacher.getId());
        return teacher.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTeacher(Long id, TeacherUpdateRequest request) {
        log.info("更新教师信息，教师ID：{}，请求参数：{}", id, request);

        // 检查教师是否存在
        EduTeacher teacher = teacherDao.selectById(id);
        if (teacher == null) {
            throw new ResourceNotFoundException("教师", id);
        }

        // 更新教师信息
        EduTeacher updateTeacher = new EduTeacher();
        BeanUtils.copyProperties(request, updateTeacher);
        updateTeacher.setId(id);

        int rows = teacherDao.updateById(updateTeacher);
        if (rows <= 0) {
            log.warn("更新教师信息失败，教师ID：{}", id);
            return false;
        }

        log.info("教师信息更新成功，教师ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTeacher(Long id) {
        log.info("删除教师，教师ID：{}", id);

        // 检查教师是否存在
        EduTeacher teacher = teacherDao.selectById(id);
        if (teacher == null) {
            throw new ResourceNotFoundException("教师", id);
        }

        // 软删除
        int rows = teacherDao.deleteById(id);
        if (rows <= 0) {
            log.warn("删除教师失败，教师ID：{}", id);
            return false;
        }

        log.info("教师删除成功，教师ID：{}", id);
        return true;
    }

    @Override
    public TeacherVO getTeacherDetail(Long id) {
        log.info("获取教师详情，教师ID：{}", id);

        TeacherVO teacherVO = teacherDao.selectTeacherDetailById(id);
        if (teacherVO == null) {
            throw new ResourceNotFoundException("教师", id);
        }

        // 设置性别描述
        if (teacherVO.getGender() != null) {
            teacherVO.setGenderDesc(teacherVO.getGender() == 1 ? "男" : "女");
        }

        return teacherVO;
    }

    @Override
    public TeacherVO getTeacherByNo(String teacherNo) {
        log.info("根据教师编号查询教师，教师编号：{}", teacherNo);

        TeacherVO teacherVO = teacherDao.selectTeacherByNo(teacherNo);
        if (teacherVO == null) {
            throw new ResourceNotFoundException("教师不存在，教师编号: " + teacherNo);
        }

        // 设置性别描述
        if (teacherVO.getGender() != null) {
            teacherVO.setGenderDesc(teacherVO.getGender() == 1 ? "男" : "女");
        }

        return teacherVO;
    }

    @Override
    public IPage<TeacherVO> getTeacherList(TeacherQueryRequest request) {
        log.info("分页查询教师列表，请求参数：{}", request);

        // 创建分页对象
        Page<TeacherVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 查询
        IPage<TeacherVO> result = teacherDao.selectTeacherPage(page, request);

        // 设置性别描述
        result.getRecords().forEach(teacherVO -> {
            if (teacherVO.getGender() != null) {
                teacherVO.setGenderDesc(teacherVO.getGender() == 1 ? "男" : "女");
            }
        });

        return result;
    }
}
