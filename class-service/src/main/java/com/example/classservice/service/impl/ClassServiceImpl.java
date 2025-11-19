package com.example.classservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.classservice.dao.ClassDao;
import com.example.classservice.dao.GradeDao;
import com.example.classservice.dao.StudentDao;
import com.example.classservice.dao.TeacherDao;
import com.example.classservice.dto.request.ClassCreateRequest;
import com.example.classservice.dto.request.ClassQueryRequest;
import com.example.classservice.dto.request.ClassUpdateRequest;
import com.example.classservice.dto.request.StudentBatchAssignRequest;
import com.example.classservice.entity.EduClass;
import com.example.classservice.entity.EduGrade;
import com.example.classservice.entity.EduStudent;
import com.example.classservice.entity.EduTeacher;
import com.example.classservice.enums.ClassStatus;
import com.example.classservice.service.ClassService;
import com.example.classservice.vo.ClassVO;
import com.example.classservice.vo.StudentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassDao classDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final GradeDao gradeDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createClass(ClassCreateRequest request) {
        log.info("创建班级: className={}, gradeId={}", request.getClassName(), request.getGradeId());

        // 1. 校验年级是否存在
        EduGrade grade = gradeDao.selectById(request.getGradeId());
        if (grade == null) {
            throw new IllegalArgumentException("年级不存在");
        }

        // 2. 校验班级编号唯一性
        Long count = classDao.selectCount(new LambdaQueryWrapper<EduClass>()
                .eq(EduClass::getClassNo, request.getClassNo()));
        if (count > 0) {
            throw new IllegalArgumentException("班级编号已存在");
        }

        // 3. 如果指定了班主任,校验班主任是否存在
        if (request.getHeadTeacherId() != null) {
            EduTeacher teacher = teacherDao.selectById(request.getHeadTeacherId());
            if (teacher == null) {
                throw new IllegalArgumentException("班主任不存在");
            }
        }

        // 4. 构建班级实体
        EduClass eduClass = new EduClass();
        BeanUtils.copyProperties(request, eduClass);
        eduClass.setCurrentStudents(0);
        eduClass.setStatus(ClassStatus.ENABLED.getCode());

        // 5. 保存到数据库
        classDao.insert(eduClass);

        log.info("班级创建成功: classId={}, classNo={}", eduClass.getId(), eduClass.getClassNo());
        return eduClass.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateClass(Long id, ClassUpdateRequest request) {
        log.info("更新班级: classId={}", id);

        // 1. 查询班级是否存在
        EduClass eduClass = classDao.selectById(id);
        if (eduClass == null) {
            throw new IllegalArgumentException("班级不存在");
        }

        // 2. 如果更新班主任,校验班主任是否存在
        if (request.getHeadTeacherId() != null) {
            EduTeacher teacher = teacherDao.selectById(request.getHeadTeacherId());
            if (teacher == null) {
                throw new IllegalArgumentException("班主任不存在");
            }
        }

        // 3. 更新班级信息
        BeanUtils.copyProperties(request, eduClass);
        int rows = classDao.updateById(eduClass);

        log.info("班级更新成功: classId={}", id);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteClass(Long id) {
        log.info("删除班级: classId={}", id);

        // 1. 查询班级是否存在
        EduClass eduClass = classDao.selectById(id);
        if (eduClass == null) {
            throw new IllegalArgumentException("班级不存在");
        }

        // 2. 检查班级是否有学生
        if (eduClass.getCurrentStudents() != null && eduClass.getCurrentStudents() > 0) {
            throw new IllegalStateException("班级还有学生,无法删除");
        }

        // 3. 软删除
        int rows = classDao.deleteById(id);

        log.info("班级删除成功: classId={}", id);
        return rows > 0;
    }

    @Override
    public ClassVO getClassDetail(Long id) {
        log.info("查询班级详情: classId={}", id);

        ClassVO classVO = classDao.selectClassDetailById(id);
        if (classVO == null) {
            throw new IllegalArgumentException("班级不存在");
        }

        // 丰富VO对象
        enrichClassVO(classVO);

        return classVO;
    }

    @Override
    public IPage<ClassVO> getClassList(ClassQueryRequest request) {
        log.info("分页查询班级列表: request={}", request);

        Page<ClassVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        IPage<ClassVO> result = classDao.selectClassPage(
                page,
                request.getClassName(),
                request.getGradeId(),
                request.getHeadTeacherId(),
                request.getSchoolYear(),
                request.getStatus()
        );

        // 丰富VO对象
        result.getRecords().forEach(this::enrichClassVO);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignHeadTeacher(Long classId, Long headTeacherId) {
        log.info("分配班主任: classId={}, headTeacherId={}", classId, headTeacherId);

        // 1. 校验班级是否存在
        EduClass eduClass = classDao.selectById(classId);
        if (eduClass == null) {
            throw new IllegalArgumentException("班级不存在");
        }

        // 2. 校验教师是否存在
        EduTeacher teacher = teacherDao.selectById(headTeacherId);
        if (teacher == null) {
            throw new IllegalArgumentException("教师不存在");
        }

        // 3. 更新班级班主任
        eduClass.setHeadTeacherId(headTeacherId);
        int rows = classDao.updateById(eduClass);

        log.info("班主任分配成功: classId={}, headTeacherId={}", classId, headTeacherId);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAssignStudents(StudentBatchAssignRequest request) {
        log.info("批量分配学生: classId={}, studentCount={}",
                request.getClassId(), request.getStudentIds().size());

        // 1. 校验班级是否存在
        EduClass eduClass = classDao.selectById(request.getClassId());
        if (eduClass == null) {
            throw new IllegalArgumentException("班级不存在");
        }

        // 2. 校验班级容量
        int newStudentCount = eduClass.getCurrentStudents() + request.getStudentIds().size();
        if (newStudentCount > eduClass.getMaxStudents()) {
            throw new IllegalStateException("班级人数已满,无法添加更多学生");
        }

        // 3. 批量更新学生班级
        for (Long studentId : request.getStudentIds()) {
            EduStudent student = studentDao.selectById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("学生不存在: studentId=" + studentId);
            }

            student.setClassId(request.getClassId());
            student.setGradeId(eduClass.getGradeId());
            studentDao.updateById(student);
        }

        // 4. 更新班级学生数
        classDao.updateStudentCount(request.getClassId(), request.getStudentIds().size());

        log.info("学生批量分配成功: classId={}, assignedCount={}",
                request.getClassId(), request.getStudentIds().size());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeStudent(Long classId, Long studentId) {
        log.info("移除学生: classId={}, studentId={}", classId, studentId);

        // 1. 校验学生是否存在且属于该班级
        EduStudent student = studentDao.selectById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("学生不存在");
        }

        if (!classId.equals(student.getClassId())) {
            throw new IllegalArgumentException("学生不属于该班级");
        }

        // 2. 移除学生（设置班级ID为null）
        student.setClassId(null);
        studentDao.updateById(student);

        // 3. 更新班级学生数
        classDao.updateStudentCount(classId, -1);

        log.info("学生移除成功: classId={}, studentId={}", classId, studentId);
        return true;
    }

    @Override
    public IPage<StudentVO> getClassStudents(Long classId, Integer pageNum, Integer pageSize) {
        log.info("查询班级学生列表: classId={}, pageNum={}, pageSize={}", classId, pageNum, pageSize);

        Page<StudentVO> page = new Page<>(pageNum, pageSize);
        return studentDao.selectStudentsByClassId(page, classId);
    }

    /**
     * 丰富ClassVO对象（设置枚举描述等）
     */
    private void enrichClassVO(ClassVO vo) {
        // 设置状态描述
        ClassStatus status = ClassStatus.getByCode(vo.getStatus());
        if (status != null) {
            vo.setStatusDesc(status.getDesc());
        }
    }
}
