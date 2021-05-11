package levkaantonov.com.study.todolist.data

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import levkaantonov.com.study.todolist.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDb : RoomDatabase() {

    abstract fun taskDao() : TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDb>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task(name = "Купить хлеба", isCompleted = true))
                dao.insert(Task(name = "Поймать всех гусей на свете", isImportant = true))
                dao.insert(Task(name = "Словить полярную сову", isImportant = true))
                dao.insert(Task(name = "Починить мой мотик"))
                dao.insert(Task(name = "Купить значки на бимер"))
                dao.insert(Task(name = "Отвезти колеса на правку", isCompleted = true))
                dao.insert(Task(name = "Купить коту еды"))
                dao.insert(Task(name = "Налить коту воды"))
                dao.insert(Task(name = "Прикрепить телик к стене"))
            }
        }
    }
}