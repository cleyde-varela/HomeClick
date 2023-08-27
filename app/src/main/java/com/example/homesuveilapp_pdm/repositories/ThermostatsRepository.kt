package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.*

/* Thermostat's repository */
class ThermostatsRepository(app: Application) {

    private val thermostatDao = HomeClickDB(app).thermostatDao()

    suspend fun getAllThermostats(id: Long): List<Thermostat> {
        return thermostatDao.getDivisionThermostat(id)
    }

    suspend fun deleteThermostat(id: Long) : Int {
        return thermostatDao.deleteThermostat(id)
    }

    suspend fun addThermostat(data: Thermostat) : Long {
        return thermostatDao.insert(data)
    }

    suspend fun setStatus(status: DeviceStatus, id: Long) : Int {
        return thermostatDao.setStatus(status, id)
    }

    suspend fun setTemperature(temperature: Int, id: Long) : Int {
        return thermostatDao.setTemperature(temperature, id)
    }

    suspend fun setMode(mode: ThermostatMode, id: Long) : Int {
        return thermostatDao.setMode(mode, id)
    }
}