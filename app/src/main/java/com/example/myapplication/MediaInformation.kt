package com.example.myapplication

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import moudle.entity.Song
import org.json.JSONStringer

// 储存每个歌单的信息
class Sheet {
    lateinit var sheet_name:String
    // 储存歌单歌曲信息
    var music_list = ArrayList<Song>()
}

class Sheet_list {
    lateinit var sheet_list:ArrayList<Sheet>
}
class MediaInformation: Application(){
    companion object {
        @JvmField
        // 储存当前正在播放的media
        var mediaPlayer = MediaPlayer()
        // 储存搜索到的歌曲信息
        var search_music = ArrayList<Song>()
        // 当前正在播放歌曲信息
        var now_song = Song()
        // 播放歌曲的顺序
        var song_order = ArrayList<Song>()
        // 记录开启的activity是歌单还是收藏还是播放记录
        var mode_act = "sheet"
        // 播放过的歌曲的记录
        var song_record = ArrayList<Song>()
        // 收藏歌单
        var collect_song = ArrayList<Song>()
        // 是否正在播放
        var isplay = false
        // 上一个正在进行的activity
        var last_act = "search"
        // 现在所处的歌单
        var now_sheet = "默认"
        var seek_change = false

        lateinit var sheet_info: JSONStringer
        // 歌单信息类
        var sheet_list = Sheet_list()
        // 歌单名称集合
        var sheet_name = ArrayList<String>()

        // 播放暂停按钮与歌曲信息text
        lateinit var music_play_pause: CheckBox
        lateinit var music_play_data: TextView
    }
}