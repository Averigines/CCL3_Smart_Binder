package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class TopicPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val topicsWithCards: List<TopicsWithCards>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return topicsWithCards.size
    }

    override fun createFragment(position: Int): Fragment {
        val topic = topicsWithCards[position].topics
        val cards = topicsWithCards[position].cards
        return TopicFragment.newInstance(topic, cards)
    }
}

class ViewPagerCallback(private val recyclerView: RecyclerView, private val topics: List<Topic>, private val db: AppDatabase) : ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)

        // Get the cards for the selected topic
        val topic = topics[position]
        val cards = db.cardDao().getCardsofTopic(topic.id)

        // Update the RecyclerView's adapter with the new data
        recyclerView.adapter = CardAdapter(cards)
    }
}

class ActivityCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val activeTopic = intent.getSerializableExtra("topic") as Topic

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        val topics = db.topicDao().getTopicsOfCategory(activeTopic.categoryId)
        val activeTopicId = topics.indexOf(activeTopic)
        val cards = db.cardDao().getCardsofTopic(activeTopic.id)
        val topicsWithCards = db.topicDao().getTopicWithCards(activeTopic.categoryId)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        //viewPager.adapter = TopicPagerAdapter(supportFragmentManager, lifecycle, topics, cards)
        viewPager.adapter = TopicPagerAdapter(supportFragmentManager, lifecycle, topicsWithCards)
        viewPager.currentItem = activeTopicId
        //val rvCards = findViewById<RecyclerView>(R.id.rvCards)
        //rvCards.adapter = CardAdapter(cards)
        //viewPager.registerOnPageChangeCallback(ViewPagerCallback(rvCards, topics, db))

    }

}