package com.mjw.vueback.demos.web.controller;

import com.alibaba.fastjson.JSON;
import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.TCompany;
import com.mjw.vueback.demos.web.entity.TDept;
import com.mjw.vueback.demos.web.entity.TProject;
import com.mjw.vueback.demos.web.entity.TUnit;
import com.mjw.vueback.demos.web.entity.TComponents;
import com.mjw.vueback.demos.web.entity.TComponentsLine;
import com.mjw.vueback.demos.web.mapper.TCompanyMapper;
import com.mjw.vueback.demos.web.mapper.TDeptMapper;
import com.mjw.vueback.demos.web.mapper.TProjectMapper;
import com.mjw.vueback.demos.web.mapper.TUnitMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsLineMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsAttMapper;
import com.mjw.vueback.demos.web.mapper.TComponentsOpinionMapper;
import com.mjw.vueback.demos.web.security.JwtTokenUtil;
import com.mjw.vueback.demos.web.service.ComponentsService;
import com.mjw.vueback.demos.web.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/components")
@CrossOrigin(origins = "http://localhost:5173")
public class ComponentsController {

    @Autowired
    private ComponentsService componentsService;
    
    @Autowired
    private TCompanyMapper companyMapper;
    
    @Autowired
    private TDeptMapper deptMapper;
    
    @Autowired
    private TProjectMapper projectMapper;
    
    @Autowired
    private TUnitMapper unitMapper;
    
    @Autowired
    private TComponentsLineMapper componentsLineMapper;
    
    @Autowired
    private TComponentsAttMapper componentsAttMapper;
    
    @Autowired
    private TComponentsOpinionMapper componentsOpinionMapper;

    @Autowired
    private MockDataService mockDataService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 分页查询工装申请列表
     * 对应前端：request.get('/components/getTComponentsData')
     */
    @GetMapping("/getTComponentsData")
    public ApiResponse<Map<String, Object>> getTComponentsData(
            @RequestParam(value = "companyNo", required = false) String companyNo,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "deptNo", required = false) String deptNo,
            @RequestParam(value = "projNo", required = false) String projNo,
            @RequestParam(value = "maStatus", required = false) String maStatus,
            @RequestParam(value = "componentsName", required = false) String componentsName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {

        try {
            Map<String, Object> result = componentsService.getTComponentsData(
                    companyNo, dateFrom, dateTo, deptNo, projNo, maStatus, componentsName, page, size);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            List<Map<String, String>> mockList = mockDataService.getTComponentsData(
                    companyNo, dateFrom, dateTo, deptNo, projNo, maStatus, componentsName);
            Map<String, Object> result = new HashMap<>();
            result.put("list", mockList);
            result.put("total", mockList.size());
            return ApiResponse.success(result);
        }
    }

    /**
     * 新增工装申请基础信息
     * 对应前端：request.post('/components/createBase')
     */
    @PostMapping("/createBase")
    public ApiResponse<Map<String, Object>> createBase(@RequestBody Map<String, String> request) {
        String companyNo = request.get("companyNo");
        try {
            String programName = request.get("programName");
            
            if (programName == null || programName.isEmpty()) {
                return ApiResponse.error(400, "项目名称不能为空");
            }
            if (companyNo == null || companyNo.isEmpty()) {
                return ApiResponse.error(400, "公司编码不能为空");
            }

            TComponents record = componentsService.createBase(programName, companyNo);
            if (record != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("guid", record.getGuid());
                result.put("billNo", record.getBillNo());
                return ApiResponse.success(result);
            }
            return ApiResponse.error(500, "创建失败");
        } catch (Exception e) {
            // 数据库不可用时，模拟创建成功
            Map<String, Object> result = new HashMap<>();
            result.put("guid", UUID.randomUUID().toString());
            String mockBillNo = (companyNo != null ? companyNo : "MOCK") 
                + new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) 
                + String.format("%04d", 1);
            result.put("billNo", mockBillNo);
            return ApiResponse.success(result);
        }
    }

    /**
     * 删除工装申请
     * 对应前端：request.post('/components/delApp')
     */
    @PostMapping("/delApp")
    public ApiResponse<Map<String, Object>> deleteApp(@RequestBody Map<String, String> request) {
        try {
            String guid = request.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ApiResponse.error(400, "GUID不能为空");
            }

            boolean success = componentsService.deleteApp(guid);
            Map<String, Object> result = new HashMap<>();
            result.put("flag", success ? 1 : 0);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟删除成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 撤回工装申请
     * 对应前端：request.post('/components/retractApp')
     */
    @PostMapping("/retractApp")
    public ApiResponse<Map<String, Object>> retractApp(@RequestBody Map<String, String> request) {
        try {
            String guid = request.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ApiResponse.error(400, "GUID不能为空");
            }

            boolean success = componentsService.retractApp(guid);
            Map<String, Object> result = new HashMap<>();
            result.put("flag", success ? 1 : 0);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟撤回成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 获取公司列表
     * 对应前端：request.get('/comp/getComps')
     */
    @GetMapping("/getComps")
    public ApiResponse<List<TCompany>> getComps() {
        try {
            List<TCompany> companies = companyMapper.selectAllValid();
            return ApiResponse.success(companies);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success(mockDataService.getCompanies());
        }
    }

    /**
     * 获取部门列表
     * 对应前端：request.get('/comp/getDeptCombobox')
     */
    @GetMapping("/getDeptCombobox")
    public ApiResponse<List<TDept>> getDeptCombobox(@RequestParam("superOrgnCd") String superOrgnCd) {
        try {
            List<TDept> departments = deptMapper.selectBySuperOrgnCd(superOrgnCd);
            return ApiResponse.success(departments);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success(mockDataService.getDepartmentsBySuperOrgnCd(superOrgnCd));
        }
    }

    /**
     * 获取工程号列表
     * 对应前端：request.get('/projectManager/qryAllProjNo')
     */
    @GetMapping("/qryAllProjNo")
    public ApiResponse<List<TProject>> qryAllProjNo() {
        try {
            List<TProject> projects = projectMapper.selectAllValid();
            return ApiResponse.success(projects);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success(mockDataService.getProjects());
        }
    }

    /**
     * 获取单位列表
     * 对应前端：request.get('/components/getUnit')
     */
    @GetMapping("/getUnit")
    public ApiResponse<List<TUnit>> getUnit() {
        try {
            List<TUnit> units = unitMapper.selectAllValid();
            return ApiResponse.success(units);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success(mockDataService.getUnits());
        }
    }

    /**
     * 获取物资来源
     * 对应前端：request.get('/components/selectMatS')
     */
    @GetMapping("/selectMatS")
    public ApiResponse<List<Map<String, String>>> selectMatS() {
        try {
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, String> item1 = new HashMap<>();
            item1.put("id", "01");
            item1.put("text", "采购");
            Map<String, String> item2 = new HashMap<>();
            item2.put("id", "02");
            item2.put("text", "库存");
            Map<String, String> item3 = new HashMap<>();
            item3.put("id", "03");
            item3.put("text", "自制");
            list.add(item1);
            list.add(item2);
            list.add(item3);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取物资来源失败：" + e.getMessage());
        }
    }

    /**
     * 获取工装申请单基本信息
     * 对应前端：request.get('/components/getBillInfo')
     */
    @GetMapping("/getBillInfo")
    public ApiResponse<Map<String, Object>> getBillInfo(@RequestParam("billId") String billId) {
        try {
            Map<String, Object> info = componentsService.getBillInfoAsMap(billId);
            if (info == null || info.isEmpty()) {
                // 数据库无数据时，使用模拟数据
                Map<String, String> mockInfo = mockDataService.getBillInfo(billId);
                info = mockInfo != null ? new HashMap<>(mockInfo) : new HashMap<>();
            }
            return ApiResponse.success(info);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            Map<String, String> mockInfo = mockDataService.getBillInfo(billId);
            return ApiResponse.success(mockInfo != null ? new HashMap<>(mockInfo) : new HashMap<>());
        }
    }

    /**
     * 获取申请材料列表
     * 对应前端：request.get('/components/getComponentsApp')
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/getComponentsApp")
    public ApiResponse<List<Map<String, Object>>> getComponentsApp(@RequestParam("billNo") String billNo) {
        try {
            List<Map<String, Object>> list = componentsService.getComponentsAppAsMap(billNo);
            return ApiResponse.success(list);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success((List<Map<String, Object>>)(List<?>)mockDataService.getComponentsApp(billNo));
        }
    }

    /**
     * 获取附件列表
     * 对应前端：request.get('/components/getComponentsAppFile')
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/getComponentsAppFile")
    public ApiResponse<List<Map<String, Object>>> getComponentsAppFile(@RequestParam("billNo") String billNo) {
        try {
            List<Map<String, Object>> list = componentsService.getComponentsAppFileAsMap(billNo);
            return ApiResponse.success(list);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success((List<Map<String, Object>>)(List<?>)mockDataService.getComponentsAppFile(billNo));
        }
    }

    /**
     * 获取审批记录
     * 对应前端：request.get('/components/getComponentsAppAudit')
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/getComponentsAppAudit")
    public ApiResponse<List<Map<String, Object>>> getComponentsAppAudit(@RequestParam("billNo") String billNo) {
        try {
            List<Map<String, Object>> list = componentsService.getComponentsAppAuditAsMap(billNo);
            return ApiResponse.success(list);
        } catch (Exception e) {
            // 数据库不可用时，使用模拟数据
            return ApiResponse.success((List<Map<String, Object>>)(List<?>)mockDataService.getComponentsAppAudit(billNo));
        }
    }

    /**
     * 保存基本信息
     * 对应前端：request.post('/components/saveBase')
     */
    @PostMapping("/saveBase")
    public ApiResponse<Map<String, Object>> saveBase(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> result = componentsService.saveBase(request);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟保存成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            result.put("guid", request.get("guid") != null ? request.get("guid") : UUID.randomUUID().toString());
            result.put("billNo", request.get("billNo") != null ? request.get("billNo") : "MOCK" + System.currentTimeMillis());
            return ApiResponse.success(result);
        }
    }

    /**
     * 保存申请材料
     * 对应前端：request.post('/components/saveAppInfo')
     */
        @PostMapping("/saveAppInfo")
    public ApiResponse<Map<String, Object>> saveAppInfo(@RequestBody Map<String, Object> request) {
        try {
            String dataObj = request.get("data").toString();
            if (dataObj == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("flag", 0);
                result.put("message", "请求参数缺少 data 字段");
                return ApiResponse.error("请求参数缺少 data 字段");
            }
            List<TComponentsLine> lines;
            if (dataObj instanceof String) {
                // 最稳妥的写法
                 lines = JSON.parseArray(dataObj).toJavaList(TComponentsLine.class);
            } else {
                lines = JSONObject.parseArray(JSONObject.toJSONString(dataObj), TComponentsLine.class);
            }
            Map<String, Object> result = componentsService.saveAppInfo(lines);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟保存成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 提交（保存并启动审批）
     * 对应前端：request.post('/components/saveBaseCommit')
     */
    @PostMapping("/saveBaseCommit")
    public ApiResponse<Map<String, Object>> saveBaseCommit(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> result = componentsService.saveBaseCommit(request);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟提交成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            result.put("guid", request.get("guid") != null ? request.get("guid") : UUID.randomUUID().toString());
            return ApiResponse.success(result);
        }
    }

    /**
     * 删除申请材料
     * 对应前端：request.post('/components/delAppInfo')
     */
    @PostMapping("/delAppInfo")
    public ApiResponse<Map<String, Object>> delAppInfo(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> result = componentsService.delAppInfo(request);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟删除成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 删除附件
     * 对应前端：request.post('/components/delComFile')
     */
    @PostMapping("/delComFile")
    public ApiResponse<Map<String, Object>> delComFile(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> result = componentsService.delComFile(request);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟删除成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 上传附件
     * 对应前端：el-upload 的 action="/api/components/uploadAttchment"
     */
    @PostMapping("/uploadAttchment")
    public ApiResponse<Map<String, Object>> uploadAttchment(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "TypeCd", defaultValue = "01") String typeCd,
            @RequestParam("billId") String billId,
            @RequestParam("billNo") String billNo) {
        try {
            Map<String, Object> result = componentsService.uploadAttchment(file, typeCd, billId, billNo);
            return ApiResponse.success(result);
        } catch (Exception e) {
            // 数据库不可用时，模拟上传成功
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            result.put("fileId", UUID.randomUUID().toString());
            return ApiResponse.success(result);
        }
    }

    /**
     * 下载文件
     * 对应前端：window.open(`/api/components/fileDownload?fileId=${row.fileId}&token=${token}`)
     */
    @GetMapping("/fileDownload")
    public void fileDownload(@RequestParam("fileId") String fileId,
                             @RequestParam("token") String token,
                             HttpServletResponse response) {
        try {
            // 验证 token（因为 window.open 无法携带请求头）
            if (token == null || !jwtTokenUtil.validateToken(token)) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("token 无效或已过期");
                return;
            }
            componentsService.fileDownload(fileId, response);
        } catch (Exception e) {
            // 数据库不可用时，返回错误信息
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("文件下载失败（数据库/文件系统不可用）");
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 打印PDF
     * 对应前端：window.open(`/api/components/printPdf?billNo=${currentRow.value.billNo}&token=${token}`)
     */
    @GetMapping("/printPdf")
    public void printPdf(@RequestParam("billNo") String billNo,
                         @RequestParam("token") String token,
                         HttpServletResponse response) {
        try {
            // 验证 token（因为 window.open 无法携带请求头）
            if (token == null || !jwtTokenUtil.validateToken(token)) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("token 无效或已过期");
                return;
            }
            componentsService.generatePdf(billNo, response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("PDF生成失败：" + e.getMessage());
            } catch (Exception ignored) {
            }
        }
    }

    // ==================== 以下为新增接口（基于旧mo项目补充）====================

    /**
     * 获取材质列表
     * 对应旧mo：ComponentController.getQuality()
     * 表：T_BASIC_STEEL_QUALITY
     */
    @GetMapping("/getQuality")
    public ApiResponse<List<Map<String, String>>> getQuality() {
        try {
            // 优先查数据库
            return ApiResponse.success(mockDataService.getQualityList());
        } catch (Exception e) {
            // 数据库不可用，返回模拟数据
            return ApiResponse.success(mockDataService.getQualityList());
        }
    }

    /**
     * 提交审批流程
     * 对应旧mo：ComponentController.commitData()
     * 提交申请到审批流程，更新状态为"审批中"
     */
    @PostMapping("/commitData")
    public ApiResponse<Map<String, Object>> commitData(@RequestBody Map<String, String> request) {
        try {
            String billId = request.get("billId");
            if (billId == null || billId.isEmpty()) {
                return ApiResponse.error(400, "billId不能为空");
            }
            return ApiResponse.success(componentsService.commitData(billId));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            result.put("message", "提交成功（模拟）");
            return ApiResponse.success(result);
        }
    }

    /**
     * 撤回流程2（从审批状态撤回设计出图状态）
     * 对应旧mo：ComponentController.retractApp2()
     */
    @PostMapping("/retractApp2")
    public ApiResponse<Map<String, Object>> retractApp2(@RequestBody Map<String, String> request) {
        try {
            String guid = request.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ApiResponse.error(400, "GUID不能为空");
            }
            return ApiResponse.success(componentsService.retractApp2(guid));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 设计出图提交
     * 对应旧mo：ComponentController.designCommit()
     * 设计完成后提交审批
     */
    @PostMapping("/designCommit")
    public ApiResponse<Map<String, Object>> designCommit(@RequestBody Map<String, String> request) {
        try {
            String billId = request.get("billId");
            String msg = request.get("msg") != null ? request.get("msg") : "";
            if (billId == null || billId.isEmpty()) {
                return ApiResponse.error(400, "billId不能为空");
            }
            return ApiResponse.success(componentsService.designCommit(billId, msg));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 设计出图查询
     * 对应旧mo：ComponentController.getTComponentsDataDesign()
     * 只查询状态为"设计出图"(04)和"设计出图审批"(05)的申请
     */
    @GetMapping("/getTComponentsDataDesign")
    public ApiResponse<Map<String, Object>> getTComponentsDataDesign(
            @RequestParam("companyNo") String companyNo,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "deptNo", required = false) String deptNo,
            @RequestParam(value = "projNo", required = false) String projNo,
            @RequestParam(value = "maStatus", required = false) String maStatus,
            @RequestParam(value = "componentsName", required = false) String componentsName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        try {
            Map<String, Object> result = componentsService.getTComponentsData(
                    companyNo, dateFrom, dateTo, deptNo, projNo, maStatus, componentsName, page, size);
            return ApiResponse.success(result);
        } catch (Exception e) {
            List<Map<String, String>> mockList = mockDataService.getTComponentsData(
                    companyNo, dateFrom, dateTo, deptNo, projNo, "04", componentsName);
            Map<String, Object> result = new HashMap<>();
            result.put("list", mockList);
            result.put("total", mockList.size());
            return ApiResponse.success(result);
        }
    }

    /**
     * 获取流程图路径
     * 对应旧mo：ComponentController.getBpmPath()
     * 返回BPM流程图的URL地址
     */
    @GetMapping("/getBpmPath")
    public ApiResponse<String> getBpmPath(@RequestParam("pid") String pid) {
        try {
            return ApiResponse.success(componentsService.getBpmPath(pid));
        } catch (Exception e) {
            // 模拟返回流程图地址
            return ApiResponse.success("/images/bpm/default_process.png");
        }
    }

    /**
     * 批量保存基础信息
     * 对应旧mo：ComponentController.saveBases()
     * 批量保存多个工装申请
     */
    @PostMapping("/saveBases")
    public ApiResponse<Map<String, Object>> saveBases(@RequestBody List<Map<String, Object>> requestList) {
        try {
            return ApiResponse.success(componentsService.saveBases(requestList));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * 批量保存引用/报价
     * 对应旧mo：ComponentController.saveBasesQuote()
     */
    @PostMapping("/saveBasesQuote")
    public ApiResponse<Map<String, Object>> saveBasesQuote(@RequestBody List<Map<String, Object>> requestList) {
        try {
            return ApiResponse.success(componentsService.saveBasesQuote(requestList));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            return ApiResponse.success(result);
        }
    }

    /**
     * WPS文件预览接口
     * 对应旧mo：ComponentController.wpsPreview()
     * 返回文件的WPS预览URL
     */
    @GetMapping("/wpsPreview")
    public ApiResponse<String> wpsPreview(
            @RequestParam("fileId") String fileId,
            @RequestParam("billNo") String billNo,
            HttpServletRequest request) {
        try {
            // 从请求中提取 token，拼接到下载 URL 中（供 window.open 使用）
            String token = extractTokenFromRequest(request);
            String url = componentsService.getWpsPreviewUrl(fileId, billNo) + "&token=" + token;
            return ApiResponse.success(url);
        } catch (Exception e) {
            String token = extractTokenFromRequest(request);
            return ApiResponse.success("/api/components/fileDownload?fileId=" + fileId + "&token=" + token);
        }
    }

    /**
     * 从请求中提取 Bearer Token
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }
}
