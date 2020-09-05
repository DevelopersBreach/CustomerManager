package com.developerbreach.customermanager.view.editor

import android.os.Bundle
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.databinding.FragmentEditorBinding
import com.developerbreach.customermanager.model.Customers


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditorFragment : Fragment() {

    private lateinit var binding: FragmentEditorBinding
    private val viewModel: EditorViewModel by viewModels()
    private var customers: Customers? = null
    private lateinit var toolbarTitleArgs: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.isConnected) {
            showNetworkDialog(requireContext())
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
        binding.context = requireContext()
        binding.toolbarTitleArgs = toolbarTitleArgs
        binding.activity = requireActivity()
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextChangeListeners()
        performNullOrEmptyCheck()
        addDataToFields()
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

    private fun setTextChangeListeners() {
        binding.billNumberEditText.doAfterTextChanged { query ->
            viewModel.validateField(query.toString(), binding.billNumberInput)
            performNullOrEmptyCheck()
        }

        binding.numberOfItemsEditText.doAfterTextChanged { query ->
            viewModel.validateField(query.toString(), binding.numberOfItemsInput)
            performNullOrEmptyCheck()
        }

        binding.customerNameEditText.doAfterTextChanged { query ->
            viewModel.validateField(query.toString(), binding.customerNameInput)
            performNullOrEmptyCheck()
        }

        binding.mailEditText.doAfterTextChanged { query ->
            viewModel.isValidEmail(query, binding.customerMailInput)
            performNullOrEmptyCheck()
        }

        binding.contactEditText.doAfterTextChanged { query ->
            viewModel.isValidContact(query, binding.customerContactInput)
            performNullOrEmptyCheck()
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

    private fun addDataToFields() {
        if (customers != null) {
            binding.billNumberEditText.setText(customers!!.billNumber)
            binding.numberOfItemsEditText.setText(customers!!.totalItems)
            binding.itemTypeDropDown.setText(customers!!.itemType)
            binding.customerNameEditText.setText(customers!!.name)
            binding.contactEditText.setText(customers!!.contact)
            binding.mailEditText.setText(customers!!.email)
            binding.dateDisplayTextView.text = customers!!.date
            binding.dateDeliveryTextView.text = customers!!.delivery
        }
    }
}