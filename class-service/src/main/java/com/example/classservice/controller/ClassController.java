package com.example.classservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.classservice.dto.request.ClassCreateRequest;
import com.example.classservice.dto.request.ClassQueryRequest;
import com.example.classservice.dto.request.ClassUpdateRequest;
import com.example.classservice.dto.request.StudentBatchAssignRequest;
import com.example.classservice.service.ClassService;
import com.example.classservice.vo.ClassVO;
import com.example.classservice.vo.StudentVO;
import com.example.utils.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * 班级管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    /**
     * 创建班级
     *
     * @param request 创建请求
     * @return 班级ID
     */
    @PostMapping
    public R<Long> createClass(@Valid @RequestBody ClassCreateRequest request) {
        try {
            Long classId = classService.createClass(request);
            return R.ok("班级创建成功", classId);
        } catch (IllegalArgumentException e) {
            log.warn("创建班级失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("创建班级异常: ", e);
            return R.error("创建班级失败");
        }
    }

    /**
     * 更新班级
     *
     * @param id      班级ID
     * @param request 更新请求
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public R<Boolean> updateClass(@PathVariable Long id,
                                   @RequestBody ClassUpdateRequest request) {
        try {
            Boolean success = classService.updateClass(id, request);
            return success ? R.ok("班级更新成功", true) : R.error("班级更新失败");
        } catch (IllegalArgumentException e) {
            log.warn("更新班级失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("更新班级异常: ", e);
            return R.error("更新班级失败");
        }
    }

    /**
     * 删除班级
     *
     * @param id 班级ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public R<Boolean> deleteClass(@PathVariable Long id) {
        try {
            Boolean success = classService.deleteClass(id);
            return success ? R.ok("班级删除成功", true) : R.error("班级删除失败");
        } catch (IllegalArgumentException e) {
            log.warn("删除班级失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("删除班级失败: {}", e.getMessage());
            return R.conflict(e.getMessage());
        } catch (Exception e) {
            log.error("删除班级异常: ", e);
            return R.error("删除班级失败");
        }
    }

    /**
     * 获取班级详情
     *
     * @param id 班级ID
     * @return 班级详情
     */
    @GetMapping("/{id}")
    public R<ClassVO> getClassDetail(@PathVariable Long id) {
        try {
            ClassVO classVO = classService.getClassDetail(id);
            return R.ok(classVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询班级详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询班级详情异常: ", e);
            return R.error("查询班级详情失败");
        }
    }

    /**
     * 分页查询班级列表
     *
     * @param request 查询请求
     * @return 班级列表
     */
    @GetMapping("/list")
    public R<IPage<ClassVO>> getClassList(ClassQueryRequest request) {
        try {
            IPage<ClassVO> page = classService.getClassList(request);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询班级列表异常: ", e);
            return R.error("查询班级列表失败");
        }
    }

    /**
     * 分配班主任
     *
     * @param id            班级ID
     * @param headTeacherId 班主任ID
     * @return 是否成功
     */
    @PutMapping("/{id}/teacher")
    public R<Boolean> assignHeadTeacher(@PathVariable Long id,
                                         @RequestParam Long headTeacherId) {
        try {
            Boolean success = classService.assignHeadTeacher(id, headTeacherId);
            return success ? R.ok("班主任分配成功", true) : R.error("班主任分配失败");
        } catch (IllegalArgumentException e) {
            log.warn("分配班主任失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("分配班主任异常: ", e);
            return R.error("分配班主任失败");
        }
    }

    /**
     * 批量分配学生
     *
     * @param id      班级ID
     * @param request 批量分配请求
     * @return 是否成功
     */
    @PostMapping("/{id}/students")
    public R<Boolean> batchAssignStudents(@PathVariable Long id,
                                           @Valid @RequestBody StudentBatchAssignRequest request) {
        try {
            request.setClassId(id);
            Boolean success = classService.batchAssignStudents(request);
            return success ? R.ok("学生分配成功", true) : R.error("学生分配失败");
        } catch (IllegalArgumentException e) {
            log.warn("批量分配学生失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("批量分配学生失败: {}", e.getMessage());
            return R.conflict(e.getMessage());
        } catch (Exception e) {
            log.error("批量分配学生异常: ", e);
            return R.error("批量分配学生失败");
        }
    }

    /**
     * 移除学生
     *
     * @param id        班级ID
     * @param studentId 学生ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}/students/{studentId}")
    public R<Boolean> removeStudent(@PathVariable Long id,
                                     @PathVariable Long studentId) {
        try {
            Boolean success = classService.removeStudent(id, studentId);
            return success ? R.ok("学生移除成功", true) : R.error("学生移除失败");
        } catch (IllegalArgumentException e) {
            log.warn("移除学生失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("移除学生异常: ", e);
            return R.error("移除学生失败");
        }
    }

    /**
     * 获取班级学生列表
     *
     * @param id       班级ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 学生列表
     */
    @GetMapping("/{id}/students")
    public R<IPage<StudentVO>> getClassStudents(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            IPage<StudentVO> page = classService.getClassStudents(id, pageNum, pageSize);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询班级学生列表异常: ", e);
            return R.error("查询班级学生列表失败");
        }
    }
}
