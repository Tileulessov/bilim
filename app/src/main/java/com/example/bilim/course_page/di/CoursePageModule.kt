package com.example.bilim.course_page.di

import com.example.bilim.course_page.presentation.CoursePageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val coursePageModule: Module = module {
      viewModel { CoursePageViewModel() }
}