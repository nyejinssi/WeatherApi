package com.ilsa1000ri.middle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object SunApi {
    private const val BASE_URL = "http://apis.data.go.kr/1360000/LivingWthrIdxServiceV4/getUVIdxV4"
    private const val SERVICE_KEY = ""
    private const val AREA_NO = "1100000000"
    private const val TIME = "2024032201"
    private const val DATA_TYPE = "xml"

    suspend fun getUVIndex(): String {
        return withContext(Dispatchers.IO) {
            val urlBuilder = StringBuilder(BASE_URL) /* URL */
            urlBuilder.append("?ServiceKey$SERVICE_KEY")
            urlBuilder.append("&ServiceKey=$SERVICE_KEY")
            urlBuilder.append("&areaNo=$AREA_NO")
            urlBuilder.append("&time=$TIME")
            urlBuilder.append("&dataType=$DATA_TYPE")

            val url = URL(urlBuilder.toString())
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Content-type", "application/json")

            val responseCode = conn.responseCode
            if (responseCode >= 200 && responseCode <= 300) {
                val inputStream = conn.inputStream
                val xmlString = inputStream.bufferedReader().use { it.readText() }

                val regex = Regex("<h0>(.*?)</h0>")
                val uvIndexValue = regex.find(xmlString)?.groupValues?.get(1) ?: ""

                // Convert UV index value to risk level text
                getUVIndexRiskLevelText(uvIndexValue)
            } else {
                // Error handling if necessary
                ""
            }
        }
    }

    private fun getUVIndexRiskLevelText(uvIndexValue: String): String {
        val uvIndex = uvIndexValue.toIntOrNull() ?: return ""

        return when {
            uvIndex >= 11 -> "위험"
            uvIndex in 8..10 -> "매우높음"
            uvIndex in 6..7 -> "높음"
            uvIndex in 3..5 -> "보통"
            else -> "낮음"
        }
    }
}
