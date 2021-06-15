package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.calculations.CalculationFunctions
import com.example.cupcake.databinding.FragmentWaistToHipBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WaistToHipFragment : Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentWaistToHipBinding
    //"activityViewModels()" property delegation to call methods
    private val calcView: CalculationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentWaistToHipBinding.inflate(inflater, container, false)
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
            waistToHipFragment = this@WaistToHipFragment
        }
    }

    fun calcWaistHip() {
        //Waist and hip circumferences
        val waist = binding.textInputEditText.text.toString()
        val hip = binding.textInputEditText2.text.toString()

        //Values to int (are they valid)
        val waistInt = calcView.inputToNum(waist)
        val hipInt = calcView.inputToNum(hip)
        val intStrWaist = calcView.isIntString(waist)
        val intStrHip = calcView.isIntString(hip)

        //If valid waist circumference
        if (waist.isNotBlank() && intStrWaist && waistInt != 0)
            setErrorFieldWaist(false)
        //If valid hip circumference
        if (hip.isNotBlank() && intStrHip && hipInt != 0)
            setErrorFieldHip(false)

        //If both values valid
        if (waist.isNotBlank() && intStrWaist && waistInt != 0
            && hip.isNotBlank() && intStrHip && hipInt != 0) {
            calcView.userWaistCir(waistInt)
            calcView.userHipCir(hipInt)
        }
        //Was a valid waist and height circumferences entered?
        if (calcView.waistCir.value != 0 && calcView.waistCir.value != null
            && calcView.hipCir.value != 0 && calcView.hipCir.value != null) {
            calcView.calcWaistHip()
            binding.showWaistHip.text = calcView.waistHipSentence.value
        }
        //Ask user to enter valid waist circumference
        if (!waist.isNotBlank() || !intStrWaist || waistInt == 0)
            setErrorFieldWaist(true)

        //Ask user to enter valid hip circumference
        if (!hip.isNotBlank() || !intStrHip || hipInt == 0)
            setErrorFieldHip(true)
    }

    private fun setErrorFieldWaist(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = "Enter valid waist circumference!"
        }
        //Otherwise, no error message
        else
            binding.textField.isErrorEnabled = false
    }

    private fun setErrorFieldHip(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField2.isErrorEnabled = true
            binding.textField2.error = "Enter valid hip circumference!"
        }
        //Otherwise, no error message
        else
            binding.textField2.isErrorEnabled = false
    }

    fun saveWaistHip() {
        if (calcView.waistHipVal.value != 0.0 && calcView.waistHipVal.value != null) {
            //Stores a string of results
            val orderSummary = getString(
                R.string.res_details,
                calcView.name.value.toString(),
                calcView.age.value.toString(),
                calcView.gender.value.toString(),
                calcView.bmi.value.toString(),
                calcView.waistHip.value.toString()
            )
            calcView.userResults(orderSummary)
            //If valid waist hip ratio, go to next part
            findNavController().navigate(R.id.action_waistToHipFragment_to_summaryFragment)
        }
        else
            pleaseValidWaistHip()
    }

    //Show error to get a valid waist hip ratio
    private fun pleaseValidWaistHip() {
        //Show message, and the error
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Invalid Waist Hip Ratio:")
            .setMessage("Please find a valid Waist Hip Ratio")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }
}