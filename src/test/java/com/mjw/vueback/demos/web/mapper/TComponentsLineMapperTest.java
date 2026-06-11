package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TComponentsLineMapper 测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class TComponentsLineMapperTest {

    @Autowired
    private TComponentsLineMapper tComponentsLineMapper;

    @Test
    public void testInsert() {
        // 创建测试数据
        TComponentsLine record = new TComponentsLine();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setMaterialName("测试材料");
        record.setUnit("KG");
        record.setDemandQty(new BigDecimal(100));
        record.setFinalDemandQty(new BigDecimal(100));
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());

        // 执行插入
        int result = tComponentsLineMapper.insert(record);
        
        // 验证插入成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsLine saved = tComponentsLineMapper.selectByGuid(guid);
        assertNotNull(saved);
        assertEquals("测试材料", saved.getMaterialName());
        
        System.out.println("TComponentsLine 插入测试成功！GUID: " + guid);
    }

    @Test
    public void testSelectByGuid() {
        // 先插入一条数据
        TComponentsLine record = new TComponentsLine();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setMaterialName("测试查询明细");
        record.setUnit("KG");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsLineMapper.insert(record);

        // 查询
        TComponentsLine result = tComponentsLineMapper.selectByGuid(guid);
        
        // 验证
        assertNotNull(result);
        assertEquals("测试查询明细", result.getMaterialName());
        
        System.out.println("TComponentsLine 查询测试成功！MaterialName: " + result.getMaterialName());
    }

    @Test
    public void testSelectByComponentsId() {
        // 先插入一条数据
        TComponentsLine record = new TComponentsLine();
        String guid = UUID.randomUUID().toString();
        String componentsId = "test-components-id-" + System.currentTimeMillis();
        record.setGuid(guid);
        record.setComponentsId(componentsId);
        record.setMaterialName("测试按申请ID查询");
        record.setUnit("KG");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsLineMapper.insert(record);

        // 按申请ID查询
        List<TComponentsLine> results = tComponentsLineMapper.selectByComponentsId(componentsId);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("TComponentsLine 按申请ID查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testUpdateByGuid() {
        // 先插入一条数据
        TComponentsLine record = new TComponentsLine();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setMaterialName("测试更新明细");
        record.setUnit("KG");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsLineMapper.insert(record);

        // 更新
        record.setMaterialName("测试更新明细-已更新");
        record.setUpdateUserId("admin");
        record.setUpdateDate(new Date());
        int result = tComponentsLineMapper.updateByGuid(record);
        
        // 验证更新成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsLine updated = tComponentsLineMapper.selectByGuid(guid);
        assertEquals("测试更新明细-已更新", updated.getMaterialName());
        
        System.out.println("TComponentsLine 更新测试成功！更新后的名称: " + updated.getMaterialName());
    }

    @Test
    public void testDeleteByGuid() {
        // 先插入一条数据
        TComponentsLine record = new TComponentsLine();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setMaterialName("测试删除明细");
        record.setUnit("KG");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsLineMapper.insert(record);

        // 删除
        int result = tComponentsLineMapper.deleteByGuid(guid);
        
        // 验证删除成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsLine deleted = tComponentsLineMapper.selectByGuid(guid);
        assertNull(deleted);
        
        System.out.println("TComponentsLine 删除测试成功！GUID: " + guid);
    }
}
