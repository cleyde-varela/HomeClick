package com.example.homesuveilapp_pdm.ui.divisions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.homesuveilapp_pdm.db.Division
import com.example.homesuveilapp_pdm.repositories.DivisionsRepository
import kotlinx.coroutines.launch

class DivisionsViewModel (app: Application) : AndroidViewModel(app) {

    private val divisionsRepo = DivisionsRepository(app)

    fun getDivisionsFlow() = divisionsRepo.getDivisionsFLow()

    fun deleteDivision(id: Long) = viewModelScope.launch {
        divisionsRepo.deleteDivision(id)
    }

    fun addDivision(data: Division) = viewModelScope.launch {
        divisionsRepo.addDivision(data)
    }

    fun setImgURI(imgURI: String, id: Long) = viewModelScope.launch {
        divisionsRepo.setImgUri(imgURI, id)
    }
}