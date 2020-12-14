package com.github.margawron.epidemicalertapp.api.users.messages

import com.github.margawron.epidemicalertapp.data.users.AccountState
import com.github.margawron.epidemicalertapp.data.users.Role
import java.time.Instant

class UserDto(
    var id: Long?,
    var username: String?,
    var useremail: String?,
    var role: Role,
    var accountCreationDate: Instant?,
    var accountExpirationDate: Instant?,
    var accountState: AccountState
)