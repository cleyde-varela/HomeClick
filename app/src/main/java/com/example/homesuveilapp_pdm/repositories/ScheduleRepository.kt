package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.HomeClickDB
import com.example.homesuveilapp_pdm.db.Schedule

/* Schedule's Repository */
class ScheduleRepository(app: Application) {

    private val scheduleDao = HomeClickDB(app).scheduleDao()

    suspend fun getSchedules(id: Long) : List<Schedule> {
        return scheduleDao.getThermostatSchedules(id)
    }

    suspend fun addSchedule(data: Schedule): Long {
        return scheduleDao.insert(data)
    }

    suspend fun deleteSchedule(id: Long): Int {
        return scheduleDao.deleteSchedule(id)
    }
}