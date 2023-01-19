package com.example.myapplication

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.lang.Math.abs

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
        //val rvCards = findViewById<RecyclerView>(R.id.rvCards)
        //rvCards.adapter = CardAdapter(cards)
        //viewPager.registerOnPageChangeCallback(ViewPagerCallback(rvCards, topics, db))

    }
}

class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
    RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int =
        context.resources.getDimension(horizontalMarginInDp).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }

}