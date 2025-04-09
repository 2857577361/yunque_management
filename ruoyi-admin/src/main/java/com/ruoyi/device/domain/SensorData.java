package com.ruoyi.device.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 传感器数据对象 sensor_data
 *
 * @author ruoyi
 * @date 2025-03-24
 */
public class SensorData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 设备 ID */
    private String stationId;

    /** 空气温度 */
    private Double airTemperature;

    /** 相对湿度 */
    private Double airHumidity;

    /** 露点温度 */
    private Double dewPointHumidity;

    /** 小时降雨量 */
    private Double hourlyRainfall;

    /** 天降雨量 */
    private Double daylyRainfall;
    private Double atmosphericPressure;

    /** 叶面湿润时间 */
    private Double wettingTimeOfLeafSurface;

    /** 日照时数 */
    private Double hoursSunshine;

    /** 土壤含水量（第1层） */
    private Double n1SoilWaterContent;

    /** 土壤含水量（第2层） */
    private Double n2SoilWaterContent;

    /** 土壤含水量（第3层） */
    private Double n3SoilWaterContent;

    /** 土壤含水量（第4层） */
    private Double n4SoilWaterContent;

    /** 土壤含水量（第5层） */
    private Double n5SoilWaterContent;

    /** 采集时间 */
    private Date collectTime;

    /** 上传时间 */
    private Date uploadTime;

    // 省略 getter 和 setter 方法

    public Double getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(Double atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public Double getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(Double airHumidity) {
        this.airHumidity = airHumidity;
    }

    public Double getDewPointHumidity() {
        return dewPointHumidity;
    }

    public void setDewPointHumidity(Double dewPointHumidity) {
        this.dewPointHumidity = dewPointHumidity;
    }

    public Double getHourlyRainfall() {
        return hourlyRainfall;
    }

    public void setHourlyRainfall(Double hourlyRainfall) {
        this.hourlyRainfall = hourlyRainfall;
    }

    public Double getDaylyRainfall() {
        return daylyRainfall;
    }

    public void setDaylyRainfall(Double daylyRainfall) {
        this.daylyRainfall = daylyRainfall;
    }

    public Double getWettingTimeOfLeafSurface() {
        return wettingTimeOfLeafSurface;
    }

    public void setWettingTimeOfLeafSurface(Double wettingTimeOfLeafSurface) {
        this.wettingTimeOfLeafSurface = wettingTimeOfLeafSurface;
    }

    public Double getHoursSunshine() {
        return hoursSunshine;
    }

    public void setHoursSunshine(Double hoursSunshine) {
        this.hoursSunshine = hoursSunshine;
    }

    public Double getN1SoilWaterContent() {
        return n1SoilWaterContent;
    }

    public void setN1SoilWaterContent(Double n1SoilWaterContent) {
        this.n1SoilWaterContent = n1SoilWaterContent;
    }

    public Double getN2SoilWaterContent() {
        return n2SoilWaterContent;
    }

    public void setN2SoilWaterContent(Double n2SoilWaterContent) {
        this.n2SoilWaterContent = n2SoilWaterContent;
    }

    public Double getN3SoilWaterContent() {
        return n3SoilWaterContent;
    }

    public void setN3SoilWaterContent(Double n3SoilWaterContent) {
        this.n3SoilWaterContent = n3SoilWaterContent;
    }

    public Double getN4SoilWaterContent() {
        return n4SoilWaterContent;
    }

    public void setN4SoilWaterContent(Double n4SoilWaterContent) {
        this.n4SoilWaterContent = n4SoilWaterContent;
    }

    public Double getN5SoilWaterContent() {
        return n5SoilWaterContent;
    }

    public void setN5SoilWaterContent(Double n5SoilWaterContent) {
        this.n5SoilWaterContent = n5SoilWaterContent;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}