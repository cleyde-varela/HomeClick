package com.example.homesuveilapp_pdm.ui.doors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.Door
import com.example.homesuveilapp_pdm.repositories.DoorsRepository

class DoorsViewModel (app: Application) : AndroidViewModel(app) {

    private val doorsRepo = DoorsRepository(app)

    suspend fun getDoors(id: Long) = doorsRepo.getDoors(id)

    suspend fun deleteDoor(id: Long) = doorsRepo.deleteDoor(id)

    suspend fun addDoor(data: Door) = doorsRepo.addDoor(data)

    suspend fun setStatus(status: DeviceStatus, id: Long) = doorsRepo.setStatus(status, id)
}