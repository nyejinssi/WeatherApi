package com.ilsa1000ri.middle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object MidLandApi {
    private const val BASE_URL = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
    private const val SERVICE_KEY = ""
    private const val PAGE_NO = "1"
    private const val NUMBER_OR_ROWS = "1"
    private const val AREA_NO = "11B10101"
    private const val TIME = "202403220600"

    suspend fun getLandIndex(): List<String> {
        return withContext(Dispatchers.IO) {
            val urlBuilder = StringBuilder(BASE_URL) /* URL */
            urlBuilder.append("?ServiceKey=$SERVICE_KEY")
            urlBuilder.append("&numOfRows=$NUMBER_OR_ROWS")
            urlBuilder.append("&pageNo=$PAGE_NO")
            urlBuilder.append("&regId=$AREA_NO")
            urlBuilder.append("&tmFc=$TIME")

            val url = URL(urlBuilder.toString())
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Content-type", "application/json")

            val responseCode = conn.responseCode
            if (responseCode >= 200 && responseCode <= 300) {
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                val xmlString = br.use { it.readText() }
                conn.disconnect()
                extractValuesFromXml(xmlString)
            } else {
                // Error handling if necessary
                emptyList()
            }
        }
    }

    fun extractValuesFromXml(xmlString: String): List<String> {
        //강수예보
        val day3am = Regex("<rnSt3Am>(.*?)</rnSt3Am>")
        val matchResultday3am = day3am.find(xmlString)
        val rday3amValue = matchResultday3am?.groupValues?.get(1) ?: ""

        val day3pm = Regex("<rnSt3Pm>(.*?)</rnSt3Pm>")
        val matchResultday3pm = day3pm.find(xmlString)
        val rday3pmValue = matchResultday3pm?.groupValues?.get(1) ?: ""

        val day4am = Regex("<rnSt4Am>(.*?)</rnSt4Am>")
        val matchResultday4am = day4am.find(xmlString)
        val rday4amValue = matchResultday4am?.groupValues?.get(1) ?: ""

        val day4pm = Regex("<rnSt4Pm>(.*?)</rnSt4Pm>")
        val matchResultday4pm = day4pm.find(xmlString)
        val rday4pmValue = matchResultday4pm?.groupValues?.get(1) ?: ""

        val day5am = Regex("<rnSt5Am>(.*?)</rnSt5Am>")
        val matchResultday5am = day5am.find(xmlString)
        val rday5amValue = matchResultday5am?.groupValues?.get(1) ?: ""

        val day5pm = Regex("<rnSt5Pm>(.*?)</rnSt5Pm>")
        val matchResultday5pm = day5pm.find(xmlString)
        val rday5pmValue = matchResultday5pm?.groupValues?.get(1) ?: ""
        //날씨 예보
        val wday3am = Regex("<rnSt3Am>(.*?)</rnSt3Am>")
        val wmatchResultday3am = wday3am.find(xmlString)
        val wday3amValue = wmatchResultday3am?.groupValues?.get(1) ?: ""

        val wday3pm = Regex("<rnSt3Pm>(.*?)</rnSt3Pm>")
        val wmatchResultday3pm = wday3pm.find(xmlString)
        val wday3pmValue = wmatchResultday3pm?.groupValues?.get(1) ?: ""

        val wday4am = Regex("<wf4Am>(.*?)</wf4Am>")
        val wmatchResultday4am = wday4am.find(xmlString)
        val wday4amValue = wmatchResultday4am?.groupValues?.get(1) ?: ""

        val wday4pm = Regex("<wf4Pm>(.*?)</wf4Pm>")
        val wmatchResultday4pm = wday4pm.find(xmlString)
        val wday4pmValue = wmatchResultday4pm?.groupValues?.get(1) ?: ""

        val wday5am = Regex("<wf5Am>(.*?)</wf5Am>")
        val wmatchResultday5am = wday5am.find(xmlString)
        val wday5amValue = wmatchResultday5am?.groupValues?.get(1) ?: ""

        val wday5pm = Regex("<wf5Pm>(.*?)</wf5Pm>")
        val wmatchResultday5pm = wday5pm.find(xmlString)
        val wday5pmValue = wmatchResultday5pm?.groupValues?.get(1) ?: ""

        return listOf(rday3amValue, rday3pmValue, rday4amValue, rday4pmValue, rday5amValue, rday3pmValue,
            wday3amValue,wday3pmValue, wday4amValue, wday4pmValue, wday5amValue,wday5pmValue )
    }
}
