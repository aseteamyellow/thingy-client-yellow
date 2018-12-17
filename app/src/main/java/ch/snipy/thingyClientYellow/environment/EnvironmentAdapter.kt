package ch.snipy.thingyClientYellow.environment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.EnvironmentsItemViewListener
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.environment.EnvironmentAdapter.EnvironmentItemViewHolder
import kotlinx.android.synthetic.main.environment_list_item.view.*
import android.graphics.BitmapFactory
import android.util.Base64


class EnvironmentAdapter(
    private val dataset: MutableList<Environment>,
    private val context: Context,
    private val listener: EnvironmentsItemViewListener
) : RecyclerView.Adapter<EnvironmentItemViewHolder>() {

    private val loggingTag = "ENVIRONMENT_ADAPTER"

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
        val iconWithoutHeader = dataset[position].icon!!.substring(22)
        val imageBytes = Base64.decode(iconWithoutHeader, 0)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.image.setImageBitmap(decodedImage)
        holder.environment = dataset[position]
        /*holder.deleteButton.setOnClickListener { view ->
            listener.onEnvironmentItemDeleteClick(
                view,
                dataset[position],
                { response ->
                    Log.d(loggingTag, response.string())
                    Toast.makeText(context, "Environment successfully removed", Toast.LENGTH_SHORT).show()
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

    class EnvironmentItemViewHolder(
        val view: View,
        private val listener: EnvironmentsItemViewListener
    ) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        lateinit var environment: Environment

        val nameTextView: TextView = view.environment_item_name
        val image: ImageView = view.environment_item_logo
        //val deleteButton: ImageButton = view.environment_list_item_delete_button

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onEnvironmentItemClick(v!!, environment)
        }
    }
}