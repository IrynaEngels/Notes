package com.irene.notes


import android.app.Application
import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import com.irene.notes.data.database.NotesDatabase
import com.irene.notes.repository.Repository
import com.irene.notes.viewmodel.NotesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NotesApplication: MultiDexApplication() {
    companion object{
        const val TAG = "DEBUG_NOTE"
        const val sharedPrefsKey = "IS_FIRST_LAUNCH"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(viewModel, dataBaseModule, repositoryModule, sharedPrefsModule)
            androidContext(this@NotesApplication)
        }
    }

    val sharedPrefsModule = module {

        single{
            getSharedPrefs(this@NotesApplication)
        }

        single<SharedPreferences.Editor> {
            getSharedPrefs(this@NotesApplication).edit()
        }
    }

    fun getSharedPrefs(androidApplication: Application): SharedPreferences{
        return  androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
    }


    private val viewModel = module {
        viewModel {
            NotesViewModel(get())
        }
    }

    private val repositoryModule = module {
        single { Repository(get()) }
    }

    private val dataBaseModule = module {
        single { NotesDatabase.invoke(get()) }
        single { get<NotesDatabase>().getNotesDao() }
    }

}