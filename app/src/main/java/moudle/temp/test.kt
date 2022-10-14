package moudle.temp

//import android.os.Build
//import androidx.annotation.RequiresApi
//import okhttp3.*
//import com.alibaba.fastjson.JSONObject
//
//import moudle.utils.future
//import moudle.entity.Song
//import moudle.entity.MusicResp
//
//fun getLrcById(songId: String): String {
//    return try {
//        val request: Request = Request.Builder()
//            .url("http://m.kugou.com/app/i/krc.php?cmd=100&timelength=999999&hash=$songId")
//            .build()
//        val call: Call = OkHttpClient().newCall(request)
//        if (false)
//        {
//            "[00:00:00]此歌曲可能没有歌词"
//        }
//        else {
//            var response = call.execute()
//            val songInfo = response.body.string().trimIndent()
//            songInfo
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//        println("获取歌词出现异常")
//        "[00:00:00]此歌曲可能没有歌词"
//    }
//}
//
//
//@RequiresApi(Build.VERSION_CODES.N)
//fun getSongById(songIds: List<String>): MusicResp<List<Song>> {
//    val songs =
//        songIds.future { songId ->
//            try {
//                val request: Request = Request.Builder()
//                    .url("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=$songId")
//                    .build()
//                val call: Call = OkHttpClient().newCall(request)
//                if (false) {
//                    Song(
//                        site = "kugou",
//                        msg = "网络异常",
//                        songid = songId
//                    )
//                } else {
//                    var response = call.execute()
//                    val out = response.body.string().trimIndent()
//                    val songInfo = JSONObject.parseObject(out)
//
//
//                    val url = songInfo.getString("url")
//                    val privilege = songInfo.getInteger("privilege")
//
//                    val radioSongId = songInfo.getString("hash")
//                    val albumImg = songInfo.getString("album_img").replace("{size}", "400")
//                    val imgUrl = songInfo.getString("imgUrl").replace("{size}", "400")
//
//                    Song(
//                        msg = if (url.isNullOrEmpty()) if (privilege == 1) "源站反馈此音频需要付费" else "找不到可用的播放地址" else "",
//                        site = "kugou",
//                        link = "http://www.kugou.com/song/#hash=$radioSongId",
//                        songid = radioSongId,
//                        title = songInfo.getString("songName"),
//                        author = songInfo.getString("singerName"),
//                        url = url,
//                        lrc = getLrcById(radioSongId),
//                        pic = if (albumImg.isEmpty()) imgUrl else albumImg
////                                albumName = song.getString("albumName")
//                    )
//                }
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Song(
//                    site = "kugou",
//                    code = 500,
//                    msg = e.message ?: "未知异常",
//                    songid = songId
//                )
//            }
//        }
//
//    return MusicResp.success(data = songs)
//
//}
//@RequiresApi(Build.VERSION_CODES.N)
//fun search(key:String): MusicResp<List<Song>>  {
//    return try {
//        val request: Request = Request.Builder()
//            .url("http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=$key&page=1&pagesize=20&showtype=1")
//            .build()
//        val call: Call = OkHttpClient().newCall(request)
////        if (!call.isExecuted()) {
////            return MusicResp.failure(code = 500, msg = "请求失败")
////        }
//        var response = call.execute()
//        val out = response.body.string().trimIndent()
//
//        val temp = JSONObject.parseObject(out)
//        val songList = temp
//            .getJSONObject("data")
//            .getJSONArray("info")
//
//        //酷狗不支持在详情获取专辑名称
//        val albumNames = HashMap<String, String>()
//        //获取歌曲ID
//        val songIds = songList.map {
//            val hash = (it as JSONObject).getString("320hash")
//            val albumName = it.getString("album_name")
//            val tmpHash = if (hash.isNullOrEmpty()) {
//                it.getString("hash")
//            } else {
//                hash
//            }
//            albumNames[tmpHash.toUpperCase()] = albumName
//            tmpHash
//        }
//        //添加专辑名称
//        val musicData = getSongById(songIds)
//            .apply {
//                data?.map {
//                    it.albumName = albumNames[it.songid?.toUpperCase()] ?: ""
//                    it
//                }
//            }
//        musicData
//    }
//    catch (e: Exception) {
//        e.printStackTrace()
//        MusicResp.failure(msg = e.message)
//    }
//}
//
//
//@RequiresApi(Build.VERSION_CODES.N)
//fun main()
//{
//    val out = search("勇气")
//    println("111")
//}