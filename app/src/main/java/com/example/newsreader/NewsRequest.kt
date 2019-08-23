package com.example.newsreader

import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors

class NewsRequest: AsyncTask<String, Void, String>() {

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

    override fun onPostExecute(s: String) {
        super.onPostExecute(s)

        try {
            val newsJsonObject = JSONObject(s)
            val newsTitle: String = newsJsonObject.getString("title")
            val newsUrl: String = newsJsonObject.getString("url")
            val stmt = MainActivity.getMyDatabase().compileStatement("INSERT INTO news(title, url) VALUES (?, ?)")
            stmt.bindString(1, newsTitle)
            stmt.bindString(2, newsUrl)
            stmt.execute()
            MainActivity.getTitles().add(newsTitle)
            MainActivity.getUrls().add(newsUrl)
            MainActivity.getNewsAdapter().notifyDataSetChanged()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }
}