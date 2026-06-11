package com.mjw.vueback.demos.web.entity;

import java.util.Date;

/**
 * 公司主体表 实体类
 */
public class TCompany {
    
    private String code;           // 公司代码
    private String descChn;        // 公司中文描述
    private String descEng;        // 公司英文描述
    private String status;         // 状态: 01-有效, 02-无效
    private String createUserId;   // 创建用户ID
    private Date createDate;       // 创建日期
    private String updateUserId;   // 更新用户ID
    private Date updateDate;       // 更新日期
    private String remark;         // 备注
    
    // 构造函数
    public TCompany() {
    }
    
    // Getter 和 Setter 方法
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescChn() {
        return descChn;
    }
    
    public void setDescChn(String descChn) {
        this.descChn = descChn;
    }
    
    public String getDescEng() {
        return descEng;
    }
    
    public void setDescEng(String descEng) {
        this.descEng = descEng;
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
