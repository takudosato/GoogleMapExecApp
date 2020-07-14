package com.example.googlemapexecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem

class GPSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("GPSActivity", "onCreate in")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)

        //「戻る」メニューボタンを表示する
        //supportActionBarはNullableなので、セーフコール演算子を使ってメソッドを呼び出す
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.d("GPSActivity", "onCreate out")
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
}