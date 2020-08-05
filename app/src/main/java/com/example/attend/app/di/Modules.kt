package com.example.attend.app.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.example.attend.app.AuthenticationManager
import com.example.attend.data.db.*
import com.example.attend.data.repository.AttendanceRepository
import com.example.attend.ui.course.CoursesViewModel
import com.example.attend.ui.home.HomeViewModel
import com.example.attend.ui.lecstudents.LecStudentViewModel
import com.example.attend.ui.lecturer.LecturerViewModel
import com.example.attend.ui.login.LoginViewModel
import com.example.attend.ui.student.StudentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LecturerViewModel(get()) }

    viewModel { CoursesViewModel(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { LecStudentViewModel(get()) }

    viewModel { StudentViewModel(get()) }

    viewModel { (handle: SavedStateHandle) -> LoginViewModel(handle, get()) }

}
val authModule = module {
    fun provideSharedPref(app: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    fun provideAuthenticationManager(sharedPreferences: SharedPreferences): AuthenticationManager {
        return AuthenticationManager(sharedPreferences)
    }

    single { provideSharedPref(androidApplication()) }
    single { provideAuthenticationManager(get()) }
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
    fun provideRepository(lecturerDao: LecturerDao,
                          courseDao: CourseDao,
                          studentDao: StudentDao,
                          studentCourseCrossRefDao: StudentCourseCrossRefDao): AttendanceRepository {
        return AttendanceRepository(lecturerDao, courseDao, studentDao, studentCourseCrossRefDao)
    }

    single { provideRepository(get(), get(), get(), get()) }
}