package com.cfuas.admin.mytest;

import java.io.Serializable;

/**
 * 包名：com.simpletour.client.ui.travel.bean
 * 描述：行车助理
 * 创建者：yankebin
 * 日期：2016/4/5
 */
public class AssistantManager implements Serializable {
    public static final int MANAGER_TYPE_SERVICE=1;
    public static final int MANAGER_TYPE_ASSISTANT=2;
    /**
     * name : 陈洁
     * mobile : 18828053572
     * nickName : 泰迪熊
     */
    private int assistantId;
    private String name;
    private String mobile;
    private String nickName;
    private String description;
    private String tourismSections;
    private int type;//1.客服 2.行车助理
    private String avatar;//头像
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(int assistantId) {
        this.assistantId = assistantId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTourismSections() {
        return tourismSections;
    }

    public void setTourismSections(String tourismSections) {
        this.tourismSections = tourismSections;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNickName() {
        return nickName;
    }
}
