package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 王琦
 * 2021/6/18
 */
@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryAllDicValues() {
        return dicValueMapper.selectAllDicValues();
    }

    @Override
    public int saveCreateDicValue(DicValue dicValue) {
        return dicValueMapper.insertDicValue(dicValue);
    }

    @Override
    public int deleteDicValueByIds(String[] ids) {
        return dicValueMapper.deleteDicValueByIds(ids);
    }


    @Override
    public DicValue queryDicValueById(String id) {
        return dicValueMapper.selectDicValueById(id);
    }


    @Override
    public int savEditDicValue(DicValue dicValue) {
        return dicValueMapper.updateDicValue(dicValue);
    }

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
