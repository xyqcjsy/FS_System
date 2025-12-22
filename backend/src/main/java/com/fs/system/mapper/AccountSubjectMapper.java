package com.fs.system.mapper;

import com.fs.system.entity.AccountSubject;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 会计科目 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface AccountSubjectMapper {

    /**
     * 查询所有会计科目
     */
    @Select("SELECT * FROM acct_subject ORDER BY subject_code")
    List<AccountSubject> selectAll();

    /**
     * 根据ID查询会计科目
     */
    @Select("SELECT * FROM acct_subject WHERE id = #{id}")
    AccountSubject selectById(Long id);

    /**
     * 根据科目编码查询会计科目
     */
    @Select("SELECT * FROM acct_subject WHERE subject_code = #{subjectCode}")
    AccountSubject selectByCode(String subjectCode);

    /**
     * 根据科目类型查询
     */
    @Select("SELECT * FROM acct_subject WHERE subject_type = #{subjectType} ORDER BY subject_code")
    List<AccountSubject> selectByType(String subjectType);

    /**
     * 根据父ID查询子科目
     */
    @Select("SELECT * FROM acct_subject WHERE parent_id = #{parentId} ORDER BY subject_code")
    List<AccountSubject> selectByParentId(Long parentId);

    /**
     * 查询所有启用的叶子科目
     */
    @Select("SELECT * FROM acct_subject WHERE is_enabled = 1 AND is_leaf = 1 ORDER BY subject_code")
    List<AccountSubject> selectEnabledLeafSubjects();

    /**
     * 插入会计科目
     */
    @Insert("INSERT INTO acct_subject (subject_code, subject_name, subject_type, parent_id, level, " +
            "is_leaf, balance_direction, is_enabled, description) " +
            "VALUES (#{subjectCode}, #{subjectName}, #{subjectType}, #{parentId}, #{level}, " +
            "#{isLeaf}, #{balanceDirection}, #{isEnabled}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AccountSubject subject);

    /**
     * 更新会计科目
     */
    @Update("UPDATE acct_subject SET subject_code = #{subjectCode}, subject_name = #{subjectName}, " +
            "subject_type = #{subjectType}, parent_id = #{parentId}, level = #{level}, " +
            "is_leaf = #{isLeaf}, balance_direction = #{balanceDirection}, is_enabled = #{isEnabled}, " +
            "description = #{description} WHERE id = #{id}")
    int update(AccountSubject subject);

    /**
     * 删除会计科目
     */
    @Delete("DELETE FROM acct_subject WHERE id = #{id}")
    int delete(Long id);

    /**
     * 检查科目编码是否存在
     */
    @Select("SELECT COUNT(*) FROM acct_subject WHERE subject_code = #{subjectCode}")
    int checkCodeExists(String subjectCode);
    
    /**
     * 根据科目编码列表查询科目
     */
    @Select("<script>" +
            "SELECT * FROM acct_subject WHERE subject_code IN " +
            "<foreach collection='list' item='code' open='(' separator=',' close=')'>" +
            "#{code}" +
            "</foreach>" +
            "</script>")
    List<AccountSubject> selectByCodesIn(List<String> codes);
}

