package com.mjw.vueback.demos.web.entity;

/**
 * 单位表
 */
public class TUnit {
    private String GUID;
    private String UNT_DESC;
    private String UNT_TYPE;
    private String ACTIVATION;
    
    // Getters and Setters
    public String getGUID() {
        return GUID;
    }
    
    public void setGUID(String GUID) {
        this.GUID = GUID;
    }
    
    public String getUNT_DESC() {
        return UNT_DESC;
    }
    
    public void setUNT_DESC(String UNT_DESC) {
        this.UNT_DESC = UNT_DESC;
    }
    
    public String getUNT_TYPE() {
        return UNT_TYPE;
    }
    
    public void setUNT_TYPE(String UNT_TYPE) {
        this.UNT_TYPE = UNT_TYPE;
    }
    
    public String getActivation() {
        return ACTIVATION;
    }
    
    public void setActivation(String ACTIVATION) {
        this.ACTIVATION = ACTIVATION;
    }
}
