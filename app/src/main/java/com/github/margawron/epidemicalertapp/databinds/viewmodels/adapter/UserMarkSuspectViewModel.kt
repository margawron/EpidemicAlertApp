package com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter

import androidx.lifecycle.ViewModel

class UserMarkSuspectViewModel(
    private val id: Long,
    private val name: String,
    private val onUserMark: OnUserMarkClick,
) : ViewModel() {

    fun getName() = "Nazwa użytkownika: $name"

    fun onButtonClick() = onUserMark.onUserMark(id, name)

    fun interface OnUserMarkClick{
        fun onUserMark(id: Long, name:String)
    }
}