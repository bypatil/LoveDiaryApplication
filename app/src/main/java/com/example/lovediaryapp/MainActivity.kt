package com.example.lovediaryapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var diaryList: MutableList<DiaryEntry>
    private lateinit var adapter: DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 💕 Animated background
        val bg = findViewById<RelativeLayout>(R.id.rootLayout)
        val animDrawable = bg.background as? AnimationDrawable
        animDrawable?.apply {
            setEnterFadeDuration(2500)
            setExitFadeDuration(2500)
            start()
        }

        // Load saved diary entries
        diaryList = loadEntries()

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DiaryAdapter(diaryList, object : DiaryAdapter.OnItemClickListener {
            override fun onEdit(entry: DiaryEntry, position: Int) {
                val intent = Intent(this@MainActivity, AddEntryActivity::class.java)
                intent.putExtra("id", entry.id)
                intent.putExtra("title", entry.title)
                intent.putExtra("content", entry.content)
                intent.putExtra("position", position)
                startActivityForResult(intent, 101)
            }

            override fun onDelete(entry: DiaryEntry, position: Int) {
                diaryList.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveEntries()
            }
        })
        recyclerView.adapter = adapter

        // Add new note
        val addButton = findViewById<FloatingActionButton>(R.id.fabAdd)
        addButton.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    // Handle new/edit results
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val id = data.getIntExtra("id", 0)
            val title = data.getStringExtra("title") ?: ""
            val content = data.getStringExtra("content") ?: ""
            val date = data.getStringExtra("date") ?: ""
            val position = data.getIntExtra("position", -1)

            if (requestCode == 100) {
                val newEntry = DiaryEntry(id, title, content, date)
                diaryList.add(0, newEntry)
                adapter.notifyItemInserted(0)
            } else if (requestCode == 101 && position != -1) {
                diaryList[position] = DiaryEntry(id, title, content, date)
                adapter.notifyItemChanged(position)
            }
            saveEntries()
        }
    }

    private fun saveEntries() {
        val sharedPref = getSharedPreferences("DiaryPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(diaryList)
        editor.putString("entries", json)
        editor.apply()
    }

    private fun loadEntries(): MutableList<DiaryEntry> {
        val sharedPref = getSharedPreferences("DiaryPrefs", Context.MODE_PRIVATE)
        val json = sharedPref.getString("entries", null)
        val type = object : TypeToken<MutableList<DiaryEntry>>() {}.type
        return if (json != null) Gson().fromJson(json, type) else mutableListOf()
    }
}
