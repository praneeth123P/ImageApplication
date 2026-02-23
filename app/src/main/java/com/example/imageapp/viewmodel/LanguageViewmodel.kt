package com.example.imageapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class Language(val code: String, val name: String, val flagEmoji: String)

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    val allLanguages = listOf(
        Language("en", "English", "🇬🇧"),
        Language("hi", "Hindi - हिंदी", "🇮🇳"),
        Language("kn", "Kannada - ಕನ್ನಡ", "🇮🇳"),
        Language("ml", "Malayalam - മലയാളം", "🇮🇳"),
        Language("mt", "Maltese - Malti", "🇲🇹"),
        Language("mr", "Marathi - मराठी", "🇮🇳"),
        Language("mn", "Mongolian - Монгол", "🇲🇳"),
        Language("my", "Myanmar (Burmese) - မြန်မာ", "🇲🇲"),
        Language("ne", "Nepali - नेपाली", "🇳🇵"),
        Language("no", "Norwegian - Norsk", "🇳🇴"),
        Language("pa", "Punjabi - ਪੰਜਾਬੀ", "🇮🇳"),
        Language("fa", "Persian - فارسی", "🇮🇷"),
        Language("pl", "Polish - Polski", "🇵🇱"),
        Language("pt", "Portuguese - Português", "🇵🇹"),
        Language("ro", "Romanian - Română", "🇷🇴"),
        Language("ru", "Russian - Русский", "🇷🇺"),
        Language("sr", "Serbian - Српски", "🇷🇸"),
        Language("si", "Sinhala - සිංහල", "🇱🇰"),
        Language("sk", "Slovak - Slovenčina", "🇸🇰"),
        Language("sl", "Slovenian - Slovenščina", "🇸🇮"),
        Language("so", "Somali - Soomaali", "🇸🇴"),
        Language("es", "Spanish - Español", "🇪🇸"),
        Language("sw", "Swahili - Kiswahili", "🇰🇪"),
        Language("sv", "Swedish - Svenska", "🇸🇪"),
        Language("tl", "Tagalog (Filipino)", "🇵🇭"),
        Language("tg", "Tajik - Тоҷикӣ", "🇹🇯"),
        Language("ta", "Tamil - தமிழ்", "🇮🇳"),
        Language("te", "Telugu - తెలుగు", "🇮🇳"),
        Language("th", "Thai - ไทย", "🇹🇭"),
        Language("tr", "Turkish - Türkçe", "🇹🇷"),
        Language("uk", "Ukrainian - Українська", "🇺🇦"),
        Language("ur", "Urdu - اردو", "🇵🇰"),
        Language("uz", "Uzbek - O'zbek", "🇺🇿"),
        Language("vi", "Vietnamese - Tiếng Việt", "🇻🇳"),
        Language("cy", "Welsh - Cymraeg", "🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
        Language("yo", "Yoruba - Yorùbá", "🇳🇬"),
        Language("zu", "Zulu - isiZulu", "🇿🇦")
    )

    private val _filteredLanguages = MutableLiveData<List<Language>>(allLanguages)
    val filteredLanguages: LiveData<List<Language>> = _filteredLanguages

    private val _selectedLanguage = MutableLiveData<String>()
    val selectedLanguage: LiveData<String> = _selectedLanguage

    init {
        _selectedLanguage.value = prefs.getString("language", "en") ?: "en"
    }

    fun selectLanguage(code: String) {
        _selectedLanguage.value = code
    }

    fun saveLanguage(code: String) {
        prefs.edit().putString("language", code).apply()
        _selectedLanguage.value = code
    }


    fun searchLanguages(query: String) {
        _filteredLanguages.value = if (query.isBlank()) {
            allLanguages
        } else {
            allLanguages.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.code.contains(query, ignoreCase = true)
            }
        }
    }
}






