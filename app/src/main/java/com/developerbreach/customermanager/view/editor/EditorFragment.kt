package com.developerbreach.customermanager.view.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.databinding.FragmentEditorBinding
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.viewModel.EditorViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditorFragment : Fragment() {

    private lateinit var binding: FragmentEditorBinding
    private val viewModel: EditorViewModel by viewModels()
    private lateinit var firestore: FirebaseFirestore
    private var isStitchCompleted = false
    private var status = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Access a Cloud Firestore instance from your Activity
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditorBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusListener()
        setCurrentDate()
        setItemDropDownType()
        setValidateAndSubmit()

        binding.baseIdEditText.addTextChangedListener(watchBaseId())
        binding.customerBillNumberEditText.addTextChangedListener(watchBillNumber())
        binding.numberOfItemsEditText.addTextChangedListener(watchNumOfItems())
        binding.customerNameEditText.addTextChangedListener(watchCustomerName())
        binding.customerMailEditText.addTextChangedListener(watchMail())
        binding.customerContactEditText.addTextChangedListener(watchContact())
    }

    private lateinit var baseId: String
    private lateinit var billNumber: String
    private lateinit var numOfItems: String
    private lateinit var customerName: String
    private lateinit var mail: String
    private lateinit var contact: String

    private fun watchBaseId(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                baseId = viewModel.validateBaseId(text.toString(), binding.baseIdInput)
            }
        }
    }

    private fun watchBillNumber(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                billNumber =
                    viewModel.validateBillNumber(text.toString(), binding.customerBillNumberInput)
            }
        }
    }

    private fun watchNumOfItems(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                numOfItems =
                    viewModel.validateNumOfItems(text.toString(), binding.numberOfItemsInput)
            }
        }
    }

    private fun watchCustomerName(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                customerName =
                    viewModel.validateCustomerName(text.toString(), binding.customerNameInput)
            }
        }
    }

    private fun watchMail(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mail = viewModel.isValidEmail(text as Editable?, binding.customerMailInput)
            }
        }
    }

    private fun watchContact(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                contact = viewModel.isValidContact(text as Editable?, binding.customerContactInput)
            }
        }
    }

    private fun setStatusListener() {
        binding.completedStatus.setOnClickListener {
            binding.completedStatus.setBackgroundResource(R.drawable.status_completed_filled)
            binding.pendingStatus.setBackgroundResource(R.drawable.status_pending_plane)
            status = 1
        }

        binding.pendingStatus.setOnClickListener {
            binding.completedStatus.setBackgroundResource(R.drawable.status_completed_plane)
            binding.pendingStatus.setBackgroundResource(R.drawable.status_pending_filled)
            status = 2
        }
    }

    private fun setCurrentDate() {
        val currentDate = Calendar.getInstance().time.toString()
        val date = currentDate.removeRange(11, 30).drop(4)
        binding.dateDisplayTextView.text = date
    }

    private fun setItemDropDownType() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.drop_down_item,
            resources.getStringArray(R.array.dropdown_content)
        )

        binding.itemTypeDropDown.setAdapter(adapter)
        binding.itemTypeDropDown.setText(adapter.getItem(0), false)
        binding.itemTypeDropDown.setOnItemClickListener { _, _, itemId, _ ->
            when (itemId) {
                0 -> getString(R.string.item_type_dress)
                1 -> getString(R.string.item_type_blouse)
                2 -> getString(R.string.item_type_alteration)
            }
        }
    }

    private fun setValidateAndSubmit() {

        val dropDownSelection = binding.itemTypeDropDown.text.toString()

        val selectedDate = binding.dateDisplayTextView.text.toString()

        when (status) {
            1 -> isStitchCompleted = true
            2 -> isStitchCompleted = false
        }

        binding.validateFields.setOnClickListener {

            val customers = Customers(
                baseId,
                billNumber,
                numOfItems,
                dropDownSelection,
                customerName,
                mail,
                contact,
                isStitchCompleted,
                selectedDate
            )

            //addToFirestore(customers)
        }
    }

    private fun addToFirestore(customers: Customers) {
        val collection = firestore.collection("customers")
        collection.add(customers)
            .addOnSuccessListener {
                Snackbar.make(requireView(), "Added Customer", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Snackbar.make(requireView(), "UnSuccessful", Snackbar.LENGTH_SHORT).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_clear -> {
                binding.baseIdEditText.setText("")
                binding.customerBillNumberEditText.setText("")
                binding.numberOfItemsEditText.setText("")
                binding.customerNameEditText.setText("")
                binding.customerMailEditText.setText("")
                binding.customerContactEditText.setText("")
            }
        }

        return super.onOptionsItemSelected(item)
    }
}