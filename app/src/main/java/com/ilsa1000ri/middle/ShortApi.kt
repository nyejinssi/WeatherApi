package com.ilsa1000ri.middle

import com.ilsa1000ri.middle.MidLandApi.extractValuesFromXml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ShortApi {
    private const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
    private const val SERVICE_KEY = ""
    private const val PAGE_NO = "1"
    private const val NUM_OF_ROWS = "10"
    private const val BASE_DATE = "20240322"
    private const val BASE_TIME = "0600"
    private const val NX = "55"
    private const val NY = "127"

    suspend fun getShortIndex(): List<String> {
        return withContext(Dispatchers.IO) {
            val urlBuilder = StringBuilder(BASE_URL) /* URL */
            urlBuilder.append("?ServiceKey$SERVICE_KEY")
            urlBuilder.append("&numOfRows=$NUM_OF_ROWS")
            urlBuilder.append("&pageNo=$PAGE_NO")
            urlBuilder.append("&base_date=$BASE_DATE")
            urlBuilder.append("&base_time=$BASE_TIME")
            urlBuilder.append("&nx=$NX")
            urlBuilder.append("&ny=$NY")

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
        val categoryRegex = Regex("<category>(.*?)</category>")
        val obsrValueRegex = Regex("<obsrValue>(.*?)</obsrValue>")

        val categoryMatches = categoryRegex.findAll(xmlString)
        val obsrValueMatches = obsrValueRegex.findAll(xmlString)

        val result = mutableListOf<String>()

        for (match in categoryMatches.zip(obsrValueMatches)) {
            val category = match.first.groupValues[1]
            val obsrValue = match.second.groupValues[1]
            result.add("$category: $obsrValue")
        }

        return result
    }
}