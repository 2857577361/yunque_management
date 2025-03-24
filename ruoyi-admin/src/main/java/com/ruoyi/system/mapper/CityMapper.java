package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.City;
import java.util.List;

public interface CityMapper {
    List<City> selectCitiesByPid(Long pid);
}