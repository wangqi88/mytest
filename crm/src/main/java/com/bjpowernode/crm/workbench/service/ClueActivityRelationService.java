package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * 王琦
 * 2021/6/19
 */
public interface ClueActivityRelationService {

    //批量插入
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> relations);

    //删除
    int deleClueActivityRelatioByClueActivityId(ClueActivityRelation relation);
}
