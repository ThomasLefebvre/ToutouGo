package fr.thomas.lefebvre.toutougo.ui.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.WelcomeActivityBinding

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=WelcomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
