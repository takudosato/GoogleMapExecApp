package com.example.googlemapexecapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import java.lang.StringBuilder

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //クラス内のprivate定数を宣言するために、CompanionObjectブロックとする
    companion object {
        private const val DATABASE_NAME = "cocktailmemo.db"
        private const val DATABASE_NAME_ACCESS = "cocktailmemo"
        private const val DATABASE_VERSION = 1
    }

    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        //データベースのカラム名文字列
        public const val COLUMN_NAME_KEYWORD = "dbc_keyword"
        public const val COLUMN_NAME_DAYTIME = "dbc_daytime"
    }

    /**
     *  アプリの最初の起動時に、一度だけ呼ばれる
     */
    override fun onCreate(db: SQLiteDatabase) {

        Log.d("DatabaseHelper", "onCreate in")

        //テーブル作成用SQL文字列の作成
        val sb = StringBuilder()
        sb.append("CREATE TABLE ${DATABASE_NAME_ACCESS}(")
        sb.append("${FeedEntry.COLUMN_NAME_KEYWORD} TEXT PRIMARY KEY, ")
        sb.append("${FeedEntry.COLUMN_NAME_DAYTIME} TEXT")
        sb.append(");")
        val sql = sb.toString()

        //SQLの実行
        db.execSQL(sql)
        Log.d("DatabaseHelper", "onCreate out")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    /**
     * データベースの全データを削除する
     *
     */
    fun cleanDBData() {

        Log.d("DatabaseHelper", "cleanDBData in")

        //主キーによる前削除
        val sql = "DELETE FROM ${DATABASE_NAME_ACCESS}"
        //SQLの実行(クエリ)
        writableDatabase.delete(DATABASE_NAME_ACCESS, null, null)

        Log.d("DatabaseHelper", "cleanDBData out")
    }

    fun insertDBOnedata(keyword: String, daytime: String) {

        Log.d("DatabaseHelper", "insertDBOnedata in")

        //インサート用SQL文字列の用意
        val sqlInsert = "INSERT INTO ${DATABASE_NAME_ACCESS} (${FeedEntry.COLUMN_NAME_KEYWORD}, ${FeedEntry.COLUMN_NAME_DAYTIME}) VALUES (?, ?)"
        //プリペアードステートメントを取得
        val stmt = writableDatabase.compileStatement(sqlInsert)

        stmt.bindString(1, keyword)
        stmt.bindString(2, daytime)

        stmt.executeInsert()

        Log.d("DatabaseHelper", "insertDBOnedata out")
    }

    /**
     *　データベースアクセスのカーソルを返す
     */
    fun getDBCursor(): Cursor {

        Log.d("DatabaseHelper", "getDBCursor in")

        //検索SQL文字列の用意
        val sql = "SELECT * FROM ${DATABASE_NAME_ACCESS}"

        Log.d("DatabaseHelper", "getDBCursor out")

        //SQLの実行(クエリ)
        return writableDatabase.rawQuery(sql, null)
    }
}