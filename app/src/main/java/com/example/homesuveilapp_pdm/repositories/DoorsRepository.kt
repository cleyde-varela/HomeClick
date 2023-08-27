package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.Door
import com.example.homesuveilapp_pdm.db.HomeClickDB

/* Door's repository */
class DoorsRepository(app: Application) {

    private val doorsDao = HomeClickDB(app).doorDao()

    suspend fun getDoors(id: Long): List<Door> {
        return doorsDao.getDivisionDoors(id)
    }

    suspend fun deleteDoor(id: Long) : Int {
        return doorsDao.deleteDoor(id)
    }

    suspend fun addDoor(data: Door) : Long {
        return doorsDao.insert(data)
    }

    suspend fun setStatus(status: DeviceStatus, id: Long) : Int {
        return doorsDao.setStatus(status, id)
    }
}