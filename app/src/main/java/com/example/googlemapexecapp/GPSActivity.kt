package com.example.googlemapexecapp

import android.Manifest
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

    //GPS情報
    private var _latitude = 0.0
    private var _longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("GPSActivity", "onCreate in")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)

        //「戻る」メニューボタンを表示する
        //supportActionBarはNullableなので、セーフコール演算子を使ってメソッドを呼び出す
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //GPS機能を有効にする
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = GPSLocationListener()
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        Log.d("GPSActivity", "onCreate out")
    }

    /**
     * GPSLocationListener
     *
     */
    private inner class GPSLocationListener : LocationListener {
        override fun onLocationChanged(p0: Location) {

            Log.d("GPSLocationListener", "onLocationChanged in")

            //GPS情報を取得
            _latitude = p0.latitude
            _longitude = p0.longitude

            //GPS情報を表示
            val tvlatitude = findViewById<TextView>(R.id.tvlatitudevalue)
            tvlatitude.text = _latitude.toString()
            val tvlongitude = findViewById<TextView>(R.id.tvlongitudevalue)
            tvlongitude.text = _longitude.toString()

            Log.d("GPSLocationListener", "onLocationChanged out")
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
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
            Log.d("GPSActivity", "onOptionsItemSelected finish()")
            finish()//ここで関数は終わらない
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
        val uriStr = "geo:${_latitude},${_longitude}"
        val uri = Uri.parse(uriStr)

        //URIでgooglemapを起動
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)

        Log.d("GPSActivity", "onMapShowButtonClick out")
    }
}