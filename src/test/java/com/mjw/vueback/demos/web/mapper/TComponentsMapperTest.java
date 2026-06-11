package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.TComponents;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TComponentsMapper 测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class TComponentsMapperTest {

    @Autowired
    private TComponentsMapper tComponentsMapper;

    @Test
    public void testInsert() {
        // 创建测试数据
        TComponents record = new TComponents();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setBillNo("TEST001");
        record.setCompanyNo("COMP001");
        record.setDeptNo("DEPT001");
        record.setComponentsName("测试工装项目");
        record.setProjNo("PROJ001");
        record.setDivCd("01");
        record.setNumberNo(new BigDecimal(10));
        record.setFinalNumberNo(new BigDecimal(10));
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        record.setMaStatus("01");

        // 执行插入
        int result = tComponentsMapper.insert(record);
        
        // 验证插入成功
        assertEquals(1, result);
        
        // 查询验证
        TComponents saved = tComponentsMapper.selectByGuid(guid);
        assertNotNull(saved);
        assertEquals("TEST001", saved.getBillNo());
        assertEquals("测试工装项目", saved.getComponentsName());
        
        System.out.println("插入测试成功！GUID: " + guid);
    }

    @Test
    public void testSelectByGuid() {
        // 先插入一条数据
        TComponents record = new TComponents();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setBillNo("TEST002");
        record.setComponentsName("测试查询");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        record.setMaStatus("01");
        tComponentsMapper.insert(record);

        // 查询
        TComponents result = tComponentsMapper.selectByGuid(guid);
        
        // 验证
        assertNotNull(result);
        assertEquals("TEST002", result.getBillNo());
        
        System.out.println("查询测试成功！BillNo: " + result.getBillNo());
    }

    @Test
    public void testSelectByBillNo() {
        // 先插入一条数据
        TComponents record = new TComponents();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setBillNo("TEST003");
        record.setComponentsName("测试按单号查询");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        record.setMaStatus("01");
        tComponentsMapper.insert(record);

        // 按单号查询
        TComponents result = tComponentsMapper.selectByBillNo("TEST003");
        
        // 验证
        assertNotNull(result);
        assertEquals(guid, result.getGuid());
        
        System.out.println("按单号查询测试成功！GUID: " + result.getGuid());
    }

    @Test
    public void testSelectList() {
        // 插入测试数据
        TComponents record = new TComponents();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setBillNo("TEST004");
        record.setComponentsName("测试列表查询");
        record.setMaStatus("01");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        tComponentsMapper.insert(record);

        // 按条件查询
        Map<String, Object> params = new HashMap<>();
        params.put("maStatus", "01");
        List<TComponents> results = tComponentsMapper.selectList(params);
        
        // 验证
        assertNotNull(results);
        assertTrue(results.size() > 0);
        
        System.out.println("列表查询测试成功！查询结果数量: " + results.size());
    }

    @Test
    public void testUpdateByGuid() {
        // 先插入一条数据
        TComponents record = new TComponents();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setBillNo("TEST005");
        record.setComponentsName("测试更新");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        record.setMaStatus("01");
        tComponentsMapper.insert(record);

        // 更新
        record.setComponentsName("测试更新-已更新");
        record.setUpdateUserId("admin");
        record.setUpdateDate(new Date());
        int result = tComponentsMapper.updateByGuid(record);
        
        // 验证更新成功
        assertEquals(1, result);
        
        // 查询验证
        TComponents updated = tComponentsMapper.selectByGuid(guid);
        assertEquals("测试更新-已更新", updated.getComponentsName());
        
        System.out.println("更新测试成功！更新后的名称: " + updated.getComponentsName());
    }

    @Test
    public void testDeleteByGuid() {
        // 先插入一条数据
        TComponents record = new TComponents();
        String guid = UUID.randomUUID().toString();
        record.setGuid(guid);
        record.setBillNo("TEST006");
        record.setComponentsName("测试删除");
        record.setCreateUserId("admin");
        record.setCreateDate(new Date());
        record.setMaStatus("01");
        tComponentsMapper.insert(record);

        // 删除
        int result = tComponentsMapper.deleteByGuid(guid);
        
        // 验证删除成功
        assertEquals(1, result);
        
        // 查询验证
        TComponents deleted = tComponentsMapper.selectByGuid(guid);
        assertNull(deleted);
        
        System.out.println("删除测试成功！GUID: " + guid);
    }
}
