package com.example.hhb.by12306.tool;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.hhb.by12306.model.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Created by hhb on 17/8/8.
 */

public enum  Util {
    INSTANCE;
    public Boolean isOnLoading = false;//

    private User user;


//    /**
//     * * 比较两个list内数据，并保留展示数据的展示状态
//     * @param orgin 当前展示的数据
//     * @param newList 新数据
//     * @return 包含当前显示状态的新数据
//     */
//    public static List<Plan> comparePlanList(List<Plan> orgin, List<Plan> newList){
//        for(int i = 0; i < orgin.size(); i++)
//        {
//            Plan orgPlan = orgin.get(i);
//            for(int j = 0; j < newList.size(); j++)
//            {
//                Plan newPlan = newList.get(j);
//                newPlan.setIsnew(true);
//                //如果计划号 和 计划类型相同－－认为是统一条数据
//                if(orgPlan.getCode() == newPlan.getCode() && orgPlan.getPlanType() == newPlan.getPlanType()){
//                    orgPlan.setIsnew(false);
//                    newList.set(j,orgPlan);
//                }
//            }
//        }
//        return newList;
//    }

    /**
     * 比较两个函数的比特长度
     * @param obj1
     * @param obj2
     * @return  null if IOException; true if equal; false if unequal
     */
    public static boolean compareObjectByteSize(Object obj1,Object obj2)throws IOException,Exception {
        try{
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(baos1);
            oos1.writeObject(obj1);

            byte[] byte1 = baos1.toByteArray();

            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
            oos2.writeObject(obj2);

            byte[] byte2 = baos2.toByteArray();

            int i1 = byte1.length;
            int i2 = byte2.length;

            if(i1 == i2){
                return true;
            }else{
                if(i1>i2){
                    return false;
                }else{
                    return false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * 执行某对象的方法
     * @param owner
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {

        Class ownerClass = owner.getClass();

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName,argsClass);

        return method.invoke(owner, args);
    }

    /**
     * 获取xml 中的string  转为数值int
     * @param mContext
     * @param id
     * @return 返回 int
     */
    public static int IntFromString(Context mContext, int id){
        int result = 0;
        try{
            String str = mContext.getResources().getString(id);
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取assets文件中的json数据
     * @param mContext
     * @param fileName
     * @return  返回String类型
     */
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    /**
     * planDetail中  显示用String－value的获取方法
     * @param field
     * @param obj
     * @return
     */
    public static String invokeToGetString(Field field, Object obj){
        String result = null;
        String key = field.getName();
        String name = key.substring(0, 1).toUpperCase() + key.substring(1); // 将属性的首字符大写，方便构造get，set方法
        String type = field.getGenericType().toString(); // 获取属性的类型
        if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = null;
            try {
                m = obj.getClass().getMethod("get" + name);
                result = (String) m.invoke(obj); // 调用getter方法获取属性值
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("class java.lang.Integer")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = null;
            try {
                m = obj.getClass().getMethod("get" + name);
                Integer i = (Integer) m.invoke(obj);
                result = i.toString();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
    /**
     * 遍历Object 对其进行制定操作
     * @param object
     * @return
     */
    public static Object dispose(Object object){
        Field[] field = object.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = object.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(object); // 调用getter方法获取属性值
                    //.....处理开始........
//                    执行处理方法
                    //.....处理结束........
                    m = object.getClass().getMethod("set"+name,String.class);
//                    m.invoke(object, t_value);
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(object);
                    if (value == null) {
                        m = object.getClass().getMethod("set"+name,Integer.class);
                        m.invoke(object, 1);
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Boolean value = (Boolean) m.invoke(object);
                    if (value == null) {
                        m = object.getClass().getMethod("set"+name,Boolean.class);
                        m.invoke(object, false);
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = object.getClass().getMethod("get" + name);
                    Date value = (Date) m.invoke(object);
                    if (value == null) {
                        m = object.getClass().getMethod("set"+name,Date.class);
                        m.invoke(object, new Date());
                    }
                }
                // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return object;
    }
    /**  getter  and  setter  **/

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
