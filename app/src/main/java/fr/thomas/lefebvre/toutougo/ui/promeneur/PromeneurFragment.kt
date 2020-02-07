package fr.thomas.lefebvre.toutougo.ui.promeneur

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fr.thomas.lefebvre.toutougo.R

class PromeneurFragment : Fragment() {

    companion object {
        fun newInstance() = PromeneurFragment()
    }

    private lateinit var viewModel: PromeneurViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_promeneur, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PromeneurViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
