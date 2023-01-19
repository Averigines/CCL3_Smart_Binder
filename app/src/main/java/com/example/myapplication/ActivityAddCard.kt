package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.room.Room

class ActivityAddCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()



        val allCategories = db.categoryDao().getAll()
        val categoryNames = ArrayList<String>()
        //categoryNames.add("")
        for(category in allCategories) {
            categoryNames.add(category.name)
        }

        val spinnerCategories: Spinner = findViewById(R.id.spinnerCategory)
        val spinnerTopics: Spinner = findViewById(R.id.spinnerTopic)
        val spinnerOptions = categoryNames.toTypedArray()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        lateinit var relatedTopics: List<Topic>

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = allCategories[position]
                relatedTopics = db.topicDao().getTopicsOfCategory(selectedCategory.id)
                val topicNames = ArrayList<String>()
                //topicNames.add("")
                for(topic in relatedTopics) {
                    topicNames.add(topic.name)
                }
                val spinnerTopicsOptions = topicNames.toTypedArray()
                val spinnerTopicsAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, spinnerTopicsOptions)
                spinnerTopicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTopics.adapter = spinnerTopicsAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)

        findViewById<Button>(R.id.btnAddCard).setOnClickListener {
            db.cardDao().insert(Card(0,etTitle.text.toString(), etContent.text.toString(),relatedTopics[spinnerTopics.selectedItemPosition].id))
            Toast.makeText(this, "Card successfully added!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}