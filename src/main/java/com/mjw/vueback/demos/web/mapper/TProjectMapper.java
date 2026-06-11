package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工程号表 Mapper
 */
@Mapper
public interface TProjectMapper {
    
    /**
     * 查询所有有效项目
     */
    List<TProject> selectAllValid();
    
    /**
     * 根据代码查询
     */
    TProject selectByCode(@Param("code") String code);
    
    /**
     * 根据条件查询
     */
    List<TProject> selectList(Map<String, Object> params);
    
    /**
     * 插入
     */
    int insert(TProject record);
    
    /**
     * 根据代码更新
     */
    int updateByCode(TProject record);
    
    /**
     * 根据代码删除
     */
    int deleteByCode(@Param("code") String code);
    
    /**
     * 插入非空字段（Selective）
     */
    int insertSelective(TProject record);
    
    /**
     * 根据代码更新非空字段（Selective）
     */
    int updateByCodeSelective(TProject record);
}
