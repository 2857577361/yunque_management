package com.ruoyi.climate.mapper;

import com.ruoyi.climate.domain.SensorWebsocketData;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SensorWebsocketDataMapper {
    List<SensorWebsocketData> selectBetweenDates(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);
}