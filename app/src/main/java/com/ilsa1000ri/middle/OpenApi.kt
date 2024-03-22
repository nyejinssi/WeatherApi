package com.ilsa1000ri.middle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object OpenApi {
    private const val BASE_URL = "api.openweathermap.org/data/2.5/forecast"
    private const val API_KEY = ""
    private const val LAT = "35"
    private const val LON = "128"

    suspend fun getAirIndex(): String {
        return withContext(Dispatchers.IO) {
            val urlBuilder = StringBuilder(OpenApi.BASE_URL) /* URL */
            urlBuilder.append("?lat${OpenApi.LAT}")
            urlBuilder.append("&lon=${OpenApi.LON}")
            urlBuilder.append("&appid=${OpenApi.API_KEY}")

            val url = URL(urlBuilder.toString())
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Content-type", "application/json")

            val responseCode = conn.responseCode
            if (responseCode >= 200 && responseCode <= 300) {
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                val sb = StringBuilder()
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                br.close()
                conn.disconnect()
                sb.toString()
            } else {
                // Error handling if necessary
                ""
            }
        }
    }
}