package com.example.hhb.by12306.tool;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.hhb.by12306.model.ResponseObject;


import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static com.example.hhb.by12306.tool.Zipper.unGZipObject;

/**
 * Created by hhb on 17/6/8.
 */

public class Soap {

    private static Soap instance = null;

    //        private static final String URL = "http://10.166.41.108:7001/yt46ws/yt46WebService?wsdl";//wyz 41 local
//    private static final String URL = "http://10.166.47.131:7001/yt46ws/yt46WebService?wsdl";//测试 47.131
    private static final String URL = "http://10.166.47.132:7001/yt46ws/yt46WebService?wsdl";//测试 47.131
//    private static final String URL = "http://10.166.47.108:7001/yt46ws/yt46WebService?wsdl";//wyz 47 local
//    private static final String URL = "http://10.160.3.116:7001/yt46ws/yt46WebService?wsdl";//北京测试
//    private static final String URL = "http://10.112.38.111:1004/yt46ws/yt46WebService?wsdl";//济南测试
//    private static final String URL = "http://10.112.38.111:1004/yt46ws/yt46WebService?wsdl";//济南测试
    private static final String URL_STR = "http://10.112.38.111:1004/yt46ws/";//济南服务地址
    private static final String URL_NAME = "yt46WebService";//济南服务名
    private static final String URL_TRANSFER = "http://122.80.61.145:8090/TransferWebService/transferws?wsdl";//济南生产转发地址
    private static final String WebNamespace = "http://ws.yt46.byxx.com/";
    private static final String WebNamespace_TRANSFER = "http://ws.transfer.gtz.byxx.com/";//济南生产转发nampspace
    private static final String ACCEPT_URL_TRANSFER = "http://ws.transfer.gtz.byxx.com/";//济南生产转发nampspace
    private static final String ACCEPT_MODELNAME_TRANSFER = "transferws";//济南生产转发nampspace
    private static final String ACCEPT_METHOD_TRANSFER = "transferMsg";//济南生产转发nampspace
    private static final String ACCEPT_XML_TRANSFER = "http://ws.transfer.gtz.byxx.com/";//济南生产转发nampspace

    private static final int TIME_OUT = 180000;//普通访问超时s
    private static final int TIME_OUT_DOWNLOAD = 80000;//下载访问超时s

    public synchronized static Soap getInstance() {
        if (instance == null) {
            instance = new Soap();
        }
        return instance;
    }

    private Soap() {

    }

    /**
     * interface
     **/
    SoapListener mSoapListener;

    public interface SoapListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         */
        void onSuccess(ResponseObject responseObject, String message);

        void onFault(ResponseObject responseObject, String message, Exception error);
    }

    public void setSoapListener(@Nullable SoapListener listener) {
        mSoapListener = listener;
    }


//    /**
//     * handler 子线程刷新UI
//     */
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 200) {
//            }
//            switch (msg.what) {
//                case Constant.LOGIN:
//                    break;
//                case Constant.LOAD_PLAN:
//                    break;
//                case Constant.LOAD_PLAN_DETAIL:
//                    break;
//                case Constant.LOAD_ORDER:
//                    break;
//                case Constant.SIGN_ORDER:
//                    break;
//                case Constant.SIGN_BY_ORDERCODE:
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        ;
//    };

    public void setContex(Context contex) {
        this.contex = contex;
    }

    private Context contex;


    private String TAG = "Soap";


    /**
     * 版本提示
     *
     * @param
     * @param
     */
    public ResponseObject version()
            throws Exception, ConnectException, SocketTimeoutException {
        // TODO: 17/6/8 填入正确的login方法
//        loadInfo(workCode, password);
        JSONObject data = null;
        String objStr = null;
        ResponseObject result = null;
        try {
            Properties tProperties = new Properties();
//            tProperties.put("workCode", workCode);
//            tProperties.put("password", password);
            result = callBody("checkVersion", tProperties,10000);

//            result = JSON.parseObject(data.toString(), ResponseObject.class);
        } catch (ConnectException e) {
            e.printStackTrace();
            throw e;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (result != null) {
            Log.d("versionInfo", "versionInfo：" + result);
        }
        return result;

    }

    /**
     * login 登录
     *
     * @param workCode
     * @param password
     */
    public ResponseObject login(String workCode, String password)
            throws Exception, ConnectException, SocketTimeoutException {
        // TODO: 17/6/8 填入正确的login方法
//        loadInfo(workCode, password);
        JSONObject data = null;
//        User userinfo = null;
        String objStr = null;
        ResponseObject result = null;
        try {
            Properties tProperties = new Properties();
            tProperties.put("workCode", workCode);
            tProperties.put("password", password);
            result = callBody("login", tProperties);

//            result = JSON.parseObject(data.toString(), ResponseObject.class);
        } catch (ConnectException e) {
            e.printStackTrace();
            throw e;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (result != null) {
            Log.d("login", "login：" + result);
        }
        return result;

    }

    /**
     * 加载planlist
     *
     * @param workCode 工号
     * @param rq       日期
     */
    public ResponseObject loadPlanList(String workCode, String rq)
            throws Exception, ConnectException, SocketTimeoutException {
        JSONObject data = null;
//        List<Plan> planlist = null;
        ResponseObject result = null;
        try {
            Properties tProperties = new Properties();
            tProperties.put("workCode", workCode);
            tProperties.put("rq", rq);
//            tProperties.put("rq", "20170807");
//            tProperties.put("rq", "20170725");
            result = callBody("loadPlan", tProperties);

        } catch (ConnectException e) {
            e.printStackTrace();
            throw e;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (result != null) {
            Log.d("planlist", "planlist：" + result);
            return result;
        } else
            return null;

    }


    /**
     * 心跳监听
     *
     * @param workCode
     * @return
     */
    public ResponseObject sendHeartBeat(String workCode)
            throws Exception, ConnectException, SocketTimeoutException {
        JSONObject data = null;
        ResponseObject result = null;
        try {
            Properties tProperties = new Properties();
            tProperties.put("workCode", workCode);
            result = callBody("heartBeat", tProperties);
//            hb = JSON.parseObject(data.toString(), ResponseObject.class);
            Log.d("heartBeat", "heartBeat:" + result);
        } catch (ConnectException e) {
            e.printStackTrace();
            throw e;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (result != null) {
            Log.d("heartBeat", "heartBeat：" + result);
            return result;
        } else
            return null;

    }


    /**
     * @param method
     * @param pro
     * @return
     * @throws Exception
     */
    private ResponseObject callBody(String method, Properties pro)
            throws Exception, ConnectException, SocketTimeoutException,RuntimeException {
        return callBody(method,pro,TIME_OUT);
    }
    private ResponseObject callBody(String method, Properties pro, int timeout)
            throws Exception, ConnectException, SocketTimeoutException,RuntimeException {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        ResponseObject result = new ResponseObject();//结果

        if (Constant.__IS_DEBUG__) {// 如果是测试环境
            SoapObject tRequest = new SoapObject(WebNamespace, method);
            if (pro != null) {
                Enumeration<Object> tEn = pro.keys();
                String tKey = null;
                while (tEn.hasMoreElements()) {
                    tKey = (String) tEn.nextElement();
                    tRequest.addProperty(tKey, pro.get(tKey));
                }
            }

            envelope.bodyOut = tRequest;
            envelope.dotNet = false;
            (new MarshalBase64()).register(envelope);

            HttpTransportSE hTransport = new HttpTransportSE(URL, timeout);
            if (method != null && method.contains("downloadTimetable")) {
                hTransport = new HttpTransportSE(URL, TIME_OUT_DOWNLOAD);
            }
            hTransport.debug = true;
            String SOAP_ACTION = WebNamespace + method;
            try {
                hTransport.call(SOAP_ACTION, envelope);
            } catch (EOFException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("input流出错");
                return result;
            } catch (ConnectException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("网络连接断开");
                return result;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("网络连接断开");
                return result;
            } catch (RuntimeException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("runtime错误");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("未知错误！");
                return result;
            }
        } else {// 如果是正式环境

            SoapObject tRequest = new SoapObject(WebNamespace_TRANSFER, "getMsg");
            tRequest.addProperty("url", "http://eaccess.jntlj.com:8000/mapping/sgdxj/byxxtransfer/");
            tRequest.addProperty("modelName", "transferws");
            tRequest.addProperty("method", "transferMsg");
            tRequest.addProperty("xmlUrl", "http://ws.transfer.gtz.byxx.com/");

            HashMap insideMap = new HashMap();// // 最里层请求参数map

            if (pro != null) {
                Enumeration<Object> tEn = pro.keys();
                String tKey = null;
                while (tEn.hasMoreElements()) {
                    tKey = (String) tEn.nextElement();
                    insideMap.put(tKey, pro.get(tKey));
                }
            }

            String insideBodyStr = JSON.toJSONString(insideMap);// 最里层请求参数


            HashMap bodyMap = new HashMap();// 转发层请求参数map
            bodyMap.put("url", URL_STR);// 目标服务地址
            bodyMap.put("modelName", URL_NAME);// 目标服务名
            bodyMap.put("xmlUrl", WebNamespace);// 目标服务命名空间
            bodyMap.put("method", method);// 目标服务命名空间
            bodyMap.put("body", insideBodyStr);// 目标服务命名空间

            String bodyStr = JSON.toJSONString(bodyMap);
            tRequest.addProperty("body", bodyStr);

            envelope.bodyOut = tRequest;
            envelope.dotNet = false;
            (new MarshalBase64()).register(envelope);

            HttpTransportSE hTransport = new HttpTransportSE(URL_TRANSFER, timeout);
            if (method != null && method.contains("downloadTimetable")) {
                hTransport = new HttpTransportSE(URL_TRANSFER, TIME_OUT_DOWNLOAD);
            }
            hTransport.debug = true;
            String SOAP_ACTION = WebNamespace_TRANSFER + "getMsg";
            try {
                hTransport.call(SOAP_ACTION, envelope);
            } catch (EOFException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("input流出错");
                return result;
            } catch (ConnectException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("网络连接断开");
                return result;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("网络连接断开");
                return result;
            } catch (RuntimeException e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("runtime错误");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.setError(e);
                result.setMessage("未知错误！");
                return result;
            }
        }

        try {
            SoapPrimitive tSoapPrimitive = null;
            Object object = envelope.getResponse();
            Object objBuf = null;
            String strBuf = null;
            if (object != null && object instanceof SoapPrimitive) {
                SoapPrimitive response = (SoapPrimitive) object;
                byte[] bytes = Base64.decode(response.toString());
                objBuf = unGZipObject(bytes);
//            strBuf =objBuf.toString(); //
//            ResponseObject responseObject = JSON.parseObject(strBuf,ResponseObject.class);
//            if(responseObject != null && responseObject.isSuccess() == true){
//                strBuf= responseObject.getObj();
//            }else if(responseObject != null){
//                Log.d("error", responseObject.getMessage());
//            }else{
//                Log.d("error", "数据读取异常！");
//            }
            } else if (object != null && object instanceof SoapObject) {
                Object obj = ((SoapObject) object).getProperty(0);
                if (obj instanceof SoapPrimitive) {
                    tSoapPrimitive = (SoapPrimitive) obj;
                    byte[] bytes = Base64.decode(tSoapPrimitive.toString());
                    objBuf = unGZipObject(bytes);
//                strBuf =objBuf.toString();
//                ResponseObject responseObject =  JSON.parseObject(strBuf,ResponseObject.class);
//                if(responseObject.isSuccess()==true){
//                    strBuf= responseObject.getObj();
//                }else if(responseObject != null){
//                    Log.d("error", responseObject.getMessage());
//                }else{
//                    Log.d("error", "数据读取异常！");
//                }
                }
            }
            String str = objBuf.toString();
            result = JSON.parseObject(str, ResponseObject.class);
        }catch (RuntimeException e) {
            e.printStackTrace();
            result.setError(e);
            result.setMessage("解压解析出错");
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.setError(e);
            result.setMessage("json解析出错");
            return result;
        }
        return result;
    }

}
