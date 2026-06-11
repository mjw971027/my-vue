package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsAttData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工装申请附件数据表 Mapper
 */
@Mapper
public interface TComponentsAttDataMapper {
    
    /**
     * 根据 GUID 查询
     */
    TComponentsAttData selectByGuid(@Param("guid") String guid);
    
    /**
     * 根据附件ID查询附件数据
     */
    List<TComponentsAttData> selectByAttId(@Param("attId") String attId);
    
    /**
     * 根据条件查询列表
     */
    List<TComponentsAttData> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TComponentsAttData record);
    
    /**
     * 根据 GUID 更新
     */
    int updateByGuid(TComponentsAttData record);
    
    /**
     * 根据 GUID 删除
     */
    int deleteByGuid(@Param("guid") String guid);
    
    /**
     * 根据附件ID删除所有附件数据
     */
    int deleteByAttId(@Param("attId") String attId);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TComponentsAttData record);
    
    /**
     * 根据GUID更新非空字段（Selective）
     */
    int updateByGuidSelective(TComponentsAttData record);
}
