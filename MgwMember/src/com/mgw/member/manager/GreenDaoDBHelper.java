package com.mgw.member.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mgw.member.DaoSession;
import com.mgw.member.NoteDao;
import com.mgw.member.RememberLoginName;
import com.mgw.member.RememberLoginNameDao;
import com.mgw.member.RememberLoginNameDao.Properties;

import de.greenrobot.dao.query.QueryBuilder;

public class GreenDaoDBHelper
{
    private static Context mContext;
    private static GreenDaoDBHelper instance;
    private NoteDao noteDao;                                                                                                                                                                                                                                                                                                                   
    public RememberLoginNameDao rememberLoginNameDao;                                                                                                                                                                                                                                                                                                                   
                                                                                                                                                                                                                                                                                                                                   
    private GreenDaoDBHelper()
    {
    }
                                                                                                                                                                                                                                                                                                                                   
    public static GreenDaoDBHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new GreenDaoDBHelper();
            if (mContext == null)
            {
                mContext = context;
            }
                                                                                                                                                                                                                                                                                                                                           
            // 数据库对象
            DaoSession daoSession = BaseApplication.getDaoSession(mContext);
            instance.noteDao = daoSession.getNoteDao();
            instance.rememberLoginNameDao = daoSession.getRememberLoginNameDao();
        }
        return instance;
    }
//                                                                                                                                                                                                                                                                                                                                   

    /** 添加登录信息数据 
     * 
     * 如果存在此条数据覆盖
     * 
     * 
     * */
    public void addRememberLoginNameDao(RememberLoginName item)
    {
    	
    	if(item.getIsRemember()){
    		rememberLoginNameDao.insert(item);
    	}else{
    		item.setLoginPassword("");
//    		rememberLoginNameDao.insert(item);
    		
    	if(isSavedRememberLoginName(item.getLoginUserName())){
    		
    	QueryBuilder<RememberLoginName> queryBuilder = rememberLoginNameDao.queryBuilder();
    	List<RememberLoginName> list = queryBuilder.where(Properties.LoginUserName.eq(item.getLoginUserName())).list();
    	RememberLoginName rememberLoginName = list.get(0);
    	rememberLoginName.setLastUseTime(item.getLastUseTime());
    	rememberLoginNameDao.update(rememberLoginName);
    	
    	}else{
    		rememberLoginNameDao.insertOrReplace(item);
    	}
    		
    		
    		
    	}
    }
    
    /** 查询 */
    public boolean isSavedRememberLoginName(String  username)
    {
        QueryBuilder<RememberLoginName> qb = rememberLoginNameDao.queryBuilder();
        qb.where(Properties.LoginUserName.eq(username));
        qb.buildCount().count();
        return qb.buildCount().count() > 0 ? true : false;// 查找收藏表
    }
    
                                                                                                                                                                                                                                                                                                                                   
    /** 查询登录用户名 */
    public List<String> getRememberLoginName()
    {
    	
    	
        QueryBuilder<RememberLoginName> qb = rememberLoginNameDao.queryBuilder();
        qb.orderAsc(Properties.LastUseTime);
        List<RememberLoginName> list = qb.list();
        
        
        List<String> listname = new ArrayList<String>();
        
        
        for(RememberLoginName ssd: list){
        	
        if(!listname.contains(ssd.getLoginUserName())){
        	listname.add(ssd.getLoginUserName());
        }
        	
        }
        
        return listname;
    }

//                                                                                                                                                                                                                                                                                                                                   
//    /** 查询 */
//    public List<CityInfo> getCityInfo()
//    {
//        return cityInfoDao.loadAll();// 查找图片相册
//    }
//                                                                                                                                                                                                                                                                                                                                   
//    /** 查询 */
//    public boolean isSaved(int Id)
//    {
//        QueryBuilder<CityInfo> qb = cityInfoDao.queryBuilder();
//        qb.where(Properties.Id.eq(Id));
//        qb.buildCount().count();
//        return qb.buildCount().count() > 0 ? true : false;// 查找收藏表
//    }
//                                                                                                                                                                                                                                                                                                                                   
//    /** 删除 */
//    public void deleteCityInfoList(int Id)
//    {
//        QueryBuilder<CityInfo> qb = cityInfoDao.queryBuilder();
//        DeleteQuery<CityInfo> bd = qb.where(Properties.Id.eq(Id)).buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//                                                                                                                                                                                                                                                                                                                                   
//    /** 删除 */
//    public void clearCityInfo()
//    {
//        cityInfoDao.deleteAll();
//    }
//                                                                                                                                                                                                                                                                                                                                   
//    /** 通过城市id查找其类型id */
//    public int getTypeId(int cityId)
//    {
//        QueryBuilder<CityInfo> qb = cityInfoDao.queryBuilder();
//        qb.where(Properties.Id.eq(cityId));
//        if (qb.list().size() > 0)
//        {
//            return qb.list().get(0).getTypeId();
//        }
//        else
//        {
//            return 0;
//        }
//    }
//                                                                                                                                                                                                                                                                                                                                   
//    /** 多重查询 */
//    public List<CityInfo> getIphRegionList(int cityId)
//    {
//        QueryBuilder<CityInfoDB> qb = cityInfoDao.queryBuilder();
//        qb.where(qb.and(Properties.CityId.eq(cityId), Properties.InfoType.eq(HBContant.CITYINFO_IR)));
//        qb.orderAsc(Properties.Id);// 排序依据
//        return qb.list();
//    }
}