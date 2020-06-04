package com.wxd.myutils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.telephony.TelephonyManager
import android.util.Log
import com.wxd.myutils.utils.MD5
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

/**
 *  Create by @author wxd
 *  @time 2019/7/26  上午 9:09
 *  @describe
 */
class    MqttService : Service() {
    var mqttAndroidClient:MqttAndroidClient?=null
    var options:MqttConnectOptions= MqttConnectOptions()
    var telephonyManager: TelephonyManager? = null
    var mMessenger:Messenger?= null
    var mService =Messenger(object :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg!!.what){
                0x1001 -> mMessenger = msg.replyTo
                0x1002 ->
                    try{
                        mqttAndroidClient!!.publish("/wanho/SR/01/SR/GPRS/"+telephonyManager!!.imei+"/up",
                            "{\"cmd\":\"online\",\"params\":{\"online\":true}}".toByteArray(),1,false)
                    }catch (e:SecurityException){
                        Log.d("Mqtt","SecurityException!!")
                    }
            }
        }
    })
    override fun onCreate() {
        super.onCreate()
        telephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        initMqtt()
        mqttAndroidClient!!.connect(options,null,object :IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("Mqtt","=====连接成功=====")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("Mqtt","=====连接失败=====\n"+exception!!.message)
            }
        })
    }


    override fun onBind(intent: Intent?): IBinder {
        return mService.binder
    }



    override fun onDestroy() {
        super.onDestroy()
        mqttAndroidClient!!.disconnect()
    }

    fun initMqtt(){
        try {
            var clientId = "DEV:"+telephonyManager!!.imei+"_" + +System.currentTimeMillis()
            mqttAndroidClient = MqttAndroidClient(applicationContext,"tcp://emqtt-test.yunext.com:18850",clientId)
            options.userName = telephonyManager!!.imei
            options.password = (MD5.encodeMD5(clientId+"2019wanhojsdkey")).toCharArray()
            options.isAutomaticReconnect = true
            options.isCleanSession = true
            options.connectionTimeout = 10
            options.keepAliveInterval = 30
            mqttAndroidClient!!.setCallback(object :MqttCallbackExtended{
                override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                    if(reconnect){
                        Log.d("Mqtt","====重连成功====")
                    }else{
                        Log.d("Mqtt","====连接成功====")
                    }
                    mqttAndroidClient!!.subscribe("/wanho/SR/01/SR/GPRS/"+telephonyManager!!.imei+"/request",1)
                }

                override fun messageArrived(topic: String?, msg: MqttMessage?) {
                    Log.d("Mqtt",topic+ msg.toString())
                    when(topic){
                        "/wanho/SR/01/SR/GPRS/"+telephonyManager!!.imei+"/request" -> {
                            var message =Message()
                            message.what = 0x101
                            message.obj = msg.toString()
                            mMessenger!!.send(message)
                        }
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.d("Mqtt","====断开连接====")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                }
            })
        }catch (e:MqttException){
            Log.d("Mqtt","====初始异常====")
        }catch (e:SecurityException){
            Log.d("Mqtt","SecurityException!!")
        }
    }
}