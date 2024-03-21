package com.ketchup.dailymanna.koin

import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import com.ketchup.dailymanna.AppDatabase
import com.ketchup.dailymanna.repositories.MannaRepository
import com.ketchup.dailymanna.viewmodel.MannaViewModel
import org.koin.android.ext.koin.androidContext

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext() }
    single { AppDatabase.getInstance(androidContext()).mannaTextDao()}
    single {PreferenceManager.getDefaultSharedPreferences(androidContext())}
    single {TextToSpeech(androidContext(), null)}

    single { MannaRepository(get()) }

    viewModel {
        MannaViewModel(
            mannaTextDao = get(),
            sharedPreferences = get(),
            textToSpeech = get()
        )
    }
}