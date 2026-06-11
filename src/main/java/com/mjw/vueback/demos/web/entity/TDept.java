package com.mjw.vueback.demos.web.entity;

import java.util.Date;

/**
 * 部门组织表 实体类
 */
public class TDept {
    
    private String orgnCd;         // 组织代码
    private String orgnDesc;       // 组织描述
    private String superOrgnCd;    // 上级组织代码(公司代码)
    private String orgnType;       // 组织类型: 01-公司, 02-部门, 03-小组
    private String status;         // 状态: 01-有效, 02-无效
    private String createUserId;   // 创建用户ID
    private Date createDate;       // 创建日期
    private String updateUserId;   // 更新用户ID
    private Date updateDate;       // 更新日期
    private String remark;         // 备注
    
    // 构造函数
    public TDept() {
    }
    
    // Getter 和 Setter 方法
    public String getOrgnCd() {
        return orgnCd;
    }
    
    public void setOrgnCd(String orgnCd) {
        this.orgnCd = orgnCd;
    }
    
    public String getOrgnDesc() {
        return orgnDesc;
    }
    
    public void setOrgnDesc(String orgnDesc) {
        this.orgnDesc = orgnDesc;
    }
    
    public String getSuperOrgnCd() {
        return superOrgnCd;
    }
    
    public void setSuperOrgnCd(String superOrgnCd) {
        this.superOrgnCd = superOrgnCd;
    }
    
    public String getOrgnType() {
        return orgnType;
    }
    
    public void setOrgnType(String orgnType) {
        this.orgnType = orgnType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreateUserId() {
        return createUserId;
    }
    
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getUpdateUserId() {
        return updateUserId;
    }
    
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
    
    public Date getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
