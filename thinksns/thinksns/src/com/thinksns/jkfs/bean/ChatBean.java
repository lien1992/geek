package com.thinksns.jkfs.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by mosl on 14-5-24.
 */
public class ChatBean {

    public  String list_id;
    public  String member_uid;
    public  String isnew;
    public  String message_num;
    public  String ctime;
    public  String list_ctime;
    public  String from_uid;
    public  String type;
    public  String tittle;
    public  String member_num;
    public  String min_max;
    public  String mtime;
    public  List<MessageChat> listMessage;
    public  String content;
    public  String from_uname;
    public  String from_face;
    public String timestmap;

    class MessageChat{
        private String from_uid;
        private String content;
        private String to_uid;
    }

    public  static ChatBean JsonToBean(JSONObject obj){
        ChatBean chatBean=new ChatBean();
        chatBean.list_id=obj.optString("list_id");
        chatBean.member_uid=obj.optString("member_uid");
        chatBean.isnew=obj.optString("new");
        chatBean.message_num=obj.optString("message_num");
        chatBean.ctime=obj.optString("ctime");
        chatBean.list_ctime=obj.optString("list_ctime");
        chatBean.from_uid=obj.optString("from_uid");
        chatBean.type=obj.optString("type");
        chatBean.tittle=obj.optString("tittle");
        chatBean.message_num=obj.optString("message_num");
        chatBean.min_max=obj.optString("min_max");
        chatBean.mtime=obj.optString("mtime");
        chatBean.content=obj.optString("content");
        chatBean.from_uname=obj.optString("from_uname");
        chatBean.from_face=obj.optString("from_face");
        chatBean.timestmap=obj.optString("timestmap");

        return chatBean;
    }


}
