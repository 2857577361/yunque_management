package com.ruoyi.climate.service.impl;

import com.ruoyi.climate.domain.ClimateAlert;
import com.ruoyi.climate.mapper.ClimateAlertMapper;
import com.ruoyi.climate.service.IClimateAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClimateAlertServiceImpl implements IClimateAlertService {

    @Autowired
    private ClimateAlertMapper climateAlertMapper;

    @Override
    public List<ClimateAlert> selectClimateAlertList(ClimateAlert climateAlert) {
        return climateAlertMapper.selectClimateAlertList(climateAlert);
    }

    @Override
    public ClimateAlert selectClimateAlertById(Long id) {
        return climateAlertMapper.selectClimateAlertById(id);
    }

    @Override
    public int updateClimateAlert(ClimateAlert climateAlert) {
        return climateAlertMapper.updateClimateAlert(climateAlert);
    }

    @Override
    public int deleteClimateAlertByIds(Long[] ids) {
        return climateAlertMapper.deleteClimateAlertByIds(ids);
    }

    @Override
    public int countUnprocessedAlerts() {
        return climateAlertMapper.countUnprocessedAlerts();
    }
}