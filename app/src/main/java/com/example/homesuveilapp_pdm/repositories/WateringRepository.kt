package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.*

/* Watering system's repository */
class WateringRepository(app: Application) {

    private val wateringDao = HomeClickDB(app).wateringDao()

    suspend fun getAreas(id: Long): List<Watering> {
        return wateringDao.getWateringAreas(id)
    }

    suspend fun deleteArea(id: Long) : Int {
        return wateringDao.deleteArea(id)
    }

    suspend fun addArea(data: Watering) : Long {
        return wateringDao.insert(data)
    }

    suspend fun setStatus(status: DeviceStatus, id: Long){
        wateringDao.setStatus(status, id)
    }

    suspend fun setIntensity(intensity: Int, id: Long){
        wateringDao.setIntensity(intensity, id)
    }

    suspend fun setMode(mode: WateringMode, id: Long){
        wateringDao.setMode(mode, id)
    }

}