package com.example.hhb.by12306.tool;

/**
 * Created by hhb on 17/8/7.
 */

public class Constant {
    /** debug模式控制 **/
    final public static boolean __IS_DEBUG__=true;
    final public static boolean __IS_FAKE_DATA__=false;


    final public static String MSG_REFRESH_HOURS = "3";

    final public static int ERROR = 110;
    final public static int SOAP_UNSUCCESS = 101;

    final public static int UPDATA_CLIENT = 102;
    final public static int GET_UNDATAINFO_ERROR = 103;
    final public static int DOWN_ERROR = 104;

    final public static int TASK_UPDATE = 105;//更新task
    final public static String TASK_SIGN = "TASK_SIGN";//sign task
    final public static String TASK_UNSIGN = "TASK_UNSIGN";//unsign task
    final public static int START_TASK = 106;//
    final public static int END_TASK = 107;//
    final public static int GET_IP = 108;//




    final public static int MAIN = 1999;
    final public static int LOAD_ORDERS = 2002;
    final public static int LOAD_TASKS = 2000;

    final public static int LOAD_TASK_DETAIL = 2001;
    final public static int LOGIN = 2004;
    final public static int LOAD_MSG = 2006;
    final public static int LOAD_TASK_MSG = 2007;
    final public static int LOAD_TASK_SIGN = 2008;

    final public static long AUTO_DELAY = 5000;//自动刷新时间


    final public static int EXIT_LOGOUT = 99;

    final public static String CLOSE = "CLOSE";//menu filter
    final public static String DEFAULT = "DEFAULT";//menu filter
    final public static String STANDBY= "STANDBY";//menu filter
    final public static String FINISHED = "FINISHED";//menu filter
    final public static String UNFINISHED = "UNFINISHED";//menu filter

}
