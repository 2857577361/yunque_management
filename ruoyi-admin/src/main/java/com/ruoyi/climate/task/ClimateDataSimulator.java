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
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClimateDataSimulator {

    @Autowired
    private SensorWebsocketDataMapper sensorWebsocketDataMapper;

    @Autowired
    private ClimateWebSocketService climateWebSocketService;

    @Autowired
    private ClimateAlertMapper climateAlertMapper;

    // 20分钟检测一次的规则（温度、湿度、气压、小时降雨量）
    @Scheduled(fixedRate = 12000000)
    public void checkFrequentRules() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyMinutesAgo = now.minusHours(24);

        List<SensorWebsocketData> dataList = sensorWebsocketDataMapper
                .selectBetweenDates(twentyMinutesAgo, now);

        boolean[] hasViolations = {false};

        dataList.forEach(data -> {
            boolean hasTempViolation = isTemperatureViolation(data);
            boolean hasHumidityViolation = isHumidityViolation(data);
            boolean hasPressureViolation = isPressureViolation(data);
            boolean hasHourlyRainfallViolation = isHourlyRainfallViolation(data);

            if (hasTempViolation || hasHumidityViolation || hasPressureViolation || hasHourlyRainfallViolation) {
                hasViolations[0] = true;

                if (hasTempViolation) {
                    String message = String.format("设备 %s 温度异常: %.1f°C (允许范围: -20~40°C) 采集时间: %s",
                            data.getDeviceId(), data.getTemperature(), data.getRecordTime());
                    createAlert(data,
                            data.getTemperature() > 40 ? "TEMP_HIGH" : "TEMP_LOW",
                            "temperature",
                            data.getTemperature(),
                            data.getTemperature() > 40 ? 40.0 : -20.0,
                            2,
                            message);
                }

                if (hasHumidityViolation) {
                    String message = String.format("设备 %s 湿度异常: %.1f%% (允许范围: 0~100%%) 采集时间: %s",
                            data.getDeviceId(), data.getHumidity(), data.getRecordTime());
                    createAlert(data,
                            data.getHumidity() > 100 ? "HUMIDITY_HIGH" : "HUMIDITY_LOW",
                            "humidity",
                            data.getHumidity(),
                            data.getHumidity() > 100 ? 100.0 : 0.0,
                            2,
                            message);
                }

                if (hasPressureViolation) {
                    String message = String.format("设备 %s 气压异常: %.1fhPa (允许范围: 950~1050hPa) 采集时间: %s",
                            data.getDeviceId(), data.getPressure(), data.getRecordTime());
                    createAlert(data,
                            data.getPressure() > 1050 ? "PRESSURE_HIGH" : "PRESSURE_LOW",
                            "pressure",
                            data.getPressure(),
                            data.getPressure() > 1050 ? 1050.0 : 950.0,
                            2,
                            message);
                }

                if (hasHourlyRainfallViolation) {
                    String message = String.format("设备 %s 小时降雨量异常: %.1fmm (湿度不足75%%) 采集时间: %s",
                            data.getDeviceId(), data.getHourlyRainfall(), data.getRecordTime());
                    createAlert(data,
                            "RAINFALL_HUMIDITY_LOW",
                            "hourly_rainfall",
                            data.getHourlyRainfall(),
                            75.0,
                            2,
                            message);
                }
            }
        });

//        String statusMessage = String.format(
//                "{\"type\":\"frequent_monitoring\", \"time\":\"%s\", \"checkedRecords\":%d, \"hasViolations\":%b}",
//                now.toString(), dataList.size(), hasViolations[0]);
//        climateWebSocketService.broadcastClimateData(statusMessage);
        String statusMessage = String.format(
                "{\"type\":\"monitoring\", \"time\":\"%s\", \"checkedRecords\":%d, \"hasViolations\":%b}",
                now.toString(), dataList.size(), hasViolations);
        climateWebSocketService.broadcastClimateData(statusMessage);
    }

    // 每天两次检测的规则（凌晨3点和下午5点）
    @Scheduled(cron = "0 0,30 3,9,17 * * ?")
    public void checkDailyRules() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        List<SensorWebsocketData> dataList = sensorWebsocketDataMapper
                .selectBetweenDates(startOfDay, endOfDay);

        // 检查记录数量是否足够
        checkRecordCount(dataList, yesterday);

        // 检查天降雨量规则
        checkDailyRainfall(dataList, yesterday);

        // 检查日照时数和叶面湿润时间
        boolean[] hasViolations = {false};

        dataList.forEach(data -> {
            boolean hasSunshineViolation = isSunshineViolation(data);
            boolean hasWettingTimeViolation = isWettingTimeViolation(data);

            if (hasSunshineViolation || hasWettingTimeViolation) {
                hasViolations[0] = true;

                if (hasSunshineViolation) {
                    String message = String.format("设备 %s 日照时数异常: %.1f小时 (允许范围: 0~12小时) 采集时间: %s",
                            data.getDeviceId(), data.getHoursSunshine(), data.getRecordTime());
                    createAlert(data,
                            data.getHoursSunshine() > 12 ? "SUNSHINE_HIGH" : "SUNSHINE_LOW",
                            "hours_sunshine",
                            data.getHoursSunshine(),
                            data.getHoursSunshine() > 12 ? 12.0 : 0.0,
                            2,
                            message);
                }

                if (hasWettingTimeViolation) {
                    String message = String.format("设备 %s 叶面湿润时间异常: %.1f小时 (允许范围: 0~24小时) 采集时间: %s",
                            data.getDeviceId(), data.getWettingTimeOfLeafSurface()/60, data.getRecordTime());
                    createAlert(data,
                            data.getWettingTimeOfLeafSurface()/60 > 24 ? "WETTING_TIME_HIGH" : "WETTING_TIME_LOW",
                            "wetting_time",
                            data.getWettingTimeOfLeafSurface()/60,
                            data.getWettingTimeOfLeafSurface()/60 > 24 ? 24.0 : 0.0,
                            2,
                            message);
                }
            }
        });

        String statusMessage = String.format(
                "{\"type\":\"daily_monitoring\", \"time\":\"%s\", \"checkedRecords\":%d, \"hasViolations\":%b}",
                now.toString(), dataList.size(), hasViolations[0]);
        climateWebSocketService.broadcastClimateData(statusMessage);
    }

    // 检查记录数量是否足够
    private void checkRecordCount(List<SensorWebsocketData> dataList, LocalDate date) {
        if (dataList.size() < 12) {
            String deviceId = dataList.isEmpty() ? "UNKNOWN" : dataList.get(0).getDeviceId();
            String message = String.format("设备 %s 上传记录缺失: 仅上传了%d条记录 (应至少12条) 日期: %s",
                    deviceId, dataList.size(), date.toString());

            ClimateAlert alert = new ClimateAlert();
            alert.setDeviceId(deviceId);
            alert.setAlertType("RECORD_COUNT_LOW");
            alert.setMetricType("record_count");
            alert.setMetricValue((double)dataList.size());
            alert.setThresholdValue(12.0);
            alert.setAlertLevel(2);
            alert.setAlertTime(LocalDateTime.now());
            alert.setStatus(0);
            climateAlertMapper.insertClimateAlert(alert);

            climateWebSocketService.sendClimateAlert(deviceId, message);
        }
    }

    // 检查天降雨量规则
    private void checkDailyRainfall(List<SensorWebsocketData> dataList, LocalDate date) {
        if (dataList.size() >= 12) {
            SensorWebsocketData lastRecord = dataList.get(11); // 第12条记录
            double dailyRainfall = lastRecord.getDaylyRainfall();

            if (dailyRainfall == 0) {
                boolean hasNonZero = dataList.stream()
                        .limit(11)
                        .anyMatch(data -> data.getDaylyRainfall() > 0);

                if (hasNonZero) {
                    createDailyRainfallAlert(lastRecord, "DAILY_RAINFALL_INCONSISTENT",
                            "天降雨量异常: 第12条记录为0但前面记录不为0", date);
                }
            } else {
                double maxPrevious = dataList.stream()
                        .limit(11)
                        .mapToDouble(SensorWebsocketData::getDaylyRainfall)
                        .max()
                        .orElse(0);

                if (dailyRainfall <= maxPrevious) {
                    createDailyRainfallAlert(lastRecord, "DAILY_RAINFALL_INCONSISTENT",
                            String.format("天降雨量异常: 第12条记录(%.1f)不大于前面最大记录(%.1f)",
                                    dailyRainfall, maxPrevious), date);
                }
            }
        }
    }

    private void createDailyRainfallAlert(SensorWebsocketData data, String alertType,
                                          String message, LocalDate date) {
        String fullMessage = String.format("设备 %s %s 日期: %s",
                data.getDeviceId(), message, date.toString());

        ClimateAlert alert = new ClimateAlert();
        alert.setDeviceId(data.getDeviceId());
        alert.setAlertType(alertType);
        alert.setMetricType("daily_rainfall");
        alert.setMetricValue(data.getDaylyRainfall());
        alert.setThresholdValue(0.0);
        alert.setAlertLevel(2);
        alert.setAlertTime(data.getRecordTime());
        alert.setStatus(0);
        climateAlertMapper.insertClimateAlert(alert);

        climateWebSocketService.sendClimateAlert(data.getDeviceId(), fullMessage);
    }

    private void createAlert(SensorWebsocketData data, String alertType, String metricType,
                             Double metricValue, Double thresholdValue, Integer alertLevel,
                             String message) {
        climateWebSocketService.sendClimateAlert(data.getDeviceId(), message);

        ClimateAlert alert = new ClimateAlert();
        alert.setDeviceId(data.getDeviceId());
        alert.setAlertType(alertType);
        alert.setMetricType(metricType);
        alert.setMetricValue(metricValue);
        alert.setThresholdValue(thresholdValue);
        alert.setAlertLevel(alertLevel);
        alert.setAlertTime(data.getRecordTime());
        alert.setStatus(0);
        climateAlertMapper.insertClimateAlert(alert);
    }

    // 温度检查 (-20~40°C)
    private boolean isTemperatureViolation(SensorWebsocketData data) {
        return data.getTemperature() < -20 || data.getTemperature() > 40;
    }

    // 湿度检查 (0~100%)
    private boolean isHumidityViolation(SensorWebsocketData data) {
        return data.getHumidity() < 0 || data.getHumidity() > 100;
    }

    // 气压检查 (950~1050hPa)
    private boolean isPressureViolation(SensorWebsocketData data) {
        return data.getPressure() < 950 || data.getPressure() > 1050;
    }

    // 小时降雨量检查 (降雨时湿度必须>75%)
    private boolean isHourlyRainfallViolation(SensorWebsocketData data) {
        return data.getHourlyRainfall() > 0 && data.getHumidity() <= 75;
    }

    // 日照时数检查 (0~12小时)
    private boolean isSunshineViolation(SensorWebsocketData data) {
        return data.getHoursSunshine() < 0 || data.getHoursSunshine() > 12;
    }

    // 叶面湿润时间检查 (0~24小时)
    private boolean isWettingTimeViolation(SensorWebsocketData data) {
        return data.getWettingTimeOfLeafSurface()/60 < 0 || data.getWettingTimeOfLeafSurface()/60 > 24;
    }
}