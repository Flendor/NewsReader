package com.example.newsreader

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val topNewsUrl: String = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty"
    private val newUrlStart: String = "https://hacker-news.firebaseio.com/v0/item/"
    private val newUrlEnd: String = ".json?print=pretty"
    private lateinit var database: Database

    companion object {
        private lateinit var newsAdapter: ArrayAdapter<String>

        fun getNewsAdapter(): ArrayAdapter<String> {
            return newsAdapter
        }
    }

    private fun requestIds(): ArrayList<String> {
        var unprocessedResult = ""
        val ids: ArrayList<String> = arrayListOf()
        val dataGetter = IdRequest()
        try {
            unprocessedResult = dataGetter.execute(topNewsUrl).get()
            val allIds = JSONArray(unprocessedResult)
            for(i:Int in 0 until allIds.length())
                ids.add(allIds.get(i).toString())
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return ids
    }

    private fun requestNews(ids: ArrayList<String>) {
        for (id: String in ids) {
            try {
                val dataGetter = NewsRequest(database)
                dataGetter.execute(newUrlStart + id + newUrlEnd)
            }
            catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openWebPage(url: String) {
        newsShow.settings.builtInZoomControls = true
        newsShow.settings.javaScriptEnabled = true
        newsShow.webChromeClient = WebChromeClient()
        newsShow.settings.useWideViewPort = true
        newsShow.settings.loadWithOverviewMode = true
        newsShow.loadUrl(url)
    }

    override fun onBackPressed() {
        if(newsShow.visibility == VISIBLE)
        {
            newsShow.visibility = INVISIBLE
            newsList.visibility = VISIBLE
        }
        else {
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            database = Database(this)
            database.getMyDatabase().execSQL("DROP TABLE IF EXISTS news")
            database.getMyDatabase().execSQL("CREATE TABLE IF NOT EXISTS news (title VARCHAR, url VARCHAR, id INT(4) PRIMARY KEY)")

            requestNews(requestIds())
            /*val c: Cursor = myDatabase.rawQuery("SELECT * FROM news", null)
            val titleIndex = c.getColumnIndex("title")
            val urlIndex = c.getColumnIndex("url")
            while (c.moveToNext()) {
                titles.add(c.getString(titleIndex))
                urls.add(c.getString(urlIndex))
            }
            c.close()*/
        }
        catch(e: Exception) {
            e.printStackTrace()
        }

        newsAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllData.getTitles()) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val tv = view.findViewById<View>(android.R.id.text1) as TextView
                tv.setTextColor(Color.BLACK)
                tv.textSize = 18f
                return view
            }
        }

        newsList.adapter = newsAdapter

        newsList.setOnItemClickListener { _, _, position, _ ->
            newsList.visibility = INVISIBLE
            newsShow.visibility = VISIBLE
            openWebPage(AllData.getUrls()[position])
        }
    }
}
