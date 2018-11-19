package ch.snipy.thingyClientYellow.environment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.environment.EnvironmentAdapter.ViewHolder
import kotlinx.android.synthetic.main.environment_list_item.view.*

class EnvironmentAdapter(
    private val dataset: List<Environment>,
    val context: Context,
    private val listener: RecyclerViewListener
) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.environment_list_item,
                parent,
                false
            ),
            listener
        )
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = dataset[position].name
    }

    class ViewHolder(val view: View, val listener: RecyclerViewListener) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val nameTextView: TextView = view.environment_item_name
        val image = view.environment_item_logo

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(v!!, layoutPosition)
        }
    }
}