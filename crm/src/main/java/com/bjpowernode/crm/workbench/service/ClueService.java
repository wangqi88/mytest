package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

/**
 * 王琦
 * 2021/6/18
 */
public interface ClueService {
    int saveCreateClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveConvert(Map<String,Object> map);
}
