package com.example.homesuveilapp_pdm.ui.watering

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.homesuveilapp_pdm.db.*
import com.example.homesuveilapp_pdm.repositories.WateringScheduleRepository

class WateringScheduleViewModel  (app: Application) : AndroidViewModel(app) {

    private val wateringScheduleRepo = WateringScheduleRepository(app)

    suspend fun getSchedules(id: Long) = wateringScheduleRepo.getWateringSchedules(id)

    suspend fun addSchedule(data: WateringSchedule) = wateringScheduleRepo.addSchedule(data)

    suspend fun deleteSchedule(id: Long) = wateringScheduleRepo.deleteSchedule(id)
}