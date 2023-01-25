package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import java.util.*
import kotlin.collections.ArrayList

class ActivityCategory : AppCompatActivity() {
    lateinit var tempList : ArrayList<Card>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "smartBinderDB").allowMainThreadQueries().build()
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        var activeTopic = intent.getSerializableExtra("topic") as Topic
        val topics = db.topicDao().getTopicsOfCategory(activeTopic.categoryId)
        val topicsWithCards = db.topicDao().getTopicWithCards(activeTopic.categoryId)
        val activeTopicId = topics.indexOf(activeTopic)

        // set category name
        val category = db.categoryDao().getById(activeTopic.categoryId)
        val tvCategoryName: TextView = findViewById(R.id.tvCategoryName)
        tvCategoryName.text = category.name

        tempList = arrayListOf()
        for (topic in topicsWithCards) {
            tempList.addAll(topic.cards)
        }
        setupViewPager(viewPager, topicsWithCards, activeTopicId)

        val fabAddCard = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddCard)
        fabAddCard.setOnClickListener {
            val currTopicPos = viewPager.currentItem
            val currTopic = topics[currTopicPos]
            val currCategory = db.categoryDao().getById(currTopic.categoryId)
            val intent = Intent(this, ActivityAddCard::class.java)
            intent.putExtra("topic", currTopic)
            intent.putExtra("category", currCategory)
            startActivity(intent)
        }

        val sv = findViewById<SearchView>(R.id.etSearch)
        val rvCards = findViewById<RecyclerView>(R.id.rvCards)
        rvCards.adapter = CardAdapter(tempList)

        sv.setOnQueryTextListener (object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {

                    rvCards.visibility = View.VISIBLE
                    fabAddCard.visibility = View.GONE
                    viewPager.visibility = View.GONE

                    for (topic in topicsWithCards) {
                        topic.cards.forEach {
                            if(it.title.lowercase(Locale.getDefault()).contains(searchText) || it.content.lowercase(Locale.getDefault()).contains((searchText))) {
                                tempList.add(it)
                            }
                        }
                    }
                    rvCards.adapter!!.notifyDataSetChanged()
                }
                else {
                    rvCards.visibility = View.GONE
                    fabAddCard.visibility = View.VISIBLE
                    viewPager.visibility = View.VISIBLE
                    tempList.clear()
                    for (topic in topicsWithCards) {
                        tempList.addAll(topic.cards)
                    }
                    rvCards.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })
    }


    private fun setupViewPager(viewPager: ViewPager2, topicWithCards: List<TopicWithCards>, activeTopicId: Int) {

        viewPager.adapter = TopicPagerAdapter(supportFragmentManager, lifecycle, topicWithCards)
        viewPager.currentItem = activeTopicId
        viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }
        viewPager.setPageTransformer(pageTransformer)
        val itemDecoration = HorizontalMarginItemDecoration(this, R.dimen.viewpager_current_item_horizontal_margin
        )
        viewPager.addItemDecoration(itemDecoration)

    }
}

