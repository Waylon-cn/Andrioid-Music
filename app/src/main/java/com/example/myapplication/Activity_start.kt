package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class Activity_start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //加载启动界面
        setContentView(R.layout.activity_start)
        val time = 1000 //设置等待时间，单位为毫秒

        val handler = Handler()
        //当计时结束时，跳转至主界面
        //当计时结束时，跳转至主界面
        handler.postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }, time.toLong())
    }
}