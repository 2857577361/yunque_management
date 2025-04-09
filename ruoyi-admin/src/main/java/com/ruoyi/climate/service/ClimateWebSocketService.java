package com.ruoyi.climate.service;

import com.ruoyi.framework.websocket.ClimateDataWebSocketHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClimateWebSocketService {

    /**
     * 广播气候数据给所有连接的客户端
     * @param message 要发送的消息内容
     */
    public void broadcastClimateData(String message) {
        ClimateDataWebSocketHandler.sendMessageToAll(message);
    }

    /**
     * 发送气候异常警报
     * @param deviceId 设备ID
     * @param message 警报消息
     */
    public void sendClimateAlert(String deviceId, String message) {
        String formattedMessage = String.format(
                "{\"type\":\"alert\",\"deviceId\":\"%s\",\"message\":\"%s\"}",
                deviceId, message);
        broadcastClimateData(formattedMessage);
    }

    public void sendMonitoringStatus(int checkedRecords, boolean hasViolations) {
        String message = String.format(
                "{\"type\":\"monitoring\", \"time\":\"%s\", \"checkedRecords\":%d, \"hasViolations\":%b}",
                LocalDateTime.now().toString(), checkedRecords, hasViolations);
        broadcastClimateData(message);
    }
    /**
     * 发送违规警报
     * @param deviceId 设备ID
     * @param ruleType 规则类型 (TEMPERATURE/HUMIDITY/PRESSURE)
     * @param message 警报消息
     */
    public void sendViolationAlert(String deviceId, String ruleType, String message) {
        String formattedMessage = String.format(
                "{\"type\":\"violation\",\"deviceId\":\"%s\",\"ruleType\":\"%s\",\"message\":\"%s\"}",
                deviceId, ruleType, message);
        broadcastClimateData(formattedMessage);
    }
}