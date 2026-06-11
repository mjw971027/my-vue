package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsOpinion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工装申请审批意见表 Mapper
 */
@Mapper
public interface TComponentsOpinionMapper {
    
    /**
     * 根据 GUID 查询
     */
    TComponentsOpinion selectByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装申请ID查询意见列表
     */
    List<TComponentsOpinion> selectByComponentsId(@Param("componentsId") String componentsId);
    
    /**
     * 根据条件查询列表
     */
    List<TComponentsOpinion> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TComponentsOpinion record);
    
    /**
     * 根据 GUID 更新
     */
    int updateByGuid(TComponentsOpinion record);
    
    /**
     * 根据 GUID 删除
     */
    int deleteByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装申请ID删除所有意见
     */
    int deleteByComponentsId(@Param("componentsId") String componentsId);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TComponentsOpinion record);
    
    /**
     * 根据GUID更新非空字段（Selective）
     */
    int updateByGuidSelective(TComponentsOpinion record);
}
