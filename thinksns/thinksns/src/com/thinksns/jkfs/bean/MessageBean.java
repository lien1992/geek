package com.thinksns.jkfs.bean;

import org.json.JSONObject;

import java.util.List;


/**
 * @author 邓思宇
 *
 *	私信内容的信息Bean
 */
public class MessageBean {

    public  String message_id;
    public  String list_id;
    public  String from_uid;
    public  String content;
    public  String is_del;
    public  String mtime;
    public  String from_uname;
    public  String from_face;
    public  String timestamp;
    public  String ctime;
    

    public  static MessageBean JsonToBean(JSONObject obj){
        MessageBean mBean=new MessageBean();
        mBean.message_id=obj.optString("message_id");
        mBean.list_id=obj.optString("list_id");
        mBean.from_uid=obj.optString("from_uid");
        mBean.content=obj.optString("content");
        mBean.is_del=obj.optString("is_del");
        mBean.mtime=obj.optString("mtime");
        mBean.from_uname=obj.optString("from_uname");
        mBean.from_face=obj.optString("from_face");
        mBean.timestamp=obj.optString("timestamp");
        mBean.ctime=obj.optString("ctime");
        return mBean;
    }


}
