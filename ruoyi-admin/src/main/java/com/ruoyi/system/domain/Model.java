package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

public class Model extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 模型ID */
    @Excel(name = "模型ID")
    private String id;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String name;

    /** 公式定义 */
    @Excel(name = "公式定义")
    private String formulaDefinition;

    /** 开始时间 */
    @Excel(name = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private Date endTime;

    /** 误差范围 */
    @Excel(name = "误差范围")
    private String errorRange;

    /** 关联的设备ID列表 */
    private List<String> deviceList;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormulaDefinition() {
        return formulaDefinition;
    }

    public void setFormulaDefinition(String formulaDefinition) {
        this.formulaDefinition = formulaDefinition;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getErrorRange() {
        return errorRange;
    }

    public void setErrorRange(String errorRange) {
        this.errorRange = errorRange;
    }

    public List<String> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<String> deviceList) {
        this.deviceList = deviceList;
    }
}