package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.dirror.lyricviewx.LyricViewX
import com.dirror.lyricviewx.OnPlayClickListener
import moudle.entity.Song
import java.text.SimpleDateFormat
import java.util.*

class Activity_lyrics : AppCompatActivity() {
    private val format = SimpleDateFormat("mm:ss")
    private var seekbarchange = false
    private var last_seek:Long = 0

    class MyTimerTask(
        val musicCur: TextView,
        val seekBar: SeekBar,
        val seekbarchange: Boolean,
        val musicLength: TextView,
        val lyricViewX: LyricViewX
    ) : TimerTask() {
        @SuppressLint("SuspiciousIndentation")
        override fun run() {
            if(MediaInformation.isplay and !seekbarchange and !MediaInformation.seek_change) {
                val format = SimpleDateFormat("mm:ss")
                if(MediaInformation.mediaPlayer.duration - MediaInformation.mediaPlayer.currentPosition < 500){
                    val song_order_array = MediaInformation.song_order.toTypedArray()
                    val song_length = song_order_array.size
                    for(i in 1..song_length){
                        if(song_order_array[i - 1].url == MediaInformation.now_song.url){
                            if(i < song_length) {
                                val song = song_order_array[i]
                                MediaInformation.mediaPlayer.reset()
                                MediaInformation.mediaPlayer.setDataSource(song.url)
                                MediaInformation.mediaPlayer.prepare()
                                MediaInformation.mediaPlayer.start() // 开始播放

                                // 将当前播放音乐信息存入全局变量
                                MediaInformation.now_song = song
                                MediaInformation.isplay = true

                                // 将该歌曲加入播放记录
                                MediaInformation.song_record.add(song)

                                //改变当前页面下方播放界面
                                MediaInformation.music_play_pause.isChecked = false
                                val song_name = MediaInformation.now_song.title
                                val song_author = MediaInformation.now_song.author
                                MediaInformation.music_play_data.setText("$song_name -- $song_author")

                                musicLength.text = format.format(MediaInformation.mediaPlayer.duration)+""
                                musicCur.text = "00:00"
                                lyricViewX.loadLyric(MediaInformation.now_song.lrc?.trimIndent())
                                break
                            }
                        }
                    }
                }
                else {
                    musicCur.text = format.format(MediaInformation.mediaPlayer.currentPosition) + ""
                    seekBar.setProgress(MediaInformation.mediaPlayer.currentPosition)
                    lyricViewX.updateTime(
                        MediaInformation.mediaPlayer.currentPosition.toLong(),
                        true
                    )
                }
            }
        }
    }


    private fun play_music(song:Song){
        MediaInformation.mediaPlayer.reset()
        MediaInformation.mediaPlayer.setDataSource(song.url)
        MediaInformation.mediaPlayer.prepare()
        MediaInformation.mediaPlayer.start() // 开始播放

        // 将当前播放音乐信息存入全局变量
        MediaInformation.now_song = song
        MediaInformation.isplay = true

        // 将该歌曲加入播放记录
        MediaInformation.song_record.add(song)

        //改变当前页面下方播放界面
        MediaInformation.music_play_pause.isChecked = false
        val song_name = MediaInformation.now_song.title
        val song_author = MediaInformation.now_song.author
        MediaInformation.music_play_data.setText("$song_name -- $song_author")
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)

        val lyricViewX = findViewById<LyricViewX>(R.id.lyricViewX)
        lyricViewX.loadLyric(MediaInformation.now_song.lrc?.trimIndent())

        lyricViewX.setDraggable(true, object : OnPlayClickListener {
            override fun onPlayClick(time: Long): Boolean {
                lyricViewX.updateTime(time)
                MediaInformation.mediaPlayer.seekTo(time.toInt())
                return true
            }
        })


        val music_play_pause:CheckBox = findViewById(R.id.music_play_pause)
        music_play_pause.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                if (MediaInformation.mediaPlayer.isPlaying) {
                    MediaInformation.mediaPlayer.pause() // 暂停播放
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

        // 进行进度条相关初始化
        val seekbar:SeekBar = findViewById(R.id.seekBar)
        if(MediaInformation.isplay){
            val music_play_pause: CheckBox = findViewById(R.id.music_play_pause)
            music_play_pause.isChecked = false

            val musicLength: TextView = findViewById(R.id.music_length)
            val musicCur:TextView = findViewById(R.id.music_cur)
            musicLength.text = format.format(MediaInformation.mediaPlayer.duration)+""
            musicCur.text = "00:00"

            seekbar.max = MediaInformation.mediaPlayer.duration


            val task = MyTimerTask(musicCur, seekbar, seekbarchange, musicLength, lyricViewX)
            Timer().schedule(task, Date(), 100)
        }

        // 进度条滑动处理
        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p1 - last_seek > 2000) {
                    val now_seek = seekbar.progress
                    MediaInformation.mediaPlayer.seekTo(now_seek)
                }
                last_seek = p1.toLong()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                seekbarchange = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                seekbarchange = false
            }


        })

        val android_return:ImageButton = findViewById(R.id.android_return)
        android_return.setOnClickListener {
            onBackPressed()
        }

        // 下一首播放
        val next_music:ImageButton = findViewById(R.id.next)
        next_music.setOnClickListener {
            seekbarchange = true
            MediaInformation.seek_change = true
            val song_order_array = MediaInformation.song_order.toTypedArray()
            val song_length = song_order_array.size
            for(i in 1..song_length){
                if(song_order_array[i - 1].url == MediaInformation.now_song.url){
                    if(i < song_length) {
                        play_music(song_order_array[i])
                        lyricViewX.loadLyric(MediaInformation.now_song.lrc?.trimIndent())
                        break
                    }
                    else{
                        Toast.makeText(this, "没有下一首播放的音乐", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            seekbarchange = false
            MediaInformation.seek_change = false
        }
        val last_music:ImageButton = findViewById(R.id.last)
        last_music.setOnClickListener {
            seekbarchange = true
            MediaInformation.seek_change = true
            val play_record = MediaInformation.song_record.toTypedArray()
            val song_length = play_record.size
            for(i in 1..song_length){
                if(play_record[i - 1].url == MediaInformation.now_song.url){
                    if(i - 2 >= 0) {
                        play_music(play_record[i - 2])
                        lyricViewX.loadLyric(MediaInformation.now_song.lrc?.trimIndent())
                        break
                    }
                    else{
                        Toast.makeText(this, "没有上一首播放的音乐", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            seekbarchange = false
            MediaInformation.seek_change = false
        }
    }

    // 返回键
//    override fun onBackPressed() {
//        finish()
////        if(MediaInformation.last_act == "search") {
////            val intent = Intent(this, Activity_search::class.java)
////            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
////            startActivity(intent)
////        }
////        else if(MediaInformation.last_act == "sheet"){
////            val intent = Intent(this, Activity_sheet::class.java)
////            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
////            startActivity(intent)
////        }
////        else{
////            val intent = Intent(this, MainActivity::class.java)
////            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
////            startActivity(intent)
////        }
//
//
//    }

}