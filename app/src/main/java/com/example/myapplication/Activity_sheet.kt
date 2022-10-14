package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Activity_sheet : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)

        // 设定定时任务
        val task = MainActivity.MyTimerTask()
        Timer().schedule(task, Date(), 100)
        // 绑定返回按钮
        val android_return:ImageButton = findViewById(R.id.android_return)
        android_return.setOnClickListener {
            onBackPressed()
        }

        // 根据输入来判定界面
        if(MediaInformation.mode_act == "sheet") {
            // 初始化该歌单界面的歌曲
            MediaInformation.sheet_list.sheet_list.forEach {
                if (it.sheet_name == MediaInformation.now_sheet) {

                    // 初始化标题为当前歌单名
                    val sheet_name: TextView = findViewById(R.id.activity_sheet_name)
                    sheet_name.text = it.sheet_name

                    // 更新歌单
                    val music_list_view: RecyclerView = findViewById(R.id.list_music_view)
                    val layoutManager = LinearLayoutManager(this)
                    music_list_view.layoutManager = layoutManager
                    val adapter = MusicAdapter(it.music_list)
                    music_list_view.adapter = adapter
                }
            }
        }
        else if(MediaInformation.mode_act == "collect"){
            // 初始化标题为播放记录
            val sheet_name: TextView = findViewById(R.id.activity_sheet_name)
            sheet_name.text = "我的收藏"

            // 初始化该歌单界面的歌曲
            val music_list_view: RecyclerView = findViewById(R.id.list_music_view)
            val layoutManager = LinearLayoutManager(this)
            music_list_view.layoutManager = layoutManager
            val adapter = MusicAdapter(MediaInformation.collect_song)
            music_list_view.adapter = adapter
        }
        else if(MediaInformation.mode_act == "record"){

            // 初始化标题为播放记录
            val sheet_name: TextView = findViewById(R.id.activity_sheet_name)
            sheet_name.text = "播放记录"

            // 初始化该歌单界面的歌曲
            val music_list_view: RecyclerView = findViewById(R.id.list_music_view)
            val layoutManager = LinearLayoutManager(this)
            music_list_view.layoutManager = layoutManager
            val adapter = MusicAdapter(MediaInformation.song_record)
            music_list_view.adapter = adapter
        }


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

        val lyrics_click: ImageButton = findViewById(R.id.lyrcis_click)
        lyrics_click.setOnClickListener {
            val intent = Intent(this, Activity_lyrics::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            MediaInformation.last_act = "sheet"
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

        if(MediaInformation.mode_act == "sheet") {
            // 每次重新打开此界面时初始化该歌单界面的歌曲
            MediaInformation.sheet_list.sheet_list.forEach {
                if (it.sheet_name == MediaInformation.now_sheet) {

                    val music_list_view: RecyclerView = findViewById(R.id.list_music_view)
                    val layoutManager = LinearLayoutManager(this)
                    music_list_view.layoutManager = layoutManager
                    val adapter = MusicAdapter(it.music_list)
                    music_list_view.adapter = adapter
                }
            }
        }
        else if(MediaInformation.mode_act == "record"){
            // 每次重新打开此界面时初始化该歌单界面的歌曲
            val music_list_view: RecyclerView = findViewById(R.id.list_music_view)
            val layoutManager = LinearLayoutManager(this)
            music_list_view.layoutManager = layoutManager
            val adapter = MusicAdapter(MediaInformation.song_record)
            music_list_view.adapter = adapter
        }
        else if(MediaInformation.mode_act == "collect"){
            // 初始化该歌单界面的歌曲
            val music_list_view: RecyclerView = findViewById(R.id.list_music_view)
            val layoutManager = LinearLayoutManager(this)
            music_list_view.layoutManager = layoutManager
            val adapter = MusicAdapter(MediaInformation.collect_song)
            music_list_view.adapter = adapter
        }
//        Toast.makeText(this, "Main activity restart", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        // 将播放按钮传入全局变量
        MediaInformation.music_play_pause = findViewById(R.id.music_play_pause)
        MediaInformation.music_play_data = findViewById(R.id.music_play_data)
//        Toast.makeText(this, "search activity start", Toast.LENGTH_SHORT).show()
    }


}