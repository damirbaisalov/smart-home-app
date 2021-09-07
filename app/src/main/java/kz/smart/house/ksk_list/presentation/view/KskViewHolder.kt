package kz.smart.house.ksk_list.presentation.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.ksk_list.models.KskApiData

class KskViewHolder(
    itemView: View,
    private val kskItemClickListener: KskItemClickListener
): RecyclerView.ViewHolder(itemView) {

    private val kskItemLayout: LinearLayout = itemView.findViewById(R.id.ksk_item_linear_layout)
    private val kskName: TextView = itemView.findViewById(R.id.ksk_item_name)

    fun onBind(kskApiData: KskApiData) {

        kskName.text = kskApiData.kskName

        kskItemLayout.setOnClickListener {
            kskItemClickListener.onKskClick(kskApiData.id)
        }
    }
}