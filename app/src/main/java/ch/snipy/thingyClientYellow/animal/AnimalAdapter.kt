package ch.snipy.thingyClientYellow.animal

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.*
import kotlinx.android.synthetic.main.animal_list_item.view.*

class AnimalAdapter (
    private val dataset: MutableList<Animal>,
    private val context: Context,
    private val listener: AnimalsItemViewListener,
    private val animalTypes: List<AnimalType>
) :
        RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    private val loggingTag = "ANIMAL_ADAPTER"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAdapter.ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.animal_list_item,
            parent,
            false
        ),
        listener
    )

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: AnimalAdapter.ViewHolder, position: Int) {

        holder.nameTextView.text = dataset[position].name
        holder.image.setImageResource(R.drawable.ic_android_red_64dp)

        holder.animal = dataset[position]

        val iconWithoutHeader = animalTypes[dataset[position].animalTypeId].icon!!.substring(22)
        val imageBytes = Base64.decode(iconWithoutHeader, 0)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.image.setImageBitmap(decodedImage)

        /*holder.deleteButton.setOnClickListener { view ->
            listener.onAnimalItemDeleteClick(
                view,
                dataset[position],
                { response ->
                    Log.d(loggingTag, response.string())
                    Toast.makeText(context, "Animal successfully removed", Toast.LENGTH_SHORT).show()
                    dataset.removeAt(position)
                    notifyDataSetChanged()
                },
                { error ->
                    Log.e(loggingTag, error.message)
                    Toast.makeText(context, "Can't remove item : ${dataset[position]}", Toast.LENGTH_SHORT).show()
                }
            )
        }*/
    }

    class ViewHolder(
        val view: View,
        private val listener: AnimalsItemViewListener
    ) :
            RecyclerView.ViewHolder(view),
            View.OnClickListener {

        lateinit var animal: Animal

        val nameTextView: TextView = view.animal_item_name
        val image: ImageView = view.animal_item_logo
        //val deleteButton: ImageButton = view.animal_list_item_delete_button

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onAnimalItemClick(v!!, animal)
        }
    }
}