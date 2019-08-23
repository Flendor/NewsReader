package com.example.newsreader

import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors

class IdRequest: AsyncTask<String, Void, String>() {

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun doInBackground(vararg urls: String): String? {
        val response: String
        try {
            val url = URL(urls[0])
            val conn = url.openConnection() as HttpURLConnection
            conn.connect()
            val `in` = conn.inputStream
            response = BufferedReader(InputStreamReader(`in`)).lines().collect(Collectors.joining("\n"))
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}