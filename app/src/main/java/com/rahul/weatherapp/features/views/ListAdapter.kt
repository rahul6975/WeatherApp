package com.rahul.weatherapp.features.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahul.weatherapp.databinding.ItemLayoutBinding
import com.rahul.weatherapp.features.model.SearchList

class ListAdapter(
    private val dataList: ArrayList<SearchList>,
    val callback: (SearchList) -> Unit,
) :
    RecyclerView.Adapter<ListAdapter.VH>() {

    inner class VH(private val view: ItemLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(dataModel: SearchList) {
            view.name.text = dataModel.name
            view.root.setOnClickListener {
                callback.invoke(dataModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                null,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

}