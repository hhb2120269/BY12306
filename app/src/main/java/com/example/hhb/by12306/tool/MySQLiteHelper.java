package com.example.hhb.by12306.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hhb.by12306.model.ResponseObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhb on 17/6/16.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    /**
     *
     * @param context 上下文
     * @param name  创建数据库的名称
     * @param factory   游标工厂
     * @param version 标示创建数据库版本 >=1
     */
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteHelper(Context context) {
        super(context, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB","----DB onCreate---");

//        db.execSQL(
//                "create table response("+
//                        "obj varchar(50000),"+
//                        "isSuccess varchar(20),"+
//                        "message varchar(100)"+
//                        ")"
//        );
//        db.execSQL(
//                "create table user("+
////                DBConstant._ID+" integer PRIMARY KEY autoincrement,"+     // FIXME: 17/6/20 主键
//                DBConstant.DUTY+" varchar(20),"+
//                DBConstant.FINGER_LIST+" varchar(20),"+
//                DBConstant.ID+" Integer,"+                  // FIXME: 17/6/20 long类型
//                DBConstant.LIMIT+" Integer,"+               // FIXME: 17/6/20 long类型
//                DBConstant.PASSWORD+" varchar(20),"+
//                DBConstant.POPEDOM_SET+" varchar(20),"+
//                DBConstant.SIMPLE_ROLE+" varchar(20),"+
//                DBConstant.TEL+" varchar(20),"+
//                DBConstant.WORK_CODE+" varchar(20),"+
//                DBConstant.WORK_NAME+" varchar(20),"+
//                DBConstant.WORK_STATION+" varchar(20),"+
//                DBConstant.WORK_TYPE+" varchar(20)"+
//                ")"
//        );
//        /**待发送／签收的命令**/
        db.execSQL(
                "create table tofinish( " +
                        "code varchar(100), " +
                        "workCode varchar(100), " +
                        "workName varchar(20), " +
                        "orderCode varchar(100), " +
                        "planDate varchar(20), " +
                        "planType varchar(20))"
        );

        db.execSQL(
                "CREATE TABLE plans (code VARCHAR(20), " +
                        "doSection VARCHAR(20)," +
                        "planDate VARCHAR(20)," +
                        "planType VARCHAR(20)," +
                        "timeMinute VARCHAR(20)," +
                        "constructItem VARCHAR(4000)," +
                        "requestCondtion VARCHAR(4000))"
        );

//        /** cache **/
//        db.execSQL(
//                "create table if not exists "+DBConstant.DATABASE_TABLE_DEFAULT+
//                        "(" +
//                        "_id integer primary key autoincrement, " +
//                        "key VARCHAR(20), " +
//                        "classtabledata text" +
//                        ")"
//        );
    }


    /**
     *
     * @param db
     * @param oldVersion 数据库旧版本号
     * @param newVersion 数据库旧版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB","----DB onUpgrade---:"+oldVersion+" to "+newVersion);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d("DB","----DB OnOpen---");
        super.onOpen(db);
    }



    /**
     * 保存
     * @param
     */
    public boolean saveObject(String key, ResponseObject resObj) {
        boolean isSuccess = false;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(resObj);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("insert into "+DBConstant.DATABASE_TABLE_DEFAULT+"(key,classtabledata) values(?,?)", new Object[] { key,data });
            database.close();
            isSuccess = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 读取
     * @param key
     * @return
     */
    public ResponseObject getObject( String key) {
        ResponseObject resObj = null;
        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor cursor = database.rawQuery("select * from "+DBConstant.DATABASE_TABLE_DEFAULT+" where key="+key, null);
        Cursor cursor = database.rawQuery("select * from "+DBConstant.DATABASE_TABLE_DEFAULT, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex("classtabledata"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    Object object = inputStream.readObject();
                    resObj = (ResponseObject) object;
                    inputStream.close();
                    arrayInputStream.close();
                    break;//这里为了测试就取一个数据
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("DB","----DB delete error---"+DBConstant.DATABASE_TABLE_DEFAULT);

                }

            }
        }
        return resObj;

    }

    /**
     * 删除表内数据
     * @param
     */
    public Boolean deleteDataAll() {
        Boolean isSuccess = false;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("delete * from "+DBConstant.DATABASE_TABLE_DEFAULT);
            database.close();
            isSuccess = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DB","----DB delete error---"+DBConstant.DATABASE_TABLE_DEFAULT);
        }
        return isSuccess;
    }




//    /**-------------------------- classplan -----------------------------**/
//    /**
//     * 将classplan集合入库
//     * @param planList
////     */
//    public void savePlanList(List<Plan> planList){
//        try{
//            SQLiteDatabase db = getWritableDatabase();//
//            if(planList!=null) {
//                ContentValues values = new ContentValues();
//                for(int i=0;i<planList.size();i++){
//                    values.put("code",""+planList.get(i).getCode() );  //车次
//                    values.put("constructItem",planList.get(i).getConstructItem() );  //任务ID @WGary 开行计划唯一值，过滤晚点跨天情况干扰
//                    values.put("doSection",planList.get(i).getDoSection() );//始发站
//                    values.put("planDate",planList.get(i).getPlanDate() );     //终到站
//                    values.put("planType",""+planList.get(i).getPlanType() );  //本站到达时间
//                    values.put("requestCondtion",planList.get(i).getRequestCondtion() );  //本站出发时间
//                    values.put("timeMinute",planList.get(i).getTimeMinute() );  //本站图定到点
//
//                    long result = db.insert("plans",null,values);
//                    Log.d("","result:"+result);
//                }
//                //   db.insert("classplan",null,values);
//            }
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


//    /**
//     * 根据code查找plan对象集合
//     * @param code
//     */
//    public List<Plan> queryPlan(String code){
//        List<Plan> list = new ArrayList<Plan>();
//        try{
//            SQLiteDatabase db = getReadableDatabase();//
//            //query(String table,String []Columns, String selection, String[]selectionArgs, String having, string　orderBy);
//            /**
//             *
//             table：表名。相当于select语句from关键字后面的部分。如果是多表联合查询，可以用逗号将两个表名分开。
//             columns：要查询出来的列名。相当于select语句select关键字后面的部分。
//             selection：查询条件子句，相当于select语句where关键字后面的部分，在条件子句允许使用占位符“?”
//             selectionArgs：对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
//             groupBy：相当于select语句group by关键字后面的部分
//             having：相当于select语句having关键字后面的部分
//             orderBy：相当于select语句order by关键字后面的部分，如：personid desc, age asc;
//             limit：指定偏移量和获取的记录数，相当于select语句limit关键字后面的部分。
//             */
////            Cursor cursor = db.query("plans", null, "code=?", new String[]{code}, null, null, null);
//            Cursor cursor = db.rawQuery("select * from plans", null);
//            if (cursor.moveToNext()){
//                Plan plan = new Plan();
//                plan.setCode(Long.parseLong(cursor.getString(cursor.getColumnIndex("code"))));
//                plan.setConstructItem(cursor.getString(cursor.getColumnIndex("constructItem")));
//                plan.setDoSection(cursor.getString(cursor.getColumnIndex("doSection")));
//                plan.setPlanDate(cursor.getString(cursor.getColumnIndex("planDate")));
//                plan.setPlanType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("planType"))));
//                plan.setRequestCondtion(cursor.getString(cursor.getColumnIndex("requestCondtion")));
//                plan.setTimeMinute(cursor.getString(cursor.getColumnIndex("timeMinute")));
//
//                list.add(plan);
//            }
//            cursor.close();//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }

    /**
     * 删除planlist
     * @param
     */
    public void deletePlanList(){
        try{
            SQLiteDatabase db = getWritableDatabase();//
            db.execSQL("delete * from plans");
            db.close();
//            db.delete("classplan", "jobId=?", new String[]{
//                    jobId
//            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

//
//
///**-------------------------- orderToSign -----------------------------**/
//    /**
//     * 将orderneedsign集合入库
//     * @param orderToSign
//     */
//    public void saveOrderNeedSign(OrderToSign orderToSign){
//        try{
//            SQLiteDatabase db = getWritableDatabase();//
//            if(orderToSign!=null) {
//                ContentValues values = new ContentValues();
////                for(int i=0;i<planList.size();i++){
//                    values.put("code",orderToSign.getCode() );
//                    values.put("orderCode",orderToSign.getOrderCode() );
//                    values.put("planDate",orderToSign.getPlanDate());
//                    values.put("planType",orderToSign.getPlanType());
//                    values.put("workCode",orderToSign.getWorkCode());
//                    values.put("workName",orderToSign.getWorkName());
//
//                long result = db.insert("tosign",null,values);
//                Log.d("","result:"+result);
//            }
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 查找orderNeedToSign对象集合
//     * @param
//     */
//    public List<OrderToSign> queryOrderToSign(){
//        List<OrderToSign> list = new ArrayList<OrderToSign>();
//        try{
//            SQLiteDatabase db = getReadableDatabase();//
//            Cursor cursor = db.rawQuery("select * from tosign", null);
//            if (cursor.moveToNext()){
//                OrderToSign orderToSign = new OrderToSign();
//                orderToSign.setCode(cursor.getString(cursor.getColumnIndex("code")));
//                orderToSign.setOrderCode(cursor.getString(cursor.getColumnIndex("orderCode")));
//                orderToSign.setPlanDate(cursor.getString(cursor.getColumnIndex("planDate")));
//                orderToSign.setPlanType(cursor.getString(cursor.getColumnIndex("planType")));
//                orderToSign.setWorkCode(cursor.getString(cursor.getColumnIndex("workCode")));
//                orderToSign.setWorkName(cursor.getString(cursor.getColumnIndex("workName")));
//
//                list.add(orderToSign);
//            }
//            cursor.close();//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }
//    /**
//     * 删除planlist
//     * @param
//     */
//    public void deleteOrderToSign(String orderCode){
//        try{
//            SQLiteDatabase db = getWritableDatabase();//
////            db.execSQL("delete from order where orderCode="+orderCode);
//            db.delete("tosign", "orderCode=?", new String[]{orderCode});
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

//    /**
//     * 根据jobid查找classplan对象集合
//     * @param jobId
//     */
//    public List<Plan> queryClassPlan(String jobId){
//        List<Plan> list = new ArrayList<Plan>();
//        try{
//            SQLiteDatabase db = getReadableDatabase();//
//            //query(String table,String []Columns, String selection, String[]selectionArgs, String having, string　orderBy);
//            /**
//             *
//             table：表名。相当于select语句from关键字后面的部分。如果是多表联合查询，可以用逗号将两个表名分开。
//             columns：要查询出来的列名。相当于select语句select关键字后面的部分。
//             selection：查询条件子句，相当于select语句where关键字后面的部分，在条件子句允许使用占位符“?”
//             selectionArgs：对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
//             groupBy：相当于select语句group by关键字后面的部分
//             having：相当于select语句having关键字后面的部分
//             orderBy：相当于select语句order by关键字后面的部分，如：personid desc, age asc;
//             limit：指定偏移量和获取的记录数，相当于select语句limit关键字后面的部分。
//             */
//            Cursor cursor = db.query("classplan", null, "jobId=?", new String[]{
//                    jobId
//            }, null, null, null);
//            //
//            if (cursor.moveToNext()){
//                ClassPlan classplan = new ClassPlan();
//                classplan.setJobId(cursor.getString(cursor.getColumnIndex("jobId")));
//                classplan.setTrainNo(cursor.getString(cursor.getColumnIndex("trainNo")));
//                classplan.setStartStn(cursor.getString(cursor.getColumnIndex("startStn")));
//                classplan.setArrStn(cursor.getString(cursor.getColumnIndex("arrStn")));
//                classplan.setLocalArrTime(cursor.getString(cursor.getColumnIndex("localArrTime")));
//                classplan.setLocalStartTime(cursor.getString(cursor.getColumnIndex("localStartTime")));
//                classplan.setLocalArrTimeMap(Long.parseLong(cursor.getString(cursor.getColumnIndex("localStartTime"))) );
//                classplan.setLocalStartTimeMap(Long.parseLong(cursor.getString(cursor.getColumnIndex("localStartTimeMap"))) );
//                classplan.setCurTrack(cursor.getString(cursor.getColumnIndex("curTrack")));
//                classplan.setTrack(cursor.getString(cursor.getColumnIndex("track")));
//                classplan.setVaryTrack(cursor.getString(cursor.getColumnIndex("varyTrack")));
//                classplan.setDy(Integer.parseInt(cursor.getString(cursor.getColumnIndex("dy"))));
//                classplan.setTrainType(cursor.getString(cursor.getColumnIndex("trainType")));
//                classplan.setThread(cursor.getString(cursor.getColumnIndex("thread")));
//                classplan.setDirection(Integer.parseInt(cursor.getString(cursor.getColumnIndex("direction"))) );
//                classplan.setStopFlag(Integer.parseInt(cursor.getString(cursor.getColumnIndex("stopFlag"))) );
//                classplan.setJxTrainNo(cursor.getString(cursor.getColumnIndex("jxTrainNo")));
//                classplan.setCurBzm(cursor.getString(cursor.getColumnIndex("curBzm")));
//                classplan.setBzm(cursor.getString(cursor.getColumnIndex("bzm")));
//                classplan.setVaryBzm(cursor.getString(cursor.getColumnIndex("varyBzm")));
//                classplan.setWd(cursor.getString(cursor.getColumnIndex("wd")));
//                classplan.setRunFlag(Integer.parseInt(cursor.getString(cursor.getColumnIndex("runFlag"))) );
//                classplan.setNeighbourStartTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("neighbourStartTime"))) );
//                classplan.setAllRcvStr(cursor.getString(cursor.getColumnIndex("allRcvStr")));
//                classplan.setJobState(Integer.parseInt(cursor.getString(cursor.getColumnIndex("jobState"))) );
//                classplan.setFloor1(cursor.getString(cursor.getColumnIndex("floor1")));
//                classplan.setFloor2(cursor.getString(cursor.getColumnIndex("floor2")));
//                classplan.setFloor3(cursor.getString(cursor.getColumnIndex("floor3")));
//                classplan.setPrveStn(cursor.getString(cursor.getColumnIndex("prveStn")));
//                classplan.setPrveStnStartTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("prveStnStartTime"))) );
//                classplan.setLocalFactStartTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("localFactStartTime"))) );
//                classplan.setLocalFactArrTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("localFactArrTime"))) );
//                classplan.setPivotProceeding(cursor.getString(cursor.getColumnIndex("pivotProceeding")));
//
//                list.add(classplan);
//            }
//            cursor.close();//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }

}
