package com.example.googlemapexecapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.StringBuilder

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //クラス内のprivate定数を宣言するために、CompanionObjectブロックとする
    companion object {
        private const val DATABASE_NAME = "cocktailmemo.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        //テーブル作成用SQL文字列の作成
        val sb = StringBuilder()
        sb.append("CREATE TABLE cocktailmemos(")
        sb.append("_id TEXT PRIMARY KEY, ")
        sb.append("keyword TEXT, ")
        sb.append(");")
        val sql = sb.toString()

        //SQLの実行
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}