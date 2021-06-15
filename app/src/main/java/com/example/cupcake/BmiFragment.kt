package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.calculations.CalculationFunctions
import com.example.cupcake.databinding.FragmentBmiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BmiFragment: Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentBmiBinding
    //"activityViewModels()" property delegation to call methods
    private val calcView: CalculationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentBmiBinding.inflate(inflater, container, false)
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
            bmiFragment = this@BmiFragment
        }
    }

    fun calcBMI() {
        //Height and weight
        val height = binding.textInputEditText.text.toString()
        val weight = binding.textInputEditText2.text.toString()

        //Values to int (are they valid)
        val heightInt = calcView.inputToNum(height)
        val weightInt = calcView.inputToNum(weight)
        val intStrHeight = calcView.isIntString(height)
        val intStrWeight = calcView.isIntString(weight)

        //If valid height
        if (height.isNotBlank() && intStrHeight && heightInt != 0)
            setErrorFieldHeight(false)
        //If valid weight
        if (weight.isNotBlank() && intStrWeight && weightInt != 0)
            setErrorFieldWeight(false)

        //If both values valid
        if (height.isNotBlank() && intStrHeight && heightInt != 0
            && weight.isNotBlank() && intStrWeight && weightInt != 0) {
            calcView.userHeight(heightInt)
            calcView.userWeight(weightInt)
        }
        //Was a valid height and weight entered?
        if (calcView.height.value != 0 && calcView.height.value != null
            && calcView.weight.value != 0 && calcView.weight.value != null) {
            calcView.calcBMI()
            binding.showBMI.text = calcView.bmiSentence.value
        }
        //Ask user to enter valid height
        if (!height.isNotBlank() || !intStrHeight || heightInt == 0)
            setErrorFieldHeight(true)

        //Ask user to enter valid weight
        if (!weight.isNotBlank() || !intStrWeight || weightInt == 0)
            setErrorFieldWeight(true)
    }

    fun saveBMI() {
        //If valid BMI, go to next part
        if (calcView.bmiVal.value != 0.0 && calcView.bmiVal.value != null)
            findNavController().navigate(R.id.action_bmiFragment_to_waistToHipFragment)
        else
            pleaseValidBMI()
    }

    private fun setErrorFieldHeight(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = "Enter valid height!"
        }
        //Otherwise, no error message
        else {
            binding.textField.isErrorEnabled = false
        }
    }

    private fun setErrorFieldWeight(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField2.isErrorEnabled = true
            binding.textField2.error = "Enter valid weight!"
        }
        //Otherwise, no error message
        else
            binding.textField2.isErrorEnabled = false

    }

    //Show error to get a valid BMI
    private fun pleaseValidBMI() {
        //Show message, and the error
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Invalid BMI:")
            .setMessage("Please find a valid BMI")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }
}