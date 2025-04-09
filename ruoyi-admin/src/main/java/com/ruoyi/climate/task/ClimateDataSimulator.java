package com.ruoyi.climate.task;

import com.ruoyi.climate.domain.SensorWebsocketData;
import com.ruoyi.climate.mapper.SensorWebsocketDataMapper;
import com.ruoyi.climate.service.ClimateWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ruoyi.climate.mapper.ClimateAlertMapper;
import com.ruoyi.climate.domain.ClimateAlert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.time.LocalDateTime;

@Component
public class ClimateDataSimulator {

    @Autowired
    private SensorWebsocketDataMapper sensorWebsocketDataMapper;

    @Autowired
    private ClimateWebSocketService climateWebSocketService;

    @Autowired
    private ClimateAlertMapper climateAlertMapper; // 新增的Mapper

    @Scheduled(fixedRate = 1200000)
    public void checkSensorDataViolations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAgo = now.minusHours(48);

        List<SensorWebsocketData> dataList = sensorWebsocketDataMapper
                .selectBetweenDates(twentyFourHoursAgo, now);

        boolean[] hasViolations = {false};

        dataList.forEach(data -> {
            boolean hasTempViolation = isTemperatureViolation(data);
            boolean hasHumidityViolation = isHumidityViolation(data);
            boolean hasPressureViolation = isPressureViolation(data);

            if (hasTempViolation || hasHumidityViolation || hasPressureViolation) {
                hasViolations[0] = true;

                // 记录温度异常
                if (hasTempViolation) {
                    String message = String.format("设备 %s 温度异常: %.1f°C (允许范围: -20~50°C) 采集时间: %s",
                            data.getDeviceId(), data.getTemperature(), data.getRecordTime());
                    climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);

                    // 写入异常表
                    ClimateAlert alert = new ClimateAlert();
                    alert.setDeviceId(data.getDeviceId());
                    alert.setAlertType(data.getTemperature() > 50 ? "TEMP_HIGH" : "TEMP_LOW");
                    alert.setMetricType("temperature");
                    alert.setMetricValue(data.getTemperature());
                    alert.setThresholdValue(data.getTemperature() > 50 ? 50.0 : -20.0);
                    alert.setAlertLevel(2); // 严重级别
                    alert.setAlertTime(data.getRecordTime()); // 使用collect_time作为异常时间
                    alert.setStatus(0);
                    climateAlertMapper.insertClimateAlert(alert);
                }

                // 记录湿度异常
                if (hasHumidityViolation) {
                    String message = String.format("设备 %s 湿度异常: %.1f%% (允许范围: 10~90%%) 采集时间: %s",
                            data.getDeviceId(), data.getHumidity(), data.getRecordTime());
                    climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);

                    ClimateAlert alert = new ClimateAlert();
                    alert.setDeviceId(data.getDeviceId());
                    alert.setAlertType(data.getHumidity() > 90 ? "HUMIDITY_HIGH" : "HUMIDITY_LOW");
                    alert.setMetricType("humidity");
                    alert.setMetricValue(data.getHumidity());
                    alert.setThresholdValue(data.getHumidity() > 90 ? 90.0 : 10.0);
                    alert.setAlertLevel(2);
                    alert.setAlertTime(data.getRecordTime());
                    alert.setStatus(0);
                    climateAlertMapper.insertClimateAlert(alert);
                }

                // 记录气压异常
                if (hasPressureViolation) {
                    String message = String.format("设备 %s 气压异常: %.1fhPa (允许范围: 950~1050hPa) 采集时间: %s",
                            data.getDeviceId(), data.getPressure(), data.getRecordTime());
                    climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);

                    ClimateAlert alert = new ClimateAlert();
                    alert.setDeviceId(data.getDeviceId());
                    alert.setAlertType(data.getPressure() > 1050 ? "PRESSURE_HIGH" : "PRESSURE_LOW");
                    alert.setMetricType("pressure");
                    alert.setMetricValue(data.getPressure());
                    alert.setThresholdValue(data.getPressure() > 1050 ? 1050.0 : 950.0);
                    alert.setAlertLevel(2);
                    alert.setAlertTime(data.getRecordTime());
                    alert.setStatus(0);
                    climateAlertMapper.insertClimateAlert(alert);
                }
            }
        });

        String statusMessage = String.format(
                "{\"type\":\"monitoring\", \"time\":\"%s\", \"checkedRecords\":%d, \"hasViolations\":%b}",
                now.toString(), dataList.size(),  hasViolations[0]);
        climateWebSocketService.broadcastClimateData(statusMessage);
    }

    // 原有的检查方法保持不变
    private boolean isTemperatureViolation(SensorWebsocketData data) {
        return data.getTemperature() < -20 || data.getTemperature() > 50;
    }

    private boolean isHumidityViolation(SensorWebsocketData data) {
        return data.getHumidity() < 10 || data.getHumidity() > 90;
    }

    private boolean isPressureViolation(SensorWebsocketData data) {
        return data.getPressure() < 950 || data.getPressure() > 1050;
    }
}
//@Component
//public class ClimateDataSimulator {
//
//    @Autowired
//    private SensorWebsocketDataMapper sensorWebsocketDataMapper;
//
//    @Autowired
//    private ClimateWebSocketService climateWebSocketService;
//
//    @Scheduled(fixedRate = 1200000)
////    @Scheduled(fixedRate = 1200)
//    public void checkSensorDataViolations() {
//        // 获取当前时间
//        LocalDateTime now = LocalDateTime.now();
//        // 获取24小时前的时间
//        LocalDateTime twentyFourHoursAgo = now.minusHours(48);
//
//        // 查询24小时内的数据
//        List<SensorWebsocketData> dataList = sensorWebsocketDataMapper
//                .selectBetweenDates(twentyFourHoursAgo, now);
//        boolean hasViolations = false;
//        // 检查每条数据是否符合规则
//        dataList.forEach(data -> {
//            if (isTemperatureViolation(data)) {
//                String message = String.format("设备 %s 温度异常: %.1f°C (允许范围: -20~50°C)",
//                        data.getDeviceId(), data.getTemperature());
//                climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);
//            }
//
//            if (isHumidityViolation(data)) {
//                String message = String.format("设备 %s 湿度异常: %.1f%% (允许范围: 10~90%%)",
//                        data.getDeviceId(), data.getHumidity());
//                climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);
//            }
//
//            if (isPressureViolation(data)) {
//                String message = String.format("设备 %s 气压异常: %.1fhPa (允许范围: 950~1050hPa)",
//                        data.getDeviceId(), data.getPressure());
//                climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);
//            }
//        });
//        // 无论是否有异常，都发送状态通知
//        String statusMessage = String.format(
//                "{\"type\":\"monitoring\", \"time\":\"%s\", \"checkedRecords\":%d, \"hasViolations\":%b}",
//                now.toString(), dataList.size(), hasViolations);
//        climateWebSocketService.broadcastClimateData(statusMessage);
//    }
//
//    private boolean isTemperatureViolation(SensorWebsocketData data) {
//        return data.getTemperature() < -20 || data.getTemperature() > 50;
//    }
//
//    private boolean isHumidityViolation(SensorWebsocketData data) {
//        return data.getHumidity() < 10 || data.getHumidity() > 90;
//    }
//
//    private boolean isPressureViolation(SensorWebsocketData data) {
//        return data.getPressure() < 950 || data.getPressure() > 1050;
//    }
//}