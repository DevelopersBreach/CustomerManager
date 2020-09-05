package com.developerbreach.customermanager.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.developerbreach.customermanager.databinding.FragmentCustomerListBinding
import java.util.concurrent.TimeUnit


/**
 * A fragment representing a list of Items.
 */
class CustomerListFragment : Fragment() {

    private val viewModel: CustomerListViewModel by viewModels()
    private lateinit var binding: FragmentCustomerListBinding
    private lateinit var adapter: CustomersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        binding.toolbarContentSearchHeader.firestoreHeader = viewModel.firestore
        postponeEnterTransition(100L, TimeUnit.MILLISECONDS)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CustomersAdapter(viewModel.query, viewModel.collection)
        binding.customersRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        // Start listening for Firestore updates
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}