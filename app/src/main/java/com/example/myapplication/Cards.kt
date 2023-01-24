import android.content.Context
import android.os.Parcelable
import androidx.cardview.widget.CardView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Cards(
    val category: String,
    val topic: String,
    val title: String,
    val content: String
    ): Parcelable {

    companion object {

        fun cardsList(context: Context) : List<Cards> {
            return listOf(
                Cards("Food", "Italian", "Pizza", "Delicious Italian dish made with tomato sauce, cheese and various toppings."),
                Cards("Nature", "Mountains", "Mount Everest", "The highest mountain in the world, located in the Himalayas between Nepal and Tibet."),
                Cards("Sports", "Basketball", "Michael Jordan", "NBA legend and considered by many to be the greatest basketball player of all time."),
                Cards("Animals", "Cats", "Lions", "Big cats from Africa, known for their mane and their roar."),
                Cards("Transportation", "Cars", "Ferrari", "Italian luxury sports car manufacturer known for their speed and design.")
            )
        }
    }
}
