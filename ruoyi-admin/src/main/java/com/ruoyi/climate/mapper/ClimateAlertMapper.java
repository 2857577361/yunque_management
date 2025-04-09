package com.ruoyi.climate.mapper;

import com.ruoyi.climate.domain.ClimateAlert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClimateAlertMapper {
    int insertClimateAlert(ClimateAlert alert);

    List<ClimateAlert> selectClimateAlertList(ClimateAlert climateAlert);
    ClimateAlert selectClimateAlertById(Long id);
    int updateClimateAlert(ClimateAlert climateAlert);
    int deleteClimateAlertByIds(Long[] ids);
    int countUnprocessedAlerts();
}