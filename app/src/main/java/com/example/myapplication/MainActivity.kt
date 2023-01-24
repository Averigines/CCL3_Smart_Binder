package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainActivity : AppCompatActivity(), OnTopicClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        //addData(db)

        var categoriesWithTopics = db.categoryDao().getCategoriesWithTopics()

        val categoryWithTopicsWithCards = db.categoryDao().getCategoriesWithTopicsWithCards()

        val inflater = LayoutInflater.from(this)
        val contentView = inflater.inflate(R.layout.popup_new_category, null)
        val popup = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        val btnAddCategory = contentView.findViewById<Button>(R.id.btnAddCategory)
        val etCategory = contentView.findViewById<EditText>(R.id.etCategory)

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = CategoryAdapter(categoryWithTopicsWithCards, this, db)

        findViewById<Button>(R.id.btnAddCard).setOnClickListener {
            startActivity(Intent(this, FlipCard::class.java))
        }

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddCategory).setOnClickListener {
            popup.isOutsideTouchable = false
            popup.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
        }

        btnAddCategory.setOnClickListener {
                val categoryName = etCategory.text.toString()
                val newCategory = Category(0, categoryName)
                db.categoryDao().insert(newCategory)
                categoriesWithTopics = db.categoryDao().getCategoriesWithTopics()
                val newAdapter = CategoryAdapter(categoryWithTopicsWithCards, this, db)
                val currState = recyclerView.layoutManager?.onSaveInstanceState()
                recyclerView.adapter = newAdapter
                recyclerView.layoutManager?.onRestoreInstanceState(currState)
                popup.dismiss()
            }
    }
    override fun onTopicClick(topic: Topic) {

        val intent = Intent(this, ActivityCategory::class.java)
        intent.putExtra("topic", topic)
        startActivity(intent)
    }

    private fun addData(db:AppDatabase) {
        db.categoryDao().deleteAll()
        db.topicDao().deleteAll()
        db.cardDao().deleteAll()

        db.categoryDao().insert(Category(0,"Biology"))
        db.categoryDao().insert(Category(0,"Chemistry"))
        db.categoryDao().insert(Category(0,"E-Sports"))
        db.categoryDao().insert(Category(0,"Golf"))
        val allCategories = db.categoryDao().getAll()
        db.topicDao().insert(Topic(0, "League", allCategories[2].id))
        db.topicDao().insert(Topic(0, "CSGO", allCategories[2].id))
        db.topicDao().insert(Topic(0, "Driver", allCategories[3].id))
        db.topicDao().insert(Topic(0, "Putter", allCategories[3].id))
        db.topicDao().insert(Topic(0, "Mammals", allCategories[0].id))
        db.topicDao().insert(Topic(0, "Forest", allCategories[0].id))
        val allTopics = db.topicDao().getAll()
        db.cardDao().insert(Card(0, "Players", "There are 10 players in total.", allTopics[1].id))
        db.cardDao().insert(Card(0, "AK47", "The AK costs 2700.", allTopics[1].id))
        db.cardDao().insert(Card(0, "Money", "You earn more money if you lose.", allTopics[1].id))

    }
}