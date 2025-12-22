package com.fs.system.service;

import com.fs.system.entity.Company;
import com.fs.system.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 企业信息Service
 */
@Service
@RequiredArgsConstructor
public class CompanyService {
    
    private final CompanyMapper companyMapper;
    
    /**
     * 查询企业信息（通常只有一条）
     */
    public Company getCompanyInfo() {
        List<Company> list = companyMapper.selectAll();
        return list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * 创建或更新企业信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdate(Company company) {
        Company existing = getCompanyInfo();
        if (existing == null) {
            return companyMapper.insert(company);
        } else {
            company.setId(existing.getId());
            return companyMapper.update(company);
        }
    }
    
    /**
     * 更新企业信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(Company company) {
        return companyMapper.update(company);
    }
}

