package com.fs.system.mapper;

import com.fs.system.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 员工 Mapper 接口
 *
 * @author FS_System
 * @since 2025-12-20
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 查询所有员工
     */
    @Select("SELECT * FROM base_employee ORDER BY id DESC")
    List<Employee> selectAll();

    /**
     * 根据ID查询员工
     */
    @Select("SELECT * FROM base_employee WHERE id = #{id}")
    Employee selectById(Long id);

    /**
     * 根据员工编号查询
     */
    @Select("SELECT * FROM base_employee WHERE employee_code = #{employeeCode}")
    Employee selectByCode(String employeeCode);

    /**
     * 根据关键字搜索员工（支持编号、名称、部门、职位模糊查询）
     */
    @Select("<script>" +
            "SELECT * FROM base_employee " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (employee_code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR employee_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR department LIKE CONCAT('%', #{keyword}, '%') " +
            "OR position LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "</where>" +
            "ORDER BY id DESC" +
            "</script>")
    List<Employee> selectByKeyword(String keyword);

    /**
     * 根据状态查询员工
     */
    @Select("SELECT * FROM base_employee WHERE status = #{status} ORDER BY id DESC")
    List<Employee> selectByStatus(String status);

    /**
     * 插入员工
     */
    @Insert("INSERT INTO base_employee (employee_code, employee_name, department, position, phone, " +
            "email, id_card, entry_date, status, remark) " +
            "VALUES (#{employeeCode}, #{employeeName}, #{department}, #{position}, #{phone}, " +
            "#{email}, #{idCard}, #{entryDate}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Employee employee);

    /**
     * 更新员工
     */
    @Update("UPDATE base_employee SET employee_code = #{employeeCode}, employee_name = #{employeeName}, " +
            "department = #{department}, position = #{position}, phone = #{phone}, email = #{email}, " +
            "id_card = #{idCard}, entry_date = #{entryDate}, status = #{status}, remark = #{remark} " +
            "WHERE id = #{id}")
    int update(Employee employee);

    /**
     * 删除员工
     */
    @Delete("DELETE FROM base_employee WHERE id = #{id}")
    int delete(Long id);

    /**
     * 检查员工编号是否存在
     */
    @Select("SELECT COUNT(*) FROM base_employee WHERE employee_code = #{employeeCode}")
    int checkCodeExists(String employeeCode);
}

