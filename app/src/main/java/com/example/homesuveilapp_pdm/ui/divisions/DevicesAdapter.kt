package com.example.homesuveilapp_pdm.ui.divisions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.*
import com.example.homesuveilapp_pdm.db.*

/**
 * The fragment_divisions_devices' adapter
 */
class DevicesAdapter(private val onLightClicked: (Light) -> Unit,
                     private val onLightLongClicked: (Light) -> Unit,
                     private val onDoorClicked: (Door) -> Unit,
                     private val onDoorLongClicked: (Door) -> Unit,
                     private val onAreaClicked: (Watering) -> Unit,
                     private val onAreaLongClicked: (Watering) -> Unit,
                     private val onThermostatClicked: (Thermostat) -> Unit,
                     private val onThermoLongClicked: (Thermostat) -> Unit) : RecyclerView.Adapter<DevicesViewHolder>() {

    /* List of devices in a division */
    var items = arrayListOf<Any>()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
        return when (viewType) {
            R.layout.device_title -> DevicesViewHolder.TitleViewHolder(
                DeviceTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.light_device -> DevicesViewHolder.LightsViewHolder(
                LightDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),onLightClicked, onLightLongClicked
            )
            R.layout.door_device -> DevicesViewHolder.DoorsViewHolder(
                DoorDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),onDoorClicked, onDoorLongClicked
            )
            R.layout.thermostat_device -> DevicesViewHolder.ThermostatViewHolder(
                ThermostatDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onThermostatClicked, onThermoLongClicked
            )
            R.layout.watering_device -> DevicesViewHolder.WateringViewHolder(
                WateringDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onAreaClicked, onAreaLongClicked
            )

            else -> throw IllegalArgumentException("The provided ViewType is invalid!")
        }
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        when (holder) {
            is DevicesViewHolder.TitleViewHolder -> holder.bind(items[position] as Title)
            is DevicesViewHolder.LightsViewHolder -> holder.bind(items[position] as Light)
            is DevicesViewHolder.DoorsViewHolder -> holder.bind(items[position] as Door)
            is DevicesViewHolder.ThermostatViewHolder -> holder.bind(items[position] as Thermostat)
            is DevicesViewHolder.WateringViewHolder -> holder.bind(items[position] as Watering)
        }
    }

    override fun getItemCount() = items.size

    /* Checks item type and returns its layout id */
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Title -> R.layout.device_title
            is Light -> R.layout.light_device
            is Door -> R.layout.door_device
            is Thermostat -> R.layout.thermostat_device
            is Watering -> R.layout.watering_device
            else -> {throw IllegalArgumentException("Invalid ViewType!")}
        }
    }

    fun delete(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
