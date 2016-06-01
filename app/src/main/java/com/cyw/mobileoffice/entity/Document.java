package com.cyw.mobileoffice.entity;

/**
 * Created by cyw on 2016/5/30.
 */
public class Document {

    private String docCode;
    private String title;
    private String creater;
    private String createrName;
    private String date;
    private String content;
    private String recipientsCode;
    private String recipients;
    private String attachment;
    private String attachmentPath;

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
