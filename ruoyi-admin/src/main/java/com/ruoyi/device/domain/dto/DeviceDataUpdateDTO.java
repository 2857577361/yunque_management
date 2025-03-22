package com.ruoyi.device.domain.dto;

import java.util.Date;

public class DeviceDataUpdateDTO {
    private Long id;
    private String deviceId;
    private String address;
    private String department;
    private String responsiblePerson;
    private String alarmPhone;
    private String coordinates;
    private Date warrantyTime;
    private String status;
    private String country;
    private String province;
    private String city;
    private String district;
    private Boolean isOpened;
    private String deptName;
    private String deptType;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getAlarmPhone() {
        return alarmPhone;
    }

    public void setAlarmPhone(String alarmPhone) {
        this.alarmPhone = alarmPhone;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Date getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(Date warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Boolean getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Boolean isOpened) {
        this.isOpened = isOpened;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }
// Getters and Setters
}