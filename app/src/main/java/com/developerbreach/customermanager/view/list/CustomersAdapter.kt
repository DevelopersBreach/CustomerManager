package com.developerbreach.customermanager.view.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.developerbreach.customermanager.databinding.ItemCustomerBinding
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.view.list.CustomersAdapter.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query


class CustomersAdapter(
    query: Query,
    private val collection: CollectionReference
) : FirestoreAdapter<CustomersViewHolder>(query) {

    class CustomersViewHolder(
        private val binding: ItemCustomerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            snapShot: DocumentSnapshot,
            collection: CollectionReference
            ) {
            val customers: Customers = snapShot.toObject(Customers::class.java)!!
            binding.customers = customers
            binding.collection = collection
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomersViewHolder {
        return CustomersViewHolder(
            ItemCustomerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomersViewHolder, position: Int) {
        val snapShot = getSnapshot(position)!!
        holder.bind(snapShot, collection)
    }
}