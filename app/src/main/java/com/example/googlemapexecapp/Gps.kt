/*
 *
 */

package com.example.googlemapexecapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat

/**
 * Class GPS
 *
 * @constructor
 *
 * @param parentACT
 */
public class GPS(parentACT: Activity) {

    //親Activityクラスを保持（これでよいのか？？）
    private var parent = parentACT

    //GPS位置情報を保持
    var _latitude = 0.0
    var _longitude = 0.0

    //GPSの位置情報が変わった時にコールバックする口
    var changeGPSpos: (() -> Unit)? = null

    /**
     * StartGPS
     * GPSを有効にする。Permissionの許可が無ければfalseを返す
     *
     * @return
     */
    public fun StartGPS() : Boolean {

        Log.d("GPS", "StartGPS in")

        //GPS機能を有効にする
        val locationManager = parent.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = GPSLocationListener()
        if (ActivityCompat.checkSelfPermission(parent.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("GPS", "StartGPS out false")
            return false
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)



        return true
    }

    /**
     * GPSLocationListener
     * 位置情報の変更時に呼び出される
     *
     */
    private inner class GPSLocationListener : LocationListener {
        override fun onLocationChanged(p0: Location) {

            Log.d("GPSLocationListener", "onLocationChanged in")

            //GPS情報を取得
            _latitude = p0.latitude
            _longitude = p0.longitude

            //コールバックが設定されていたら通知
            changeGPSpos?.invoke()

            Log.d("GPSLocationListener", "onLocationChanged out")
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

}