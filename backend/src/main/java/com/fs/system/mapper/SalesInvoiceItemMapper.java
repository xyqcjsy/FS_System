package com.fs.system.mapper;

import com.fs.system.entity.SalesInvoiceItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 销售发票明细Mapper
 */
@Mapper
public interface SalesInvoiceItemMapper {
    
    @Select("SELECT * FROM biz_sales_invoice_item WHERE invoice_id = #{invoiceId}")
    List<SalesInvoiceItem> selectByInvoiceId(Long invoiceId);
    
    @Insert("INSERT INTO biz_sales_invoice_item (invoice_id, item_name, specification, unit, quantity, " +
            "unit_price, amount, tax_rate, remark) " +
            "VALUES (#{invoiceId}, #{itemName}, #{specification}, #{unit}, #{quantity}, #{unitPrice}, " +
            "#{amount}, #{taxRate}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SalesInvoiceItem item);
    
    @Delete("DELETE FROM biz_sales_invoice_item WHERE invoice_id = #{invoiceId}")
    int deleteByInvoiceId(Long invoiceId);
}

