package com.ruoyi.models.domain;

import java.util.List;
import java.util.Map;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
public class DeviceDiseaseModelRelation {
    private String deviceId;
    private List<DeviceDiseaseModelVo> diseaseModels; // [{diseaseId:1, modelId:1}, ...]

    // 格式示例：
    // 删除设备所有数据: {"deviceId":"DEV001"}
    // 删除设备+病害: {"deviceId":"DEV001", "diseaseModels":[{"diseaseId":1}]}
    // 删除设备+病害+模型: {"deviceId":"DEV001", "diseaseModels":[{"diseaseId":1, "modelId":101}]}

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<DeviceDiseaseModelVo> getDiseaseModels() {
        return diseaseModels;
    }

    public void setDiseaseModels(List<DeviceDiseaseModelVo> diseaseModels) {
        this.diseaseModels = diseaseModels;
    }
}