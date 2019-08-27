package com.example.newsreader

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase

class Database(private val myContext: Context) {

    private var myDatabase: SQLiteDatabase = myContext.openOrCreateDatabase("NewsReader", MODE_PRIVATE, null)

    fun getMyDatabase(): SQLiteDatabase {
        return myDatabase
    }
}