package com.ruoyi.climate.domain;

import java.time.LocalDateTime;

public class SensorWebsocketData {
    private Long id;
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private Double pressure;
    private LocalDateTime recordTime;
    private String status;

    // 新增字段
    private Double hourlyRainfall;
    private Double daylyRainfall;
    private Double wettingTimeOfLeafSurface;
    private Double hoursSunshine;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}