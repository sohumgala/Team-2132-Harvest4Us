package com.example.myfirstapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class CustomAdapterCart(private var mList: List<ItemsViewModel>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<CustomAdapterCart.ViewHolder>(), Filterable {

    private var data: List<ItemsViewModel> = mList

    val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    private val client = OkHttpClient()
    var responseString = ""

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_cart, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        var producer = ItemsViewModel.producer
        var dateEdited = ItemsViewModel.dateEdited
        var product_id = ItemsViewModel.product_id
        var quantity = ItemsViewModel.quantity
        var username = ItemsViewModel.consumerUsername

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.produceType

        holder.subtextView.text = ItemsViewModel.produceCategory

        val decimal = BigDecimal(3.14159265359).setScale(2, RoundingMode.HALF_EVEN)
        holder.priceText.setText(BigDecimal(ItemsViewModel.price.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString())

        holder.itemQuantity.setText(ItemsViewModel.quantity.toString())

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(ItemsViewModel)
        }

        holder.deleteButton.setOnClickListener {
            post("https://crwpdbho85.execute-api.us-east-1.amazonaws.com/dev/delete-from-cart", "\"producer\" : \"$producer\", \"consumer\" : \"$username\", \"product_id\" : \"$product_id\", \"quantity\" : \"$quantity\"")
            Thread.sleep(1400)
            val intent = Intent(holder.context, CartActivity::class.java)
            holder.context.startActivity(intent)
        }

        holder.itemQuantity.doAfterTextChanged {
            if (holder.itemQuantity.text.toString() != "") {
                var quantityChange = holder.itemQuantity.text.toString().toInt() - quantity
                if (quantityChange < 0) {
                    quantityChange *= -1
                    post(
                        "https://crwpdbho85.execute-api.us-east-1.amazonaws.com/dev/delete-from-cart",
                        "\"producer\" : \"$producer\", \"consumer\" : \"$username\", \"product_id\" : \"$product_id\", \"quantity\" : \"$quantityChange\""
                    )
                    Thread.sleep(1400)
                    val intent = Intent(holder.context, CartActivity::class.java)
                    holder.context.startActivity(intent)
                } else if (quantityChange > 0) {
                    post(
                        "https://crwpdbho85.execute-api.us-east-1.amazonaws.com/dev/add-to-cart",
                        "\"consumer\" : \"$username\", \"producer\" : \"$producer\", \"product_id\" : \"$product_id\", \"date_added\" : \"$dateEdited\", \"quantity\" : \"$quantityChange\""
                    )
                    Thread.sleep(1400)
                    val intent = Intent(holder.context, CartActivity::class.java)
                    holder.context.startActivity(intent)
                }
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var producer = ""
        var product_id = 0

        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val subtextView: TextView = itemView.findViewById(R.id.subtextView)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val itemQuantity: EditText = itemView.findViewById(R.id.itemQuantity)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val context = itemView.context
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

    fun post(url: String, json: String) {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                responseString = response.body?.string()!!
                println(responseString)
            }
        })
    }
}
