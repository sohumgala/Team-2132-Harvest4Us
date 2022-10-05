package com.amm.harvest4us

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CustomAdapterResource(private val mList: List<ResourceViewModel>, private val cellClickListenerResource: CellClickListenerResource) : RecyclerView.Adapter<CustomAdapterResource.ViewHolder>(), Filterable {

    private var data: List<ResourceViewModel> = mList

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_resource, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ResourceViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ResourceViewModel.produceType

        holder.subtextView.text = ResourceViewModel.description

        holder.itemView.setOnClickListener {
            cellClickListenerResource.onCellClickListener(ResourceViewModel)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val subtextView: TextView = itemView.findViewById(R.id.subtextView)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                data = filterResults.values as List<ResourceViewModel>
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
