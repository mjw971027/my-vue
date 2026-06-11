package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsAtt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工装申请附件表 Mapper
 */
@Mapper
public interface TComponentsAttMapper {
    
    /**
     * 根据 GUID 查询
     */
    TComponentsAtt selectByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装申请ID查询附件列表
     */
    List<TComponentsAtt> selectByComponentsId(@Param("componentsId") String componentsId);
    
    /**
     * 根据条件查询列表
     */
    List<TComponentsAtt> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TComponentsAtt record);
    
    /**
     * 根据 GUID 更新
     */
    int updateByGuid(TComponentsAtt record);
    
    /**
     * 根据 GUID 删除
     */
    int deleteByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装申请ID删除所有附件
     */
    int deleteByComponentsId(@Param("componentsId") String componentsId);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TComponentsAtt record);
    
    /**
     * 根据GUID更新非空字段（Selective）
     */
    int updateByGuidSelective(TComponentsAtt record);
}
