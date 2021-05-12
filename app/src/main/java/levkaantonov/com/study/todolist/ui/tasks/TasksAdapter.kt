package levkaantonov.com.study.todolist.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import levkaantonov.com.study.todolist.data.Task
import levkaantonov.com.study.todolist.databinding.ItemTaskBinding

class TasksAdapter (private val listener: OnItemClickListener) : ListAdapter<Task, RecyclerView.ViewHolder>(TasksDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        (holder as TaskViewHolder).bind(currentItem)
    }

    interface OnItemClickListener{
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    //ViewHolder
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                checkboxComplete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkboxComplete.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkboxComplete.isChecked = task.isCompleted
                tvTaskName.text = task.name
                tvTaskName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant
            }
        }
    }
}

class TasksDiffUtilCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task) =
        oldItem == newItem
}

