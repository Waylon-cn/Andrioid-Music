package com.example.myapplication

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    // 定时任务
    class MyTimerTask() : TimerTask() {
        @SuppressLint("SuspiciousIndentation")
        override fun run() {
            if(MediaInformation.isplay and !MediaInformation.seek_change)
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
                            break
                        }
                    }
                }
            }

        }
    }
    // 初始化音乐播放
    private fun initMediaPlayer() {
        val assetManager = assets
        val fd = assetManager.openFd("Lemon2.mp3")
        MediaInformation.mediaPlayer.reset()
//        val play_url = "https://sharefs.ali.kugou.com/202210061846/413128908eed5b8f2ce484217a1ad2b7/G228/M03/09/15/xJQEAF8hDg-AHlq9AD56UC_MCIs018.mp3"
        MediaInformation.mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        // 再次设置音乐时没有reset会出错

//        mediaPlayer.setDataSource(play_url)
        MediaInformation.mediaPlayer.prepare()

//        mediaPlayer.seekTo(0) // 移动到某一播放进度
//
//        mediaPlayer.getCurrentPosition()// 获取当前播放进度

    }
    private fun initsheet()
    {
        val sheet_temp = Sheet()
        sheet_temp.sheet_name = "默认"
        val sheet_list = ArrayList<Sheet>()
        sheet_list.add(sheet_temp)
        MediaInformation.sheet_list.sheet_list = sheet_list

        MediaInformation.sheet_name.add("默认")

        // RecycleView初始化
        val sheet_list_view: RecyclerView = findViewById(R.id.sheet_name_View)
        val layoutManager = LinearLayoutManager(this)
        sheet_list_view.layoutManager = layoutManager
        val adapter = SheetAdapter(MediaInformation.sheet_name)
        sheet_list_view.adapter = adapter
    }

    // 创建新建菜单对话框
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    private fun create_new_sheetdialog()
    {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogview = inflater.inflate(R.layout.new_song_sheet, null)
        builder.setView(dialogview)
        builder.create()
        val dialog = builder.show()
        dialog.show()

        // 新建的歌单歌名
        val sheet_name:EditText = dialogview.findViewById(R.id.sheet_name)
        // 确定和取消键
        val yes:Button = dialogview.findViewById(R.id.yes)
        yes.setOnClickListener  {
            MediaInformation.sheet_name.add(sheet_name.text.toString())

            val sheet_list_view: RecyclerView = findViewById(R.id.sheet_name_View)
            val layoutManager = LinearLayoutManager(this)
            sheet_list_view.layoutManager = layoutManager
            val adapter = SheetAdapter(MediaInformation.sheet_name)
            sheet_list_view.adapter = adapter

            // 将新建的歌单信息加入全局变量
            val sheet_temp = Sheet()
            sheet_temp.sheet_name = sheet_name.text.toString()
            MediaInformation.sheet_list.sheet_list.add(sheet_temp)
            dialog.dismiss()
        }

        val no:Button = dialogview.findViewById(R.id.no)
        no.setOnClickListener {
            dialog.dismiss()
        }

    }


    // 更多按钮对话框
    private fun more_info()
    {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)

        val choices = arrayOf("计时器", "设置", "关于我们")

        // 选定添加的歌单后将歌曲信息加入对应的全局变量下
        builder.setItems(choices, DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            val choice = choices[i]
            if(choice == "计时器"){
                val builder = CustomDialog.Builder(this)
                builder.setPositiveButton(DialogInterface.OnClickListener { dialogInterface, _ ->

                    dialogInterface.dismiss()

                })
                builder.setNegativeButton(DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                })

                val dialog=builder.create().show()
//                Toast.makeText(this, "该功能暂未开放", Toast.LENGTH_SHORT).show()
            }
            else if(choice == "设置"){
                Toast.makeText(this, "该功能暂未开放", Toast.LENGTH_SHORT).show()
            }
            else{
                AlertDialog.Builder(this).apply {
                    setTitle("关于我们")
                    setMessage("2022年10月11日                             " +
                            "Xiaolong Wen. Haoming Yin.")
                    setCancelable(true)

                    show()
                }
            }
        })
        builder.create()
        builder.show()
    }


    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化播放数据
        initMediaPlayer()
        // 初始化歌单数据
        initsheet()
        // 设定定时任务
        val task = MyTimerTask()
        Timer().schedule(task, Date(), 100)
//        MediaInformation.main_sheet_name = findViewById(R.id.main_sheet_name)
        // 初始化歌单数据
//        Initsheet()
//        write()

        // 开启搜索界面
        val search:ImageButton = findViewById(R.id.search)
        search.setOnClickListener {
            val intent = Intent(this, Activity_search::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        // 开启歌词界面
        val lyrics_click:ImageButton = findViewById(R.id.lyrcis_click)
        lyrics_click.setOnClickListener {
            val intent = Intent(this, Activity_lyrics::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            MediaInformation.last_act = "main"
            startActivity(intent)
        }

        // 绑定音乐播放与暂停按钮
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

        // 绑定新建歌单按钮
        val new_song_sheet:ImageButton = findViewById(R.id.new_song_sheet)
        new_song_sheet.setOnClickListener {
            create_new_sheetdialog()
        }

        // 控制歌单的可见与不可见
        val check:CheckBox = findViewById(R.id.check)
        check.setOnCheckedChangeListener { _, isChecked ->
            if(!isChecked){
                val sheet_list:RecyclerView = findViewById(R.id.sheet_name_View)
                sheet_list.isVisible = false
            }
            else{
                val sheet_list:RecyclerView = findViewById(R.id.sheet_name_View)
                sheet_list.isVisible = true
            }
        }

        // 绑定收藏按钮
        val music_collection1:Button = findViewById(R.id.collection)
        val music_collection2:ImageButton = findViewById(R.id.collection2)
        music_collection1.setOnClickListener {
            MediaInformation.mode_act = "collect"
            val intent = Intent(this, Activity_sheet::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
        music_collection2.setOnClickListener {
            MediaInformation.mode_act = "collect"
            val intent = Intent(this, Activity_sheet::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
        // 绑定播放记录按钮
        val music_record1:ImageButton = findViewById(R.id.record1)
        val music_record2:Button = findViewById(R.id.recored2)
        music_record1.setOnClickListener {
            MediaInformation.mode_act = "record"
            val intent = Intent(this, Activity_sheet::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
        music_record2.setOnClickListener {
            MediaInformation.mode_act = "record"
            val intent = Intent(this, Activity_sheet::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        // 绑定左上角更多按钮
        val more_info:ImageButton = findViewById(R.id.more)
        more_info.setOnClickListener {
            more_info()
        }

        // 编辑歌单按钮
        val change_sheet:ImageButton = findViewById(R.id.change_sheet)
        change_sheet.setOnClickListener {
            Toast.makeText(this, "该功能暂未开放", Toast.LENGTH_SHORT).show()
        }

        // 判断是否有在播放音乐
        if(MediaInformation.isplay){
            val music_play_pause: CheckBox = findViewById(R.id.music_play_pause)
            music_play_pause.isChecked = false
            val music_play_data: TextView = findViewById(R.id.music_play_data)
            val song_name = MediaInformation.now_song.title
            val song_author = MediaInformation.now_song.author
            music_play_data.setText("$song_name -- $song_author")
        }

//        Toast.makeText(this, "Main activity create", Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
//        Toast.makeText(this, "Main activity start", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
//        Toast.makeText(this, "Main activity resume", Toast.LENGTH_SHORT).show()
    }
    override fun onPause() {
        super.onPause()
//        Toast.makeText(this, "Main activity pause", Toast.LENGTH_SHORT).show()
    }
    override fun onStop() {
        super.onStop()
//        Toast.makeText(this, "Main activity stop", Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        super.onDestroy()
//        Toast.makeText(this, "Main activity destroy", Toast.LENGTH_SHORT).show()
    }
    override fun onRestart() {
        super.onRestart()

        if(MediaInformation.isplay){
            val music_play_pause:CheckBox = findViewById(R.id.music_play_pause)
            music_play_pause.isChecked = false
            val music_play_data:TextView = findViewById(R.id.music_play_data)
            val song_name = MediaInformation.now_song.title
            val song_author = MediaInformation.now_song.author
            music_play_data.setText("$song_name -- $song_author")
        }
//        Toast.makeText(this, "Main activity restart", Toast.LENGTH_SHORT).show()
    }

}
