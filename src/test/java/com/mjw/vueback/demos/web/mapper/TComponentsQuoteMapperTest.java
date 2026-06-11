package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsQuote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TComponentsQuoteMapper 测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class TComponentsQuoteMapperTest {

    @Autowired
    private TComponentsQuoteMapper tComponentsQuoteMapper;

    @Test
    public void testInsert() {
        // 创建测试数据
        TComponentsQuote record = new TComponentsQuote();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentId("test-component-id");
        record.setIsUsed("01");
        record.setIsQuoted("02");
        record.setComDate(new Date());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());

        // 执行插入
        int result = tComponentsQuoteMapper.insert(record);
        
        // 验证插入成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsQuote saved = tComponentsQuoteMapper.selectByGuid(guid);
        assertNotNull(saved);
        assertEquals("01", saved.getIsUsed());
        
        System.out.println("TComponentsQuote 插入测试成功！GUID: " + guid);
    }

    @Test
    public void testSelectByGuid() {
        // 先插入一条数据
        TComponentsQuote record = new TComponentsQuote();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentId("test-component-id");
        record.setIsUsed("01");
        record.setIsQuoted("02");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsQuoteMapper.insert(record);

        // 查询
        TComponentsQuote result = tComponentsQuoteMapper.selectByGuid(guid);
        
        // 验证
        assertNotNull(result);
        assertEquals("01", result.getIsUsed());
        
        System.out.println("TComponentsQuote 查询测试成功！IsUsed: " + result.getIsUsed());
    }

    @Test
    public void testSelectByComponentId() {
        // 先插入一条数据
        TComponentsQuote record = new TComponentsQuote();
        String guid = UUID.randomUUID().toString();
        String componentId = "test-component-id-" + System.currentTimeMillis();
        record.setGuid(guid);
        record.setComponentId(componentId);
        record.setIsUsed("01");
        record.setIsQuoted("02");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsQuoteMapper.insert(record);

        // 按工装ID查询
        List<TComponentsQuote> results = tComponentsQuoteMapper.selectByComponentId(componentId);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("TComponentsQuote 按工装ID查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testSelectList() {
        // 插入测试数据
        TComponentsQuote record = new TComponentsQuote();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentId("test-component-id");
        record.setIsUsed("01");
        record.setIsQuoted("02");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsQuoteMapper.insert(record);

        // 按条件查询
        List<TComponentsQuote> results = tComponentsQuoteMapper.selectList(null);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("TComponentsQuote 列表查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testUpdateByGuid() {
        // 先插入一条数据
        TComponentsQuote record = new TComponentsQuote();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentId("test-component-id");
        record.setIsUsed("01");
        record.setIsQuoted("02");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsQuoteMapper.insert(record);

        // 更新
        record.setIsUsed("02");
        record.setUpdateUserId("admin");
        record.setUpdateDate(new Date());
        int result = tComponentsQuoteMapper.updateByGuid(record);
        
        // 验证更新成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsQuote updated = tComponentsQuoteMapper.selectByGuid(guid);
        assertEquals("02", updated.getIsUsed());
        
        System.out.println("TComponentsQuote 更新测试成功！更新后的IsUsed: " + updated.getIsUsed());
    }

    @Test
    public void testDeleteByGuid() {
        // 先插入一条数据
        TComponentsQuote record = new TComponentsQuote();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentId("test-component-id");
        record.setIsUsed("01");
        record.setIsQuoted("02");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsQuoteMapper.insert(record);

        // 删除
        int result = tComponentsQuoteMapper.deleteByGuid(guid);
        
        // 验证删除成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsQuote deleted = tComponentsQuoteMapper.selectByGuid(guid);
        assertNull(deleted);
        
        System.out.println("TComponentsQuote 删除测试成功！GUID: " + guid);
    }
}
