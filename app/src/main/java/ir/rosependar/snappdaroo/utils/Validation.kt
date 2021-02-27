package ir.rosependar.snappdaroo.utils

import android.util.Patterns
import android.widget.EditText
import java.util.regex.Pattern


object Validation {

    fun checkName(editText: EditText): Boolean {
        val input = editText.text.toString()
        if (input.length < 3) {
            editText.error = "نام حداقل باید ۳ رقمی باشد"
            editText.requestFocus()
            return false
        }

        if (input == "" || input.isEmpty()) {
            editText.error = "نام نباید خالی باشد"
            editText.requestFocus()
            return false
        }
        return true
    }

    fun checkEmail(editText: EditText): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        val checked = pattern.matcher(editText.text.toString()).matches()
        if(!checked) {
            editText.error = "ایمیل صحیح نمی باشد."
            editText.requestFocus()
        }
        return checked
    }

    fun checkAddress(editText: EditText): Boolean {
        val input = editText.text.toString()
        if (input.length < 10) {
            editText.error = "آٔدرس حداقل باید 10 رقمی باشد"
            editText.requestFocus()
            return false
        }

        if (input == "" || input.isEmpty()) {
            editText.error = "آدرس نباید خالی باشد"
            editText.requestFocus()
            return false
        }
        return true
    }

    fun checkPostalCode(editText: EditText): Boolean {
        val input = editText.text.toString().trim()
        if (input.length != 10) {
            editText.error = "کد پستی باید 10 رقمی باشد"
            editText.requestFocus()
            return false
        }

        if (input == "" || input.isEmpty()) {
            editText.error = "کد پستی نباید خالی باشد"
            editText.requestFocus()
            return false
        }
        return true
    }

}