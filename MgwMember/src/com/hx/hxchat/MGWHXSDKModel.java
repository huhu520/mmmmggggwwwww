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

import java.util.List;
import java.util.Map;

import android.content.Context;

import com.hx.applib.model.DefaultHXSDKModel;
import com.hx.hxchat.db.DbOpenHelper;
import com.hx.hxchat.db.TopUserDao;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.domain.User;

public class MGWHXSDKModel extends DefaultHXSDKModel{

    public MGWHXSDKModel(Context ctx) {
        super(ctx);
    }

    // demo will use HuanXin roster
    public boolean getUseHXRoster() {
        return true;
    }
    
    // demo will switch on debug mode
    public boolean isDebugMode(){
        return true;
    }
    
    public boolean saveContactList(List<User> contactList) {
        UserDao dao = new UserDao(context);
        dao.saveContactList(contactList);
        return true;
    }

    public Map<String, User> getContactList() {
        UserDao dao = new UserDao(context);
        return dao.getContactList();
    }
    public Map<String, TopUser> getTopUserList() {
        // TODO Auto-generated method stub
        TopUserDao dao = new TopUserDao(context);
        return dao.getTopUserList();
    }
    public boolean saveTopUserList(List<TopUser> contactList) {
        // TODO Auto-generated method stub
        TopUserDao dao = new TopUserDao(context);
        dao.saveTopUserList(contactList);
        return true;
    }
     
    public void closeDB() {
        DbOpenHelper.getInstance(context).closeDB();
    }
    
    @Override
    public String getAppProcessName() {
        return context.getPackageName();
    }
}
