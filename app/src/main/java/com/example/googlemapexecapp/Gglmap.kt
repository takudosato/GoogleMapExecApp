package com.example.googlemapexecapp

import android.content.Intent
import android.net.Uri
import java.net.URLEncoder

/**
 * GoogleMapを起動するためのクラス
 *
 */
class Gglmap {

    companion object {

        /**
         * keywordstrの文字列をキーワードとし、Googlemap起動のIntentを返す
         *
         * @param keywordstr
         * @return
         */
        public fun makeIntent(keywordstr: String): Intent {

            //キーワード文字列をURLエンコードする
            val keyword = URLEncoder.encode(keywordstr, "UTF-8")

            //マップアプリと連携する、URIオブジェクトを作成
            // "?q="を使ってURIエンコードされたクエリ文字列を渡したインテントリクエスト
            val uriStr = "geo:0,0?q=${keyword}"
            val gmmIntentUri = Uri.parse(uriStr)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            //mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mapIntent.setPackage("com.google.android.apps.maps")

            return mapIntent
        }
    }
}