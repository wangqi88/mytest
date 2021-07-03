package com.bjpowernode.crm.commons.domain;

/**
 * 王琦
 * 2021/6/5
 */
public class ReturnObject {

    private String code;//业务执行的代码状态
    private String message;//对code的说明
    private Object retData;//一个对象，多个对象

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
