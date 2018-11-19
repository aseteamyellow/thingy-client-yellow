package ch.snipy.thingyClientYellow.animal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.AnimalsItemViewListener
import ch.snipy.thingyClientYellow.R
import kotlinx.android.synthetic.main.animal_list_item.view.*

class AnimalAdapter(
    private val dataset: List<Animal>,
    private val context: Context,
    private val listener: AnimalsItemViewListener
) :
        RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

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
        holder.image.setImageResource(R.mipmap.default_animal_logo)
        holder.animal = dataset[position]
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

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onAnimalItemClick(v!!, animal)
        }
    }
}