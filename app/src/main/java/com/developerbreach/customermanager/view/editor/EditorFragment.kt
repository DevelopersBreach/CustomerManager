package com.developerbreach.customermanager.view.editor

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.databinding.FragmentEditorBinding
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.utils.AppTextWatcher
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.developerbreach.customermanager.utils.isNetworkConnected
import com.developerbreach.customermanager.viewModel.EditorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditorFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel: EditorViewModel by viewModels()
    private lateinit var binding: FragmentEditorBinding
    private lateinit var firestore: FirebaseFirestore
    private var customers: Customers? = null
    private lateinit var toolbarTitleArgs: String

    private lateinit var billNumber: String
    private lateinit var numOfItems: String
    private lateinit var customerName: String
    private lateinit var dropDownSelection: String
    private lateinit var currentDate: String
    private lateinit var deliveryDate: String
    private lateinit var mail: String
    private lateinit var contact: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Access a Cloud Firestore instance from your Activity
        firestore = FirebaseFirestore.getInstance()

        if (!isNetworkConnected(requireContext())) {
            showNoNetworkDialog()
        }

        customers = EditorFragmentArgs.fromBundle(requireArguments()).editorFragmentArgs
        toolbarTitleArgs = EditorFragmentArgs.fromBundle(requireArguments()).toolbarTitleArgs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditorBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        setFragmentToolbar()
        return binding.root
    }

    private fun setFragmentToolbar() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentEditor)
        binding.toolbarFragmentEditor.title = toolbarTitleArgs
        binding.toolbarFragmentEditor.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentDate()
        setItemDropDownType()

        binding.billNumberEditText.addTextChangedListener(watchBillNumber())
        binding.numberOfItemsEditText.addTextChangedListener(watchNumOfItems())
        binding.customerNameEditText.addTextChangedListener(watchCustomerName())
        binding.mailEditText.addTextChangedListener(watchMail())
        binding.contactEditText.addTextChangedListener(watchContact())
        performNullOrEmptyCheck()
        addDataToFields()

        binding.submitButton.setOnClickListener(firestoreSubmitListener())
    }

    private fun addDataToFields() {
        if (customers != null) {
            binding.billNumberEditText.setText(customers!!.billNumber)
            binding.numberOfItemsEditText.setText(customers!!.totalItems)
            binding.itemTypeDropDown.setText(customers!!.itemType)
            binding.customerNameEditText.setText(customers!!.name)
            binding.contactEditText.setText(customers!!.contact)
            binding.mailEditText.setText(customers!!.email)
            binding.dateDisplayTextView.text = customers!!.date
        }
    }

    private fun performNullOrEmptyCheck() {
        if (binding.billNumberEditText.text.isNullOrEmpty() ||
            binding.numberOfItemsEditText.text.isNullOrEmpty() ||
            binding.customerNameEditText.text.isNullOrEmpty() ||
            binding.mailEditText.text.isNullOrEmpty() ||
            binding.contactEditText.text.isNullOrEmpty()
        ) {
            binding.validateFields.visibility = View.INVISIBLE
        } else {
            binding.validateFields.visibility = View.VISIBLE
        }
    }

    private fun setCurrentDate() {

        val dayOfMonth = viewModel.date
        val month = viewModel.month
        val year = viewModel.year

        val currentDate = "$dayOfMonth/$month/$year"
        binding.dateDisplayTextView.text = currentDate

        deliveryDate = "${dayOfMonth + 1}/$month/$year"
        binding.dateDeliveryTextView.text = deliveryDate

        binding.dateDeliveryTextView.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { view, i, i2, i3 ->
                onDateSet(view, i, i2, i3)
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        binding.dateDisplayTextView.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { view, i, i2, i3 ->
                onDateSet(view, i, i2, i3)
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.calendar.set(Calendar.YEAR, year)
        viewModel.calendar.set(Calendar.MONTH, month)
        viewModel.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val format = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(viewModel.calendar.time)
        binding.dateDeliveryTextView.text = format
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
                2 -> getString(R.string.item_type_ghagra)
                3 -> getString(R.string.item_type_lehenga)
                4 -> getString(R.string.item_type_long_frock)
            }
        }

        dropDownSelection = binding.itemTypeDropDown.text.toString()
    }

    private fun firestoreSubmitListener(): (View) -> Unit = {
        val customers = Customers(
            billNumber,
            numOfItems,
            dropDownSelection,
            customerName,
            mail,
            contact,
            false,
            currentDate,
            deliveryDate
        )

        if (isNetworkConnected(requireContext())) {
            val collection = firestore.collection(COLLECTION_PATH)
            collection.document(billNumber).set(customers)
                .addOnSuccessListener { firestoreSuccessListener(customerName, billNumber) }
                .addOnFailureListener { firestoreFailureListener() }
        } else {
            showNoNetworkDialog()
        }
    }

    private fun showNoNetworkDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Widget_Customer_Dialog)
            .setTitle(getString(R.string.dialog_error_title_text))
            .setMessage(getString(R.string.dialog_error_message_text))
            .setPositiveButton(getString(R.string.dialog_positive_button_ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun firestoreSuccessListener(
        customerName: String,
        billNumber: String,
    ) {
        MaterialAlertDialogBuilder(requireContext(), R.style.Widget_Customer_Dialog)
            .setTitle("$customerName - $billNumber")
            .setMessage(getString(R.string.dialog_positive_message_text))
            .setPositiveButton(getString(R.string.dialog_positive_button_ok)) { dialog, _ ->
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun firestoreFailureListener() {
        Snackbar.make(
            requireView(),
            getString(R.string.customer_firestore_unsuccessful),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_clear -> {
                binding.billNumberEditText.setText("")
                binding.numberOfItemsEditText.setText("")
                binding.customerNameEditText.setText("")
                binding.mailEditText.setText("")
                binding.contactEditText.setText("")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun watchBillNumber(): TextWatcher = object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            billNumber =
                viewModel.validateCustomerDetails(text.toString(), binding.billNumberInput)
            performNullOrEmptyCheck()
        }
    }

    private fun watchNumOfItems(): TextWatcher = object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            numOfItems =
                viewModel.validateCustomerDetails(text.toString(), binding.numberOfItemsInput)
            performNullOrEmptyCheck()
        }
    }

    private fun watchCustomerName(): TextWatcher = object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            customerName =
                viewModel.validateCustomerDetails(text.toString(), binding.customerNameInput)
            performNullOrEmptyCheck()
        }
    }

    private fun watchMail(): TextWatcher = object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            mail = viewModel.isValidEmail(text as Editable?, binding.customerMailInput)
            performNullOrEmptyCheck()
        }
    }

    private fun watchContact(): TextWatcher = object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            contact = viewModel.isValidContact(text as Editable?, binding.customerContactInput)
            performNullOrEmptyCheck()
        }
    }
}