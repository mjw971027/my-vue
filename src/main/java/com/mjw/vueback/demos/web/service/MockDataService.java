package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模拟数据服务
 * 当数据库表不存在时，提供临时固定数据
 */
@Service
public class MockDataService {

    private final List<TCompany> companies = new ArrayList<>();
    private final List<TDept> departments = new ArrayList<>();
    private final List<TProject> projects = new ArrayList<>();
    private final List<TUnit> units = new ArrayList<>();
    private final List<Map<String, String>> materialSources = new ArrayList<>();
    private final List<Map<String, String>> tComponentsMockList = new ArrayList<>();
    private final Map<String, List<Map<String, String>>> tComponentsLineMock = new HashMap<>();
    private final Map<String, List<Map<String, String>>> tComponentsAttMock = new HashMap<>();
    private final Map<String, List<Map<String, String>>> tComponentsOpinionMock = new HashMap<>();

    // 状态码 -> 状态描述映射
    private static final Map<String, String> STATUS_DESC = new HashMap<>();
    // 工装类别码 -> 类别描述映射
    private static final Map<String, String> DIV_DESC = new HashMap<>();

    static {
        STATUS_DESC.put("00", "退回");
        STATUS_DESC.put("01", "编制");
        STATUS_DESC.put("02", "审批中");
        STATUS_DESC.put("03", "完成");
        STATUS_DESC.put("04", "设计出图");
        STATUS_DESC.put("05", "设计出图审批");

        DIV_DESC.put("01", "生产通用");
        DIV_DESC.put("02", "生产专用");
        DIV_DESC.put("03", "安措");
        DIV_DESC.put("04", "科研通用");
        DIV_DESC.put("05", "科研专用");
    }

    @PostConstruct
    public void init() {
        initCompanies();
        initDepartments();
        initProjects();
        initUnits();
        initMaterialSources();
        initTComponents();
    }

    private void initCompanies() {
        companies.add(createCompany("C001", "临港生产公司", "Lingang Manufacturing Co., Ltd."));
        companies.add(createCompany("C002", "闵行研发中心", "Minhang R&D Center"));
        companies.add(createCompany("C003", "浦东分公司", "Pudong Branch Company"));
    }

    private TCompany createCompany(String code, String descChn, String descEng) {
        TCompany c = new TCompany();
        c.setCode(code);
        c.setDescChn(descChn);
        c.setDescEng(descEng);
        c.setStatus("01");
        c.setCreateDate(new Date());
        return c;
    }

    private void initDepartments() {
        // C001的部门
        departments.add(createDept("D001", "机加工车间", "C001", "02"));
        departments.add(createDept("D002", "焊接车间", "C001", "02"));
        departments.add(createDept("D003", "装配车间", "C001", "02"));
        departments.add(createDept("D004", "技术部", "C001", "02"));

        // C002的部门
        departments.add(createDept("D005", "设计部", "C002", "02"));
        departments.add(createDept("D006", "研发实验室", "C002", "02"));
        departments.add(createDept("D007", "测试中心", "C002", "02"));

        // C003的部门
        departments.add(createDept("D008", "项目管理部", "C003", "02"));
        departments.add(createDept("D009", "质量管理部", "C003", "02"));
    }

    private TDept createDept(String orgnCd, String orgnDesc, String superOrgnCd, String orgnType) {
        TDept d = new TDept();
        d.setOrgnCd(orgnCd);
        d.setOrgnDesc(orgnDesc);
        d.setSuperOrgnCd(superOrgnCd);
        d.setOrgnType(orgnType);
        d.setStatus("01");
        return d;
    }

    private void initProjects() {
        projects.add(createProject("PJ2024001", "大型风电叶片模具研制", "C001", "Wind Blade Mold Dev"));
        projects.add(createProject("PJ2024002", "自动化焊接夹具改造", "C001", "Auto Welding Fixture Upgrade"));
        projects.add(createProject("PJ2024003", "高精度测量工装设计", "C002", "Precision Measurement Fixture"));
        projects.add(createProject("PJ2024004", "新型复合材料成型模具", "C001", "Composite Material Mold"));
        projects.add(createProject("PJ2024005", "船舶舾装件生产线改造", "C003", "Ship Outfitting Line Upgrade"));
    }

    private TProject createProject(String code, String descChn, String companyNo, String descEng) {
        TProject p = new TProject();
        p.setCode(code);
        p.setDescChn(descChn);
        p.setDescEng(descEng);
        p.setCompanyNo(companyNo);
        p.setStatus("01");
        return p;
    }

    private void initUnits() {
        units.add(createUnit("U001", "个", "数量单位"));
        units.add(createUnit("U002", "套", "数量单位"));
        units.add(createUnit("U003", "件", "数量单位"));
        units.add(createUnit("U004", "台", "数量单位"));
        units.add(createUnit("U005", "米", "长度单位"));
        units.add(createUnit("U006", "千克", "重量单位"));
        units.add(createUnit("U007", "吨", "重量单位"));
    }

    private TUnit createUnit(String guid, String untDesc, String untType) {
        TUnit u = new TUnit();
        u.setGUID(guid);
        u.setUNT_DESC(untDesc);
        u.setUNT_TYPE(untType);
        u.setActivation("Y");
        return u;
    }

    private void initMaterialSources() {
        materialSources.add(createMatSource("01", "采购"));
        materialSources.add(createMatSource("02", "库存"));
        materialSources.add(createMatSource("03", "自制"));
    }

    private Map<String, String> createMatSource(String id, String text) {
        Map<String, String> item = new HashMap<>();
        item.put("id", id);
        item.put("text", text);
        return item;
    }

    private void initTComponents() {
        // 创建5条模拟工装申请单
        String[] statuses = {"01", "02", "03", "00", "02"};
        String[] companies = {"C001", "C001", "C002", "C001", "C003"};
        String[] depts = {"D001", "D002", "D005", "D001", "D008"};
        String[] deptNames = {"机加工车间", "焊接车间", "设计部", "机加工车间", "项目管理部"};
        String[] projs = {"PJ2024001", "PJ2024002", "PJ2024003", "PJ2024001", "PJ2024005"};
        String[] divCds = {"01", "02", "01", "03", "02"};
        String[] names = {"风电叶片夹具制作", "焊接工装改造", "高精度测量底座", "安措防护工装", "舾装件模具"};
        String[] appUsers = {"张三", "李四", "王五", "赵六", "孙七"};
        String[] empNos = {"EMP001", "EMP002", "EMP003", "EMP004", "EMP005"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            Map<String, String> item = new LinkedHashMap<>();
            String guid = UUID.randomUUID().toString();
            String dateStr = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, -1);

            String billNo = companies[i] + new SimpleDateFormat("yyyyMMdd").format(new Date()) + String.format("%04d", i + 1);
            String maStatus = statuses[i];
            String divCd = divCds[i];

            item.put("guid", guid);
            item.put("billNo", billNo);
            item.put("companyNo", companies[i]);
            item.put("deptNo", depts[i]);
            item.put("deptName", deptNames[i]);
            item.put("componentsName", names[i]);
            item.put("projNo", projs[i]);
            item.put("divCd", divCd);
            item.put("divDesc", DIV_DESC.getOrDefault(divCd, "未知"));
            item.put("numberNo", String.valueOf(10 + i * 5));
            item.put("finalNumberNo", String.valueOf(8 + i * 4));
            item.put("maStatus", maStatus);
            item.put("maStatusDesc", STATUS_DESC.getOrDefault(maStatus, "未知"));
            item.put("appUser", appUsers[i]);
            item.put("empNo", empNos[i]);
            item.put("createUserId", empNos[i]);
            item.put("createDate", dateStr);
            item.put("tel", "1380013800" + (i + 1));
            item.put("remark", "模拟数据 - " + names[i]);

            tComponentsMockList.add(item);

            // 为每个工装添加2条申请材料
            List<Map<String, String>> lines = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Map<String, String> line = new LinkedHashMap<>();
                String lineGuid = UUID.randomUUID().toString();
                line.put("guid", lineGuid);
                line.put("componentsId", guid);
                line.put("materialNo", "MAT" + (i + 1) + String.format("%02d", j + 1));
                line.put("materialName", j == 0 ? "Q235钢板 10mm" : "45#圆钢 50mm");
                line.put("unit", i == 0 ? "U003" : "U001");
                line.put("unitDesc", i == 0 ? "件" : "个");
                line.put("demandQty", String.valueOf(20 + j * 10));
                line.put("finalDemandQty", String.valueOf(18 + j * 8));
                line.put("materialCost", String.valueOf(500 + j * 200));
                line.put("materialSources", "01");
                line.put("materialSourcesDesc", "采购");
                line.put("activation", "Y");
                line.put("quality", j == 0 ? "Q235" : "45#");
                line.put("thk1", "10");
                line.put("w1", "200");
                line.put("l", "500");
                line.put("remark", "模拟材料");
                line.put("createUserId", empNos[i]);
                line.put("createDate", dateStr);
                lines.add(line);
            }
            tComponentsLineMock.put(guid, lines);

            // 为已提交完成的工装添加审批记录
            if ("03".equals(maStatus) || "02".equals(maStatus)) {
                List<Map<String, String>> opinions = new ArrayList<>();
                Map<String, String> op1 = new LinkedHashMap<>();
                op1.put("guid", UUID.randomUUID().toString());
                op1.put("componentsId", guid);
                op1.put("createUserId", "审批人A");
                op1.put("opinion", "同意，请按计划执行");
                op1.put("stepName", "部门主管审批");
                op1.put("menuName", "同意");
                op1.put("createDate", dateStr);
                opinions.add(op1);

                if ("03".equals(maStatus)) {
                    Map<String, String> op2 = new LinkedHashMap<>();
                    op2.put("guid", UUID.randomUUID().toString());
                    op2.put("componentsId", guid);
                    op2.put("createUserId", "审批人B");
                    op2.put("opinion", "批准");
                    op2.put("stepName", "总经理审批");
                    op2.put("menuName", "同意");
                    op2.put("createDate", dateStr);
                    opinions.add(op2);
                }
                tComponentsOpinionMock.put(guid, opinions);
            }

            // 为第一个工装添加附件
            if (i == 0) {
                List<Map<String, String>> files = new ArrayList<>();
                Map<String, String> file1 = new LinkedHashMap<>();
                file1.put("fileId", UUID.randomUUID().toString());
                file1.put("componentsId", guid);
                file1.put("fileName", "设计图纸_v1.pdf");
                file1.put("fileType", "01");
                file1.put("fileSize", "204800");
                file1.put("createUserId", empNos[i]);
                file1.put("createDate", dateStr);
                files.add(file1);

                Map<String, String> file2 = new LinkedHashMap<>();
                file2.put("fileId", UUID.randomUUID().toString());
                file2.put("componentsId", guid);
                file2.put("fileName", "材料清单.xlsx");
                file2.put("fileType", "01");
                file2.put("fileSize", "51200");
                file2.put("createUserId", empNos[i]);
                file2.put("createDate", dateStr);
                files.add(file2);

                tComponentsAttMock.put(guid, files);
            }
        }
    }

    // ==================== 对外暴露的查询方法 ====================

    public List<TCompany> getCompanies() {
        return companies;
    }

    public List<TDept> getDepartmentsBySuperOrgnCd(String superOrgnCd) {
        return departments.stream()
                .filter(d -> d.getSuperOrgnCd().equals(superOrgnCd))
                .collect(Collectors.toList());
    }

    public List<TProject> getProjects() {
        return projects;
    }

    public List<TUnit> getUnits() {
        return units;
    }

    public List<Map<String, String>> getMaterialSources() {
        return materialSources;
    }

    /**
     * 获取材质列表（模拟 T_BASIC_STEEL_QUALITY 表数据）
     */
    public List<Map<String, String>> getQualityList() {
        List<Map<String, String>> qualities = new ArrayList<>();
        String[][] data = {
            {"Q235", "Q235碳素结构钢"},
            {"Q345", "Q345低合金高强度钢"},
            {"45#", "45#优质碳素钢"},
            {"40Cr", "40Cr合金结构钢"},
            {"304", "304不锈钢"},
            {"316L", "316L不锈钢"},
            {"2A12", "2A12硬铝合金"},
            {"H62", "H62黄铜"}
        };
        for (String[] item : data) {
            Map<String, String> map = new HashMap<>();
            map.put("code", item[0]);
            map.put("name", item[1]);
            qualities.add(map);
        }
        return qualities;
    }

    public List<Map<String, String>> getTComponentsData(String companyNo, String dateFrom, String dateTo,
                                                        String deptNo, String projNo, String maStatus,
                                                        String componentsName) {
        return tComponentsMockList.stream()
                .filter(item -> companyNo == null || companyNo.isEmpty() || companyNo.equals(item.get("companyNo")))
                .filter(item -> deptNo == null || deptNo.isEmpty() || deptNo.equals(item.get("deptNo")))
                .filter(item -> projNo == null || projNo.isEmpty() || projNo.equals(item.get("projNo")))
                .filter(item -> maStatus == null || maStatus.isEmpty() || maStatus.equals(item.get("maStatus")))
                .filter(item -> componentsName == null || componentsName.isEmpty()
                        || item.get("componentsName").contains(componentsName)
                        || item.get("billNo").contains(componentsName))
                .collect(Collectors.toList());
    }

    public Map<String, String> getBillInfo(String billId) {
        return tComponentsMockList.stream()
                .filter(item -> billId.equals(item.get("guid")))
                .findFirst()
                .orElse(null);
    }

    public List<Map<String, String>> getComponentsApp(String billNo) {
        // 根据billNo找到guid
        Map<String, String> main = tComponentsMockList.stream()
                .filter(item -> billNo.equals(item.get("billNo")))
                .findFirst()
                .orElse(null);
        if (main == null) return new ArrayList<>();
        String guid = main.get("guid");
        return tComponentsLineMock.getOrDefault(guid, new ArrayList<>());
    }

    public List<Map<String, String>> getComponentsAppFile(String billNo) {
        Map<String, String> main = tComponentsMockList.stream()
                .filter(item -> billNo.equals(item.get("billNo")))
                .findFirst()
                .orElse(null);
        if (main == null) return new ArrayList<>();
        String guid = main.get("guid");
        return tComponentsAttMock.getOrDefault(guid, new ArrayList<>());
    }

    public List<Map<String, String>> getComponentsAppAudit(String billNo) {
        Map<String, String> main = tComponentsMockList.stream()
                .filter(item -> billNo.equals(item.get("billNo")))
                .findFirst()
                .orElse(null);
        if (main == null) return new ArrayList<>();
        String guid = main.get("guid");
        return tComponentsOpinionMock.getOrDefault(guid, new ArrayList<>());
    }

    public static String getStatusDesc(String status) {
        return STATUS_DESC.getOrDefault(status, "未知");
    }

    public static String getDivDesc(String divCd) {
        return DIV_DESC.getOrDefault(divCd, "未知");
    }
}
