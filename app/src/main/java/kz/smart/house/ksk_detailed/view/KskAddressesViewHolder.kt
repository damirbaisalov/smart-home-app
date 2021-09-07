package kz.smart.house.ksk_detailed.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.registration.models.StreetApiData

class KskAddressesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val kskAddress: TextView = itemView.findViewById(R.id.ksk_address_item_adress_text_view)

    fun onBind(streetApiData: StreetApiData) {
        kskAddress.text = (streetApiData.street + ", " + streetApiData.nomer)
    }
}