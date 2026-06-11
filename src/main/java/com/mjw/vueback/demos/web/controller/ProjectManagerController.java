package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.TProject;
import com.mjw.vueback.demos.web.mapper.TProjectMapper;
import com.mjw.vueback.demos.web.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目管理相关 Controller
 * 对应前端 ComponentsCreate.vue 中调用的 /projectManager/qryAllProjNo
 */
@RestController
@RequestMapping("/api/projectManager")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectManagerController {

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private MockDataService mockDataService;

    /**
     * 获取工程号列表
     * GET /api/projectManager/qryAllProjNo
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
}
