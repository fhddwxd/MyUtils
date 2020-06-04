package com.wxd.myutils

import android.Manifest
import android.content.Context
import android.content.ServiceConnection
import android.graphics.Color
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import com.wxd.myutils.utils.MD5
import com.wxd.myutils.views.ClockView
import com.wxd.myutils.views.WaveHelper
import com.wxd.myutils.views.WaveView
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import android.content.ComponentName
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private var clockView:ClockView?=null
    var waveView:WaveView?=null
    var waveHelper:WaveHelper?=null
    var mqttClient:MqttClient?=null
    var options:MqttConnectOptions = MqttConnectOptions()
    var telephonyManager:TelephonyManager?=null
    var mBound :Boolean = false
    var mService :Messenger?=null
    var messenger = Messenger(object :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg!!.what){
                0x101 -> Log.d("MainActivity" ,msg.obj.toString())
            }
        }
    })

    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            mService = Messenger(service)
            var message = Message()
            message.what = 0x1001
            message.replyTo = messenger
            mService!!.send(message)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave)
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        waveView = findViewById(R.id.waveView)
        waveView!!.setWaveColor(
                Color.parseColor("#881FABFF"),
                Color.parseColor("#881FABFF"))
        waveView!!.setBorder(0, Color.parseColor("#FFFFFF"))
        waveView!!.setShapeType(WaveView.ShapeType.ROUND_CORNER)
        waveView!!.waveShiftRatio = 1f
        waveView!!.amplitudeRatio = 0.1f
        waveView!!.waveLengthRatio = 2f
        waveHelper= WaveHelper(waveView)
        waveHelper!!.start()
//        var intent = Intent(this@MainActivity,MqttService::class.java)
//        bindService(intent,mConnection,Context.BIND_AUTO_CREATE)
//        initMqtt()
        clockView = findViewById(R.id.myClock)
        clockView!!.setCalendar()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        waveHelper!!.cancel()
        unbindService(mConnection)
        super.onDestroy()
    }
    fun initMqtt(){
        try {
            var clientId = "DEV:"+telephonyManager!!.imei+"_" + +System.currentTimeMillis()
            mqttClient = MqttClient("tcp://emqtt-test.yunext.com:18850",
                    clientId, MemoryPersistence())
            options.isCleanSession = true
            options.userName = telephonyManager!!.imei
            options.password = (MD5.encodeMD5(clientId+"2019wanhojsdkey")).toCharArray()
            options.connectionTimeout = 10
            options.keepAliveInterval = 30
            options.isAutomaticReconnect = true
            mqttClient!!.setCallback(object : MqttCallbackExtended {
                override fun connectionLost(cause: Throwable) {
                    Log.d("Mqtt",cause.message)
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    Log.d("Mqtt",topic+String(message.payload))
                }
                override fun deliveryComplete(token: IMqttDeliveryToken) {

                }

                override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                    Log.d("Mqtt","====连接成功=====")
                    mqttClient!!.subscribe("/wanho/SR/01/SR/GPRS/"+telephonyManager!!.imei+"/request")
                }
            })
            try {
                mqttClient!!.connect(options)
            }catch (e:MqttException){
                Log.d("Mqtt","连接异常"+e.message)
            }

        } catch (e: MqttException) {
            Log.d("Mqtt","MQTTClient异常" + e.message)
        }catch (e:SecurityException){
            Log.d("Mqtt","SecurityException!!")
        }

    }

    fun publish(v: View){
//        if(mqttClient!!.isConnected){
//            mqttClient!!.publish("/wanho/SR/01/SR/GPRS/"+telephonyManager!!.imei+"/up",
//                    "{\"cmd\":\"online\",\"params\":{\"online\":true}}".toByteArray(),0,false)
//        }else{
//            mqttClient!!.connect(options)
//            mqttClient!!.publish("/wanho/SR/01/SR/GPRS/"+telephonyManager!!.imei+"/up",
//                    "{\"cmd\":\"online\",\"params\":{\"online\":true}}".toByteArray(),0,false)
//        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == -1) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 100)
        }
//        var message = Message()
//        message.what = 0x1002
//        mService!!.send(message)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
