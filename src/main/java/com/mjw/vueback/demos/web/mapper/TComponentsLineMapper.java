package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工装申请明细表 Mapper
 */
@Mapper
public interface TComponentsLineMapper {
    
    /**
     * 根据 GUID 查询
     */
    TComponentsLine selectByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装申请ID查询明细列表
     */
    List<TComponentsLine> selectByComponentsId(@Param("componentsId") String componentsId);
    
    /**
     * 根据条件查询列表
     */
    List<TComponentsLine> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TComponentsLine record);
    
    /**
     * 根据 GUID 更新
     */
    int updateByGuid(TComponentsLine record);
    
    /**
     * 根据 GUID 删除
     */
    int deleteByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装申请ID删除所有明细
     */
    int deleteByComponentsId(@Param("componentsId") String componentsId);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TComponentsLine record);
    
    /**
     * 根据GUID更新非空字段（Selective）
     */
    int updateByGuidSelective(TComponentsLine record);


    String getNewMactNo();
}
