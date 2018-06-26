package com.wxd.myutils.SoapService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

public class SoapService implements ISoapService {
    private String nameSpace;//命名空间
    private String methodName;
    private String url;//网址
    private String soapAction;
    private int methodLenth = 0;//参数个数
    private String methodNames[];//参数名
    private Object methodValues[];//参数值
    private HashMap<String, Object> maps;//装在参数的map

    public SoapService(String nameSpace, String methodName, String url, String[] methodNames, Object[] methodValues) {
        super();
        this.nameSpace = nameSpace;
        this.methodName = methodName;
        this.url=url;
        this.methodNames = methodNames;
        this.methodValues = methodValues;
        this.soapAction = this.nameSpace + this.methodName;
        this.maps = new HashMap<String, Object>();
    }

    private boolean isMethodMapsOk() {
        int lenthN = methodNames.length;
        int lenthV = methodValues.length;
        if (lenthN != lenthV) {
            return false;
        } else {
            for (int i = 0; i < lenthV; i++) {
                maps.put(methodNames[i], methodValues[i]);
            }
            this.methodLenth = methodValues.length;
            return true;
        }
    }

    public Object LoadResult() throws IOException,
            XmlPullParserException {
        //step1 指定WebService的命名空间和调用方法
        SoapObject request = new SoapObject(nameSpace, methodName);  //用到的域名和方法名
        //step2 设置调用方法的参数值，这里的参数要与webservices的完全一致
        if (this.isMethodMapsOk()) {
            for (int i = 0; i < this.methodLenth; i++) {
                request.addProperty(methodNames[i], maps.get(methodNames[i]));
            }

            //step3 生成调用WebService方法的SOAP请求信息，并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            //设置是否调用的是dotNet下的WebService
            envelope.bodyOut = request;
          //  envelope.dotNet = true;
            //必须，等价于envelope。bodyOut = request;
            envelope.setOutputSoapObject(request);    //设置请求

            //step4 创建HttpTransportSE对象
            HttpTransportSE ht = new HttpTransportSE(url); //服务器发布的IP信息

            ht.debug = true; // 使用调试功能
            //	ht.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

            //step5 调用WebService
            ht.call(soapAction, envelope);
            Object object = (Object)envelope.getResponse();
            //step6  使用getResponse方法获得WebService方法的返回结果
            if (object != null) {
                //取值
                return envelope.bodyIn;
            }
            return null;
        } else {
            return null;
        }
    }
}
