package com.example.googlemapexecapp

import android.database.sqlite.SQLiteDatabase
import android.text.format.DateFormat
import java.util.*

class KeywordList {

    //キーワードの情報が格納されたリスト
    private val keywordList: MutableList<MutableMap<String, String>> = mutableListOf()

    init {

    }

    public fun get() :MutableList<MutableMap<String, String>> {
        return keywordList
    }

    /**
     *
     */
    public fun add(keyword: String, daytime: String, isAddLast: Boolean = false) {

        run loop@{
            //重複を防ぐために、同じKeywordの要素を削除し、先頭に追加する
            if (keywordList.isNotEmpty()) {
                keywordList.forEach {
                    if (it.containsValue(keyword)) {
                        keywordList.remove(it)
                        return@loop
                    }
                }
            }
        }

        val settime: String

        if (daytime == "") {
            //現在時刻取得
            val date: Date = Date()
            settime = DateFormat.format("yyyy/MM/dd kk:mm:ss", date).toString()
        }
        else {
            settime = daytime
        }

        //-----------------------------
        //新たに要素を先頭に追加する
        val elmnt =  mutableMapOf("keyword" to keyword, "daytime" to settime)

        if (isAddLast) {
            keywordList.add(elmnt)
        }
        else {
            keywordList.add(0, elmnt)
        }

    }


    public fun saveData(db: SQLiteDatabase){

        //重複を防ぐために、同じKeywordの要素を削除し、先頭に追加する
        if (keywordList.isNotEmpty()) {

            //インサート用SQL文字列の用意
            val sqlInsert = "INSERT INTO cocktailmemos (keyword, daytime) VALUES (?, ?)"
            //プリペアードステートメントを取得
            var stmt = db.compileStatement(sqlInsert)

            keywordList.forEach {
                val keyword = it["keyword"]
                val daytime = it["daytime"]

                stmt.bindString(1, keyword)
                stmt.bindString(2, daytime)

                //インサートSQLの実行
                stmt.executeInsert()
            }
        }
    }
}