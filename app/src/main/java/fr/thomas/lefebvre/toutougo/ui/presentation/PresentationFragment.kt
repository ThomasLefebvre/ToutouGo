package fr.thomas.lefebvre.toutougo.ui.presentation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.PresentationFragmentBinding

class PresentationFragment : Fragment() {


    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)
        val binding: PresentationFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.presentation_fragment, container, false)
        binding.viewModel=viewModel
        binding.lifecycleOwner=activity

        binding.floatingActionButtonNextPresentation.setOnClickListener {
            viewModel.activity.value=true
            view!!.findNavController()
                .navigate(R.id.action_presentationFragment_to_userInfos)
        }
        return binding.root
    }


}

