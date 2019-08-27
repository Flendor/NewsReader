package com.example.newsreader

object AllData {
    private val titles: ArrayList<String> = arrayListOf()
    private val urls: ArrayList<String> = arrayListOf()

    fun getTitles(): ArrayList<String> {
        return titles
    }

    fun getUrls(): ArrayList<String>{
        return urls
    }
}