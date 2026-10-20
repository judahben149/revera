package com.jj.revera.shared

import androidx.lifecycle.ViewModel
import com.jj.revera.core.data.repository.UserPreferencesRepository
import com.jj.revera.core.model.UserPreferences
import kotlinx.coroutines.flow.StateFlow

/** App-wide state that outlives individual screens (theme today; session/onboarding later). */
internal class AppViewModel(
    preferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences
}
