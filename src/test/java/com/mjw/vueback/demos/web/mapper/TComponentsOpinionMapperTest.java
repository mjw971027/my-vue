package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsOpinion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TComponentsOpinionMapper 测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class TComponentsOpinionMapperTest {

    @Autowired
    private TComponentsOpinionMapper tComponentsOpinionMapper;

    @Test
    public void testInsert() {
        // 创建测试数据
        TComponentsOpinion record = new TComponentsOpinion();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setOpinionType("01");
        record.setOpinionContent("测试意见内容");
        record.setAuditUserId("admin");
        record.setAuditUserName("管理员");
        record.setAuditDate(new Date());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());

        // 执行插入
        int result = tComponentsOpinionMapper.insert(record);
        
        // 验证插入成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsOpinion saved = tComponentsOpinionMapper.selectByGuid(guid);
        assertNotNull(saved);
        assertEquals("测试意见内容", saved.getOpinionContent());
        
        System.out.println("TComponentsOpinion 插入测试成功！GUID: " + guid);
    }

    @Test
    public void testSelectByGuid() {
        // 先插入一条数据
        TComponentsOpinion record = new TComponentsOpinion();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setOpinionType("01");
        record.setOpinionContent("测试查询意见");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsOpinionMapper.insert(record);

        // 查询
        TComponentsOpinion result = tComponentsOpinionMapper.selectByGuid(guid);
        
        // 验证
        assertNotNull(result);
        assertEquals("测试查询意见", result.getOpinionContent());
        
        System.out.println("TComponentsOpinion 查询测试成功！OpinionContent: " + result.getOpinionContent());
    }

    @Test
    public void testSelectByComponentsId() {
        // 先插入一条数据
        TComponentsOpinion record = new TComponentsOpinion();
        String guid = UUID.randomUUID().toString();
        String componentsId = "test-components-id-" + System.currentTimeMillis();
        record.setGuid(guid);
        record.setComponentsId(componentsId);
        record.setOpinionType("01");
        record.setOpinionContent("测试按申请ID查询意见");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsOpinionMapper.insert(record);

        // 按申请ID查询
        List<TComponentsOpinion> results = tComponentsOpinionMapper.selectByComponentsId(componentsId);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("TComponentsOpinion 按申请ID查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testUpdateByGuid() {
        // 先插入一条数据
        TComponentsOpinion record = new TComponentsOpinion();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setOpinionType("01");
        record.setOpinionContent("测试更新意见");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsOpinionMapper.insert(record);

        // 更新
        record.setOpinionContent("测试更新意见-已更新");
        record.setUpdateUserId("admin");
        record.setUpdateDate(new Date());
        int result = tComponentsOpinionMapper.updateByGuid(record);
        
        // 验证更新成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsOpinion updated = tComponentsOpinionMapper.selectByGuid(guid);
        assertEquals("测试更新意见-已更新", updated.getOpinionContent());
        
        System.out.println("TComponentsOpinion 更新测试成功！更新后的内容: " + updated.getOpinionContent());
    }

    @Test
    public void testDeleteByGuid() {
        // 先插入一条数据
        TComponentsOpinion record = new TComponentsOpinion();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setOpinionType("01");
        record.setOpinionContent("测试删除意见");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsOpinionMapper.insert(record);

        // 删除
        int result = tComponentsOpinionMapper.deleteByGuid(guid);
        
        // 验证删除成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsOpinion deleted = tComponentsOpinionMapper.selectByGuid(guid);
        assertNull(deleted);
        
        System.out.println("TComponentsOpinion 删除测试成功！GUID: " + guid);
    }
}
