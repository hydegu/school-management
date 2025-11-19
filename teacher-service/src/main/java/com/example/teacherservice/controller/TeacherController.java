package com.example.teacherservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.teacherservice.dto.request.TeacherCreateRequest;
import com.example.teacherservice.dto.request.TeacherQueryRequest;
import com.example.teacherservice.dto.request.TeacherUpdateRequest;
import com.example.teacherservice.service.TeacherService;
import com.example.teacherservice.vo.TeacherVO;
import com.example.utils.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 教师管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /**
     * 创建教师
     *
     * @param request 创建请求
     * @return 教师ID
     */
    @PostMapping
    public R<Long> createTeacher(@Valid @RequestBody TeacherCreateRequest request) {
        try {
            Long teacherId = teacherService.createTeacher(request);
            return R.ok("教师创建成功", teacherId);
        } catch (IllegalArgumentException e) {
            log.warn("创建教师失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("创建教师异常: ", e);
            return R.error("创建教师失败");
        }
    }

    /**
     * 更新教师信息
     *
     * @param id      教师ID
     * @param request 更新请求
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public R<Boolean> updateTeacher(@PathVariable Long id,
                                     @Valid @RequestBody TeacherUpdateRequest request) {
        try {
            Boolean success = teacherService.updateTeacher(id, request);
            return success ? R.ok("教师信息更新成功", true) : R.error("教师信息更新失败");
        } catch (IllegalArgumentException e) {
            log.warn("更新教师失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("更新教师异常: ", e);
            return R.error("更新教师失败");
        }
    }

    /**
     * 删除教师
     *
     * @param id 教师ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public R<Boolean> deleteTeacher(@PathVariable Long id) {
        try {
            Boolean success = teacherService.deleteTeacher(id);
            return success ? R.ok("教师删除成功", true) : R.error("教师删除失败");
        } catch (IllegalArgumentException e) {
            log.warn("删除教师失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("删除教师异常: ", e);
            return R.error("删除教师失败");
        }
    }

    /**
     * 获取教师详情
     *
     * @param id 教师ID
     * @return 教师详情
     */
    @GetMapping("/{id}")
    public R<TeacherVO> getTeacherDetail(@PathVariable Long id) {
        try {
            TeacherVO teacherVO = teacherService.getTeacherDetail(id);
            return R.ok(teacherVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询教师详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询教师详情异常: ", e);
            return R.error("查询教师详情失败");
        }
    }

    /**
     * 根据教师编号查询教师
     *
     * @param teacherNo 教师编号
     * @return 教师信息
     */
    @GetMapping("/no/{teacherNo}")
    public R<TeacherVO> getTeacherByNo(@PathVariable String teacherNo) {
        try {
            TeacherVO teacherVO = teacherService.getTeacherByNo(teacherNo);
            return R.ok(teacherVO);
        } catch (IllegalArgumentException e) {
            log.warn("根据教师编号查询教师失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("根据教师编号查询教师异常: ", e);
            return R.error("根据教师编号查询教师失败");
        }
    }

    /**
     * 分页查询教师列表
     *
     * @param request 查询请求
     * @return 教师列表
     */
    @GetMapping("/list")
    public R<IPage<TeacherVO>> getTeacherList(TeacherQueryRequest request) {
        try {
            IPage<TeacherVO> page = teacherService.getTeacherList(request);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询教师列表异常: ", e);
            return R.error("查询教师列表失败");
        }
    }
}
