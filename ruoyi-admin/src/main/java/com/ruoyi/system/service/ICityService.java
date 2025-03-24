package com.ruoyi.system.service;

import com.ruoyi.system.domain.City;
import java.util.List;

public interface ICityService {
    List<City> getCitiesByPid(Long pid);
}