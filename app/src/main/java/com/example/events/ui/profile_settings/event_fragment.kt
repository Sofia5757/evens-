package com.example.events.ui.profile_settings

class event_fragment {
    private lateinit var viewModel: EventViewModel
        private lateinit var eventAdapter: EventAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_event, container, false)

            viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

            // Инициализация RecyclerView
            val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
            eventAdapter = EventAdapter(emptyList())
            recyclerView.adapter = eventAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Наблюдение за изменениями в данных из ViewModel
            viewModel.events.observe(viewLifecycleOwner, Observer { events ->
                events?.let {
                    eventAdapter = EventAdapter(it)
                    recyclerView.adapter = eventAdapter
                }
            })
}