package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 单位 Mapper 接口
 */
@Mapper
public interface TUnitMapper {
    
    /**
     * 查询所有有效单位
     */
    List<TUnit> selectAllValid();
    
    /**
     * 根据ID查询
     */
    TUnit selectByGuid(@Param("guid") String guid);
}
