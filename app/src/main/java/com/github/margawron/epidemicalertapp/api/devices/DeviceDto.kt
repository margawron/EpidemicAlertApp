package com.github.margawron.epidemicalertapp.api.devices

data class DeviceDto(
    var id: Long?,
    var manufacturer: String,
    var firebaseToken: String,
    var deviceName: String,
    var serialNumber: String,
    var deviceOwnerId: Long
)