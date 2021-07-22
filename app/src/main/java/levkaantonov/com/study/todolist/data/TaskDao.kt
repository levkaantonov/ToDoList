package levkaantonov.com.study.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean) : Flow<List<Task>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDate(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }

    @Query("select * from tasks where (isCompleted!= :hideCompleted  or isCompleted = 0) and name like '%' || :searchQuery || '%' order by isImportant desc, name")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean) : Flow<List<Task>>

    @Query("select * from tasks where (isCompleted!= :hideCompleted  or isCompleted = 0) and name like '%' || :searchQuery || '%' order by isImportant desc, createdDate")
    fun getTasksSortedByDate(searchQuery: String, hideCompleted: Boolean) : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("delete from tasks where isCompleted = 1")
    suspend fun deleteCompletedTasks()
}