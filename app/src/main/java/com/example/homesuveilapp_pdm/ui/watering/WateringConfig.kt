package com.example.homesuveilapp_pdm.ui.watering

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
import com.example.homesuveilapp_pdm.databinding.FragmentWateringConfigBinding
import com.example.homesuveilapp_pdm.db.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Fragment for watering configuration
 */
class WateringConfig : Fragment(), MenuProvider {

    private lateinit var binding: FragmentWateringConfigBinding
    private val DIV_NUMBER = 10 // number to divide the progress number
    private val wateringViewModel by viewModels<WateringViewModel>()
    private val wateringScheduleViewModel by viewModels<WateringScheduleViewModel>()
    private val args: WateringConfigArgs by navArgs()
    private var wateringSchedules = listOf<WateringSchedule>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWateringConfigBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Sets saved watering values */
        binding.powerBtnLabel.text = args.wateringStatus.toString()
        if (args.wateringStatus == DeviceStatus.OFF) {
            binding.WateringPowerBtn.setImageResource(R.drawable.power_off)
        } else {
            binding.WateringPowerBtn.setImageResource(R.drawable.power_on)
        }

        /* Power button's toggle */
        binding.WateringPowerBtn.setOnClickListener {
            if (args.wateringStatus == DeviceStatus.ON) {
                binding.powerBtnLabel.text = DeviceStatus.OFF.toString()
                binding.WateringPowerBtn.setImageResource(R.drawable.power_off)
                lifecycleScope.launch {
                    wateringViewModel.setStatus(
                        DeviceStatus.OFF,
                        args.wateringID
                    )
                }
            } else {
                binding.powerBtnLabel.text = DeviceStatus.ON.toString()
                binding.WateringPowerBtn.setImageResource(R.drawable.power_on)
                lifecycleScope.launch {
                    wateringViewModel.setStatus(
                        DeviceStatus.ON,
                        args.wateringID
                    )
                }
            }
        }


        binding.WateringSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            /* Method that gives the progress when the seekBar is changed */
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var dropsImageId = 0
                seekBar.progress = i // progress who comes from seekbar

                val progress = "$i%"
                binding.WateringTextView.text = progress // update the text with the new value which comes from progress

                if (i > DIV_NUMBER) {
                     dropsImageId = i / DIV_NUMBER
                }

                setIntensity(dropsImageId)

                lifecycleScope.launch {
                    wateringViewModel.setIntensity(i, args.wateringID)
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
        val modeSpinner = binding.WateringModeBtn

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
                setMode(selection)
                lifecycleScope.launch {
                    wateringViewModel.setMode(
                        modeSpinnerSelection(modeSpinner.selectedItem.toString()),
                        args.wateringID
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Not needed here
            }
        }

        /* Schedule spinner */
        lifecycleScope.launch {
            wateringSchedules = wateringScheduleViewModel.getSchedules(args.wateringID)

            val wateringSchedulesList = setScheduleOptions(wateringSchedules)

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                wateringSchedulesList
            )

            val scheduleSpinner = binding.WateringScheduleBtn
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
                    setSchedule(wateringSchedules, scheduleSpinner.selectedItem.toString())
                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Unused here
                }
            }
        }
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    /* Returns the watering Mode based on the user's spinner selection */
    private fun modeSpinnerSelection(selection: String) : WateringMode {
        return when (selection) {
            "Mode" -> WateringMode.MODE
            "Fixed" -> WateringMode.FIXED
            "Moving" -> WateringMode.MOVING
            else -> {
                Snackbar.make(requireView(), "Wrong mode selection", Snackbar.LENGTH_SHORT).show()
                WateringMode.MODE
            }
        }
    }

    /**
     * Method that changes the image in mode button
     */
    private fun setMode(wateringMode: WateringMode){
        when (wateringMode) {
            WateringMode.FIXED -> binding.WateringModeBtn.setBackgroundResource(R.drawable.stop)
            WateringMode.MOVING -> binding.WateringModeBtn.setBackgroundResource(R.drawable.move)
            WateringMode.MODE -> binding.WateringModeBtn.setBackgroundResource(R.drawable.ic_baseline_mode_watering)
        }
    }
    /* Method that takes a value which is the intensity and changes the background of the textView */
    private fun setIntensity(i: Int) {
        when(i) {
            0 -> binding.WateringTextView.setBackgroundResource(R.drawable.drops)
            1 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsa)
            2 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsb)
            3 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsc)
            4 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsd)
            5 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropse)
            6 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsf)
            7 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsg)
            8 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsh)
            9 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsi)
            10 -> binding.WateringTextView.setBackgroundResource(R.drawable.dropsj)
        }
    }

    /* Creates a list of String given a list of Schedule */
    private fun setScheduleOptions(schedules: List<WateringSchedule>) : List<String> {
        val result = mutableListOf<String>()

        result.add("SCHEDULE")

        schedules.forEach {
            result.add(it.name)
        }
        return result
    }

    /* Changes watering values according to the schedule chosen */
    private fun setSchedule(schedules: List<WateringSchedule>, spinnerOption: String) {
        schedules.forEach {
            if (spinnerOption == "SCHEDULE") {
                setChanges(args.wateringItensity, args.wateringMode)
            } else if (it.name == spinnerOption) {
                setChanges(it.intensity, it.wateringMode)
            }
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
                    WateringConfigDirections.actionWateringConfigToWateringAddSchedule(
                        args.wateringID
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

    /* setSchedule() auxiliary */
    private fun setChanges(intensity: Int, mode: WateringMode) {
        val intensityTxt = "$intensity%"
        binding.WateringTextView.text = intensityTxt
        binding.modeBtnLabel.text = mode.toString()
        binding.WateringSeekbar.progress = (intensity)
        setIntensity(intensity / DIV_NUMBER)
        binding.WateringSeekbar.progress = intensity
        setMode(mode)
        setModeSelection(mode)
    }

    /* Sets a selection to modeSpinner */
    private fun setModeSelection(mode: WateringMode) {
        when(mode) {
            WateringMode.MODE -> binding.WateringModeBtn.setSelection(0)
            WateringMode.FIXED -> binding.WateringModeBtn.setSelection(1)
            WateringMode.MOVING -> binding.WateringModeBtn.setSelection(2)
        }
    }
}

