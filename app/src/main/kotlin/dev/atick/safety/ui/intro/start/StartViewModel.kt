package dev.atick.safety.ui.intro.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.data.SingleLiveEvent
import dev.atick.safety.repository.intro.IntroRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    introRepository: IntroRepository
) : ViewModel() {

    private val _userId = MutableLiveData<SingleLiveEvent<String?>>()
    val userId: LiveData<SingleLiveEvent<String?>>
        get() = _userId

    init {
        viewModelScope.launch {
            delay(1000L)
            val result = introRepository.getUserId()
            if (result.isSuccess) {
                val id = result.getOrNull()
                _userId.postValue(SingleLiveEvent(id))
            } else {
                _userId.postValue(SingleLiveEvent(null))
            }
        }
    }
}