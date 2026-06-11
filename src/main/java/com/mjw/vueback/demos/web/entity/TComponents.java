package com.mjw.vueback.demos.web.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工装申请表
 */
public class TComponents {
    private String guid;
    private String billNo;
    private String companyNo;
    private String deptNo;
    private String componentsName;
    private String projNo;
    private String divCd;
    private BigDecimal numberNo;
    private BigDecimal finalNumberNo;
    private String appointDutyDeptYn;
    private String dwgno;
    private String materialTotalCost;
    private String mhBdgt;
    private String createUserId;
    private Date createDate;
    private Date needDate;
    private String updateUserId;
    private Date updateDate;
    private String remark;
    private String maProcessId;
    private String maStatus;
    private String tel;
    private Date appDate;
    private String designStatus;
    private String boId;
    
    private String deptName;  // 部门描述（不从数据库存储，只用于展示）

    // Getters and Setters
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getComponentsName() {
        return componentsName;
    }

    public void setComponentsName(String componentsName) {
        this.componentsName = componentsName;
    }

    public String getProjNo() {
        return projNo;
    }

    public void setProjNo(String projNo) {
        this.projNo = projNo;
    }

    public String getDivCd() {
        return divCd;
    }

    public void setDivCd(String divCd) {
        this.divCd = divCd;
    }

    public BigDecimal getNumberNo() {
        return numberNo;
    }

    public void setNumberNo(BigDecimal numberNo) {
        this.numberNo = numberNo != null ? numberNo : BigDecimal.ZERO;
    }

    public BigDecimal getFinalNumberNo() {
        return finalNumberNo;
    }

    public void setFinalNumberNo(BigDecimal finalNumberNo) {
        this.finalNumberNo = finalNumberNo != null ? finalNumberNo : BigDecimal.ZERO;
    }

    public String getAppointDutyDeptYn() {
        return appointDutyDeptYn;
    }

    public void setAppointDutyDeptYn(String appointDutyDeptYn) {
        this.appointDutyDeptYn = appointDutyDeptYn;
    }

    public String getDwgno() {
        return dwgno;
    }

    public void setDwgno(String dwgno) {
        this.dwgno = dwgno;
    }

    public String getMaterialTotalCost() {
        return materialTotalCost;
    }

    public void setMaterialTotalCost(String materialTotalCost) {
        this.materialTotalCost = materialTotalCost;
    }

    public String getMhBdgt() {
        return mhBdgt;
    }

    public void setMhBdgt(String mhBdgt) {
        this.mhBdgt = mhBdgt;
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

    public Date getNeedDate() {
        return needDate;
    }

    public void setNeedDate(Date needDate) {
        this.needDate = needDate;
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

    public String getMaProcessId() {
        return maProcessId;
    }

    public void setMaProcessId(String maProcessId) {
        this.maProcessId = maProcessId;
    }

    public String getMaStatus() {
        return maStatus;
    }

    public void setMaStatus(String maStatus) {
        this.maStatus = maStatus;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getDesignStatus() {
        return designStatus;
    }

    public void setDesignStatus(String designStatus) {
        this.designStatus = designStatus;
    }

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }
}
