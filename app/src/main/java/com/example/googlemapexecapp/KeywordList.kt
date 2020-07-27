package com.example.googlemapexecapp

import android.text.format.DateFormat
import java.util.*

class KeywordList {

    //キーワードの情報が格納されたリスト
    private val keywordList: MutableList<MutableMap<String, String>> = mutableListOf()


    public fun get() :MutableList<MutableMap<String, String>> = keywordList

    /**
     *
     */
    public fun add(keyword: String) {

        //重複を防ぐために、同じKeywordの要素を削除し、先頭に追加する
        if (keywordList.isNotEmpty()) {
            keywordList.forEach {
                if(it.containsValue(keyword))
                {
                    keywordList.remove(it)
                }
            }
        }

        //-----------------------------
        //現在時刻取得
        val date: Date = Date()
        val daytime = DateFormat.format("yyyy/MM/dd kk:mm:ss", date).toString()

        //新たに要素を先頭に追加する
        val elmnt =  mutableMapOf("keyword" to keyword, "daytime" to daytime)
        keywordList.add(0, elmnt)

    }
}