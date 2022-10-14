package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Activity_explore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        val homepage: ImageButton = findViewById(R.id.homepage)   //通过id获取到按钮实例
        homepage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val search2:ImageButton = findViewById(R.id.search2)
        search2.setOnClickListener{
            val intent = Intent(this, Activity_search::class.java)
            startActivity(intent)
        }

    }
}