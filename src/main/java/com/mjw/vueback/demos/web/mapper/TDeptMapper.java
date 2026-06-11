package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门组织表 Mapper
 */
@Mapper
public interface TDeptMapper {
    
    /**
     * 根据上级组织代码查询所有有效部门
     */
    List<TDept> selectBySuperOrgnCd(@Param("superOrgnCd") String superOrgnCd);
    
    /**
     * 根据组织代码查询
     */
    TDept selectByOrgnCd(@Param("orgnCd") String orgnCd);
    
    /**
     * 根据条件查询
     */
    List<TDept> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TDept record);
    
    /**
     * 根据组织代码更新
     */
    int updateByOrgnCd(TDept record);
    
    /**
     * 根据组织代码删除
     */
    int deleteByOrgnCd(@Param("orgnCd") String orgnCd);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TDept record);
    
    /**
     * 根据组织代码更新非空字段（Selective）
     */
    int updateByOrgnCdSelective(TDept record);
}
