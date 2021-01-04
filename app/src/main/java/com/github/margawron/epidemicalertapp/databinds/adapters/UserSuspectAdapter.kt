package com.github.margawron.epidemicalertapp.databinds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.margawron.epidemicalertapp.databinding.UserMarkSuspectItemBinding
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.UserMarkSuspectViewModel

class UserSuspectAdapter(private val items: List<UserMarkSuspectViewModel>) :
    RecyclerView.Adapter<UserSuspectAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserMarkSuspectItemBinding.inflate(inflater,parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: UserMarkSuspectItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: UserMarkSuspectViewModel){
            binding.vm = item
            binding.executePendingBindings()
        }
    }
}