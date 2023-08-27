package com.example.homesuveilapp_pdm.ui.divisions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homesuveilapp_pdm.databinding.FragmentAddDeviceBinding
import com.example.homesuveilapp_pdm.db.*
import com.example.homesuveilapp_pdm.ui.doors.DoorsViewModel
import com.example.homesuveilapp_pdm.ui.lights.LightsViewModel
import com.example.homesuveilapp_pdm.ui.thermostat.ThermostatViewModel
import com.example.homesuveilapp_pdm.ui.watering.WateringViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Fragment to add a new device
 */
class AddDevice : Fragment() {

    private lateinit var binding : FragmentAddDeviceBinding
    private val args: AddDeviceArgs by navArgs()
    private val lightsViewModel by viewModels<LightsViewModel>()
    private val doorsViewModel by viewModels<DoorsViewModel>()
    private val thermostatViewModel by viewModels<ThermostatViewModel>()
    private val wateringViewModel by viewModels<WateringViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDeviceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createDeviceBtn.setOnClickListener {
            /* Creates a device based on the user's spinner selection */
            when (binding.devicesTypeSpinner.selectedItem.toString()) {
                "Light" -> lifecycleScope.launch {
                    lightsViewModel.addLight(
                    Light(args.divisionID,
                    binding.deviceNameEditText.text.toString(),
                    DeviceStatus.OFF))
                    findNavController().popBackStack()
                }
                "Door" -> lifecycleScope.launch {
                    doorsViewModel.addDoor(
                        Door(args.divisionID,
                            binding.deviceNameEditText.text.toString(),
                            DeviceStatus.OFF))
                    findNavController().popBackStack()
                }
                "Thermostat" -> lifecycleScope.launch {
                    thermostatViewModel.addThermostat(
                        Thermostat(
                            args.divisionID,
                            binding.deviceNameEditText.text.toString(),
                            DeviceStatus.OFF,
                            0,
                            ThermostatMode.COOL))
                    findNavController().popBackStack()
                }
                "Watering area" -> lifecycleScope.launch {
                    wateringViewModel.addArea(
                        Watering(
                            args.divisionID,
                            binding.deviceNameEditText.text.toString(),
                            DeviceStatus.OFF,
                            0,
                            WateringMode.MODE))
                    findNavController().popBackStack()
                }
                    else -> {
                    Snackbar.make(requireView(), "Something went wrong!", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }
    }
}