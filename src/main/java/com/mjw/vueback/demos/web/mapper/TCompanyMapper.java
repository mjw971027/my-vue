package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公司主体表 Mapper
 */
@Mapper
public interface TCompanyMapper {
    
    /**
     * 查询所有有效公司
     */
    List<TCompany> selectAllValid();
    
    /**
     * 根据代码查询
     */
    TCompany selectByCode(@Param("code") String code);
    
    /**
     * 根据条件查询
     */
    List<TCompany> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TCompany record);
    
    /**
     * 根据代码更新
     */
    int updateByCode(TCompany record);
    
    /**
     * 根据代码删除
     */
    int deleteByCode(@Param("code") String code);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TCompany record);
    
    /**
     * 根据代码更新非空字段（Selective）
     */
    int updateByCodeSelective(TCompany record);
}
