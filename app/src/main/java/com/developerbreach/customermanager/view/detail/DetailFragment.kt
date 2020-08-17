package com.developerbreach.customermanager.view.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.databinding.DetailFragmentBinding
import com.developerbreach.customermanager.model.Customers


class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var customers: Customers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customers = DetailFragmentArgs.fromBundle(requireArguments()).customerDetailArgs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.customer = customers
        binding.navController = findNavController()
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarDetailFragment)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit_details_menu_item -> {
                findNavController().navigate(R.id.listToEditorFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}