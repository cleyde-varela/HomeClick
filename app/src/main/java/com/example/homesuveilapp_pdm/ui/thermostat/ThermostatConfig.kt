package com.example.homesuveilapp_pdm.ui.thermostat

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.FragmentThermostatConfigBinding
import com.example.homesuveilapp_pdm.db.DeviceStatus
import com.example.homesuveilapp_pdm.db.Schedule
import com.example.homesuveilapp_pdm.db.ThermostatMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch



/**
 * Fragment for thermostat configuration
 */
class ThermostatConfig : Fragment(), MenuProvider {

    private lateinit var binding: FragmentThermostatConfigBinding
    private val thermostatViewModel by viewModels<ThermostatViewModel>()
    private val scheduleViewModel by viewModels<ScheduleViewModel>()
    private val args: ThermostatConfigArgs by navArgs()
    private var schedules = listOf<Schedule>()
    private val maxTemperature = 15
    private val minTemperature = 15

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThermostatConfigBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Sets saved thermostat values */
        binding.powerBtnLabel.text = args.thermostatStatus.toString()
        if (args.thermostatStatus == DeviceStatus.OFF) {
            binding.thermostatPowerBtn.setImageResource(R.drawable.power_off)
        } else {
            binding.thermostatPowerBtn.setImageResource(R.drawable.power_on)
        }

        /* Power button's toggle */
        binding.thermostatPowerBtn.setOnClickListener {
            if (args.thermostatStatus == DeviceStatus.OFF) {
                binding.powerBtnLabel.text = DeviceStatus.ON.toString()
                binding.thermostatPowerBtn.setImageResource(R.drawable.power_on)
                lifecycleScope.launch {
                    thermostatViewModel.setStatus(
                        DeviceStatus.ON,
                        args.thermostatID
                    )
                }
            } else {
                binding.powerBtnLabel.text = DeviceStatus.OFF.toString()
                binding.thermostatPowerBtn.setImageResource(R.drawable.power_off)
                lifecycleScope.launch {
                    thermostatViewModel.setStatus(
                        DeviceStatus.OFF,
                        args.thermostatID
                    )
                }
            }
        }

        /* Seekbar functionality */
        binding.thermostatSeekbar.max = maxTemperature
        binding.thermostatSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var temp = 0

            override fun onProgressChanged(seekBar: SeekBar, i: Int, fromUser: Boolean) {
                temp = i + minTemperature
                seekBar.progress = i
                val temperatureInserted = temp.toString() + "ºC"
                binding.thermostatTextview.text = temperatureInserted
                changeThermoSeekBar(temp)
                lifecycleScope.launch {
                    thermostatViewModel.setTemperature(
                        temp,
                        args.thermostatID
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Not needed here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Not needed here
            }
        })

        /* Mode spinner */
        val modeSpinner = binding.thermostatModeBtn

        modeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selection = modeSpinnerSelection(modeSpinner.selectedItem.toString())
                binding.modeBtnLabel.text = selection.toString()
                changeModeBtn(selection)
                lifecycleScope.launch {
                    thermostatViewModel.setMode(
                        modeSpinnerSelection(modeSpinner.selectedItem.toString()),
                        args.thermostatID
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Not needed here
            }
        }

        /**
         *  Schedule spinner
         *  https://www.mysamplecode.com/2012/10/android-spinner-programmatically.html
         */
        lifecycleScope.launch {
            schedules = scheduleViewModel.getSchedules(args.thermostatID)

            val schedulesList = setScheduleOptions(schedules)

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                schedulesList
            )

            val scheduleSpinner = binding.thermostatScheduleBtn
            scheduleSpinner.adapter = adapter
            adapter.notifyDataSetChanged()

            scheduleSpinner.setSelection(0)
            scheduleSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.scheduleBtnLabel.text = scheduleSpinner.selectedItem.toString()
                    setSchedule(schedules, scheduleSpinner.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Unused here
                }
            }
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    /* Returns the ThermostatMode based on the user's spinner selection */
    private fun modeSpinnerSelection(selection: String) : ThermostatMode {
        return when (selection) {
            "Cool" -> ThermostatMode.COOL
            "Heat" -> ThermostatMode.HEAT
            "Fan" -> ThermostatMode.FAN
            "Mode" -> ThermostatMode.MODE
            else -> {
                Snackbar.make(requireView(), "Wrong mode selection", Snackbar.LENGTH_SHORT).show()
                ThermostatMode.MODE
            }
        }
    }

    /* Changes mode's button state in xml */
    private fun changeModeBtn(mode: ThermostatMode) {
        when (mode) {
            ThermostatMode.COOL -> binding.thermostatModeBtn.setBackgroundResource(R.drawable.ic_baseline_snowflake_24)
            ThermostatMode.HEAT -> binding.thermostatModeBtn.setBackgroundResource(R.drawable.ic_baseline_heat_24)
            ThermostatMode.FAN -> binding.thermostatModeBtn.setBackgroundResource(R.drawable.ic_baseline_fan_24)
            ThermostatMode.MODE -> binding.thermostatModeBtn.setBackgroundResource(R.drawable.no_selection_mode)
        }
    }

    /* Changes temperature's textview color according to seekbar value */
    private fun changeThermoSeekBar(temperature: Int) {
        if (temperature < 18) {
            binding.thermostatTextview.setBackgroundResource(R.drawable.temp_circle_a)
        } else if (temperature in 19..20) {
            binding.thermostatTextview.setBackgroundResource(R.drawable.temp_circle_b)
        } else if (temperature in 22..23) {
            binding.thermostatTextview.setBackgroundResource(R.drawable.temp_circle_c)
        } else if (temperature in 25..26) {
            binding.thermostatTextview.setBackgroundResource(R.drawable.temp_circle_d)
        } else if (temperature > 27) {
            binding.thermostatTextview.setBackgroundResource(R.drawable.temp_circle_e)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.schedule, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.addIcon -> {
                /* Navigates to the fragment responsible for adding a new schedule */
                findNavController().navigate(
                    ThermostatConfigDirections.actionThermostatConfigToAddSchedule(
                        args.thermostatID
                    )
                )
                true
            }
            R.id.threeDots -> {
                Snackbar.make(requireView(), "Dots aren't available yet!", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> {
                false
            }
        }
    }

    /* Creates a list of String given a list of Schedule */
    private fun setScheduleOptions(schedules: List<Schedule>) : List<String> {
        val result = mutableListOf<String>()

        result.add("SCHEDULE")

        schedules.forEach {
            result.add(it.name)
        }
        return result
    }

    /* Changes thermostat values according to the schedule chosen */
    private fun setSchedule(schedules: List<Schedule>, spinnerOption: String) {
        schedules.forEach {
            if (spinnerOption == "SCHEDULE") {
                setChanges(args.thermostatTemp, args.thermostatMode)
            } else if (it.name == spinnerOption) {
                setChanges(it.modeTemperature, it.thermostatMode)
            }
        }
    }

    /* setSchedule() auxiliary */
    private fun setChanges(temp: Int, mode: ThermostatMode) {
        val temperature = temp.toString() + "ºC"
        binding.thermostatTextview.text = temperature
        changeThermoSeekBar(temp  +  minTemperature)
        changeModeBtn(mode)
        setModeSelection(mode)
        binding.thermostatSeekbar.progress = (temp -  minTemperature)
        binding.modeBtnLabel.text = mode.toString()
    }

    /* Sets a selection to modeSpinner */
    private fun setModeSelection(mode: ThermostatMode) {
        when (mode) {
            ThermostatMode.COOL -> binding.thermostatModeBtn.setSelection(1)
            ThermostatMode.HEAT -> binding.thermostatModeBtn.setSelection(2)
            ThermostatMode.FAN -> binding.thermostatModeBtn.setSelection(3)
            ThermostatMode.MODE -> binding.thermostatModeBtn.setSelection(0)
        }
    }
}