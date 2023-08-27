package com.example.homesuveilapp_pdm.ui.thermostat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homesuveilapp_pdm.databinding.FragmentAddScheduleBinding
import com.example.homesuveilapp_pdm.db.Schedule
import com.example.homesuveilapp_pdm.db.ThermostatMode
import kotlinx.coroutines.launch

/**
 * Fragment to add a new schedule for the thermostat
 */
class AddSchedule : Fragment() {

    private lateinit var binding: FragmentAddScheduleBinding
    private val scheduleViewModel by viewModels<ScheduleViewModel>()
    private val args: AddScheduleArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddScheduleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.temperaturePicker.minValue = 15
        binding.temperaturePicker.maxValue = 30

        /* Get inserted data and saves to db */
        binding.createScheduleBtn.setOnClickListener {
            lifecycleScope.launch {
                scheduleViewModel.addSchedule(
                    Schedule(
                        args.thermostatID,
                        binding.scheduleNameEditText.text.toString(),
                        modeSelection(binding.modesSpinner.selectedItem.toString()),
                        binding.temperaturePicker.value
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    /* Returns the thermostat mode based on the user's spinner selection */
    private fun modeSelection(selection: String): ThermostatMode {
        return when (selection) {
            "Cool" -> ThermostatMode.COOL
            "Heat" -> ThermostatMode.HEAT
            "Fan" -> ThermostatMode.FAN
            "Mode" -> ThermostatMode.MODE
            else -> {
                ThermostatMode.MODE
            }
        }
    }
}