package levkaantonov.com.study.todolist.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import levkaantonov.com.study.todolist.data.PreferencesManager
import levkaantonov.com.study.todolist.data.SortOrder
import levkaantonov.com.study.todolist.data.Task
import levkaantonov.com.study.todolist.data.TaskDao
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferenceFlow


    private val taskEventChannel = Channel<TaskEvent>()
    val taskEvent: Flow<TaskEvent> = taskEventChannel.receiveAsFlow()


    private val taskFlow = combine(searchQuery, preferencesFlow)
    { query, filteredPreferences ->
        Pair(query, filteredPreferences)
    }.flatMapLatest {(query, filteredPreferences) ->
        taskDao.getTasks(query, filteredPreferences.sortOrder, filteredPreferences.hideCompleted)
    }

    val tasks = taskFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedSelected(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task){}

    fun onTaskCheckedChange(task: Task, isChecked: Boolean)= viewModelScope.launch{
        taskDao.update(task.copy(isCompleted = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TaskEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    sealed class TaskEvent{
        data class ShowUndoDeleteTaskMessage(val task: Task) : TaskEvent()
    }
}
