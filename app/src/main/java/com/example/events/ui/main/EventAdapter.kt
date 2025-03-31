package com.example.events.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.events.data.entities.Event
import com.example.events.databinding.ItemEventBinding

class EventAdapter(
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    val items = mutableListOf<Event>()

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.root.setOnClickListener {
                onClick.invoke(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemEventBinding.inflate(
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
    }

}
class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.event_date)
        private val timeTextView: TextView = itemView.findViewById(R.id.event_time)
        private val titleTextView: TextView = itemView.findViewById(R.id.event_title)
        private val locationTextView: TextView = itemView.findViewById(R.id.event_location)
        private val hostTextView: TextView = itemView.findViewById(R.id.event_host)

        fun bind(event: Event) {
            dateTextView.text = event.date
            timeTextView.text = event.time
            titleTextView.text = event.title
            locationTextView.text = event.location
            hostTextView.text = event.host
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }
}