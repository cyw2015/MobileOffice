package com.cyw.mobileoffice.util;

/**
 * Created by cyw on 2016/5/27.
 */
public class AppURL {
    public final static String BASE = "http://cyw.ngrok.cc";//基础
    public final static String LOGINURL = BASE + "/login";//登录
    public final static String GETEMPIMAGEURL = BASE + "/person/getUserEmpImage.do";//获取图片路径
    public final static String GETCONTACTS = BASE + "/person/getContacts.do";/*通讯录*/
    public final static String GETVIEWDOCPAGE = BASE + "/document/getViewDocPage.do";//获取可看的公文
    public static final String IMAGEPATH = BASE + "/imageEmp/";//下载图片
    public static final String GETDOCDETAIL = BASE + "/document/findDocDetailByCode.do";//公文详情
    public static final String GETDOCUMENTURL = BASE + "/document/downloadDoc.do";//下载文件
    public static final String GETPUBDOCURL = BASE + "/document/getDocPubPage.do";//获取自己发布的公文
    public static final String GETDOCDETAILDATA = BASE + "/document/getDocDetailData.do";//获取详情
    public static final String SendAppr = BASE + "/document/sendAppr.do";//送审
    public static final String DELETEPUBDOC = BASE+"/document/deletePubDoc.do";//删除发布的公文
}
