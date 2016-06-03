package com.cyw.mobileoffice.entity;

import java.io.Serializable;

/**
 * Created by cyw on 2016/5/30.
 */
public class Document implements Serializable{

    private String docCode;//公文编号
    private String title;//公文标题
    private String creater;//创建人编号
    private String createrName;//创建人
    private String date;//发布日期
    private String content;//内容
    private String recipientsCode;//收文人编号
    private String recipients;//收文人
    private String attachment;//附件
    private String attachmentPath;//附件路径
    private String editTime;//创建时间
    private String state;//状态
    private String apprName;//审批人
    private String apprAdvice;//审批意见
    private String apprDate;//审批日期

    public String getApprName() {
        return apprName;
    }

    public void setApprName(String apprName) {
        this.apprName = apprName;
    }

    public String getApprAdvice() {
        return apprAdvice;
    }

    public void setApprAdvice(String apprAdvice) {
        this.apprAdvice = apprAdvice;
    }

    public String getApprDate() {
        return apprDate;
    }

    public void setApprDate(String apprDate) {
        this.apprDate = apprDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getRecipientsCode() {
        return recipientsCode;
    }

    public void setRecipientsCode(String recipientsCode) {
        this.recipientsCode = recipientsCode;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public Document(){}
    public Document(String date, String title, String creater, String createrName) {
        this.date = date;
        this.title = title;
        this.creater = creater;
        this.createrName = createrName;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
