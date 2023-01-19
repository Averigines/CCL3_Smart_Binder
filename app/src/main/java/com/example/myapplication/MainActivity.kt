package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        addData(db)

        val cl = findViewById<ConstraintLayout>(R.id.clMain)
        val allTopics = db.topicDao().getAll()
        val categoriesWithTopics = db.categoryDao().getCategoriesWithTopics()
        val allCards = db.cardDao().getAll()

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = CategoryAdapter(categoriesWithTopics, this)

        findViewById<Button>(R.id.btnAddCard).setOnClickListener {
            startActivity(Intent(this, ActivityAddCard::class.java))
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