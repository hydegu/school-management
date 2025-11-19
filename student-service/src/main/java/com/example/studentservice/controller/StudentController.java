package com.example.studentservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.studentservice.dto.request.StudentCreateRequest;
import com.example.studentservice.dto.request.StudentQueryRequest;
import com.example.studentservice.dto.request.StudentUpdateRequest;
import com.example.studentservice.service.StudentService;
import com.example.studentservice.vo.StudentVO;
import com.example.utils.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 学生管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * 创建学生
     *
     * @param request 创建请求
     * @return 学生ID
     */
    @PostMapping
    public R<Long> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        try {
            Long studentId = studentService.createStudent(request);
            return R.ok("学生创建成功", studentId);
        } catch (IllegalArgumentException e) {
            log.warn("创建学生失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("创建学生异常: ", e);
            return R.error("创建学生失败");
        }
    }

    /**
     * 更新学生信息
     *
     * @param id      学生ID
     * @param request 更新请求
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public R<Boolean> updateStudent(@PathVariable Long id,
                                     @Valid @RequestBody StudentUpdateRequest request) {
        try {
            Boolean success = studentService.updateStudent(id, request);
            return success ? R.ok("学生信息更新成功", true) : R.error("学生信息更新失败");
        } catch (IllegalArgumentException e) {
            log.warn("更新学生失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("更新学生异常: ", e);
            return R.error("更新学生失败");
        }
    }

    /**
     * 删除学生
     *
     * @param id 学生ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public R<Boolean> deleteStudent(@PathVariable Long id) {
        try {
            Boolean success = studentService.deleteStudent(id);
            return success ? R.ok("学生删除成功", true) : R.error("学生删除失败");
        } catch (IllegalArgumentException e) {
            log.warn("删除学生失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("删除学生异常: ", e);
            return R.error("删除学生失败");
        }
    }

    /**
     * 获取学生详情
     *
     * @param id 学生ID
     * @return 学生详情
     */
    @GetMapping("/{id}")
    public R<StudentVO> getStudentDetail(@PathVariable Long id) {
        try {
            StudentVO studentVO = studentService.getStudentDetail(id);
            return R.ok(studentVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询学生详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询学生详情异常: ", e);
            return R.error("查询学生详情失败");
        }
    }

    /**
     * 根据学号查询学生
     *
     * @param studentNo 学号
     * @return 学生信息
     */
    @GetMapping("/no/{studentNo}")
    public R<StudentVO> getStudentByNo(@PathVariable String studentNo) {
        try {
            StudentVO studentVO = studentService.getStudentByNo(studentNo);
            return R.ok(studentVO);
        } catch (IllegalArgumentException e) {
            log.warn("根据学号查询学生失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("根据学号查询学生异常: ", e);
            return R.error("根据学号查询学生失败");
        }
    }

    /**
     * 分页查询学生列表
     *
     * @param request 查询请求
     * @return 学生列表
     */
    @GetMapping("/list")
    public R<IPage<StudentVO>> getStudentList(StudentQueryRequest request) {
        try {
            IPage<StudentVO> page = studentService.getStudentList(request);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询学生列表异常: ", e);
            return R.error("查询学生列表失败");
        }
    }
}
