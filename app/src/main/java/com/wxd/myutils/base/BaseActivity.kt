package com.wxd.myutils.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar

/**
 *  Create by @author wxd
 *  @time 2019/9/25  上午 9:10
 *  @describe
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView()
//        setSupportActionBar(mToolbar)
        initData()
        ImmersionBar.with(this)
            .statusBarColor(getBarColorId())
            .statusBarDarkFont(true)
            .init()

    }

    abstract fun getLayoutResId(): Int
    abstract fun getBarColorId(): Int
    abstract fun initView()
    abstract fun initData()

    protected fun startActivity(z: Class<*>) {
        startActivity(Intent(this, z))
    }

    protected fun startActivity(z: Class<*>, name: String, value: Boolean) {
        val intent = Intent(this, z).putExtra(name, value)
        startActivity(intent)
    }

}