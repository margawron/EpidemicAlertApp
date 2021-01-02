package com.github.margawron.epidemicalertapp.databinds.viewmodels.fragment

import android.app.Activity
import android.graphics.Color
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.zones.ZoneDto
import com.github.margawron.epidemicalertapp.api.zones.ZoneEmergencyLevel
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.data.zones.ZoneRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon
import com.google.maps.android.data.geojson.GeoJsonPolygon
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ZoneFragmentViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository,
    private val zoneRepository: ZoneRepository,
) : ViewModel() {

    companion object {
        var jsonObject: JSONObject? = null
    }

    lateinit var activity: Activity
    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var layer: GeoJsonLayer
    private var zonesLiveData: LiveData<List<ZoneDto>>? = null
    private var marker: Marker? = null
    private lateinit var googleMap: GoogleMap

    fun mapReadyCallback() = OnMapReadyCallback {
        googleMap = it
        loadGeoJson()
        setupOnClickListener()
    }

    private fun loadGeoJson() {
        viewModelScope.launch {
            setupZoom()
            withContext(Dispatchers.IO) {
                initMapLayer()
            }
        }
    }

    private fun setupOnClickListener(){
        googleMap.setOnMapLongClickListener { latLng ->
            marker?.remove()
            val featureContainingLatLng = layer.features.find { feature ->
                when(val geometry = feature.geometry){
                    is GeoJsonMultiPolygon -> {
                        val containingPolygon = geometry.polygons.find{ polygon ->
                            PolyUtil.containsLocation(latLng, polygon.outerBoundaryCoordinates, false)
                        }
                        return@find containingPolygon != null
                    }
                    is GeoJsonPolygon -> {
                        return@find PolyUtil.containsLocation(latLng, geometry.outerBoundaryCoordinates, false)
                    }
                    else -> throw NotImplementedError("Should be more to implement ?")
                }
            }
            val id = featureContainingLatLng?.getProperty("id")?.toLong()
            val zoneWithId = zoneRepository.getZones().find {
                it.id == id
            }
            zoneWithId?.let { zone ->
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                    .title(zone.name)
                    .snippet("${activity.getString(R.string.safety_zone)} ${activity.getString(zone.zoneEmergencyLevel.stringId)}")
                val color = when (zone.zoneEmergencyLevel) {
                    ZoneEmergencyLevel.GREEN -> BitmapDescriptorFactory.HUE_GREEN
                    ZoneEmergencyLevel.YELLOW -> BitmapDescriptorFactory.HUE_YELLOW
                    ZoneEmergencyLevel.RED -> BitmapDescriptorFactory.HUE_RED
                }
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color))
                marker = googleMap.addMarker(markerOptions)
            }
        }
    }

    private suspend fun initMapLayer() {
        if(jsonObject == null) {
            val geoJsonInputStream =
                activity.resources.openRawResource(R.raw.powiaty_mid_res)
            val byteArrayOutputStream = ByteArrayOutputStream()
            geoJsonInputStream.copyTo(byteArrayOutputStream)
            jsonObject = JSONObject(byteArrayOutputStream.toString())
        }
        layer = GeoJsonLayer(googleMap, jsonObject)
        layer.features?.forEach {
            if (it.polygonStyle == null) {
                val style = GeoJsonPolygonStyle()
                it.polygonStyle = style
            }
            it.polygonStyle.strokeWidth = 1f
        }
        withContext(Dispatchers.Main){
            layer.addLayerToMap()
        }
        refreshZoneColors(layer)
    }
    private suspend fun refreshZoneColors(layer: GeoJsonLayer) {
        zoneRepository.fetchZones()
        zonesLiveData = if(zonesLiveData == null) {
            zoneRepository.getZonesLiveData()
        }else{
            zonesLiveData?.removeObservers(lifecycleOwner)
            zoneRepository.getZonesLiveData()
        }
        val zonesObserver = Observer<List<ZoneDto>> { zones ->
            zones.forEach { zone ->
                val matchingZone = layer.features.find {
                    it.getProperty("id").toLong() == zone.id
                }
                matchingZone?.let {
                    if (it.polygonStyle == null) {
                        val style = GeoJsonPolygonStyle()
                        it.polygonStyle = style
                    }
                    it.polygonStyle.fillColor = when (zone.zoneEmergencyLevel) {
                        ZoneEmergencyLevel.GREEN -> makeColorTransparent(Color.GREEN)
                        ZoneEmergencyLevel.YELLOW -> makeColorTransparent(Color.YELLOW)
                        ZoneEmergencyLevel.RED -> makeColorTransparent(Color.RED)
                    }
                    it.polygonStyle.strokeWidth = 1f
                }
            }
        }
        withContext(Dispatchers.Main){
            zonesLiveData?.observe(lifecycleOwner,zonesObserver)
        }
    }

    private suspend fun setupZoom() {
        val currentLocation = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?> { measurement ->
            measurement?.apply {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        measurement.latitude,
                        measurement.longitude
                    ), 8.0f
                )
                googleMap.animateCamera(cameraUpdate)
                currentLocation.removeObservers(lifecycleOwner)
            }
        }
        withContext(Dispatchers.Main){
            currentLocation.observe(lifecycleOwner, observer)
        }
    }

    private fun makeColorTransparent(color: Int) = (color and 0x00FFFFFF) or 0x55000000
}