package me.mehdi.countries

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.mehdi.countries.db.Country

class CountryListAdapter internal constructor(
    context: Context
): RecyclerView.Adapter<CountryListAdapter.ViewHolder> (){

    private val inflater = LayoutInflater.from(context)
    private var countries = emptyList<Country>()
    lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder (view){
        val title: TextView? = view.findViewById(R.id.itemTitle)
        val image: ImageView?  = view.findViewById(R.id.itemImage)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_country_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title!!.text = countries[position].name
        //holder.image.setImageResource(countries[position].imageUri)
        holder.image?.setOnClickListener{
            itemClickListener.onClick(position)
        }

    }

    public fun setCountries(countries: List<Country>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onClick(position: Int);
    }

}