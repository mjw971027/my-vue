package com.mjw.vueback.demos.web.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工装申请明细表
 */
public class TComponentsLine {
    private String guid;
    private String componentsId;
    private String activation;
    private String materialNo;
    private String materialName;
    private String unit;
    private BigDecimal demandQty;
    private BigDecimal finalDemandQty;
    private BigDecimal materialCost;
    private String materialSources;
    private String remark;
    private String quality;
    private BigDecimal thk1;
    private BigDecimal thk2;
    private BigDecimal w1;
    private BigDecimal w2;
    private BigDecimal l;
    private String createNode;
    private String createUserId;
    @JSONField(format = "EEE MMM dd HH:mm:ss zzz yyyy")
    private Date createDate;
    private String updateUserId;
    @JSONField(format = "EEE MMM dd HH:mm:ss zzz yyyy")
    private Date updateDate;
    private String boId;

    // Getters and Setters
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(String componentsId) {
        this.componentsId = componentsId;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getDemandQty() {
        return demandQty;
    }

    public void setDemandQty(BigDecimal demandQty) {
        this.demandQty = demandQty != null ? demandQty : BigDecimal.ZERO;
    }

    public BigDecimal getFinalDemandQty() {
        return finalDemandQty;
    }

    public void setFinalDemandQty(BigDecimal finalDemandQty) {
        this.finalDemandQty = finalDemandQty != null ? finalDemandQty : BigDecimal.ZERO;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost != null ? materialCost : BigDecimal.ZERO;
    }

    public String getMaterialSources() {
        return materialSources;
    }

    public void setMaterialSources(String materialSources) {
        this.materialSources = materialSources;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public BigDecimal getThk1() {
        return thk1;
    }

    public void setThk1(BigDecimal thk1) {
        this.thk1 = thk1 != null ? thk1 : BigDecimal.ZERO;
    }

    public BigDecimal getThk2() {
        return thk2;
    }

    public void setThk2(BigDecimal thk2) {
        this.thk2 = thk2 != null ? thk2 : BigDecimal.ZERO;
    }

    public BigDecimal getW1() {
        return w1;
    }

    public void setW1(BigDecimal w1) {
        this.w1 = w1 != null ? w1 : BigDecimal.ZERO;
    }

    public BigDecimal getW2() {
        return w2;
    }

    public void setW2(BigDecimal w2) {
        this.w2 = w2 != null ? w2 : BigDecimal.ZERO;
    }

    public BigDecimal getL() {
        return l;
    }

    public void setL(BigDecimal l) {
        this.l = l != null ? l : BigDecimal.ZERO;
    }

    public String getCreateNode() {
        return createNode;
    }

    public void setCreateNode(String createNode) {
        this.createNode = createNode;
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

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }
}
