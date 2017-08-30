package com.example.hhb.by12306.tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hhb.by12306.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhb on 17/6/16.
 */

public class DBManager {
    private static MySQLiteHelper helper;
    public synchronized static MySQLiteHelper getInstance(Context context){
        if(helper == null){
            helper = new MySQLiteHelper(context);
        }
        return helper;
    }

    /**
     * 执行sql语句
     * @param db
     * @param sql
     */
    public static void execSQL(SQLiteDatabase db, String sql){
        if(db != null){
            if(sql != null && !"".equals(sql)){
                db.execSQL(sql);
            }
        }
    }

    /**
     *
     * @param db
     * @param sql
     * @param selecttionArgs
     * @return
     */
    public static Cursor selectDataBySql(SQLiteDatabase db, String sql, String[] selecttionArgs){
        Cursor cursor = null;
        if(db!= null){
            cursor = db.rawQuery(sql,selecttionArgs);
        }
        return cursor;
    }

    /**
     * 返回user对象
     */
    public static List<User> cursorToList(Cursor cursor){
        List<User> list= new ArrayList<User>();
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int index = cursor.getColumnIndex(DBConstant._ID);
            int _id = cursor.getInt(index);

            User user = new User();
            user.setDuty(cursor.getString(cursor.getColumnIndex(DBConstant.DUTY)));
//            user.setFingerList(cursor.getString(cursor.getColumnIndex(DBConstant.FINGER_LIST)));
            user.setId(cursor.getLong(cursor.getColumnIndex(DBConstant.ID)));
            user.setLimit(cursor.getLong(cursor.getColumnIndex(DBConstant.LIMIT)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(DBConstant.PASSWORD)));
//            user.setPopedomSet(cursor.getString(cursor.getColumnIndex(DBConstant.POPEDOM_SET)));
            user.setSimpleRole(cursor.getString(cursor.getColumnIndex(DBConstant.SIMPLE_ROLE)));
            user.setPhoneNum(cursor.getString(cursor.getColumnIndex(DBConstant.TEL)));
            user.setLastLogin(cursor.getLong(cursor.getColumnIndex("lastLogin")));
            user.setPrivilege(cursor.getString(cursor.getColumnIndex("Privilege")));
            user.setWorkerCode(cursor.getString(cursor.getColumnIndex(DBConstant.WORKER_CODE)));
            user.setWorkName(cursor.getString(cursor.getColumnIndex(DBConstant.WORK_NAME)));
            user.setWorkStation(cursor.getString(cursor.getColumnIndex(DBConstant.WORK_STATION)));
            user.setWorkType(cursor.getString(cursor.getColumnIndex(DBConstant.WORK_TYPE)));

            list.add(user);
        }
        return list;
    }
}
