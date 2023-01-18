package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.room.Room
import androidx.viewpager.widget.ViewPager

class TopicPagerAdapter(fragmentManager: FragmentManager, private val topics: List<Topic>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val topic = topics[position]
        return TopicFragment.newInstance(topic)
    }

    override fun getCount() = topics.size
}

class ActivityCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val bundle = intent.extras
        val categoryID = bundle?.getInt("categoryID")

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        val topics = db.topicDao().getTopicsOfCategory(categoryID!!)


        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = TopicPagerAdapter(supportFragmentManager, topics)
    }
}