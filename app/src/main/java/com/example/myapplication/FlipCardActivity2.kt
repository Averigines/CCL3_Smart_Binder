package com.example.myapplication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


data class CardModel(
    var Category: String,
    var subCategory: String,
    var cardTitle: String,
    var cardContent: String
)

data class CardViewModel(
    var Front: CardView,
    var Back: CardView,
    var FrontTitle: TextView,
    var FrontContent: TextView,
    var BackContent: TextView
)
/*
class FlipCard : AppCompatActivity() {

    //declaration for card animation
    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private lateinit var fadeAnim: AnimatorSet
    private var isFront: Boolean = true

    private lateinit var cardList: MutableList<CardModel>
    private lateinit var cardToDo: MutableList<CardViewModel>
    private lateinit var cardElements: MutableList<CardViewModel>
    private lateinit var cardFront: CardView
    private lateinit var cardBack: CardView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card)

        cardList = addCards() // generate list of cards (max size = 5)
        cardToDo = cardElements

        /* hideAllButFirst(cardElements) // turn all element from cards to do list to invisible but the first one
        animInit() // Init of the animations

        // flip card animation findViewById
        val btnFlip: Button = findViewById(R.id.btn_flip)
        cardFront = cardElements[0].Front
        cardBack = cardElements[0].Back


        btnFlip.setOnClickListener {
            if (isFront) {
                isFront = false
                bindAnimation(cardFront, frontAnim)
                bindAnimation(cardBack, backAnim)
            } else {
                isFront = true
                bindAnimation(cardBack, frontAnim)
                bindAnimation(cardFront, backAnim)
            }
        }

        // touch event on the card
        cardElements.forEach{ card ->
            card.Back.setOnTouchListener(
                View.OnTouchListener { view, event ->

                    // variables to store current configuration of quote card.
                    val displayMetrics = resources.displayMetrics
                    val cardWidth = cardBack.width
                    val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)
                    val minSwipeDistance = 280

                    when (event.action) {

                        MotionEvent.ACTION_MOVE -> {
                            // get x coordinate of the touch
                            val newX = event.rawX

                            if (!isFront) { // check if card is turned
                                // carry out swipe only if newX - cardWidth < cardStart, that is
                                // the card is swiped to the left side, not to the right
                                if (newX - cardWidth < cardStart) { // or newX < cardStart + cardWidth
                                    cardBack.animate()
                                        .x(
                                            min(cardStart,newX - (cardWidth / 2)
                                            ) // for right it should be added
                                        )
                                        .setDuration(0)
                                        .start()
                                }
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            var currentX = cardBack.x
                            if (!isFront) {
                                if (abs(currentX) > minSwipeDistance) {
                                    bindAnimation(cardBack, fadeAnim)
                                    Handler().postDelayed(
                                        {removeFirstAndChangeVisibility(cardToDo)},
                                        200
                                    )
                                    isFront = true
                                } else {
                                    cardBack.animate()
                                        .x(cardStart).duration = 350
                                }

                            }
                        }
                    }

                    // required to by-pass lint warning
                    view.performClick()
                    return@OnTouchListener true
                }
            )
        } */
    }

    private fun bindAnimation(element: CardView, anim: AnimatorSet) {
        anim.setTarget(element)
        anim.start()
    }

    private fun animInit() {
        val scale = applicationContext.resources.displayMetrics.density
        cardElements.forEach { card ->
            card.Front.cameraDistance = 8000 * scale
            card.Back.cameraDistance = 8000 * scale
        }

        frontAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.back_animator
        ) as AnimatorSet
        fadeAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.fade_animator
        ) as AnimatorSet
    }

    private fun addCards() : MutableList<CardModel> {
        val cardList = mutableListOf<CardModel>()

        val card1 = CardModel("Food", "Italian", "Pizza", "Delicious Italian dish made with tomato sauce, cheese and various toppings.")
        val card2 = CardModel("Nature", "Mountains", "Mount Everest", "The highest mountain in the world, located in the Himalayas between Nepal and Tibet.")
        val card3 = CardModel("Sports", "Basketball", "Michael Jordan", "NBA legend and considered by many to be the greatest basketball player of all time.")
        val card4 = CardModel("Animals", "Cats", "Lions", "Big cats from Africa, known for their mane and their roar.")
        val card5 = CardModel("Transportation", "Cars", "Ferrari", "Italian luxury sports car manufacturer known for their speed and design.")

        cardList.add(card1)
        cardList.add(card2)
        cardList.add(card3)
        cardList.add(card4)
        cardList.add(card5)

        return cardList
    }

    private fun hideAllButFirst(list: MutableList<CardViewModel>) {
        list.forEachIndexed{index, card ->
            if(index != 0) {
                card.Front.visibility = View.INVISIBLE
                card.Back.visibility = View.INVISIBLE
            } else {
                card.Front.visibility = View.VISIBLE
                card.Back.visibility = View.VISIBLE
            }
        }
    }

    private fun removeFirstAndChangeVisibility(list: MutableList<CardViewModel>) {
        list.removeAt(0)
        if (list.isNotEmpty()) {
            cardFront = list[0].Front
            cardBack = list[0].Back
        }
        hideAllButFirst(list)
    }
}
 */