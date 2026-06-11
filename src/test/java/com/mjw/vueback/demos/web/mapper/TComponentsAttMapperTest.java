package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponentsAtt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TComponentsAttMapper 测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class TComponentsAttMapperTest {

    @Autowired
    private TComponentsAttMapper tComponentsAttMapper;

    @Test
    public void testInsert() {
        // 创建测试数据
        TComponentsAtt record = new TComponentsAtt();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setFileName("test.txt");
        record.setFilePath("/upload/test.txt");
        record.setFileSize("1024");
        record.setFileType("txt");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());

        // 执行插入
        int result = tComponentsAttMapper.insert(record);
        
        // 验证插入成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsAtt saved = tComponentsAttMapper.selectByGuid(guid);
        assertNotNull(saved);
        assertEquals("test.txt", saved.getFileName());
        
        System.out.println("TComponentsAtt 插入测试成功！GUID: " + guid);
    }

    @Test
    public void testSelectByGuid() {
        // 先插入一条数据
        TComponentsAtt record = new TComponentsAtt();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setFileName("测试查询附件");
        record.setFilePath("/upload/test.pdf");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttMapper.insert(record);

        // 查询
        TComponentsAtt result = tComponentsAttMapper.selectByGuid(guid);
        
        // 验证
        assertNotNull(result);
        assertEquals("测试查询附件", result.getFileName());
        
        System.out.println("TComponentsAtt 查询测试成功！FileName: " + result.getFileName());
    }

    @Test
    public void testSelectByComponentsId() {
        // 先插入一条数据
        TComponentsAtt record = new TComponentsAtt();
        String guid = UUID.randomUUID().toString();
        String componentsId = "test-components-id-" + System.currentTimeMillis();
        record.setGuid(guid);
        record.setComponentsId(componentsId);
        record.setFileName("测试按申请ID查询附件");
        record.setFilePath("/upload/test.doc");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttMapper.insert(record);

        // 按申请ID查询
        List<TComponentsAtt> results = tComponentsAttMapper.selectByComponentsId(componentsId);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("TComponentsAtt 按申请ID查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testUpdateByGuid() {
        // 先插入一条数据
        TComponentsAtt record = new TComponentsAtt();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setFileName("测试更新附件");
        record.setFilePath("/upload/old.txt");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttMapper.insert(record);

        // 更新
        record.setFileName("测试更新附件-已更新");
        record.setFilePath("/upload/new.txt");
        record.setUpdateUserId("admin");
        record.setUpdateDate(new Date());
        int result = tComponentsAttMapper.updateByGuid(record);
        
        // 验证更新成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsAtt updated = tComponentsAttMapper.selectByGuid(guid);
        assertEquals("测试更新附件-已更新", updated.getFileName());
        
        System.out.println("TComponentsAtt 更新测试成功！更新后的文件名: " + updated.getFileName());
    }

    @Test
    public void testDeleteByGuid() {
        // 先插入一条数据
        TComponentsAtt record = new TComponentsAtt();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setComponentsId("test-components-id");
        record.setFileName("测试删除附件");
        record.setFilePath("/upload/delete.txt");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsAttMapper.insert(record);

        // 删除
        int result = tComponentsAttMapper.deleteByGuid(guid);
        
        // 验证删除成功
        assertEquals(1, result);
        
        // 查询验证
        TComponentsAtt deleted = tComponentsAttMapper.selectByGuid(guid);
        assertNull(deleted);
        
        System.out.println("TComponentsAtt 删除测试成功！GUID: " + guid);
    }
}
