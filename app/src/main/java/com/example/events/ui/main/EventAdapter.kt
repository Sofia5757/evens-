package com.example.events.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.events.R
import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.databinding.ItemEventBinding
import com.example.events.databinding.ItemEventMainBinding

class EventAdapter(
    private val onParticipantsClick: (Event) -> Unit,
    private val onClick: (Event) -> Unit,
    private val onCancelClick: (Event) -> Unit,
    private val onDeleteClick: (Event) -> Unit,
    var myProfile: User? = null
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    val items = mutableListOf<Event>()

    inner class EventViewHolder(private val binding: ItemEventMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                tvName.text = event.name
                btParticipants.isVisible = myProfile?.teacher == true && event.participants.isNotEmpty()
                btParticipants.setOnClickListener {
                    onParticipantsClick.invoke(event)
                }
                root.setOnClickListener{
                    onClick.invoke(event)
                }
                if(myProfile?.id in event.participants){
                    btParticipate.text = binding.root.context.getString(R.string.cancel_participation)
                    btParticipate.setOnClickListener {
                        onCancelClick.invoke(event)
                    }
                }else{
                    if(myProfile?.id == event.accompanistId){
                        btParticipate.text = binding.root.context.getString(R.string.cancel_event)
                        btParticipate.setOnClickListener {
                            onDeleteClick.invoke(event)
                        }
                    }else {
                        btParticipate.text = binding.root.context.getString(R.string.participate)
                        btParticipate.setOnClickListener {
                            onClick.invoke(event)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemEventMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(events: List<Event>){
        items.clear()
        items.addAll(events)
        notifyDataSetChanged()
    }

}