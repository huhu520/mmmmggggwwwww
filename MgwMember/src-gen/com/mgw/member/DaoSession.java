package com.mgw.member;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.mgw.member.Note;
import com.mgw.member.TopUser;
import com.mgw.member.RememberLoginName;

import com.mgw.member.NoteDao;
import com.mgw.member.TopUserDao;
import com.mgw.member.RememberLoginNameDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;
    private final DaoConfig topUserDaoConfig;
    private final DaoConfig rememberLoginNameDaoConfig;

    private final NoteDao noteDao;
    private final TopUserDao topUserDao;
    private final RememberLoginNameDao rememberLoginNameDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        topUserDaoConfig = daoConfigMap.get(TopUserDao.class).clone();
        topUserDaoConfig.initIdentityScope(type);

        rememberLoginNameDaoConfig = daoConfigMap.get(RememberLoginNameDao.class).clone();
        rememberLoginNameDaoConfig.initIdentityScope(type);

        noteDao = new NoteDao(noteDaoConfig, this);
        topUserDao = new TopUserDao(topUserDaoConfig, this);
        rememberLoginNameDao = new RememberLoginNameDao(rememberLoginNameDaoConfig, this);

        registerDao(Note.class, noteDao);
        registerDao(TopUser.class, topUserDao);
        registerDao(RememberLoginName.class, rememberLoginNameDao);
    }
    
    public void clear() {
        noteDaoConfig.getIdentityScope().clear();
        topUserDaoConfig.getIdentityScope().clear();
        rememberLoginNameDaoConfig.getIdentityScope().clear();
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public TopUserDao getTopUserDao() {
        return topUserDao;
    }

    public RememberLoginNameDao getRememberLoginNameDao() {
        return rememberLoginNameDao;
    }

}
