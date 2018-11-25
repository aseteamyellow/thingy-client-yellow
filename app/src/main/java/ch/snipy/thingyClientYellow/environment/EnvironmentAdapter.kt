package ch.snipy.thingyClientYellow.environment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.EnvironmentsItemViewListener
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.environment.EnvironmentAdapter.EnvironmentItemViewHolder
import kotlinx.android.synthetic.main.environment_list_item.view.*

class EnvironmentAdapter(
    private val dataset: List<Environment>,
    private val context: Context,
    private val listener: EnvironmentsItemViewListener
) : RecyclerView.Adapter<EnvironmentItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvironmentItemViewHolder {
        return EnvironmentItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.environment_list_item,
                parent,
                false
            ),
            listener
        )
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: EnvironmentItemViewHolder, position: Int) {
        holder.nameTextView.text = dataset[position].name
        holder.environment = dataset[position]
        holder.deleteButton.setOnClickListener { view ->
            listener.onEnvironmentItemDeleteClick(
                view,
                dataset[position]
            )
        }
    }

    class EnvironmentItemViewHolder(
        val view: View,
        private val listener: EnvironmentsItemViewListener
    ) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        lateinit var environment: Environment

        val nameTextView: TextView = view.environment_item_name
        val image: ImageView = view.environment_item_logo
        val deleteButton: ImageButton = view.environment_list_item_delete_button

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onEnvironmentItemClick(v!!, environment)
        }
    }
}