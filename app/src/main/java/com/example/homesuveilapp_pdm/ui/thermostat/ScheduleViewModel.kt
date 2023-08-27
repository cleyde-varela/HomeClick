package com.example.homesuveilapp_pdm.ui.thermostat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.homesuveilapp_pdm.db.Schedule
import com.example.homesuveilapp_pdm.repositories.ScheduleRepository

class ScheduleViewModel (app: Application) : AndroidViewModel(app) {

    private val scheduleRepo = ScheduleRepository(app)

    suspend fun getSchedules(id: Long) = scheduleRepo.getSchedules(id)

    suspend fun addSchedule(data: Schedule) = scheduleRepo.addSchedule(data)

    suspend fun deleteSchedule(id: Long) = scheduleRepo.deleteSchedule(id)
}