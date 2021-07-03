package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 王琦
 * 2021/6/9
 */
@Controller
public class DicTypeController {

    @Autowired
    private DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/type/index.do")
    public String index(Model model) {
        List<DicType> dicTypeList = dicTypeService.queryAllDicTypes();
        model.addAttribute("dicTypeList", dicTypeList);
        return "settings/dictionary/type/index";
    }


    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String toSave() {

        return "settings/dictionary/type/save";

    }

    @RequestMapping("/settings/dictionary/type/checkCode.do")
    @ResponseBody
    public ReturnObject checkCode(String code) {
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        ReturnObject returnObject = new ReturnObject();
        if (dicType == null) {
            returnObject.setCode("1");

        } else {
            returnObject.setCode("0");
            returnObject.setMessage("编码重复");
        }
        return returnObject;
    }


    @RequestMapping("/settings/dictionary/type/saveCreateDicType.do")
    @ResponseBody
    public ReturnObject saveCreateDicType(DicType dicType) {
        ReturnObject returnObject = new ReturnObject();
        int i = dicTypeService.saveCreateDicType(dicType);
        if (i == 0) {
            returnObject.setCode("0");
            returnObject.setMessage("保存失败！");
        } else {
            returnObject.setCode("1");
            returnObject.setMessage("保存成功！");
        }
        return returnObject;
    }

    //字典类型编辑第一步
    @RequestMapping("/settings/dictionary/type/editDicType.do")
    public String editDicType(String code, Model model) {
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        model.addAttribute("dicType", dicType);
        return "settings/dictionary/type/edit";
    }

    //字典类型更新第二步
    @RequestMapping("/settings/dictionary/type/saveEditDicType.do")
    @ResponseBody
    public ReturnObject saveEditDicType(DicType dicType) {
        ReturnObject returnObject = new ReturnObject();

        int i = dicTypeService.saveEditDicType(dicType);
        if (i != 0) {
            returnObject.setCode("1");
            returnObject.setMessage("更新成功！");
        } else {
            returnObject.setCode("0");
            returnObject.setMessage("更新失败！");
        }
        return returnObject;
    }

    //字典类型删除
    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    @ResponseBody
    public ReturnObject deleteDicTypeByCodes(String[] code) {
        ReturnObject returnObject = new ReturnObject();
        int i = dicTypeService.deleteDicTypeByCodes(code);
        if (i != 0) {
            returnObject.setCode("1");
            returnObject.setMessage("删除成功！");
        } else {
            returnObject.setCode("0");
            returnObject.setMessage("删除失败！");
        }
        return returnObject;
    }


}
