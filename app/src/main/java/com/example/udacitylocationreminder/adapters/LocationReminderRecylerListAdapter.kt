package com.example.udacitylocationreminder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.udacitylocationreminder.adapters.LocationReminderRecylerListAdapter.ViewHolder
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import com.example.udacitylocationreminder.databinding.ReminderListItemBinding

class LocationReminderRecylerListAdapter :
    ListAdapter<ReminderTableEntity, ViewHolder>(LocationReminderDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val listItemBinding: ReminderListItemBinding) :
        RecyclerView.ViewHolder(listItemBinding.root) {

        fun bind(item: ReminderTableEntity) {
            listItemBinding.dataItem = item
            listItemBinding.executePendingBindings()
        }

        companion object {
            internal fun from(parent: ViewGroup): ViewHolder {
                val binding = ReminderListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    class LocationReminderDiffUtil : DiffUtil.ItemCallback<ReminderTableEntity>() {
        override fun areItemsTheSame(
            oldItem: ReminderTableEntity,
            newItem: ReminderTableEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReminderTableEntity,
            newItem: ReminderTableEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}