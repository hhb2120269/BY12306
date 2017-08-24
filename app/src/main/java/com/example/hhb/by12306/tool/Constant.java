package com.example.hhb.by12306.tool;

/**
 * Created by hhb on 17/8/7.
 */

public class Constant {
    /** debug模式控制 **/
    final public static boolean __IS_DEBUG__=true;
    final public static boolean __IS_FAKE_DATA__=true;


    final public static int ERROR = 110;
    final public static int SOAP_UNSUCCESS = 101;

    final public static int UPDATA_CLIENT = 102;
    final public static int GET_UNDATAINFO_ERROR = 103;
    final public static int DOWN_ERROR = 104;

    final public static int TASK_UPDATE = 105;//更新task
    final public static int START_TASK = 106;//
    final public static int END_TASK = 107;//
    final public static int GET_IP = 108;//




    final public static int MAIN = 1999;
    final public static int LOAD_ORDERS = 2002;
    final public static int LOAD_TASKS = 2000;

    final public static int LOAD_TASK_DETAIL = 2001;
    final public static int SIGN_ORDER = 2003;
    final public static int LOGIN = 2004;
    final public static int SIGN_BY_ORDERCODE = 2005;
    final public static int LOAD_MSG = 2006;

    final public static long AUTO_DELAY = 20000;//自动刷新时间


    final public static int EXIT_LOGOUT = 99;//自动刷新时间

    final public static int CLOSE = 1901;//自动刷新时间
    final public static int DEFAULT = 1902;//自动刷新时间
    final public static int FINISHED = 1903;//自动刷新时间
    final public static int UNFINISHED = 1904;//自动刷新时间
}
