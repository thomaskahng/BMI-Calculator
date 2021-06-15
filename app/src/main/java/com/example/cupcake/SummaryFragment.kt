package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.calculations.CalculationFunctions
import com.example.cupcake.databinding.FragmentSummaryBinding

class SummaryFragment : Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentSummaryBinding
    //"activityViewModels()" property delegation to call methods
    private val calcView: CalculationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //Sets software life cycle owner
            lifecycleOwner = viewLifecycleOwner

            //Bind view model with shared model inside layout
            calcModel = calcView

            //Binds data variable with fragment instance (XML data variable calls functions)
            summaryFragment = this@SummaryFragment
        }
    }

    fun sendResults() {
        //Subject of email
        val subject = "BMI and Waist Hip Ratio results"

        //Stores a string of BMI calculation information
        val bmiSummary = getString(
            R.string.res_details_final,
            calcView.name.value.toString(),
            calcView.age.value.toString(),
            calcView.gender.value.toString(),
            calcView.bmi.value.toString(),
            calcView.waistHip.value.toString()
        )

        //Implicit intent of type plain text with intent extras for email subject
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, subject)
            .putExtra(Intent.EXTRA_TEXT, bmiSummary)

        //Prevent the app from crashing if no app to handle intent
        if (activity?.packageManager?.resolveActivity(intent, 0) != null)
            startActivity(intent)
    }

    //Cancel by resetting the view and navigating back to start
    fun quit() {
        calcView.reset()
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
    }
}