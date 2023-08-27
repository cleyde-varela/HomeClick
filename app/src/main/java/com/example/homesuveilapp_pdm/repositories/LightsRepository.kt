package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.HomeClickDB
import com.example.homesuveilapp_pdm.db.Light

/* Light's repository */
class LightsRepository(app: Application) {

    private val lightsDao = HomeClickDB(app).lightsDao()

    suspend fun getLights(id: Long): List<Light> {
        return lightsDao.getDivisionLights(id)
    }

    suspend fun deleteLight(id: Long) : Int {
        return lightsDao.deleteLight(id)
    }

    suspend fun addLight(data: Light) : Long {
        return lightsDao.insert(data)
    }

    suspend fun setStatus(status: DeviceStatus, id: Long) : Int {
        return lightsDao.setStatus(status, id)
    }
}