package levkaantonov.com.study.todolist.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"
private const val USER_PREFS_NAME = "user_preferences"

enum class SortOrder { BY_NAME, BY_DATE }

data class FilteredPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore by preferencesDataStore(USER_PREFS_NAME)

    val preferenceFlow: Flow<FilteredPreferences> =
        context.dataStore.data
        .catch { ex ->
            if(ex is IOException){
                Log.e(TAG, "Error reading prefs", ex )
                emit(emptyPreferences())
            }else{
                throw ex
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilteredPreferences(sortOrder, hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKeys{
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }
}