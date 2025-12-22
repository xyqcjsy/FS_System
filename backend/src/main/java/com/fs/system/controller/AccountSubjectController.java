package com.fs.system.controller;

import com.fs.system.common.Result;
import com.fs.system.entity.AccountSubject;
import com.fs.system.service.AccountSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会计科目控制器
 *
 * @author FS_System
 * @since 2025-12-20
 */
@RestController
@RequestMapping("/account-subject")
@RequiredArgsConstructor
public class AccountSubjectController {

    private final AccountSubjectService subjectService;

    /**
     * 查询所有会计科目
     */
    @GetMapping("/list")
    public Result<List<AccountSubject>> list() {
        List<AccountSubject> list = subjectService.getAllSubjects();
        return Result.successData(list);
    }

    /**
     * 根据ID查询会计科目
     */
    @GetMapping("/{id}")
    public Result<AccountSubject> getById(@PathVariable Long id) {
        AccountSubject subject = subjectService.getSubjectById(id);
        return Result.successData(subject);
    }

    /**
     * 根据类型查询会计科目
     */
    @GetMapping("/type/{type}")
    public Result<List<AccountSubject>> getByType(@PathVariable String type) {
        List<AccountSubject> list = subjectService.getSubjectsByType(type);
        return Result.successData(list);
    }

    /**
     * 查询子科目
     */
    @GetMapping("/children/{parentId}")
    public Result<List<AccountSubject>> getChildren(@PathVariable Long parentId) {
        List<AccountSubject> list = subjectService.getChildSubjects(parentId);
        return Result.successData(list);
    }

    /**
     * 查询所有启用的叶子科目（用于记账）
     */
    @GetMapping("/leaf")
    public Result<List<AccountSubject>> getLeafSubjects() {
        List<AccountSubject> list = subjectService.getEnabledLeafSubjects();
        return Result.successData(list);
    }

    /**
     * 新增会计科目
     */
    @PostMapping
    public Result<Void> create(@RequestBody AccountSubject subject) {
        subjectService.createSubject(subject);
        return Result.success("会计科目创建成功");
    }

    /**
     * 更新会计科目
     */
    @PutMapping
    public Result<Void> update(@RequestBody AccountSubject subject) {
        subjectService.updateSubject(subject);
        return Result.success("会计科目更新成功");
    }

    /**
     * 删除会计科目
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return Result.success("会计科目删除成功");
    }
}

