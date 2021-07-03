package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 王琦
 * 2021/6/15
 */
@Controller
public class ActivityController {
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService  activityService;

    /*@Autowired
    private ActivityRemark activityRemark;*/


    //跳到首页
    @RequestMapping("/workbench/activity/index.do")
    public String index(Model model){
        List<User> userList=userService.queryAllUsers();
        model.addAttribute("userList", userList);
        return "workbench/activity/index";
    }

    //保存
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity,HttpSession session){

        User user = (User)session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.saveCreateActivity(activity);

            if (ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {

                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    public @ResponseBody Object queryActivityForPageByCondition(int pageNo,int pageSize,String name,String owner,String startDate,String endDate){
        HashMap map = new HashMap();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //市场活动集合
        List<Activity>activityList=activityService.queryActivityForPageByCondition(map);


        //市场活动总数
        long totalRows=activityService.queryCountOfActivityByCondition(map);

        //单独为这个方法做一个map对象
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    //修改
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(String id){
        Activity activity = activityService.queryActivityById(id);

        return activity;
    }

    //更新
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity,HttpSession session){

        User user = (User)session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        activity.setEditBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.saveEditActivity(activity);

            if (ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnObject;
    }

    //删除
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.deleteActivityByIds(id);

            if (ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnObject;
    }




   //批量导出
    @RequestMapping("/workbench/activity/exportAllActivity.do")

    public void exportAllActivity(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //1.从数据库中把所有的市场活动对象取出
        List<Activity> activityList = activityService.queryAllActivityForDetail();

        //2.创造工作簿
        HSSFWorkbook wb =new  HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);//1行

        //1列 ID
        HSSFCell cell = row.createCell(0);//列
        cell.setCellValue("ID");

        //2列 所有者
        cell = row.createCell(1);
        cell.setCellValue("所有者");

        //3列 名称
        cell = row.createCell(2);
        cell.setCellValue("名称");

        //4列 开始时间
        cell = row.createCell(3);
        cell.setCellValue("开始时间");

        //5列 结束时间
        cell = row.createCell(4);
        cell.setCellValue("结束时间");

        //6列 成本
        cell = row.createCell(5);
        cell.setCellValue("成本");

        //7列 结束时间
        cell = row.createCell(6);
        cell.setCellValue("描述");

        //样式对象
        HSSFCellStyle style = wb.createCellStyle();

        //对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);


        if (activityList!=null){
            Activity activity = null;
            for (int i=0;i<activityList.size();i++){
                activity = activityList.get(i);
                //第二行开始
                row=sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());

                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());

                cell = row.createCell(2);
                cell.setCellValue(activity.getName());

                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());

                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());

                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());

                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
            }
        }

        //下载
        //1.设置相应类型，默认的情况下，浏览器认为服务器返回的html文件 excel->流
        response.setContentType("application/octet-stream;charset=UTF-8");
        String fileName = URLEncoder.encode("市场活动列表","UTF-8");
        //2.设置相应头信息,Header
        response.addHeader("Content-Disposition", "attachment;="+fileName+".xls");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();//有些输出流带有buffer(缓冲区)
        wb.close();
    }

    //选择导出
    @RequestMapping("/workbench/activity/exportActivitySelective.do")
    public void exportActivitySelective(String[] id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.queryActivityForDetailByIds(id);

        //2.创造工作簿
        HSSFWorkbook wb =new  HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);//1行

        //1列 ID
        HSSFCell cell = row.createCell(0);//列
        cell.setCellValue("ID");

        //2列 所有者
        cell = row.createCell(1);
        cell.setCellValue("所有者");

        //3列 名称
        cell = row.createCell(2);
        cell.setCellValue("名称");

        //4列 开始时间
        cell = row.createCell(3);
        cell.setCellValue("开始时间");

        //5列 结束时间
        cell = row.createCell(4);
        cell.setCellValue("结束时间");

        //6列 成本
        cell = row.createCell(5);
        cell.setCellValue("成本");

        //7列 结束时间
        cell = row.createCell(6);
        cell.setCellValue("描述");

        //样式对象
        HSSFCellStyle style = wb.createCellStyle();

        //对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);

        if (activityList!=null){
            Activity activity = null;
            for (int i=0;i<activityList.size();i++){
                activity = activityList.get(i);
                //第二行开始
                row=sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());

                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());

                cell = row.createCell(2);
                cell.setCellValue(activity.getName());

                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());

                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());

                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());

                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
            }
        }

        //下载
        //1.设置相应类型，默认的情况下，浏览器认为服务器返回的html文件 excel->流
        response.setContentType("application/octet-stream;charset=UTF-8");
        String browser =request.getHeader("User-Agent");

        String fileName = URLEncoder.encode("市场活动列表","UTF-8");
        //2.设置相应头信息,Header
        response.addHeader("Content-Disposition", "attachment;="+fileName+".xls");
        //获取输出流
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();//有些输出流带有buffer(缓冲区)
        wb.close();
    }

    //处理上传
    @RequestMapping("/workbench/activity/fileUpload.do")
    public @ResponseBody Object fileUpload(MultipartFile myFile, String username) throws IOException {
        System.out.println(username);
        //取文件名
        String filename = myFile.getOriginalFilename();
        File file = new File("d:\\testDir",filename);
        myFile.transferTo(file);//将文件传到指定的d:\\testDir文件中


        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

        return returnObject;

    }
    //处理上传
    @RequestMapping("workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,String username,HttpSession session){

        User user = (User)session.getAttribute(Contants.SESSION_USER);
        //返回对象
        Map<String,Object> retMap = new HashMap<>();

        //list集合来存放excel中每一行就是一个市场活动对象
        List<Activity> activityList = new ArrayList<>();

        InputStream is = null;
        try {
            is = activityFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //is是上传文件
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet =wb.getSheetAt(0);//第一张表

        HSSFRow row = null;
        HSSFCell cell = null;

        Activity activity = null;

        //跳过标题
        for (int i = 1;i<=sheet.getLastRowNum();i++){
            row = sheet.getRow(i);
            activity = new Activity();
            activity.setId(UUIDUtils.getUUID());
            activity.setOwner(user.getId());
            activity.setCreateBy(user.getId());
            activity.setCreateTime(DateUtils.formatDateTime(new Date()));

            for (int j = 0;j<row.getLastCellNum();j++){
                cell = row.getCell(j);
                String cellValue = getCellValue(cell);
                if (j==0){
                    activity.setName(cellValue);
                }else if (j==1){
                    activity.setStartDate(cellValue);
                }else if (j==2){
                    activity.setEndDate(cellValue);
                }else if (j==3){
                    activity.setCost(cellValue);
                }else if (j==4){
                    activity.setDescription(cellValue);
                }
            }
            activityList.add(activity);
        }
        //插入的几行记录
        int ret = activityService.saveCreateActivityByList(activityList);
        retMap.put("code", Contants.RETURN_OBJECT_CODE_SUCCESS);
        retMap.put("count", ret);

        return retMap;
    }
    //将cell单元格数据传过来判断类型并用合适方法取出后转成字符串
    public static  String getCellValue(HSSFCell cell){
        String ret = "";
        switch (cell.getCellType()){
            case  HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue()+"";
                break;
            default:
                ret="";
        }
        return ret;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,Model model){
        Activity activity = activityService.queryActivityForDetailById(id);
        model.addAttribute("activity", activity);
        return "workbench/activity/detail";
    }

}
