package com.developerbreach.customermanager.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.developerbreach.customermanager.databinding.FragmentCustomerListBinding
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


/**
 * A fragment representing a list of Items.
 */
class CustomerListFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var query: Query
    private lateinit var binding: FragmentCustomerListBinding
    private lateinit var adapter: CustomersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Access a Cloud Firestore instance from your Activity
        firestore = FirebaseFirestore.getInstance()
        query = firestore.collection(COLLECTION_PATH)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CustomersAdapter(query)
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