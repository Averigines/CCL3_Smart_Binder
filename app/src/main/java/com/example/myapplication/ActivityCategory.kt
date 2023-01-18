package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class TopicPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val topics: List<Topic>, private val activeTopic: Topic) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun createFragment(position: Int): Fragment {
        val topic = topics[position]
        return TopicFragment.newInstance(topic)
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

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = TopicPagerAdapter(supportFragmentManager, lifecycle, topics, activeTopic)
        viewPager.currentItem = activeTopicId
    }
}