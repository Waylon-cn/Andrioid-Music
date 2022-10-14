package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moudle.entity.MusicResp
import moudle.entity.Song
import moudle.search
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class Activity_search : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    // 放回主线程进行ui方面的操作进行反馈
    private fun showResponse(out:MusicResp<List<Song>>) {
        runOnUiThread {
            val Songlist = ArrayList<Song>()
            out.data?.forEach {
                Songlist.add(it)
            }
            // 注册MusicAdapter，滑动模块

            // 将搜索到的信息加入全局变量
            MediaInformation.search_music = Songlist

            val music_list_view:RecyclerView = findViewById(R.id.list_music_view)
            val layoutManager = LinearLayoutManager(this)
            music_list_view.layoutManager = layoutManager
            val adapter = MusicAdapter(Songlist)
            music_list_view.adapter = adapter
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    // 单独创建一个进程搜索音乐，因为网络请求不能在主线程进行
    private fun search_music(inputText: String) {
        thread {
            try {
                val out = search(inputText)
                showResponse(out)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 设定定时任务
        val task = MainActivity.MyTimerTask()
        Timer().schedule(task, Date(), 100)

        // 绑定开始播放按钮与暂停按钮
        val music_play_pause:CheckBox = findViewById(R.id.music_play_pause)
        music_play_pause.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                if (MediaInformation.mediaPlayer.isPlaying) {
                    MediaInformation.mediaPlayer.pause()
                    // 暂停播放
                    MediaInformation.isplay = false
                }
            }
            else{
                if (!MediaInformation.mediaPlayer.isPlaying) {
                    MediaInformation.mediaPlayer.start() // 开始播放
                    MediaInformation.isplay = true
                }
            }
        }



        // 绑定搜索按钮
        val search: EditText = findViewById(R.id.input_search)
        search.setOnEditorActionListener { _, _, _ ->
            val inputText = search.text.toString()
            search_music(inputText)

            // 如果点击了回车键，即搜索键，就弹出一个toast
//            if ((keyCode == EditorInfo.IME_ACTION_DONE) or (keyCode == EditorInfo.IME_ACTION_SEARCH) or (keyCode == IME_ACTION_GO)) {
            false

        }

        // 绑定返回按钮
        val android_return:ImageButton = findViewById(R.id.android_return)
        android_return.setOnClickListener {
            onBackPressed()
        }

        // 开启歌词界面
        val lyrics_click:ImageButton = findViewById(R.id.lyrcis_click)
        lyrics_click.setOnClickListener {
            val intent = Intent(this, Activity_lyrics::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            MediaInformation.last_act = "search"
            startActivity(intent)
        }

        if(MediaInformation.isplay){
            val music_play_pause: CheckBox = findViewById(R.id.music_play_pause)
            music_play_pause.isChecked = false
            val music_play_data: TextView = findViewById(R.id.music_play_data)
            val song_name = MediaInformation.now_song.title
            val song_author = MediaInformation.now_song.author
            music_play_data.setText("$song_name -- $song_author")
        }

//        Toast.makeText(this, "search activity create", Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        // 将播放按钮传入全局变量
        MediaInformation.music_play_pause = findViewById(R.id.music_play_pause)
        MediaInformation.music_play_data = findViewById(R.id.music_play_data)
//        Toast.makeText(this, "search activity start", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
//        Toast.makeText(this, "search activity resume", Toast.LENGTH_SHORT).show()
    }
    override fun onPause() {
        super.onPause()
//        Toast.makeText(this, "search activity pause", Toast.LENGTH_SHORT).show()
    }
    override fun onStop() {
        super.onStop()
//        Toast.makeText(this, "search activity stop", Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        super.onDestroy()
//        Toast.makeText(this, "search activity destroy", Toast.LENGTH_SHORT).show()
    }
    override fun onRestart() {
        super.onRestart()

        if(MediaInformation.isplay){
            val music_play_pause: CheckBox = findViewById(R.id.music_play_pause)
            music_play_pause.isChecked = false
            val music_play_data: TextView = findViewById(R.id.music_play_data)
            val song_name = MediaInformation.now_song.title
            val song_author = MediaInformation.now_song.author
            music_play_data.setText("$song_name -- $song_author")
        }
//        Toast.makeText(this, "search activity restart", Toast.LENGTH_SHORT).show()
    }
}



