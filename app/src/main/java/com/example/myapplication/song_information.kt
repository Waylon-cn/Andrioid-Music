package com.example.myapplication

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import moudle.entity.Song


class MusicAdapter(val songlist: List<Song>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val songname: TextView = view.findViewById(R.id.songname)
        val signername: TextView = view.findViewById(R.id.signername)
        val play: ImageButton = view.findViewById(R.id.play)
        val more: ImageButton = view.findViewById(R.id.more)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // 添加到对应歌单
        fun add_sheet_to(song:Song)
        {
            val builder = AlertDialog.Builder(parent.context)
            builder.setTitle("添加到歌单：")
            builder.setCancelable(true)

            //读取全局变量中的歌曲信息
            val lesson: Array<String> = MediaInformation.sheet_name.toTypedArray()

            // 选定添加的歌单后将歌曲信息加入对应的全局变量下
            builder.setItems(lesson, DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                val check_sheet_name = lesson[i]
                MediaInformation.sheet_list.sheet_list.forEach{
                    if(it.sheet_name == check_sheet_name){
                        it.music_list.add(song)
                    }
                }
            })
            builder.create()
            builder.show()
        }
        // 添加到我的收藏
        fun add_to_collect(song:Song){
            MediaInformation.collect_song.add(song)
        }
        //弹出按钮框
        fun showPopupMenu(view: View, song:Song) {
            val popupMenu = PopupMenu(parent.context, view)
            //menu 布局
            popupMenu.menuInflater.inflate(R.menu.music_more, popupMenu.menu)
            //点击事件
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add_mysheet -> add_sheet_to(song)
                    R.id.add_mylove -> add_to_collect(song)
                    else -> {}
                }
                false
            }
            //关闭事件
            popupMenu.setOnDismissListener {
            }
            //显示菜单，不要少了这一步
            popupMenu.show()
        }

        fun play_music(play_url:String, song:Song){
            MediaInformation.mediaPlayer.reset()
            MediaInformation.mediaPlayer.setDataSource(play_url)
            MediaInformation.mediaPlayer.prepare()
            MediaInformation.mediaPlayer.start() // 开始播放

            // 将当前搜索到的所有歌曲加入到全局变量顺序播放
            MediaInformation.song_order = songlist as ArrayList<Song>
            // 将该歌曲加入播放记录
            MediaInformation.song_record.add(song)

            // 将当前播放音乐信息存入全局变量
            MediaInformation.now_song = song
            MediaInformation.isplay = true

            //改变当前页面下方播放界面
            MediaInformation.music_play_pause.isChecked = false
            val song_name = MediaInformation.now_song.title
            val song_author = MediaInformation.now_song.author
            MediaInformation.music_play_data.setText("$song_name -- $song_author")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_information, parent, false)
        val viewHolder = ViewHolder(view)
        // 整个块的点击事件
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val song = songlist[position]

            val play_url = song.url
            if (play_url != "") {
                if (play_url != null) {
                    play_music(play_url, song)
                }
            } else {
                Toast.makeText(parent.context, "此音乐暂时没有音源！！！",
                    Toast.LENGTH_SHORT).show()
            }
        }
        // 播放按钮的点击事件
        viewHolder.play.setOnClickListener {
            val position = viewHolder.adapterPosition
            val song = songlist[position]

            val play_url = song.url
            if (play_url != "") {
                if (play_url != null) {
                    play_music(play_url, song)
                }
            } else {
                Toast.makeText(parent.context, "此音乐暂时没有音源！！！",
                    Toast.LENGTH_SHORT).show()
            }
        }

        // 更多按钮的点击事件
        viewHolder.more.setOnClickListener {
            val position = viewHolder.adapterPosition
            val song = songlist[position]
            showPopupMenu(viewHolder.more, song)
        }

        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val song = songlist[position]
        holder.songname.text = song.title
        holder.signername.text = song.author
        holder.play.setImageResource(R.drawable.play)
        holder.more.setImageResource(R.drawable.more)
    }
    override fun getItemCount() = songlist.size
}
