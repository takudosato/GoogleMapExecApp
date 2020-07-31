package com.example.googlemapexecapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat

class GPSActivity : AppCompatActivity() {

    //GPS情報を持つクラス
    private lateinit var gps: GPS

    var uriStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("GPSActivity", "onCreate in")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)

        //GPS値の初期化
        uriStr = ""

        //「戻る」メニューボタンを表示する
        //supportActionBarはNullableなので、セーフコール演算子を使ってメソッドを呼び出す
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //GPSによる位置情報取得処理のスター
        gps = GPS(this)
        if(!gps.StartGPS()) {
            return
        }

        //新たなGPS位置情報を受信したら、表示を更新する
        gps.changeGPSpos = {
            dispGPSInfo()
        }


        Log.d("GPSActivity", "onCreate out")
    }

    /**
     * dispGPSInfo
     * GPS情報を表示する
     */
    private fun dispGPSInfo() {
        //GPS情報を表示
        val tvlatitude = findViewById<TextView>(R.id.tvlatitudevalue)
        tvlatitude.text = gps._latitude.toString()
        val tvlongitude = findViewById<TextView>(R.id.tvlongitudevalue)
        tvlongitude.text = gps._longitude.toString()
    }

    public fun onChangeDPS() {

    }

    /**
     * onOptionsItemSelected
     *
     * 「戻る」オプションボタン押下時
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("GPSA", "onOptionsItemSelected in")

        //戻るボタンはandroid.R.id.home
        if (item.itemId == android.R.id.home) {

            val result = Intent()
            result.putExtra("ruiStr", uriStr)
            setResult(Activity.RESULT_OK, result)

            Log.d("GPSActivity", "onOptionsItemSelected finish()")
            finish()
        }

        Log.d("GPSActivity", "onOptionsItemSelected out")

        //親クラスの戻り値
        return super.onOptionsItemSelected(item)
    }

    /**
     * onMapShowButtonClick
     *
     * @param view
     */
    fun onMapShowButtonClick(view: View) {

        Log.d("GPSActivity", "onMapShowButtonClick in")

        //GPS情報からURIを作成
        uriStr = "geo:${gps._latitude},${gps._longitude}"
        val uri = Uri.parse(uriStr)

        //URIでgooglemapを起動
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)

        Log.d("GPSActivity", "onMapShowButtonClick out")
    }
}
