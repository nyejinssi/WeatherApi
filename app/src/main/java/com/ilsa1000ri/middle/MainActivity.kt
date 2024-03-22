package com.ilsa1000ri.middle

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 텍스트뷰 참조 초기화
        val txtUVIndex = findViewById<TextView>(R.id.txtUVIndex)
        val txtAirIndex = findViewById<TextView>(R.id.txtAirIndex)
        val txtWeekIndex = findViewById<TextView>(R.id.txtWEEKIndex)
        val txtLandIndex = findViewById<TextView>(R.id.txtLandIndex)
        val txtShortIndex = findViewById<TextView>(R.id.txtShortIndex)

        // UV 지수 가져오기
        GlobalScope.launch(Dispatchers.Main) {
            val uvIndex = SunApi.getUVIndex()
            val AirIndex = AirApi.getAirIndex()
            val WeekIndex = ThisWeekApi.getWEEKIndex()
            val LandIndex = MidLandApi.getLandIndex()
            val ShortIndex = ShortApi.getShortIndex()

            txtUVIndex.text = uvIndex
            txtAirIndex.text = AirIndex
            txtWeekIndex.text = WeekIndex.joinToString(", ")
            txtLandIndex.text = LandIndex.joinToString(", ")
            txtShortIndex.text = ShortIndex.joinToString( ", " )
        }
    }
}