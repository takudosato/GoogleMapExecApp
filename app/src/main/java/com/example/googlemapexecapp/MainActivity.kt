package com.example.googlemapexecapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate in")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate out")
    }

    /**
     * onCreateOptionsMenu
     *
     * <p>メニュー初期化処理</p>
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        Log.d("MainActivity", "onCreateOptionsMenu in")

        //オプションメニュー用のxmlをインフレイト
        menuInflater.inflate(R.menu.menu_options_list, menu)

        Log.d("MainActivity", "onCreateOptionsMenu out")

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * onOptionsItemSelected
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.d("MainActivity", "onOptionsItemSelected in")

        //メニューで選択された場合
        when(item.itemId) {
            //現在地が選択された場合、GPSActivityに遷移する
            R.id.ofMenuOptionGPS -> {
                //インテントオブジェクトを生成
                val intent = Intent(applicationContext, GPSActivity::class.java)
                //オフジェクトを起動
                startActivity(intent)
                Log.d("MainActivity", "startActivity")
            }

            //終了が選択された場合、終了処理を行う
            R.id.ofMenuOptionExit -> {
                //finishAndRemoveTask()はAPILevel21以降
                //finish()の場合、excludeFromRecents="true"を設定しないとタスク画面の履歴に残る
                Log.d("MainActivity", "onOptionsItemSelected finish()")
                finish()
            }
        }

        Log.d("MainActivity", "onOptionsItemSelected out")

        return super.onOptionsItemSelected(item)
    }

}

/*
Log.d("MA onCreate", "in")
Log.d("MA onCreate", "out")
*/