package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.HomeClickDB
import com.example.homesuveilapp_pdm.db.WateringSchedule

class WateringScheduleRepository(app: Application) {

    private val wateringScheduleDao = HomeClickDB(app).wateringScheduleDao()

    suspend fun getWateringSchedules(id: Long) : List<WateringSchedule> {
        return wateringScheduleDao.getWateringSchedules(id)
    }

    suspend fun addSchedule(data: WateringSchedule): Long {
        return wateringScheduleDao.insert(data)
    }

    suspend fun deleteSchedule(id: Long): Int {
        return wateringScheduleDao.deleteSchedule(id)
    }
}