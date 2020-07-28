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

    /**
     * キーワードリストのスタート処理
     * Databaseにデータがあれば、それを取得して表示する
     *
     * @param db
     */
    public fun startList(db: SQLiteDatabase){

        //主キーによる検索SQL文字列の用意
        val sql = "SELECT * FROM cocktailmemos"
        //SQLの実行(クエリ)
        val cursor = db.rawQuery(sql, null)

        //データベースから取得した値を格納する変数の用意
        var kw = ""
        var dt = ""

        //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベースのデータを取得する
        while(cursor.moveToNext()) {
            //カラムインデックス値を取得
            val idxkw = cursor.getColumnIndex("keyword")
            //カラムのインデックス値を元に実際のデータを取得
            kw = cursor.getString(idxkw)
            val idxdt = cursor.getColumnIndex("daytime")
            //カラムのインデックス値を元に実際のデータを取得
            dt = cursor.getString(idxdt)

            //-----------------------------
            //キーワードと時刻をリストに登録
            this.add(kw, dt, true)
        }
    }

    /**
* リスト情報をDatabaseに登録する
    *
     * @param db
     */
    public fun saveData(db: SQLiteDatabase){

        //主キーによる前削除
        val sql = "DELETE FROM cocktailmemos"
        //SQLの実行(クエリ)
        db.delete("cocktailmemos", null, null)

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