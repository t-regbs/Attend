package com.example.attend.app.di

import android.app.Application
import androidx.room.Room
import com.example.attend.data.db.*
import com.example.attend.data.repository.AttendanceRepository
import com.example.attend.ui.lecturer.LecturerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LecturerViewModel(get())
    }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AttendanceDatabase {
        return Room.databaseBuilder(application,
            AttendanceDatabase::class.java,
            "UniAttendance.db")
            .build()
    }

    fun provideLecturerDao(database: AttendanceDatabase): LecturerDao {
        return database.lecturerDao
    }

    fun provideCourseDao(database: AttendanceDatabase): CourseDao {
        return database.courseDao
    }

    fun provideStudentDao(database: AttendanceDatabase): StudentDao {
        return database.studentDao
    }

    fun provideAttendanceDao(database: AttendanceDatabase): AttendanceDao {
        return database.attendanceDao
    }

    fun provideStudentCourseCrossRefDao(database: AttendanceDatabase): StudentCourseCrossRefDao {
        return database.studentCourseCrossRefDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideLecturerDao(get()) }
    single { provideAttendanceDao(get()) }
    single { provideStudentCourseCrossRefDao(get()) }
    single { provideStudentDao(get()) }
    single { provideCourseDao(get()) }
}

val repositoryModule = module {
    fun provideRepository(lecturerDao: LecturerDao): AttendanceRepository {
        return AttendanceRepository(lecturerDao)
    }

    single { provideRepository(get()) }
}