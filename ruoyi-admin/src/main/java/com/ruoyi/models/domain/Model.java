package com.ruoyi.models.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 模型对象 model
 *
 * @author ruoyi
 * @date 2025-03-24
 */
public class Model extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模型ID */
    private Long modelId;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String modelName;

    /** 公式定义 */
    @Excel(name = "公式定义")
    private String formulaDefinition;

    /** 开始预测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始预测时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startPredictionTime;

    /** 结束预测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束预测时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endPredictionTime;

    /** 误差范围 */
    @Excel(name = "误差范围")
    private Double errorRange;

    /** 关联设备ID列表 */
    private List<String> deviceIds;

    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getModelName()
    {
        return modelName;
    }
    public void setFormulaDefinition(String formulaDefinition)
    {
        this.formulaDefinition = formulaDefinition;
    }

    public String getFormulaDefinition()
    {
        return formulaDefinition;
    }
    public void setStartPredictionTime(Date startPredictionTime)
    {
        this.startPredictionTime = startPredictionTime;
    }

    public Date getStartPredictionTime()
    {
        return startPredictionTime;
    }
    public void setEndPredictionTime(Date endPredictionTime)
    {
        this.endPredictionTime = endPredictionTime;
    }

    public Date getEndPredictionTime()
    {
        return endPredictionTime;
    }
    public void setErrorRange(Double errorRange)
    {
        this.errorRange = errorRange;
    }

    public Double getErrorRange()
    {
        return errorRange;
    }
    public void setDeviceIds(List<String> deviceIds)
    {
        this.deviceIds = deviceIds;
    }

    public List<String> getDeviceIds()
    {
        return deviceIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("modelId", getModelId())
                .append("modelName", getModelName())
                .append("formulaDefinition", getFormulaDefinition())
                .append("startPredictionTime", getStartPredictionTime())
                .append("endPredictionTime", getEndPredictionTime())
                .append("errorRange", getErrorRange())
                .append("deviceIds", getDeviceIds())
                .toString();
    }
}