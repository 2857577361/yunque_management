package com.ruoyi.climate.service;

import com.ruoyi.climate.domain.ClimateAlert;
import java.util.List;

public interface IClimateAlertService {
    List<ClimateAlert> selectClimateAlertList(ClimateAlert climateAlert);
    ClimateAlert selectClimateAlertById(Long id);
    int updateClimateAlert(ClimateAlert climateAlert);
    int deleteClimateAlertByIds(Long[] ids);
    int countUnprocessedAlerts();
}