package ch.snipy.thingyClientYellow.animal

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.AnimalsItemViewListener
import kotlinx.android.synthetic.main.animal_list_item.view.*

class AnimalAdapter(
    private val dataset: Array<Animal>,
    val context: Context,
    private val listener: AnimalsItemViewListener
) :
    RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAdapter.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: AnimalAdapter.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(
        val view: View,
        private val listener: AnimalsItemViewListener
    ) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        lateinit var animal: Animal

        val nameTextView: TextView = view.animal_item_name
        val image: ImageView = view.animal_item_logo

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(v!!, animal)
        }
    }
}