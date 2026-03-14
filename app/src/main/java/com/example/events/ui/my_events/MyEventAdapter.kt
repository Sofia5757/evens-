package com.example.events.ui.my_events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.events.R
import com.example.events.data.entities.Event
import com.example.events.databinding.ItemEventBinding
import com.example.events.utils.toEventDate
import com.example.events.utils.toEventTime

class MyEventAdapter(
    private val onClick: (Event) -> Unit
): RecyclerView.Adapter<MyEventAdapter.EventViewHolder>() {

    val items = mutableListOf<Event>()

    inner class EventViewHolder(private val binding: ItemEventBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(event: Event){
            binding.apply {
                root.setOnClickListener{
                    onClick.invoke(event)
                }
                eventTitle.text = event.name
                eventHost.text = root.context.getString(R.string.accompanist_value, event.accompanistName)
                eventPlace.text = event.place
                date.text = event.date.toEventDate()
                time.text = event.date.toEventTime()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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