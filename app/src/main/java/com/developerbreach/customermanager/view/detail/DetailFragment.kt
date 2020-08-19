package com.developerbreach.customermanager.view.detail

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.databinding.DetailFragmentBinding
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var customers: Customers
    private lateinit var collection: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customers = DetailFragmentArgs.fromBundle(requireArguments()).customerDetailArgs
        collection = FirebaseFirestore.getInstance().collection(COLLECTION_PATH)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.customer = customers
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        setHasOptionsMenu(true)
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarDetailFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarDetailFragment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_customer_menu_item -> showDeleteDialog(
                customers,
                requireContext()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog(
        customers: Customers,
        context: Context
    ) {
        MaterialAlertDialogBuilder(context, R.style.Widget_Customer_Dialog)
            .setTitle("Delete Customer - ${customers.billNumber}")
            .setMessage(context.getString(R.string.dialog_message_text_delete))
            .setPositiveButtonIcon(ContextCompat.getDrawable(context, R.drawable.ic_delete))
            .setPositiveButton(context.getString(R.string.dialog_positive_button_delete)) { _, _ ->
                deleteButtonListener(customers, context)
            }
            .setNegativeButton(context.getString(R.string.dialog_negative_button_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteButtonListener(
        customers: Customers,
        context: Context
    ) {
        collection.document(customers.billNumber.toString())
            .delete()
            .addOnSuccessListener {
                findNavController().navigateUp()
                Toast.makeText(context, "Deleted Unsuccessfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Delete Unsuccessful", Toast.LENGTH_SHORT).show()
            }
    }
}