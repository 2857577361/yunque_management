package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.City;
import com.ruoyi.system.mapper.CityMapper;
import com.ruoyi.system.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CityServiceImpl implements ICityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<City> getCitiesByPid(Long pid) {
        return cityMapper.selectCitiesByPid(pid);
    }
}