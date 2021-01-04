package com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter

import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.data.users.Role

class UserChangePrivilegesViewModel(
    private val id: Long,
    private val username: String,
    private val mail: String,
    private val role: Role,
    private val onClickCallback: OnSetRoleClickCallback,
    val roles: List<String>,
): ViewModel(){
    var rolePosition = role.ordinal

    fun getId() = "Identyfikator: $id"
    fun getUsername() = "Nazwa użytkownika: $username"
    fun getMail() = "Mail: $mail"

    fun onSetRoleClick(){
        rolePosition.let{
            onClickCallback.onSetRoleClick(id, Role.values()[it])
        }
    }

    fun interface OnSetRoleClickCallback{
        fun onSetRoleClick(id: Long, role: Role)
    }
}