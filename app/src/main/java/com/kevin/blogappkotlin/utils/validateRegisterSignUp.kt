package com.kevin.blogappkotlin.utils

import java.util.regex.Pattern

class validateRegisterSignUp {
//companion object para hacer metodos estaticos y no tener que instanciar la clase.
companion object{
    fun validateEmail(email: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    fun validatePassword(password: String): Boolean {
        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{4,}$")
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun validateForm(email: String, password: String, passwordConfirmation: String): Boolean {
        return !email.isEmpty() &&
                !password.isEmpty() &&
                !passwordConfirmation.isEmpty() &&
                validateEmail(email) &&
                validatePassword(password) &&
                password == passwordConfirmation
    }
}

}