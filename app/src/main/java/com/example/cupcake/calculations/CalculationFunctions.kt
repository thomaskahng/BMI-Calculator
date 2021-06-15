package com.example.cupcake.calculations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculationFunctions: ViewModel() {
    //Name of user
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    //Age of user
    private var _age = MutableLiveData(0)
    val age: LiveData<Int>
        get() = _age

    //Age of user (string)
    private var _ageString = MutableLiveData<String>()
    val ageString: LiveData<String> = _ageString

    //Gender of user
    private var _gender = MutableLiveData<String>("Male")
    val gender: LiveData<String> = _gender

    //Unit of measurement
    private var _unit = MutableLiveData<String>("Imperial")
    val unit: LiveData<String> = _unit

    //Height of user
    private var _height = MutableLiveData(0)
    val height: LiveData<Int>
        get() = _height

    //Weight of user
    private var _weight = MutableLiveData(0)
    val weight: LiveData<Int>
        get() = _weight

    //BMI value of user
    private var _bmiVal = MutableLiveData(0.0)
    val bmiVal: LiveData<Double>
        get() = _bmiVal

    //Waist circumference of user
    private var _waistCir = MutableLiveData(0)
    val waistCir: LiveData<Int>
        get() = _waistCir

    //Hip circumference of user
    private var _hipCir = MutableLiveData(0)
    val hipCir: LiveData<Int>
        get() = _hipCir

    //Waist hip ratio value of user
    private var _waistHipVal = MutableLiveData(0.0)
    val waistHipVal: LiveData<Double>
        get() = _waistHipVal

    //BMI of user
    private var _bmi = MutableLiveData<String>()
    val bmi: LiveData<String> = _bmi

    //BMI of user sentence
    private var _bmiSentence = MutableLiveData<String>()
    val bmiSentence: LiveData<String> = _bmiSentence


    //Waist hip ratio of user
    private var _waistHip = MutableLiveData<String>()
    val waistHip: LiveData<String> = _waistHip

    //Waist hip ratio of user sentence
    private var _waistHipSentence = MutableLiveData<String>()
    val waistHipSentence: LiveData<String> = _waistHipSentence

    //Waist hip ratio of user sentence
    private var _results = MutableLiveData<String>()
    val results: LiveData<String> = _results

    init {
        _age.value = 20
        _ageString.value = _age.value.toString()
    }

    //Reset all values
    fun reset() {
        _name.value = ""
        _age.value = 20
        _ageString.value = _age.value.toString()

        _gender.value = "Male"
        _unit.value = "Imperial"

        _height.value = 0
        _weight.value = 0

        _waistCir.value = 0
        _hipCir.value = 0

        _bmiVal.value = 0.0
        _waistHipVal.value = 0.0

        _bmi.value = ""
        _bmiSentence.value = ""
        _waistHip.value = ""
        _waistHipSentence.value = ""
        _results.value = ""
    }

    //Set name
    fun userName(name: String) {
        _name.value = name
    }

    //Was name entered?
    fun isNameEntered(): Boolean {
        if (!_name.value.isNullOrEmpty())
            return true
        else
            return false
    }

    //Set gender
    fun userGender(gender: String) {
        _gender.value = gender
    }

    //Set age
    fun userAge(age: Int) {
        _age.value = age
        _ageString.value = age.toString()
    }

    //Get age
    fun findAge(): Int? {
        return _age.value
    }

    //Unit of measurement
    fun userUnit(unit: String) {
        _unit.value = unit
    }

    //Set height
    fun userHeight(height: Int) {
        _height.value = height
    }

    //Set weight
    fun userWeight(weight: Int) {
        _weight.value = weight
    }

    //Set waist circumference
    fun userWaistCir(waistCir: Int) {
        _waistCir.value = waistCir
    }

    //Set hip circumference
    fun userHipCir(hipCir: Int) {
        _hipCir.value = hipCir
    }

    //Calculate BMI
    fun calcBMI() {
        if (_height.value != null && _weight.value != null
            && _height.value != 0 && _weight.value != 0) {
            var finVal = 0.0

            //Calculate based on customary or metric
            if (_unit.value == "Imperial") {
                finVal = 703 * _weight.value!!.toDouble() /
                        (_height.value!!.toDouble() * _height.value!!.toDouble())
            }
            if (_unit.value == "Metric")
                finVal = _weight.value!!.toDouble() /
                        ((_height.value!!.toDouble() / 100.0) *
                        (_height.value!!.toDouble() / 100.0))

            //Convert to final value
            _bmiVal.value = String.format("%.1f", finVal).toDouble()
            //Get final BMI
            bmiStatus()
        }
    }

    private fun bmiStatus() {
        //What does BMI indicate
        var status = ""
        if (_bmiVal.value!! < 18.5)
            status = " (Underweight)"
        else if (_bmiVal.value!! >= 18.5 && _bmiVal.value!! < 25)
            status = " (Normal)"
        else if (_bmiVal.value!! >= 25 && _bmiVal.value!! < 30)
            status = " (Overweight)"
        else
            status = " (Obese)"
        //Calculate BMI that will be shown*/
        _bmi.value = _bmiVal.value.toString() + "" + status
        _bmiSentence.value = "Your BMI is: " + _bmi.value
    }

    //Calculate waist hip ratio
    fun calcWaistHip() {
        if (_waistCir.value != null && hipCir.value != null
            && _waistCir.value != 0 && hipCir.value != 0
        ) {

            //Calculate waist hip ratio
            var finVal = _waistCir.value!!.toDouble() / hipCir.value!!.toDouble()
            _waistHipVal.value = String.format("%.2f", finVal).toDouble()
            var status = ""

            //If user is male
            if (_gender.value == "Male") {
                if (_waistHipVal.value!! <= 0.95)
                    status = " (Low Risk)"
                else if (_waistHipVal.value!! >= 0.96 && _waistHipVal.value!! < 1.0)
                    status = " (Moderate Risk)"
                else
                    status = " (High Risk)"
            }
            //If user is female
            if (_gender.value == "Female") {
                if (_waistHipVal.value!! <= 0.8)
                    status = " (Low Risk)"
                else if (_waistHipVal.value!! >= 0.81 && _waistHipVal.value!! < 0.85)
                    status = " (Moderate Risk)"
                else
                    status = " (High Risk)"
            }
            //Calculate waist hip ratio that will be shown
            _waistHip.value = _waistHipVal.value.toString() + status
            _waistHipSentence.value = "Your Waist Hip Ratio is: " + _waistHip.value
        }
    }

    fun inputToNum(input: String): Int {
        var num = 0

        //Is user input string a number?
        try {
            num = Integer.parseInt(input)
        }
        catch(e: NumberFormatException) {
            num = 0
        }
        return num
    }

    fun isIntString(input: String): Boolean {
        var isInt = true

        //Is user input string a number?
        try {
            var num = Integer.parseInt(input)
        }
        catch(e: NumberFormatException) {
            isInt = false
        }
        //Can be converted to int, then true, else false
        if (isInt == true)
            return true
        else
            return false
    }

    //User results
    fun userResults(res: String) {
        _results.value = res
    }
}