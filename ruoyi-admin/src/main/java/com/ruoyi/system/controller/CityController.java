package com.ruoyi.system.controller;

import com.ruoyi.system.domain.City;
import com.ruoyi.system.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/city")
public class CityController {

    @Autowired
    private ICityService cityService;

    @GetMapping("/tree")
    public List<Map<String, Object>> getCityTree() {
        List<City> provinces = cityService.getCitiesByPid(10000000L); // 查询所有省份
        List<Map<String, Object>> result = new ArrayList<>();

        for (City province : provinces) {
            Map<String, Object> provinceMap = new HashMap<>();
            provinceMap.put("value", province.getName());
            provinceMap.put("label", province.getName());

            List<City> cities = cityService.getCitiesByPid(province.getId()); // 查询该省份下的所有城市
            List<Map<String, Object>> cityList = new ArrayList<>();

            for (City city : cities) {
                Map<String, Object> cityMap = new HashMap<>();
                cityMap.put("value", city.getName());
                cityMap.put("label", city.getName());

                List<City> districts = cityService.getCitiesByPid(city.getId()); // 查询该城市下的所有区县
                List<Map<String, Object>> districtList = new ArrayList<>();

                for (City district : districts) {
                    Map<String, Object> districtMap = new HashMap<>();
                    districtMap.put("value", district.getName());
                    districtMap.put("label", district.getName());
                    districtList.add(districtMap);
                }

                if (!districtList.isEmpty()) {
                    cityMap.put("children", districtList);
                }
                cityList.add(cityMap);
            }

            if (!cityList.isEmpty()) {
                provinceMap.put("children", cityList);
            }
            result.add(provinceMap);
        }
        return result;
    }
}