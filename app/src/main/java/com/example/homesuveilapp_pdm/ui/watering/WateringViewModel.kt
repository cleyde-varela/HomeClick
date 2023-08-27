package com.example.homesuveilapp_pdm.ui.watering

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.Watering
import com.example.homesuveilapp_pdm.db.WateringMode
import com.example.homesuveilapp_pdm.repositories.WateringRepository

class WateringViewModel (app: Application) : AndroidViewModel(app) {

    private val wateringRepo = WateringRepository(app)

    suspend fun getAreas(id: Long) = wateringRepo.getAreas(id)

    suspend fun deleteArea(id: Long) = wateringRepo.deleteArea(id)

    suspend fun addArea(data: Watering) = wateringRepo.addArea(data)

    suspend fun setStatus(status: DeviceStatus, id: Long) = wateringRepo.setStatus(status, id)

    suspend fun setIntensity( intensity: Int, id: Long ) = wateringRepo.setIntensity(intensity, id)

    suspend fun setMode( mode: WateringMode, id: Long ) = wateringRepo.setMode(mode, id)
}