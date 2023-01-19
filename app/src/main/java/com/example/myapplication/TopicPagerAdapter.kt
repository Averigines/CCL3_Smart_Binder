package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

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