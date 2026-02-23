package com.example.imageapp.utils


/*import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MLKitTranslator {

    private var translator: com.google.mlkit.nl.translate.Translator? = null

    fun translate(
        sourceLang: String,
        targetLang: String,
        text: String,
        onResult: (String) -> Unit
    ) {
        if (sourceLang == targetLang) {
            onResult(text)
            return
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(
                TranslateLanguage.fromLanguageTag(sourceLang) ?: TranslateLanguage.ENGLISH
            )
            .setTargetLanguage(
                TranslateLanguage.fromLanguageTag(targetLang) ?: TranslateLanguage.ENGLISH
            )
            .build()

        translator = Translation.getClient(options)

        translator!!.downloadModelIfNeeded()
            .addOnSuccessListener {
                translator!!.translate(text)
                    .addOnSuccessListener { onResult(it) }
                    .addOnFailureListener { onResult(text) }
            }
            .addOnFailureListener { onResult(text) }
    }

    fun close() {
        translator?.close()
    }
}*/