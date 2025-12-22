package com.fs.system.service;

import com.fs.system.entity.AccountSubject;
import com.fs.system.exception.BusinessException;
import com.fs.system.mapper.AccountSubjectMapper;
import com.fs.system.mapper.VoucherEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会计科目服务类
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Service
@RequiredArgsConstructor
public class AccountSubjectService {

    private final AccountSubjectMapper subjectMapper;
    private final VoucherEntryMapper entryMapper;

    /**
     * 查询所有会计科目
     */
    public List<AccountSubject> getAllSubjects() {
        return subjectMapper.selectAll();
    }

    /**
     * 根据ID查询会计科目
     */
    public AccountSubject getSubjectById(Long id) {
        AccountSubject subject = subjectMapper.selectById(id);
        if (subject == null) {
            throw new BusinessException("会计科目不存在");
        }
        return subject;
    }

    /**
     * 根据类型查询会计科目
     */
    public List<AccountSubject> getSubjectsByType(String subjectType) {
        return subjectMapper.selectByType(subjectType);
    }

    /**
     * 查询子科目
     */
    public List<AccountSubject> getChildSubjects(Long parentId) {
        return subjectMapper.selectByParentId(parentId);
    }

    /**
     * 查询所有启用的叶子科目（用于记账）
     */
    public List<AccountSubject> getEnabledLeafSubjects() {
        return subjectMapper.selectEnabledLeafSubjects();
    }

    /**
     * 新增会计科目
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSubject(AccountSubject subject) {
        // 检查科目编码是否存在
        if (subjectMapper.checkCodeExists(subject.getSubjectCode()) > 0) {
            throw new BusinessException("科目编码已存在");
        }

        // 如果有父科目，更新父科目为非叶子节点
        if (subject.getParentId() != null && subject.getParentId() > 0) {
            AccountSubject parent = subjectMapper.selectById(subject.getParentId());
            if (parent != null && parent.getIsLeaf()) {
                parent.setIsLeaf(false);
                subjectMapper.update(parent);
            }
        }

        subjectMapper.insert(subject);
    }

    /**
     * 更新会计科目
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSubject(AccountSubject subject) {
        AccountSubject existing = subjectMapper.selectById(subject.getId());
        if (existing == null) {
            throw new BusinessException("会计科目不存在");
        }

        // 检查科目编码是否与其他科目重复
        AccountSubject duplicate = subjectMapper.selectAll().stream()
                .filter(s -> s.getSubjectCode().equals(subject.getSubjectCode()) 
                        && !s.getId().equals(subject.getId()))
                .findFirst()
                .orElse(null);
        if (duplicate != null) {
            throw new BusinessException("科目编码已存在");
        }

        subjectMapper.update(subject);
    }

    /**
     * 删除会计科目
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSubject(Long id) {
        AccountSubject subject = subjectMapper.selectById(id);
        if (subject == null) {
            throw new BusinessException("会计科目不存在");
        }

        // 检查是否有子科目
        List<AccountSubject> children = subjectMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new BusinessException("该科目存在子科目，无法删除");
        }

        // 检查是否有关联的凭证分录
        int entryCount = entryMapper.countBySubjectId(id);
        if (entryCount > 0) {
            throw new BusinessException("该科目已被 " + entryCount + " 条凭证分录使用，无法删除");
        }

        subjectMapper.delete(id);
    }
}

