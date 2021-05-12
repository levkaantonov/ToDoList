package levkaantonov.com.study.todolist.utils

import androidx.appcompat.widget.SearchView

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(p0: String?): Boolean = true

        override fun onQueryTextChange(p0: String?): Boolean {
            listener(p0.orEmpty())
            return true
        }
    })
}