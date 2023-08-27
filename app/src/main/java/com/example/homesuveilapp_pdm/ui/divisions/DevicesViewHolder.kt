package com.example.homesuveilapp_pdm.ui.divisions

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.*
import com.example.homesuveilapp_pdm.db.*

/**
 * ViewHolder class that allows multiple view types in recyclerview in fragment_division_devices
 */
sealed class DevicesViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    /* Devices title ViewHolder */
    class TitleViewHolder(private val binding: DeviceTitleBinding) : DevicesViewHolder(binding) {
        fun bind(title: Title) {
            binding.devicesTitle.text = title.title
        }
    }

    /* Lights ViewHolder */
    @SuppressLint("SetTextI18n")
    class LightsViewHolder(private val binding : LightDeviceBinding,
                           private val onClick: (Light) -> Unit,
                           private val onLongClick: (Light) -> Unit) : DevicesViewHolder(binding)
    {
        private lateinit var light : Light

        init {
            binding.lightBtn.setOnClickListener {
                onClick(light)
            }

            binding.root.setOnLongClickListener {
                onLongClick(light)
                true
            }
        }

        fun bind(light: Light) {
            this.light = light
            if (light.lightStatus == DeviceStatus.OFF) {
                binding.lightBtn.setImageResource(R.drawable.light_off)
            } else {
                binding.lightBtn.setImageResource(R.drawable.light_on)
            }
            binding.lightName.text = light.lightName
            binding.lightStatus.text = light.lightStatus.toString()
        }
    }

    /* Doors ViewHolder */
    class DoorsViewHolder(private val binding : DoorDeviceBinding,
                          private val onClick: (Door) -> Unit,
                          private val onLongClick: (Door) -> Unit) : DevicesViewHolder(binding) {

        private lateinit var door : Door

        init {
            binding.doorBtn.setOnClickListener {
                onClick(door)
            }

            binding.root.setOnLongClickListener {
                onLongClick(door)
                true
            }
        }

        fun bind(door: Door) {
            this.door = door
            if (door.doorStatus == DeviceStatus.OFF) {
                binding.doorBtn.setImageResource(R.drawable.power_off)
            } else {
                binding.doorBtn.setImageResource(R.drawable.power_on)
            }
            binding.doorName.text = door.doorName
            binding.doorStatus.text = door.doorStatus.toString()
        }
    }

    /* Thermostat ViewHolder */
    class ThermostatViewHolder(private val binding: ThermostatDeviceBinding,
                               private val onClick: (Thermostat) -> Unit,
                               private val onLongClick: (Thermostat) -> Unit) : DevicesViewHolder(binding) {

        private lateinit var thermostat: Thermostat

        init {
            binding.thermostatBtn.setOnClickListener {
                onClick(thermostat)
            }

            binding.root.setOnLongClickListener {
                onLongClick(thermostat)
                true
            }
        }

        fun bind(thermostat: Thermostat) {
            this.thermostat = thermostat
            binding.thermostatName.text = thermostat.thermostatName
            binding.thermostatStatus.text = thermostat.thermostatStatus.toString()
        }
    }

    /* Watering ViewHolder */
    class WateringViewHolder(private val binding : WateringDeviceBinding,
                             private val onClick: (Watering) -> Unit,
                             private val onLongClick: (Watering) -> Unit) : DevicesViewHolder(binding) {

        private lateinit var wateringArea: Watering

        init {
            binding.areaBtn.setOnClickListener {
                // Go to watering config fragment
                onClick(wateringArea)
            }

            binding.root.setOnLongClickListener {
                onLongClick(wateringArea)
                true
            }
        }

        fun bind(watering: Watering) {
            this.wateringArea = watering
            binding.areaName.text = watering.areaName
            binding.areaStatus.text = watering.areaStatus.toString()
        }
    }
}