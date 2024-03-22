package com.ilsa1000ri.middle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object AirApi {
    private const val BASE_URL = "http://apis.data.go.kr/1360000/LivingWthrIdxServiceV4/getAirDiffusionIdxV4"
    private const val SERVICE_KEY = ""
    private const val AREA_NO = "1100000000"
    private const val TIME = "2024032209"
    private const val DATA_TYPE = "xml"

    suspend fun getAirIndex(): String {
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

                val regex = Regex("<h3>(.*?)</h3>")
                val SunIndexValue = regex.find(xmlString)?.groupValues?.get(1) ?: ""

                // Convert UV index value to risk level text
                getSunIndexRiskLevelText(SunIndexValue)
            } else {
                // Error handling if necessary
                ""
            }
        }
    }

    private fun getSunIndexRiskLevelText(uvIndexValue: String): String {
        val sunIndex = uvIndexValue.toIntOrNull() ?: return ""

        return when (sunIndex) {
            100 -> "낮음"
            75 -> "보통"
            50 -> "높음"
            25 -> "매우높음"
            else -> "기타"
        }
    }
}
