package com.wxd.myutils

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wxd.myutils.Views.ClockView

class MainActivity : AppCompatActivity() {
    private var clockView:ClockView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clockView = findViewById(R.id.myClock)
        clockView!!.setCalendar()
    }
}
