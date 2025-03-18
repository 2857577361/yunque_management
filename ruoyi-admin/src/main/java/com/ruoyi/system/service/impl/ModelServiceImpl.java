package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Model;
import com.ruoyi.system.mapper.ModelMapper;
import com.ruoyi.system.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Model> selectModelListWithDevices(String modelId, Date startTime, Date endTime) {
        return modelMapper.selectModelListWithDevices(modelId, startTime, endTime);
    }

    @Override
    public Model selectModelById(String id) {
        return modelMapper.selectModelById(id);
    }

    @Override
    public int updateModel(Model model) {
        return modelMapper.updateModel(model);
    }

    @Override
    public int deleteModelById(String id) {
        return modelMapper.deleteModelById(id);
    }
}