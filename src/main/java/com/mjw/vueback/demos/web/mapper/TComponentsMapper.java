package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponents;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工装申请表 Mapper
 */
@Mapper
public interface TComponentsMapper {
    
    /**
     * 根据 GUID 查询
     */
    TComponents selectByGuid(@Param("guid") String guid);
    
    /**
     * 根据申请单号查询
     */
    TComponents selectByBillNo(@Param("billNo") String billNo);
    
    /**
     * 根据条件查询列表
     */
    List<TComponents> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TComponents record);
    
    /**
     * 根据 GUID 更新
     */
    int updateByGuid(TComponents record);
    
    /**
     * 根据 GUID 删除
     */
    int deleteByGuid(@Param("guid") String guid);
    
    /**
     * 根据申请单号删除
     */
    int deleteByBillNo(@Param("billNo") String billNo);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TComponents record);
    
    /**
     * 根据GUID更新非空字段（Selective）
     */
    int updateByGuidSelective(TComponents record);
    
    /**
     * 根据billNo前缀查询当天最大的billNo
     * @param billNoPrefix 前缀（公司编码 + 年月日）
     * @return 当天最大的billNo
     */
    String selectMaxBillNoByPrefix(@Param("billNoPrefix") String billNoPrefix);
}
