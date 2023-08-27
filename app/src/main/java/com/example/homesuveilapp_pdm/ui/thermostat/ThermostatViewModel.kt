package com.example.homesuveilapp_pdm.ui.thermostat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.Thermostat
import com.example.homesuveilapp_pdm.db.ThermostatMode
import com.example.homesuveilapp_pdm.repositories.ThermostatsRepository

class ThermostatViewModel (app: Application) : AndroidViewModel(app) {

    private val thermostatRepo = ThermostatsRepository(app)

    suspend fun getAllThermostats(id: Long)  = thermostatRepo.getAllThermostats(id)

    suspend fun deleteThermostat(id: Long) = thermostatRepo.deleteThermostat(id)

    suspend fun addThermostat(data: Thermostat) = thermostatRepo.addThermostat(data)

    suspend fun setStatus(status: DeviceStatus, id: Long) = thermostatRepo.setStatus(status, id)

    suspend fun setTemperature(temperature: Int, id: Long) = thermostatRepo.setTemperature(temperature, id)

    suspend fun setMode(mode: ThermostatMode, id: Long) = thermostatRepo.setMode(mode, id)
}