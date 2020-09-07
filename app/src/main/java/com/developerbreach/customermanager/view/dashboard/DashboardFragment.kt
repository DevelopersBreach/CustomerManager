package com.developerbreach.customermanager.view.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.auth.AuthenticationState
import com.developerbreach.customermanager.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.navController = findNavController()
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.authenticationState.observe(viewLifecycleOwner, { authState ->
            if (authState == AuthenticationState.AUTHENTICATED) {
                val googleUsername = FirebaseAuth.getInstance().currentUser!!.displayName
                val welcomeLabel = getString(R.string.welcome_login_message, googleUsername)
                binding.loggedInUserName.text = welcomeLabel
            } else if (authState == AuthenticationState.UNAUTHENTICATED) {
                findNavController().navigate(
                    R.id.dashboardToLoginFragment
                )
            }
        })
    }
}