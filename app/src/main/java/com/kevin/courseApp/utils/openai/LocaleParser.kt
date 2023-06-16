
package com.kevin.courseApp.utils.openai

import java.util.Locale

class LocaleParser {
    companion object {
        fun parse(language: String): Locale {
            return Locale.forLanguageTag(language)
        }
    }
}