package com.example.homesuveilapp_pdm.ui.lights

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.Light
import com.example.homesuveilapp_pdm.repositories.LightsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LightsViewModel (app: Application) : AndroidViewModel(app) {

    private val lightsRepo = LightsRepository(app)

    suspend fun getLights(id: Long) = lightsRepo.getLights(id)

    suspend fun deleteLight(id: Long) = lightsRepo.deleteLight(id)

    suspend fun addLight(data: Light) = lightsRepo.addLight(data)

    suspend fun setStatus(status: DeviceStatus, id: Long) = lightsRepo.setStatus(status, id)
}