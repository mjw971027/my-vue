package com.mjw.vueback.demos.web.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.entity.TComponents;
import com.mjw.vueback.demos.web.entity.TComponentsAtt;
import com.mjw.vueback.demos.web.entity.TComponentsLine;
import com.mjw.vueback.demos.web.entity.TComponentsOpinion;
import com.mjw.vueback.demos.web.entity.TComponentsQuote;
import com.mjw.vueback.demos.web.entity.TUnit;
import com.mjw.vueback.demos.web.mapper.TComponentsAttMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsLineMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsOpinionMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsQuoteMapper;
import com.mjw.vueback.demos.web.mapper.TUnitMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComponentsService {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsService.class);

    @Autowired
    private TComponentsMapper tComponentsMapper;

    @Autowired
    private TComponentsLineMapper tComponentsLineMapper;

    @Autowired
    private TComponentsAttMapper tComponentsAttMapper;

    @Autowired
    private TComponentsOpinionMapper tComponentsOpinionMapper;

    @Autowired
    private TComponentsQuoteMapper tComponentsQuoteMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    
    @Value("${file.upload.path:E:/upload/files/}")
    private String uploadPath;

    /**
     * 分页查询工装申请列表
     */
    public Map<String, Object> getTComponentsData(String companyNo, String dateFrom, String dateTo,
                                                  String deptNo, String projNo, String maStatus,
                                                  String componentsName, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyNo", companyNo);
        params.put("deptNo", deptNo);
        params.put("projNo", projNo);
        params.put("maStatus", maStatus);
        params.put("componentsName", componentsName);
        
        // 日期格式转换：前端传YYYYMMDD，转为Date类型
        try {
            if (dateFrom != null && !dateFrom.isEmpty()) {
                params.put("createDateStart", DATE_FORMAT.parse(dateFrom));
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                params.put("createDateEnd", DATE_FORMAT.parse(dateTo));
            }
        } catch (ParseException e) {
            throw new RuntimeException("日期格式错误，应为yyyyMMdd", e);
        }
        
        try {
            // 优先查询数据库
            List<TComponents> list = tComponentsMapper.selectList(params);
            
            // 将实体转换为Map并补充前端需要的字段
            List<Map<String, Object>> mapList = list.stream().map(this::tComponentsToMap).collect(Collectors.toList());
            
            // 查询总数
            int total = mapList.size();
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", mapList);
            result.put("total", total);
            return result;
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            throw e; // 交给上层Controller处理
        }
    }

    /**
     * 将TComponents实体转换为Map，补充前端需要的派生字段
     */
    private Map<String, Object> tComponentsToMap(TComponents tc) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("guid", tc.getGuid());
        map.put("billNo", tc.getBillNo());
        map.put("companyNo", tc.getCompanyNo());
        map.put("deptNo", tc.getDeptNo());
        map.put("deptName", tc.getDeptName() != null ? tc.getDeptName() : "");
        map.put("componentsName", tc.getComponentsName());
        map.put("projNo", tc.getProjNo());
        map.put("divCd", tc.getDivCd());
        // 补充前端需要的派生字段
        map.put("divDesc", MockDataService.getDivDesc(tc.getDivCd()));
        map.put("numberNo", tc.getNumberNo() != null ? tc.getNumberNo().toString() : "0");
        map.put("finalNumberNo", tc.getFinalNumberNo() != null ? tc.getFinalNumberNo().toString() : "0");
        map.put("maStatus", tc.getMaStatus());
        map.put("maStatusDesc", MockDataService.getStatusDesc(tc.getMaStatus()));
        String rawCreateUser = tc.getCreateUserId();
        String createUserName = resolveUserName(rawCreateUser);
        map.put("appUser", createUserName);
        map.put("empNo", createUserName);
        map.put("createUserId", rawCreateUser);
        map.put("createUserName", createUserName);
        map.put("createDate", tc.getCreateDate() != null ? tc.getCreateDate().toString() : "");
        map.put("tel", tc.getTel() != null ? tc.getTel() : "");
        map.put("remark", tc.getRemark() != null ? tc.getRemark() : "");
        map.put("maProcessId", tc.getMaProcessId());
        map.put("materialTotalCost", tc.getMaterialTotalCost() != null ? tc.getMaterialTotalCost() : "");
        map.put("mhBdgt", tc.getMhBdgt() != null ? tc.getMhBdgt() : "");
        map.put("dwgno", tc.getDwgno() != null ? tc.getDwgno() : "");
        map.put("needDate", tc.getNeedDate() != null ? tc.getNeedDate().toString() : "");
        return map;
    }

    /**
     * 根据 createUserId 解析出用户名
     * createUserId 可能是数字ID（种子数据）或是用户名（程序写入），统一解析为用户名
     */
    private String resolveUserName(String createUserId) {
        if (createUserId == null || createUserId.isEmpty()) {
            return "";
        }
        // 尝试按数字ID解析
        try {
            Long userId = Long.parseLong(createUserId.trim());
            SysUser user = userService.findById(userId);
            if (user != null) {
                return user.getUsername();
            }
        } catch (NumberFormatException ignored) {
            // 不是数字ID，可能是用户名
        }
        // 尝试按用户名查找（如果本身已经是用户名或者ID解析失败回退查找）
        SysUser user = userService.findByUsername(createUserId.trim());
        if (user != null) {
            return user.getUsername();
        }
        // 都没找到，返回原始值
        return createUserId;
    }

    /**
     * 创建工装申请基础信息
     * billNo规则：公司编码 + 年月日 + 四位流水号
     */
    public TComponents createBase(String programName, String companyNo) {
        SysUser sysUser = userService.getCurrentUser();
        TComponents record = new TComponents();
        record.setGuid(UUID.randomUUID().toString());
        record.setComponentsName(programName);
        record.setMaStatus("01"); // 编制状态
        record.setCreateDate(new Date());
        record.setCompanyNo(companyNo);
        record.setDeptNo("DEPT001");
        record.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
        
        // 生成billNo：公司编码 + 年月日 + 四位流水号
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String billNoPrefix = companyNo + dateStr;
        
        // 查询当天最大的billNo
        String maxBillNo = tComponentsMapper.selectMaxBillNoByPrefix(billNoPrefix);
        
        int sequence = 1; // 默认从1开始
        if (maxBillNo != null && maxBillNo.length() >= billNoPrefix.length() + 4) {
            // 提取最后四位流水号
            String seqStr = maxBillNo.substring(billNoPrefix.length());
            try {
                sequence = Integer.parseInt(seqStr) + 1;
            } catch (NumberFormatException e) {
                sequence = 1;
            }
        }
        
        // 格式化为四位流水号
        String billNo = billNoPrefix + String.format("%04d", sequence);
        record.setBillNo(billNo);
        
        int result = tComponentsMapper.insertSelective(record);
        if (result > 0) {
            return record;
        }
        return null;
    }

    /**
     * 删除工装申请
     */
    public boolean deleteApp(String guid) {
        // 先删除相关的明细、附件、审批意见
        tComponentsLineMapper.deleteByComponentsId(guid);
        tComponentsAttMapper.deleteByComponentsId(guid);
        tComponentsOpinionMapper.deleteByComponentsId(guid);
        
        // 再删除主表记录
        int result = tComponentsMapper.deleteByGuid(guid);
        return result > 0;
    }

    /**
     * 撤回工装申请
     */
    public boolean retractApp(String guid) {
        TComponents record = tComponentsMapper.selectByGuid(guid);
        if (record == null) {
            return false;
        }
        // 将状态改为"编制"（01）
        record.setMaStatus("01");
        SysUser sysUser = userService.getCurrentUser();
        record.setUpdateUserId(sysUser != null ? sysUser.getUsername() : "admin");
        record.setUpdateDate(new Date());
        
        int result = tComponentsMapper.updateByGuidSelective(record);
        return result > 0;
    }

    /**
     * 获取工装申请单基本信息
     */
    public TComponents getBillInfo(String billId) {
        return tComponentsMapper.selectByGuid(billId);
    }

    /**
     * 获取申请材料列表
     */
    public List<TComponentsLine> getComponentsApp(String billNo) {
        // 先根据billNo查询GUID
        TComponents components = tComponentsMapper.selectByBillNo(billNo);
        if (components != null) {
            return tComponentsLineMapper.selectByComponentsId(components.getGuid());
        }
        return new ArrayList<>();
    }

    /**
     * 获取附件列表
     */
    public List<TComponentsAtt> getComponentsAppFile(String billNo) {
        TComponents components = tComponentsMapper.selectByBillNo(billNo);
        if (components != null) {
            return tComponentsAttMapper.selectByComponentsId(components.getGuid());
        }
        return new ArrayList<>();
    }

    /**
     * 获取审批记录
     */
    public List<TComponentsOpinion> getComponentsAppAudit(String billNo) {
        TComponents components = tComponentsMapper.selectByBillNo(billNo);
        if (components != null) {
            return tComponentsOpinionMapper.selectByComponentsId(components.getGuid());
        }
        return new ArrayList<>();
    }

    /**
     * 获取工装申请单基本信息（返回Map，包含前端需要的所有字段）
     */
    public Map<String, Object> getBillInfoAsMap(String billId) {
        TComponents tc = tComponentsMapper.selectByGuid(billId);
        if (tc == null) {
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("billNo", tc.getBillNo());
        map.put("companyEnDesc", tc.getCompanyNo()); // 前端期望companyEnDesc
        map.put("deptDesc", tc.getDeptName() != null ? tc.getDeptName() : tc.getDeptNo());
        String rawCreateUser = tc.getCreateUserId();
        map.put("appUser", resolveUserName(rawCreateUser));
        map.put("createUserId", rawCreateUser);
        map.put("createUserName", resolveUserName(rawCreateUser));
        map.put("appDate", tc.getAppDate() != null ? tc.getAppDate().toString() : "");
        map.put("projNo", tc.getProjNo());
        map.put("divCd", tc.getDivCd());
        map.put("divDesc", MockDataService.getDivDesc(tc.getDivCd()));
        map.put("numberNo", tc.getNumberNo() != null ? tc.getNumberNo().toString() : "0");
        map.put("tel", tc.getTel());
        map.put("needDate", tc.getNeedDate() != null ? tc.getNeedDate().toString() : "");
        map.put("dwgno", tc.getDwgno());
        map.put("materialTotalCost", tc.getMaterialTotalCost());
        map.put("finalNumberNo", tc.getFinalNumberNo() != null ? tc.getFinalNumberNo().toString() : "0");
        map.put("mhBdgt", tc.getMhBdgt());
        map.put("componentsName", tc.getComponentsName());
        map.put("remark", tc.getRemark());
        map.put("guid", tc.getGuid());
        return map;
    }

    /**
     * 获取申请材料列表（返回List<Map>）
     */
    public List<Map<String, Object>> getComponentsAppAsMap(String billNo) {
        TComponents components = tComponentsMapper.selectByBillNo(billNo);
        if (components == null) {
            return new ArrayList<>();
        }
        List<TComponentsLine> lines = tComponentsLineMapper.selectByComponentsId(components.getGuid());
        return lines.stream().map(line -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("guid", line.getGuid());
            map.put("componentsId", line.getComponentsId());
            map.put("activation", line.getActivation());
            map.put("materialNo", line.getMaterialNo());
            map.put("materialName", line.getMaterialName());
            map.put("unit", line.getUnit());
            map.put("demandQty", line.getDemandQty());
            map.put("finalDemandQty", line.getFinalDemandQty());
            map.put("materialCost", line.getMaterialCost());
            map.put("materialSources", line.getMaterialSources());
            map.put("quality", line.getQuality());
            map.put("thk1", line.getThk1());
            map.put("thk2", line.getThk2());
            map.put("w1", line.getW1());
            map.put("w2", line.getW2());
            map.put("l", line.getL());
            map.put("remark", line.getRemark());
            map.put("createUserId", line.getCreateUserId());
            map.put("createDate", line.getCreateDate() != null ? line.getCreateDate().toString() : "");
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 获取附件列表（返回List<Map>）
     */
    public List<Map<String, Object>> getComponentsAppFileAsMap(String billNo) {
        TComponents components = tComponentsMapper.selectByBillNo(billNo);
        if (components == null) {
            return new ArrayList<>();
        }
        List<TComponentsAtt> files = tComponentsAttMapper.selectByComponentsId(components.getGuid());
        return files.stream().map(file -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("fileId", file.getGuid());
            map.put("componentsId", file.getComponentsId());
            map.put("fileName", file.getFileName());
            map.put("fileType", file.getFileType());
            map.put("fileSize", file.getFileSize());
            map.put("createUserId", file.getCreateUserId());
            map.put("createDate", file.getCreateDate() != null ? file.getCreateDate().toString() : "");
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 获取审批记录（返回List<Map>）
     */
    public List<Map<String, Object>> getComponentsAppAuditAsMap(String billNo) {
        TComponents components = tComponentsMapper.selectByBillNo(billNo);
        if (components == null) {
            return new ArrayList<>();
        }
        List<TComponentsOpinion> opinions = tComponentsOpinionMapper.selectByComponentsId(components.getGuid());
        return opinions.stream().map(op -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("guid", op.getGuid());
            map.put("componentsId", op.getComponentsId());
            map.put("createUserId", op.getAuditUserName() != null ? op.getAuditUserName() : op.getAuditUserId());
            map.put("createUserName", op.getAuditUserName() != null ? op.getAuditUserName() : op.getAuditUserId());
            map.put("opinion", op.getOpinionContent());
            map.put("stepName", op.getOpinionType());
            map.put("createDate", op.getAuditDate() != null ? op.getAuditDate().toString() : "");
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 保存基本信息
     */
    @Transactional
    public Map<String, Object> saveBase(Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 前端传来的数据嵌套在 data 字段中（可能是 Map 或 JSON 字符串）
            Object dataObj = request.get("data");
            if (dataObj == null) {
                result.put("flag", 0);
                result.put("message", "请求参数缺少 data 字段");
                return result;
            }
            Map<String, Object> dataMap;
            if (dataObj instanceof String) {
                dataMap = objectMapper.readValue((String) dataObj, new TypeReference<Map<String, Object>>() {});
            } else {
                dataMap = (Map<String, Object>) dataObj;
            }

            String guid = (String) dataMap.get("guid");
            String billNo = (String) dataMap.get("billNo");
            String projNo = (String) dataMap.get("projNo");
            String divCd = (String) dataMap.get("divCd");
            Object numberNo = dataMap.get("numberNo");
            Object finalNumberNo = dataMap.get("finalNumberNo");
            String tel = (String) dataMap.get("tel");
            String needDate = (String) dataMap.get("needDate");
            String dwgno = (String) dataMap.get("dwgno");
            Object materialTotalCost = dataMap.get("materialTotalCost");
            String mhBdgt = (String) dataMap.get("mhBdgt");
            String componentsName = (String) dataMap.get("componentsName");
            String remark = (String) dataMap.get("remark");
            
            TComponents record;
            if (guid != null && !guid.isEmpty()) {
                // 更新
                record = tComponentsMapper.selectByGuid(guid);
                if (record == null) {
                    result.put("flag", 0);
                    result.put("message", "记录不存在");
                    return result;
                }
            } else {
                // 新增
                record = new TComponents();
                record.setGuid(UUID.randomUUID().toString());
                record.setMaStatus("01");
                record.setCreateDate(new Date());
                SysUser sysUser = userService.getCurrentUser();
                record.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            }
            
            // 设置字段
            record.setProjNo(projNo);
            record.setDivCd(divCd);
            record.setNumberNo(new java.math.BigDecimal(numberNo != null ? numberNo.toString() : "0"));
            record.setFinalNumberNo(new java.math.BigDecimal(finalNumberNo != null ? finalNumberNo.toString() : "0"));
            record.setTel(tel);
            if (needDate != null && !needDate.isEmpty()) {
                try {
                    record.setNeedDate(DATE_FORMAT.parse(needDate));
                } catch (ParseException e) {
                    // 尝试其他日期格式
                    try {
                        record.setNeedDate(new SimpleDateFormat("yyyy-MM-dd").parse(needDate));
                    } catch (ParseException e2) {
                        // 忽略日期解析错误
                    }
                }
            }
            record.setDwgno(dwgno);
            record.setMaterialTotalCost(materialTotalCost != null ? materialTotalCost.toString() : "0");
            record.setMhBdgt(mhBdgt);
            record.setComponentsName(componentsName);
            record.setRemark(remark);
            SysUser sysUser = userService.getCurrentUser();
            record.setUpdateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            record.setUpdateDate(new Date());
            
            if (guid != null && !guid.isEmpty()) {
                tComponentsMapper.updateByGuidSelective(record);
            } else {
                // 生成billNo
                String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String billNoPrefix = record.getCompanyNo() + dateStr;
                String maxBillNo = tComponentsMapper.selectMaxBillNoByPrefix(billNoPrefix);
                int sequence = 1;
                if (maxBillNo != null && maxBillNo.length() >= billNoPrefix.length() + 4) {
                    String seqStr = maxBillNo.substring(billNoPrefix.length());
                    try {
                        sequence = Integer.parseInt(seqStr) + 1;
                    } catch (NumberFormatException e) {
                        sequence = 1;
                    }
                }
                billNo = billNoPrefix + String.format("%04d", sequence);
                record.setBillNo(billNo);
                
                tComponentsMapper.insertSelective(record);
            }
            
            result.put("flag", 1);
            result.put("guid", record.getGuid());
            result.put("billNo", record.getBillNo());
            return result;
            
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "保存失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 保存申请材料
     */
    @Transactional
    public Map<String, Object> saveAppInfo(List<TComponentsLine> lines) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            for (TComponentsLine line : lines) {
                if (line.getGuid() != null && !line.getGuid().isEmpty()) {
                    // 更新
                    tComponentsLineMapper.updateByGuidSelective(line);
                } else {
                    // 新增
                    line.setGuid(UUID.randomUUID().toString());
                    line.setMaterialNo(tComponentsLineMapper.getNewMactNo());
                    SysUser sysUser = userService.getCurrentUser();
                    line.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
                    line.setCreateDate(new Date());
                    tComponentsLineMapper.insertSelective(line);
                }
            }
            
            result.put("flag", 1);
            return result;
            
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "保存失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 提交（保存并启动审批）
     */
    @Transactional
    public Map<String, Object> saveBaseCommit(Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 先保存基本信息
            Map<String, Object> saveResult = saveBase(request);
            if (saveResult.get("flag").equals(0)) {
                return saveResult;
            }
            
            String guid = (String) saveResult.get("guid");
            
            // 保存申请材料
            String data2 = (String) request.get("data2");
            if (data2 != null && !data2.isEmpty()) {
                // 使用Jackson解析JSON数组
                List<TComponentsLine> lines = objectMapper.readValue(data2, new TypeReference<List<TComponentsLine>>() {});
                saveAppInfo(lines);
            }
            
            // 更新状态为"已提交"（02）
            TComponents record = tComponentsMapper.selectByGuid(guid);
            if (record != null) {
                record.setMaStatus("02");
                SysUser sysUser = userService.getCurrentUser();
                record.setUpdateUserId(sysUser != null ? sysUser.getUsername() : "admin");
                record.setUpdateDate(new Date());
                tComponentsMapper.updateByGuidSelective(record);
            }
            
            // TODO: 启动审批流程
            
            result.put("flag", 1);
            return result;
            
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "提交失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 删除申请材料
     */
    @Transactional
    public Map<String, Object> delAppInfo(Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String data = (String) request.get("data");
            if (data == null || data.isEmpty()) {
                result.put("flag", 0);
                result.put("message", "参数不能为空");
                return result;
            }
            
            // 使用Jackson解析JSON数组
            List<String> guids = objectMapper.readValue(data, new TypeReference<List<String>>() {});
            for (String guid : guids) {
                if (guid != null && !guid.trim().isEmpty()) {
                    tComponentsLineMapper.deleteByGuid(guid.trim());
                }
            }
            
            result.put("flag", 1);
            return result;
            
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "删除失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 删除附件
     */
    @Transactional
    public Map<String, Object> delComFile(Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String data = (String) request.get("data");
            if (data == null || data.isEmpty()) {
                result.put("flag", 0);
                result.put("message", "参数不能为空");
                return result;
            }
            
            // 使用Jackson解析JSON数组
            List<String> fileIds = objectMapper.readValue(data, new TypeReference<List<String>>() {});
            for (String fileId : fileIds) {
                if (fileId != null && !fileId.trim().isEmpty()) {
                    // 先查询文件路径，删除物理文件
                    TComponentsAtt att = tComponentsAttMapper.selectByGuid(fileId.trim());
                    if (att != null && att.getFilePath() != null) {
                        File file = new File(att.getFilePath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    
                    // 再删除数据库记录
                    tComponentsAttMapper.deleteByGuid(fileId.trim());
                }
            }
            
            result.put("flag", 1);
            return result;
            
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "删除失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 上传附件
     */
    @Transactional
    public Map<String, Object> uploadAttchment(MultipartFile file, String typeCd, String billId, String billNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExt;
            String filePath = uploadPath + newFilename;
            
            // 保存文件
            File dest = new File(filePath);
            file.transferTo(dest);
            
            // 保存数据库记录
            TComponentsAtt att = new TComponentsAtt();
            att.setGuid(UUID.randomUUID().toString());
            att.setComponentsId(billId);
            att.setFileName(originalFilename);
            att.setFilePath(filePath);
            att.setFileSize(String.valueOf(file.getSize()));
            att.setFileType(typeCd);
            SysUser sysUser = userService.getCurrentUser();
            att.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            att.setCreateDate(new Date());
            tComponentsAttMapper.insertSelective(att);
            
            result.put("flag", 1);
            result.put("fileId", att.getGuid());
            return result;
            
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "上传失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 生成并输出工装申请 PDF（iText 表格样式）
     */
    public void generatePdf(String billNo, HttpServletResponse response) {
        try {
            // 获取申请单信息
            TComponents tc = tComponentsMapper.selectByBillNo(billNo);
            if (tc == null) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("未找到申请单：" + billNo);
                return;
            }

            // 加载中文字体
            BaseFont bfChinese = null;
            String[] fontPaths = {
                    "C:/Windows/Fonts/msyh.ttf",
                    "C:/Windows/Fonts/simhei.ttf",
                    "C:/Windows/Fonts/simsun.ttf",
                    "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc",
                    "/System/Library/Fonts/PingFang.ttc"
            };
            for (String fp : fontPaths) {
                File f = new File(fp);
                if (f.exists()) {
                    try {
                        bfChinese = BaseFont.createFont(fp, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        break;
                    } catch (Exception ignored) {
                    }
                }
            }
            boolean hasChinese = bfChinese != null;
            Font titleFont = hasChinese ? new Font(bfChinese, 18, Font.BOLD) : new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font headerFont = hasChinese ? new Font(bfChinese, 11, Font.BOLD) : new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font normalFont = hasChinese ? new Font(bfChinese, 10) : new Font(Font.FontFamily.HELVETICA, 9);
            Font tableHeaderFont = hasChinese ? new Font(bfChinese, 9, Font.BOLD) : new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
            Font tableFont = hasChinese ? new Font(bfChinese, 9) : new Font(Font.FontFamily.HELVETICA, 8);

            // 先设置响应头，再写入内容
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"components_" + billNo + ".pdf\"");

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // ========== 标题 ==========
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph(hasChinese ? "工装申请单" : "Workwear Application Form", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // ========== 基本信息表格（2列） ==========
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);
            infoTable.setWidths(new float[]{25, 75});

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[][] fields = {
                    {hasChinese ? "申请单号" : "Bill No", tc.getBillNo() != null ? tc.getBillNo() : ""},
                    {hasChinese ? "项目名称" : "Project Name", tc.getComponentsName() != null ? tc.getComponentsName() : ""},
                    {hasChinese ? "工程号" : "Project No", tc.getProjNo() != null ? tc.getProjNo() : ""},
                    {hasChinese ? "申请日期" : "Apply Date", tc.getCreateDate() != null ? sdf.format(tc.getCreateDate()) : ""},
                    {hasChinese ? "需求日期" : "Need Date", tc.getNeedDate() != null ? sdf.format(tc.getNeedDate()) : ""},
                    {hasChinese ? "需求量" : "Quantity", tc.getNumberNo() != null ? tc.getNumberNo().toString() : "0"},
                    {hasChinese ? "状态" : "Status", MockDataService.getStatusDesc(tc.getMaStatus())},
                    {hasChinese ? "备注" : "Remark", tc.getRemark() != null ? tc.getRemark() : ""}
            };

            for (String[] field : fields) {
                PdfPCell labelCell = new PdfPCell(new Phrase(field[0], headerFont));
                labelCell.setBorderColor(BaseColor.LIGHT_GRAY);
                labelCell.setBackgroundColor(new BaseColor(240, 240, 240));
                labelCell.setPadding(5);
                labelCell.setFixedHeight(22);

                PdfPCell valueCell = new PdfPCell(new Phrase(field[1], normalFont));
                valueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                valueCell.setPadding(5);
                valueCell.setFixedHeight(22);

                infoTable.addCell(labelCell);
                infoTable.addCell(valueCell);
            }
            document.add(infoTable);

            // ========== 申请材料表格 ==========
            com.itextpdf.text.Paragraph matTitle = new com.itextpdf.text.Paragraph(hasChinese ? "申请材料" : "Materials", headerFont);
            matTitle.setSpacingAfter(8);
            document.add(matTitle);

            List<TComponentsLine> lines = tComponentsLineMapper.selectByComponentsId(tc.getGuid());
            Map<String, String> unitCache = new HashMap<>();

            PdfPTable matTable = new PdfPTable(5);
            matTable.setWidthPercentage(100);
            matTable.setWidths(new float[]{18, 30, 12, 12, 28});

            // 列名
            String[] matHeaders = hasChinese
                    ? new String[]{"物资编码", "物资名称", "需求数", "单位", "备注"}
                    : new String[]{"Code", "Name", "Qty", "Unit", "Remark"};
            for (String h : matHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(h, tableHeaderFont));
                cell.setBackgroundColor(new BaseColor(200, 220, 240));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(4);
                cell.setFixedHeight(20);
                matTable.addCell(cell);
            }

            // 数据行
            for (TComponentsLine line : lines) {
                String unitName = "";
                if (line.getUnit() != null) {
                    unitName = unitCache.get(line.getUnit());
                    if (unitName == null) {
                        TUnit tUnit = tUnitMapper.selectByGuid(line.getUnit());
                        unitName = tUnit != null ? tUnit.getUNT_DESC() : line.getUnit();
                        unitCache.put(line.getUnit(), unitName);
                    }
                }
                String[][] rowData = {
                        {line.getMaterialNo() != null ? line.getMaterialNo() : ""},
                        {line.getMaterialName() != null ? line.getMaterialName() : ""},
                        {line.getDemandQty() != null ? line.getDemandQty().toString() : "0"},
                        {unitName},
                        {line.getRemark() != null ? line.getRemark() : ""}
                };
                for (String[] data : rowData) {
                    PdfPCell cell = new PdfPCell(new Phrase(data[0], tableFont));
                    cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell.setPadding(4);
                    cell.setFixedHeight(18);
                    matTable.addCell(cell);
                }
            }
            document.add(matTable);

            // ========== 审批意见过程 ==========
            List<TComponentsOpinion> opinions = tComponentsOpinionMapper.selectByComponentsId(tc.getGuid());
            if (opinions != null && !opinions.isEmpty()) {
                com.itextpdf.text.Paragraph opTitle = new com.itextpdf.text.Paragraph(
                        hasChinese ? "审批意见过程" : "Approval Process", headerFont);
                opTitle.setSpacingBefore(10);
                opTitle.setSpacingAfter(8);
                document.add(opTitle);

                // 审批步骤名称映射
                Map<String, String> stepNameMap = new HashMap<>();
                stepNameMap.put("01", "部门主管审批");
                stepNameMap.put("02", "技术主管审批");
                stepNameMap.put("03", "总经理审批");

                PdfPTable opTable = new PdfPTable(4);
                opTable.setWidthPercentage(100);
                opTable.setWidths(new float[]{15, 18, 40, 27});
                opTable.setSpacingAfter(15);

                // 表头
                String[] opHeaders = hasChinese
                        ? new String[]{"审批步骤", "审批人", "审批意见", "审批日期"}
                        : new String[]{"Step", "Approver", "Opinion", "Date"};
                for (String h : opHeaders) {
                    PdfPCell cell = new PdfPCell(new Phrase(h, tableHeaderFont));
                    cell.setBackgroundColor(new BaseColor(200, 220, 240));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(4);
                    cell.setFixedHeight(20);
                    opTable.addCell(cell);
                }

                // 按审核日期排序
                opinions.sort(Comparator.comparing(
                        o -> o.getAuditDate() != null ? o.getAuditDate() : o.getCreateDate(),
                        Comparator.nullsLast(Comparator.naturalOrder())));

                for (TComponentsOpinion op : opinions) {
                    String stepName = stepNameMap.getOrDefault(op.getOpinionType(), "审批");
                    String approver = op.getAuditUserName() != null ? op.getAuditUserName() : "";
                    String opinion = op.getOpinionContent() != null ? op.getOpinionContent() : "";
                    String date = op.getAuditDate() != null ? sdf.format(op.getAuditDate())
                            : (op.getCreateDate() != null ? sdf.format(op.getCreateDate()) : "");

                    String[][] opRow = {{stepName}, {approver}, {opinion}, {date}};
                    for (String[] opData : opRow) {
                        PdfPCell cell = new PdfPCell(new Phrase(opData[0], tableFont));
                        cell.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell.setPadding(4);
                        cell.setFixedHeight(18);
                        opTable.addCell(cell);
                    }
                }
                document.add(opTable);
            }

            // ========== 底部日期 ==========
            com.itextpdf.text.Paragraph footer = new com.itextpdf.text.Paragraph(
                    hasChinese ? "生成日期: " + sdf.format(new Date()) : "Generated: " + sdf.format(new Date()),
                    normalFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            logger.error("操作失败", e);
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("PDF生成失败：" + e.getMessage());
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 下载文件
     */
    public void fileDownload(String fileId, HttpServletResponse response) {
        FileInputStream fis = null;
        OutputStream os = null;
        
        try {
            TComponentsAtt att = tComponentsAttMapper.selectByGuid(fileId);
            if (att == null) {
                response.getWriter().write("文件不存在");
                return;
            }
            
            File file = new File(att.getFilePath());
            if (!file.exists()) {
                response.getWriter().write("文件不存在");
                return;
            }
            
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + 
                new String(att.getFileName().getBytes("UTF-8"), "ISO-8859-1") + "\"");
            
            // 写入文件
            fis = new FileInputStream(file);
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            
        } catch (Exception e) {
            logger.error("操作失败", e);
        } finally {
            try {
                if (fis != null) fis.close();
                if (os != null) os.close();
            } catch (Exception e) {
                logger.error("操作失败", e);
            }
        }
    }

    // ==================== 以下为新增 Service 方法（基于旧mo项目补充）====================

    /**
     * 提交审批流程
     * 更新状态为"审批中"(02)，并创建审批流程记录
     */
    @Transactional
    public Map<String, Object> commitData(String billId) {
        Map<String, Object> result = new HashMap<>();
        try {
            TComponents record = tComponentsMapper.selectByGuid(billId);
            if (record == null) {
                result.put("flag", 0);
                result.put("message", "申请单不存在");
                return result;
            }

            // 只有"编制"(01)状态才允许提交
            if (!"01".equals(record.getMaStatus())) {
                result.put("flag", 0);
                result.put("message", "当前状态不允许提交");
                return result;
            }

            // 更新状态为"审批中"(02)
            record.setMaStatus("02");
            SysUser sysUser = userService.getCurrentUser();
            record.setUpdateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            record.setUpdateDate(new Date());
            tComponentsMapper.updateByGuidSelective(record);

            // 创建审批意见记录（系统自动）
            TComponentsOpinion opinion = new TComponentsOpinion();
            opinion.setGuid(UUID.randomUUID().toString());
            opinion.setComponentsId(billId);
            opinion.setOpinionType("01");
            opinion.setOpinionContent("提交审批");
            opinion.setAuditUserId(sysUser != null ? sysUser.getUsername() : "admin");
            opinion.setAuditUserName(sysUser != null ? sysUser.getUsername() : "admin");
            opinion.setAuditDate(new Date());
            opinion.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            opinion.setCreateDate(new Date());
            tComponentsOpinionMapper.insertSelective(opinion);

            result.put("flag", 1);
            return result;
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "提交失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 撤回流程2（从审批中状态撤回到设计出图状态）
     */
    @Transactional
    public Map<String, Object> retractApp2(String guid) {
        Map<String, Object> result = new HashMap<>();
        try {
            TComponents record = tComponentsMapper.selectByGuid(guid);
            if (record == null) {
                result.put("flag", 0);
                result.put("message", "申请单不存在");
                return result;
            }

            // 从"审批中"(02)或"设计出图审批"(05)撤回到"设计出图"(04)
            String currentStatus = record.getMaStatus();
            if ("02".equals(currentStatus) || "05".equals(currentStatus)) {
                record.setMaStatus("04"); // 设计出图
            } else {
                result.put("flag", 0);
                result.put("message", "当前状态不允许撤回");
                return result;
            }

            SysUser sysUser = userService.getCurrentUser();
            record.setUpdateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            record.setUpdateDate(new Date());
            tComponentsMapper.updateByGuidSelective(record);

            result.put("flag", 1);
            return result;
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "撤回失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 设计出图提交
     * 从"设计出图"(04)状态提交到"设计出图审批"(05)
     */
    @Transactional
    public Map<String, Object> designCommit(String billId, String msg) {
        Map<String, Object> result = new HashMap<>();
        try {
            TComponents record = tComponentsMapper.selectByGuid(billId);
            if (record == null) {
                result.put("flag", 0);
                result.put("message", "申请单不存在");
                return result;
            }

            if (!"04".equals(record.getMaStatus())) {
                result.put("flag", 0);
                result.put("message", "当前状态不允许设计提交");
                return result;
            }

            // 更新状态为"设计出图审批"(05)
            record.setMaStatus("05");
            record.setDesignStatus("01");
            SysUser sysUser = userService.getCurrentUser();
            record.setUpdateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            record.setUpdateDate(new Date());
            tComponentsMapper.updateByGuidSelective(record);

            // 创建审批意见
            TComponentsOpinion opinion = new TComponentsOpinion();
            opinion.setGuid(UUID.randomUUID().toString());
            opinion.setComponentsId(billId);
            opinion.setOpinionType("02");
            opinion.setOpinionContent(msg != null && !msg.isEmpty() ? msg : "设计完成，提交审批");
            opinion.setAuditUserId(sysUser != null ? sysUser.getUsername() : "admin");
            opinion.setAuditUserName(sysUser != null ? sysUser.getUsername() : "admin");
            opinion.setAuditDate(new Date());
            opinion.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
            opinion.setCreateDate(new Date());
            tComponentsOpinionMapper.insertSelective(opinion);

            result.put("flag", 1);
            return result;
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "设计提交失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 获取BPM流程图路径
     */
    public String getBpmPath(String pid) {
        // 旧系统集成BPM引擎，当前简化实现
        // 如需实际流程图，可从T_COMPONENTS.MA_PROCESS_ID关联查询
        if (pid != null && !pid.isEmpty()) {
            // 模拟返回流程图路径
            return "/images/bpm/default_process.png";
        }
        return null;
    }

    /**
     * 批量保存基础信息
     */
    @Transactional
    public Map<String, Object> saveBases(List<Map<String, Object>> requestList) {
        Map<String, Object> result = new HashMap<>();
        try {
            int successCount = 0;
            for (Map<String, Object> request : requestList) {
                Map<String, Object> saveResult = saveBase(request);
                if (saveResult.get("flag").equals(1)) {
                    successCount++;
                }
            }
            result.put("flag", successCount > 0 ? 1 : 0);
            result.put("successCount", successCount);
            result.put("totalCount", requestList.size());
            return result;
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "批量保存失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 批量保存引用/报价
     */
    @Transactional
    public Map<String, Object> saveBasesQuote(List<Map<String, Object>> requestList) {
        Map<String, Object> result = new HashMap<>();
        try {
            int successCount = 0;
            for (Map<String, Object> request : requestList) {
                // 保存到T_COMPONENTS_QUOTE表
                TComponentsQuote quote = new TComponentsQuote();
                String guid = UUID.randomUUID().toString();
                quote.setGuid(guid);
                quote.setComponentId((String) request.get("componentId"));
                quote.setIsUsed((String) request.getOrDefault("isUsed", "01"));
                quote.setIsQuoted((String) request.getOrDefault("isQuoted", "01"));
                String comDate = (String) request.get("comDate");
                if (comDate != null && !comDate.isEmpty()) {
                    try {
                        quote.setComDate(new SimpleDateFormat("yyyy-MM-dd").parse(comDate));
                    } catch (ParseException ignored) {}
                }
                SysUser sysUser = userService.getCurrentUser();
                quote.setCreateUserId(sysUser != null ? sysUser.getUsername() : "admin");
                quote.setCreateDate(new Date());
                tComponentsQuoteMapper.insertSelective(quote);
                successCount++;
            }
            result.put("flag", successCount > 0 ? 1 : 0);
            result.put("successCount", successCount);
            result.put("totalCount", requestList.size());
            return result;
        } catch (Exception e) {
            logger.error("操作失败", e);
            result.put("flag", 0);
            result.put("message", "批量保存报价失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 获取WPS文件预览URL
     */
    public String getWpsPreviewUrl(String fileId, String billNo) {
        // 旧系统集成WPS Web Office SDK，当前简化实现
        // 返回文件下载URL作为预览URL
        return "/api/components/fileDownload?fileId=" + fileId;
    }
}
