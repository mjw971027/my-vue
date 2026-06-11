package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsQuote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工装申请引用/报价表 Mapper
 */
@Mapper
public interface TComponentsQuoteMapper {
    
    /**
     * 根据 GUID 查询
     */
    TComponentsQuote selectByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装ID查询引用/报价列表
     */
    List<TComponentsQuote> selectByComponentId(@Param("componentId") String componentId);
    
    /**
     * 根据条件查询列表
     */
    List<TComponentsQuote> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TComponentsQuote record);
    
    /**
     * 根据 GUID 更新
     */
    int updateByGuid(TComponentsQuote record);
    
    /**
     * 根据 GUID 删除
     */
    int deleteByGuid(@Param("guid") String guid);
    
    /**
     * 根据工装ID删除所有引用/报价
     */
    int deleteByComponentId(@Param("componentId") String componentId);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TComponentsQuote record);
    
    /**
     * 根据GUID更新非空字段（Selective）
     */
    int updateByGuidSelective(TComponentsQuote record);
}
