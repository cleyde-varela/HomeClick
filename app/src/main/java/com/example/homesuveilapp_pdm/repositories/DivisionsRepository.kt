package com.example.homesuveilapp_pdm.repositories

import android.app.Application
import com.example.homesuveilapp_pdm.db.Division
import com.example.homesuveilapp_pdm.db.HomeClickDB
import kotlinx.coroutines.flow.Flow

/* Division's repository */
class DivisionsRepository(app: Application) {

    private val divisionsDao = HomeClickDB(app).divisionsDao()

    fun getDivisionsFLow(): Flow<List<Division>> {
        return divisionsDao.getAllDivisionsFlow()
    }

    suspend fun deleteDivision(id: Long) : Int {
        return divisionsDao.deleteDivision(id)
    }

    suspend fun addDivision(data: Division) : Long {
        return divisionsDao.insert(data)
    }

    suspend fun setImgUri(imgURI: String, id: Long) {
        return divisionsDao.setImageUri(imgURI, id)
    }
}