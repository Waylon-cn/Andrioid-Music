package moudle

import khttp.get
import com.alibaba.fastjson2.JSONObject
//
//fun main()
//{
//    val resp = get(
//        url = "http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=lemon&page=1&pagesize=20&showtype=1",
//        headers = mapOf(
//            "Referer" to "http://m.kugou.com/v2/static/html/search.html",
//            "User-Agent" to "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"
//        )
//    )
//    val radioData = resp.jsonObject
//    val songList = radioData
//        .getJSONObject("data")
//        .getJSONArray("info")
//}
import khttp.get


fun main(args: Array<out String>) {
    // Get our IP
    println(JSONObject.parseObject(get("http://httpbin.org/ip").text))
    // Get our IP in a simpler way
    val data = get("http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=lemon&page=1&pagesize=20&showtype=1").text
    println("lll")

    println(data)
    println("111")

}