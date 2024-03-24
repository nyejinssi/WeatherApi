package com.ilsa1000ri.middle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object RealShortApi {
    private const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
    private const val SERVICE_KEY = ""
    private const val PAGE_NO = "1"
    private const val NUM_OF_ROWS = "10"
    private const val BASE_DATE = "20240324"
    private const val BASE_TIME = "1700"
    private const val NX = "55"
    private const val NY = "127"
    suspend fun getRealShortIndex(): List<String> {
        return withContext(Dispatchers.IO) {
            val urlBuilder = StringBuilder(RealShortApi.BASE_URL) /* URL */
            urlBuilder.append("?ServiceKey=${RealShortApi.SERVICE_KEY}")
            urlBuilder.append("&numOfRows=${RealShortApi.NUM_OF_ROWS}")
            urlBuilder.append("&pageNo=${RealShortApi.PAGE_NO}")
            urlBuilder.append("&base_date=${RealShortApi.BASE_DATE}")
            urlBuilder.append("&base_time=${RealShortApi.BASE_TIME}")
            urlBuilder.append("&nx=${RealShortApi.NX}")
            urlBuilder.append("&ny=${RealShortApi.NY}")

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
        val obsrValueRegex = Regex("<fcstValue>(.*?)</fcstValue>")
        val fcstDateRegex = Regex("<fcstDate>(.*?)</fcstDate>")
        val fcstTimeRegex = Regex("<fcstTime>(.*?)</fcstTime>")

        val categoryMatches = categoryRegex.findAll(xmlString)
        val fcstValueMatches = obsrValueRegex.findAll(xmlString)
        val fcstDateMatches = fcstDateRegex.findAll(xmlString)
        val fcstTimeMatches = fcstTimeRegex.findAll(xmlString)

        val result = mutableListOf<String>()

        val categoryIterator = categoryMatches.iterator()
        val fcstValueIterator = fcstValueMatches.iterator()
        val fcstDateIterator = fcstDateMatches.iterator()
        val fcstTimeIterator = fcstTimeMatches.iterator()

        while (categoryIterator.hasNext() && fcstValueIterator.hasNext() && fcstDateIterator.hasNext() && fcstTimeIterator.hasNext()) {
            val categoryMatch = categoryIterator.next()
            val fcstValueMatch = fcstValueIterator.next()
            val fcstDateMatch = fcstDateIterator.next()
            val fcstTimeMatch = fcstTimeIterator.next()

            val category = categoryMatch.groupValues[1]
            val fcstValue = fcstValueMatch.groupValues[1]
            val fcstDate = fcstDateMatch.groupValues[1]
            val fcstTime = fcstTimeMatch.groupValues[1]

            result.add("$category: $fcstValue, Date: $fcstDate, Time: $fcstTime")
        }

        return result
    }
}