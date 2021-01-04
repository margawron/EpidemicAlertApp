package com.github.margawron.epidemicalertapp.databinds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.margawron.epidemicalertapp.databinding.UserChangeRoleItemBinding
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.UserChangePrivilegesViewModel

class UserPrivilegesAdapter(private val items: List<UserChangePrivilegesViewModel>) :
    RecyclerView.Adapter<UserPrivilegesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserChangeRoleItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: UserChangeRoleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserChangePrivilegesViewModel) {
            binding.vm = item
            binding.executePendingBindings()
        }
    }
}