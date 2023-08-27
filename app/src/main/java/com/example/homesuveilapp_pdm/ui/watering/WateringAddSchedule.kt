package com.example.homesuveilapp_pdm.ui.watering

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homesuveilapp_pdm.databinding.FragmentWateringAddScheduleBinding
import com.example.homesuveilapp_pdm.db.WateringMode
import com.example.homesuveilapp_pdm.db.WateringSchedule
import kotlinx.coroutines.launch

/**
 * Fragment to add a new schedule for the watering system
 */
class WateringAddSchedule : Fragment() {

    private lateinit var binding: FragmentWateringAddScheduleBinding
    private val wateringScheduleViewModel by viewModels<WateringScheduleViewModel>()
    private val args: WateringAddScheduleArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWateringAddScheduleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.intensityPicker.minValue = 0
        binding.intensityPicker.maxValue = 100

        /* Get inserted data and saves to db */
        binding.createScheduleBtn.setOnClickListener {
            lifecycleScope.launch {
                wateringScheduleViewModel.addSchedule(

                    WateringSchedule(
                        args.wateringID,
                        binding.scheduleNameEditText.text.toString(),
                        modeSelection(binding.modesSpinner.selectedItem.toString()),
                        binding.intensityPicker.value
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    /* Returns the watering mode based on the user's spinner selection */
    private fun modeSelection(selection: String): WateringMode {
        return when (selection) {
            "Fixed" -> WateringMode.FIXED
            "Moving" -> WateringMode.MOVING
            "Mode" -> WateringMode.MODE
            else -> {
                WateringMode.MODE
            }
        }
    }
}