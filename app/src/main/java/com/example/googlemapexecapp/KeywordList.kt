package com.example.googlemapexecapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.format.DateFormat
import android.util.Log
import java.util.*

class KeywordList(context: Context) {

    //親Activityクラスを保持
    private val parentContext: Context
    //データベースヘルパーオブジェクト
    private val _helper: DatabaseHelper
    //キーワードの情報が格納されたリスト
    private val keywordList: MutableList<MutableMap<String, String>> = mutableListOf()

    /**
     * 初期化処理
     */
    init {
        //Activityのコンテキストを保持
        parentContext = context
        //データベースヘルパーの登録
        _helper = DatabaseHelper(parentContext)
    }

    public fun get() :MutableList<MutableMap<String, String>> {
        Log.d("KeywordList", "get in / out")
        return keywordList
    }

    /**
     * リストに一行登録する
     *
     * @param keyword　検索文字列（キーワード）
     * @param daytime　検索時間
     * @param isAddLast　リストの最後に追加する場合は、ここにtrueとする
     */
    public fun add(keyword: String, daytime: String, isAddLast: Boolean = false) {

        Log.d("KeywordList", "add in")

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

        //リストに追加。リストの最後か先頭かで切り分け
        if (isAddLast) {
            keywordList.add(elmnt)
        }
        else {
            keywordList.add(0, elmnt)
        }
        Log.d("KeywordList", "add out")
    }

    /**
     * キーワードリストのスタート処理
     * Databaseにデータがあれば、それを取得して表示する
     *
     */
    public fun startList(){

        Log.d("KeywordList", "startList in")

        //データベースアクセスのカーソルを取得
        val cursor: Cursor = _helper.getDBCursor()

        //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベースのデータを取得する
        while(cursor.moveToNext()) {
            //カラムインデックス値を取得
            val idxkw = cursor.getColumnIndex(DatabaseHelper.FeedEntry.COLUMN_NAME_KEYWORD)
            //カラムのインデックス値を元に実際のデータを取得
            val keyword = cursor.getString(idxkw)
            val idxdt = cursor.getColumnIndex(DatabaseHelper.FeedEntry.COLUMN_NAME_DAYTIME)
            //カラムのインデックス値を元に実際のデータを取得
            val daytime = cursor.getString(idxdt)

            //-----------------------------
            //キーワードと時刻をリストに登録
            this.add(keyword, daytime, true)
        }
        Log.d("KeywordList", "startList out")
    }

    /**
     * リストのデータをデータベースに登録する
     * データベース登録を全て削除し、新規に現在のリスト情報を登録する
     *
     */
    public fun saveData(){

        Log.d("KeywordList", "saveData in")

        //全てのデータベース情報を削除
        _helper.cleanDBData()

        //データベースに登録
        if (keywordList.isNotEmpty()) {
            keywordList.forEach {
                val keyword = it["keyword"].toString()
                val daytime = it["daytime"].toString()

                //データベースに１データ登録
                _helper.insertDBOnedata(keyword, daytime)
            }
        }
        Log.d("KeywordList", "saveData out")
    }

    /**
     * リストを削除する。データベース上からも削除する
     *
     */
    public fun removeListAll() {

        Log.d("KeywordList", "removeListAll in")

        //全てのデータベース情報を削除
        _helper.cleanDBData()

        //すべてのリスト情報を削除
        keywordList.clear()

        Log.d("KeywordList", "removeListAll out")
    }

    /**
     * データベースヘルパのクローズ処理
     */
    fun close() {
        Log.d("KeywordList", "close in")
        _helper.close()
        Log.d("KeywordList", "close out")
    }
}