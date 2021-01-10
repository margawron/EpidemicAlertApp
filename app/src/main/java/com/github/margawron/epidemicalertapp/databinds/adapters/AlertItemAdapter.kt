package com.github.margawron.epidemicalertapp.databinds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.margawron.epidemicalertapp.databinding.AlertItemBinding
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.AlertViewModel

class AlertItemAdapter(private val items: List<AlertViewModel>):
    RecyclerView.Adapter<AlertItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlertItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlertViewModel) {
            binding.vm = item
            binding.executePendingBindings()
        }
    }
}