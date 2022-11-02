package com.amm.harvest4us

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.amm.harvest4us.items.CartItem

class CustomAdapterCart(private var cart: CartItem, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<CustomAdapterCart.ViewHolder>()/*, Filterable*/ {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_cart, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produceItem = cart.items[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(produceItem.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = produceItem.produceType

        holder.subtextView.text = produceItem.produceCategory

        holder.priceText.text =
            (produceItem.price * produceItem.quantityInOrder).toString()

        holder.itemQuantity.setText(produceItem.quantityInOrder.toString())

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(produceItem)
        }

        holder.deleteButton.setOnClickListener {
            // backend delete item from cart call will go here, if we end up using this - JC
        }

        holder.itemQuantity.doAfterTextChanged {
            // Looks like this was intended to handle a "quantity modifier" for individual items. - JC
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return cart.items.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var producer = ""
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val subtextView: TextView = itemView.findViewById(R.id.subtextView)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val itemQuantity: EditText = itemView.findViewById(R.id.itemQuantity)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val context = itemView.context
    }

    // This looks like it was intended to "search" your cart? Commented it out for now - JC
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
//                data = filterResults.values as List<ProduceItem>
//                notifyDataSetChanged()
//            }
//
//            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
//                val queryString = charSequence?.toString()?.toLowerCase()
//
//                val filterResults = Filter.FilterResults()
//                filterResults.values = if (queryString == null || queryString.isEmpty()) {
//                    mList
//                } else {
//                    mList.filter {
//                        it.produceType.lowercase(Locale.getDefault()).contains(queryString)
//                    }
//                }
//                return filterResults
//            }
//        }
//    }
}
