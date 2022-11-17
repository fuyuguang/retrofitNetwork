package com.mvvm.provider.data;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-06-17.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
public class UserInfo {

    public String user_head;
    public String userid;
    public String e3pUserId;

    public String e3pDepartmentName;

    public String e3pLoginToken;
    public String e3pCorporationID;
    public boolean powerLimit;
    public String departmentId;
    public String username;
    public int status;


    public String mType;
    public boolean isdistributor;


    /**
     * 酒快到进货APP和名酒城进货APP同时修改。根据角色判断用户登录看到的内容。
     * 管理者角色，可以在“我的”界面中的“配送中心”模块点击进入，
     * 店长和店员角色登录直接跳转到配送中心首页，店长和店员角色无法看到进货APP的其他内容。
     */
    public boolean isMan;//true：是骑手

    public boolean isManager() {//管理者

        return !isMan;

    }

    public boolean isAllot;//true:具有分配骑手的权利

    public boolean Allotcation() {
        return isAllot;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user_head='" + user_head + '\'' +
                ", userid='" + userid + '\'' +
                ", e3pUserId='" + e3pUserId + '\'' +
                ", e3pDepartmentName='" + e3pDepartmentName + '\'' +
                ", e3pLoginToken='" + e3pLoginToken + '\'' +
                ", e3pCorporationID='" + e3pCorporationID + '\'' +
                ", powerLimit=" + powerLimit +
                ", departmentId='" + departmentId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
