package com.example.googlemapexecapp

class KeywordList {

    //キーワードの情報が格納されたリスト
    private val keywordList: MutableList<MutableMap<String, String>> = mutableListOf()


    public fun get() :MutableList<MutableMap<String, String>> = keywordList

    public fun add(keyword: String, daytime: String) {
        val elmnt =  mutableMapOf("keyword" to keyword, "daytime" to daytime)
        keywordList.add(0, elmnt)
    }
}