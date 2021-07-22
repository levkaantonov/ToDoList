package levkaantonov.com.study.todolist.ui.deleteallcompleted

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment: DialogFragment() {
    private val viewModel: DeleteAllCompletedViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Вы действительно хотите удалить все выполненые задания?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes"){ _, _ ->
                viewModel.onConfirmClick()
            }
            .create()
}