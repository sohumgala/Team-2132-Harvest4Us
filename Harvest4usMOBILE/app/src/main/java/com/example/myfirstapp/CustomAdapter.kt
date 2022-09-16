package com.example.myfirstapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CustomAdapter(private val mList: List<ItemsViewModel>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(), Filterable {

    private var data: List<ItemsViewModel> = mList

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.produceType

        holder.subtextView.text = ItemsViewModel.produceCategory

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(ItemsViewModel)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val subtextView: TextView = itemView.findViewById(R.id.subtextView)
        val imageViewCart: ImageView = itemView.findViewById(R.id.imageViewCart)

        fun ViewHolder(ItemView: View) {
            imageViewCart.setOnClickListener() {
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                data = filterResults.values as List<ItemsViewModel>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) {
                    mList
                } else {
                    mList.filter {
                        it.produceType.lowercase(Locale.getDefault()).contains(queryString)
                    }
                }
                return filterResults
            }
        }
    }
}
