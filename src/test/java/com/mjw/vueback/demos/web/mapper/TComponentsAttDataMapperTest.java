package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsAttData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TComponentsAttDataMapper 测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class TComponentsAttDataMapperTest {

    @Autowired
    private TComponentsAttDataMapper tComponentsAttDataMapper;

    @Test
    public void testInsert() {
        // 创建测试数据
        TComponentsAttData record = new TComponentsAttData();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setAttId("test-att-id");
        record.setFileData("测试文件数据".getBytes());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());

        // 执行插入
        int result = tComponentsAttDataMapper.insert(record);
        
        // 验证插入成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsAttData saved = tComponentsAttDataMapper.selectByGuid(guid);
        assertNotNull(saved);
        assertNotNull(saved.getFileData());
        
        System.out.println("TComponentsAttData 插入测试成功！GUID: " + guid);
    }

    @Test
    public void testSelectByGuid() {
        // 先插入一条数据
        TComponentsAttData record = new TComponentsAttData();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setAttId("test-att-id");
        record.setFileData("测试查询数据".getBytes());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttDataMapper.insert(record);

        // 查询
        TComponentsAttData result = tComponentsAttDataMapper.selectByGuid(guid);
        
        // 验证
        assertNotNull(result);
        assertNotNull(result.getFileData());
        
        System.out.println("TComponentsAttData 查询测试成功！GUID: " + result.getGuid());
    }

    @Test
    public void testSelectByAttId() {
        // 先插入一条数据
        TComponentsAttData record = new TComponentsAttData();
        String guid = UUID.randomUUID().toString();
        String attId = "test-att-id-" + System.currentTimeMillis();
        record.setGuid(guid);
        record.setAttId(attId);
        record.setFileData("测试按附件ID查询".getBytes());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttDataMapper.insert(record);

        // 按附件ID查询
        List<TComponentsAttData> results = tComponentsAttDataMapper.selectByAttId(attId);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("TComponentsAttData 按附件ID查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testUpdateByGuid() {
        // 先插入一条数据
        TComponentsAttData record = new TComponentsAttData();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setAttId("test-att-id");
        record.setFileData("测试更新数据".getBytes());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttDataMapper.insert(record);

        // 更新
        record.setFileData("测试更新数据-已更新".getBytes());
        int result = tComponentsAttDataMapper.updateByGuid(record);
        
        // 验证更新成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsAttData updated = tComponentsAttDataMapper.selectByGuid(guid);
        assertNotNull(updated);
        assertNotNull(updated.getFileData());
        
        System.out.println("TComponentsAttData 更新测试成功！");
    }

    @Test
    public void testDeleteByGuid() {
        // 先插入一条数据
        TComponentsAttData record = new TComponentsAttData();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setAttId("test-att-id");
        record.setFileData("测试删除数据".getBytes());
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttDataMapper.insert(record);

        // 删除
        int result = tComponentsAttDataMapper.deleteByGuid(guid);
        
        // 验证删除成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsAttData deleted = tComponentsAttDataMapper.selectByGuid(guid);
        assertNull(deleted);
        
        System.out.println("TComponentsAttData 删除测试成功！GUID: " + guid);
    }
}
