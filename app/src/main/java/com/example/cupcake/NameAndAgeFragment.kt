package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.calculations.CalculationFunctions
import com.example.cupcake.databinding.FragmentNameAndAgeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NameAndAgeFragment : Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentNameAndAgeBinding
    //"activityViewModels()" property delegation to call methods
    private val calcView: CalculationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentNameAndAgeBinding.inflate(inflater, container, false)
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
            nameAndAgeFragment = this@NameAndAgeFragment
        }
        //Update the UI
        updateValues()
    }

    fun editAge(action: String) {
        var age = 0

        //Possible new age based on user action
        if (action == "Increase")
            age = calcView.findAge()?.plus(1)!!
        if (action == "Decrease")
            age = calcView.findAge()?.minus(1)!!

        //Edit age by 1 and update if valid
        if (age!! >= 0 && age!! <= 100) {
            if (age != null)
                calcView.userAge(age)
        }
        else
            pleaseValidAge();
    }

    fun saveName() {
        //Evaluate user input and convert to answer of problem
        val name = binding.textInputEditText.text.toString()
        calcView.userName(name)

        //Was a valid name and age entered?
        if (calcView.isNameEntered() && calcView.age.value != 0 &&
            calcView.age.value != null) {
            setErrorField(false)
            findNavController().navigate(R.id.action_nameAndAgeFragment_to_bmiFragment)
        }
        else {
            //Invalid name?
            if (!calcView.isNameEntered())
                setErrorField(true)

            //Invalid age?
            if (calcView.age.value == 0 || calcView.age.value == null)
                pleaseValidAge()
        }
    }

    private fun setErrorField(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.enter_valid_name)
        }
        //Otherwise, no error message
        else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    //Show error to enter a valid age
    private fun pleaseValidAge() {
        //Show message, and the error
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Invalid age:")
            .setMessage("Please enter a valid age")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }

    //Update the age in display
    private fun updateValues() {
        binding.showAge.text = calcView.ageString.value
    }
}