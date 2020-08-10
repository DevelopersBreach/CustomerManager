package com.developerbreach.customermanager.view.editor

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.databinding.FragmentEditorBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditorFragment : Fragment() {

    private lateinit var binding: FragmentEditorBinding

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

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.drop_down_item,
            resources.getStringArray(R.array.dropdown_content)
        )

        binding.itemTypeDropDown.setAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_clear -> {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}