/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hx.hxchat;

import java.util.Map;

import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.hx.applib.controller.HXSDKHelper;
import com.hx.applib.model.HXSDKModel;
import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.receiver.CallReceiver;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.constant.Define_C;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.uitls.UIUtils;


/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * @author easemob
 *
 */
public class MGWHXSDKHelper extends HXSDKHelper{

    /**
     * contact list in cache
     */
    private Map<String, User> contactList;
    private Map<String, TopUser> topUserList;
    private CallReceiver callReceiver;
    
    @Override
    protected void initHXOptions(){
        super.initHXOptions();
        // you can also get EMChatOptions to set related SDK options
        // EMChatOptions options = EMChatManager.getInstance().getChatOptions();
    }

    @Override
    protected OnMessageNotifyListener getMessageNotifyListener(){
        // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
      return new OnMessageNotifyListener() {

          @Override
          public String onNewMessageNotify(EMMessage message) {
              // 设置状态栏的消息提示，可以根据message的类型做相应提示
              String ticker = CommonUtils.getMessageDigest(message, appContext);
              if(message.getType() == Type.TXT)
                  ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
              return message.getFrom() + ": " + ticker;
          }

          @Override
          public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
              return null;
             // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
          }

          @Override
          public String onSetNotificationTitle(EMMessage message) {
              //修改标题,这里使用默认
              return null;
          }

          @Override
          public int onSetSmallIcon(EMMessage message) {
              //设置小图标
              return 0;
          }
      };
    }
    
    @Override
    protected OnNotificationClickListener getNotificationClickListener(){
        return new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(appContext, ChatActivity.class);
                ChatType chatType = message.getChatType();
                if (chatType == ChatType.Chat) { // 单聊信息
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { // 群聊信息
                            // message.getTo()为群聊id
                    intent.putExtra("groupId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                return intent;
            }
        };
    }
    
    @Override
    protected void onConnectionConflict(){

    	
//		BaseApplication.getApplication().showConflictDialog(MainActivity.mainActivity);
    }
    
    @Override
    protected void onCurrentAccountRemoved(){

    }
    
    
    @Override
    protected void initListener(){
        super.initListener();
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        if(callReceiver == null)
            callReceiver = new CallReceiver();
        appContext.registerReceiver(callReceiver, callFilter);    
    }

    @Override
    protected HXSDKModel createModel() {
        return new MGWHXSDKModel(appContext);
    }
    
    /**
     * get demo HX SDK Model
     */
    public MGWHXSDKModel getModel(){
        return (MGWHXSDKModel) hxModel;
    }
    
    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
    
    	if (getHXId() != null && contactList == null) {
            contactList = ((MGWHXSDKModel) getModel()).getContactList();
        }
    	Define_C.s_shouldflushcontactlist = true;
        return contactList;
    }

    
    
    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }
    /**
     * 获取内存中置顶好友 t
     * 
     * @return
     */
    public Map<String, TopUser> getTopUserList() {
        if (getHXId() != null && topUserList == null) {
            topUserList = ((MGWHXSDKModel) getModel()).getTopUserList();
        }

        return topUserList;
    }

    /**
     * 设置置顶好友到内存中
     * 
     * @param contactList
     */
    public void setTopUserList(Map<String, TopUser> topUserList) {
        this.topUserList = topUserList;
    }
    @Override
    public void logout(final EMCallBack callback){
        endCall();
        super.logout(new EMCallBack(){

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
//                setContactList(null);
                getModel().closeDB();
                
                if(callback != null){
                    callback.onSuccess();
                }
                UIUtils.showToastSafe("成功退出");
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }
            
        });
    }
    
    void endCall(){
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
