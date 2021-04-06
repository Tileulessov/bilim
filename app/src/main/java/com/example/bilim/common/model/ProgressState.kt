package com.example.bilim.common.model

sealed class ProgressState {
    object Loading : ProgressState()
    object Done : ProgressState()
}