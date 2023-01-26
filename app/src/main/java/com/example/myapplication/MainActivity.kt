package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), OnTopicClickListener {
    lateinit var tempListCards: ArrayList<Card>

    /*override fun onResume() {
        super.onResume()
        val newAdapter = CategoryAdapter(categoryWithTopicsWithCards, this, db)
        recyclerView.adapter = newAdapter

    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        //addData(db)

        setUpBottomNav(this, "llHome")

        var categoryWithTopicsWithCards = db.categoryDao().getCategoriesWithTopicsWithCards()
        var allCards = db.cardDao().getAll()
        tempListCards = arrayListOf()
        tempListCards.addAll(allCards)

        val inflater = LayoutInflater.from(this)
        val contentView = inflater.inflate(R.layout.popup_new_category, null)
        val popup = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        val ibClosePopup = contentView.findViewById<ImageButton>(R.id.imageButton)
        ibClosePopup.setOnClickListener { popup.dismiss() }

        val fabAddCategory = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddCategory)
        val btnAddCategory = contentView.findViewById<Button>(R.id.btnAddCategory)
        val etCategory = contentView.findViewById<EditText>(R.id.etCategory)
        val sv = findViewById<SearchView>(R.id.etSearch)

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = CategoryAdapter(categoryWithTopicsWithCards, this, db)

        val rvCards = findViewById<RecyclerView>(R.id.rvCards)
        rvCards.adapter = CardAdapter(tempListCards)

        fabAddCategory.setOnClickListener {
            popup.isOutsideTouchable = false
            popup.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
        }

        btnAddCategory.setOnClickListener {
                val categoryName = etCategory.text.toString()
                val newCategory = Category(0, categoryName)
                db.categoryDao().insert(newCategory)
                categoryWithTopicsWithCards = db.categoryDao().getCategoriesWithTopicsWithCards()
                val newAdapter = CategoryAdapter(categoryWithTopicsWithCards, this, db)
                val currState = recyclerView.layoutManager?.onSaveInstanceState()
                recyclerView.adapter = newAdapter
                recyclerView.layoutManager?.onRestoreInstanceState(currState)
                popup.dismiss()
            }

        sv.setOnQueryTextListener (object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempListCards.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    recyclerView.visibility = View.GONE
                    fabAddCategory.visibility = View.GONE
                    rvCards.visibility = View.VISIBLE
                    allCards = db.cardDao().getAll()
                    allCards.forEach {
                        if(it.title.lowercase(Locale.getDefault()).contains(searchText) || it.content.lowercase(Locale.getDefault()).contains((searchText))) {
                                tempListCards.add(it)
                        }
                    }
                    rvCards.adapter!!.notifyDataSetChanged()
                }
                else {
                    recyclerView.visibility = View.VISIBLE
                    fabAddCategory.visibility = View.VISIBLE
                    rvCards.visibility = View.GONE
                    tempListCards.clear()
                    tempListCards.addAll(allCards)
                    rvCards.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })
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
        db.cardDao().insert(Card(0, "Dust2", "Dust2 is the oldest map in the pool.", allTopics[1].id))
        db.cardDao().insert(Card(0, "Nuke", "Nuke is fun,", allTopics[1].id))
        db.cardDao().insert(Card(0, "Terrorist", "Bad", allTopics[1].id))
        db.cardDao().insert(Card(0, "XC548", "The XC548 is a great beginner driver", allTopics[2].id))

    }
}

