package com.example.lovediaryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        titleEditText = findViewById(R.id.etTitle)
        contentEditText = findViewById(R.id.etContent)
        saveButton = findViewById(R.id.btnSave)

        val entryId = intent.getIntExtra("id", 0)
        val entryTitle = intent.getStringExtra("title")
        val entryContent = intent.getStringExtra("content")
        val position = intent.getIntExtra("position", -1)

        if (entryId != 0) {
            titleEditText.setText(entryTitle)
            contentEditText.setText(entryContent)
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val date = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

            val resultIntent = Intent()
            resultIntent.putExtra("id", if (entryId != 0) entryId else (0..10000).random())
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("content", content)
            resultIntent.putExtra("date", date)
            if (position != -1) resultIntent.putExtra("position", position)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
