package com.fs.system.mapper;

import com.fs.system.entity.SalesInvoice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 销售发票Mapper
 */
@Mapper
public interface SalesInvoiceMapper {
    
    @Select("<script>" +
            "SELECT si.id, si.invoice_no, si.customer_id, si.invoice_date, si.total_amount, " +
            "si.tax_amount, si.discount_amount, si.final_amount, si.status, si.payment_method, " +
            "si.payment_date, si.is_posted, si.posted_at, si.remark, " +
            "si.created_at, si.updated_at, c.customer_name " +
            "FROM biz_sales_invoice si " +
            "LEFT JOIN base_customer c ON si.customer_id = c.id " +
            "<where>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (si.invoice_no LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR c.customer_name LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "  <if test='status != null and status != \"\"'>" +
            "    AND si.status = #{status}" +
            "  </if>" +
            "</where>" +
            "ORDER BY si.invoice_date DESC, si.id DESC" +
            "</script>")
    List<SalesInvoice> selectByCondition(@Param("keyword") String keyword, @Param("status") String status);
    
    @Select("SELECT si.id, si.invoice_no, si.customer_id, si.invoice_date, si.total_amount, " +
            "si.tax_amount, si.discount_amount, si.final_amount, si.status, si.payment_method, " +
            "si.payment_date, si.is_posted, si.posted_at, si.remark, " +
            "si.created_at, si.updated_at, c.customer_name " +
            "FROM biz_sales_invoice si " +
            "LEFT JOIN base_customer c ON si.customer_id = c.id " +
            "WHERE si.id = #{id}")
    SalesInvoice selectById(Long id);
    
    @Insert("INSERT INTO biz_sales_invoice (invoice_no, customer_id, invoice_date, total_amount, tax_amount, " +
            "discount_amount, final_amount, status, payment_method, payment_date, remark) " +
            "VALUES (#{invoiceNo}, #{customerId}, #{invoiceDate}, #{totalAmount}, #{taxAmount}, " +
            "#{discountAmount}, #{finalAmount}, #{status}, #{paymentMethod}, #{paymentDate}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SalesInvoice salesInvoice);
    
    @Update("UPDATE biz_sales_invoice SET customer_id=#{customerId}, invoice_date=#{invoiceDate}, " +
            "total_amount=#{totalAmount}, tax_amount=#{taxAmount}, discount_amount=#{discountAmount}, " +
            "final_amount=#{finalAmount}, status=#{status}, payment_method=#{paymentMethod}, " +
            "payment_date=#{paymentDate}, remark=#{remark} WHERE id=#{id}")
    int update(SalesInvoice salesInvoice);
    
    @Update("UPDATE biz_sales_invoice SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Update("UPDATE biz_sales_invoice SET is_posted=1, posted_at=NOW() WHERE id=#{id}")
    int markAsPosted(Long id);
    
    @Delete("DELETE FROM biz_sales_invoice WHERE id=#{id}")
    int delete(Long id);
}

