class Project(var name: String) {
    private val tasks: MutableList<Task> = mutableListOf()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun deleteTask(task: Task) {
        tasks.remove(task)
    }

//    fun deleteTaskByName(taskName: String) {
//        tasks.removeIf { it.name == taskName }
//    }

    fun editTask(oldName: String, newName: String) {
        val task = findTaskByName(oldName)
        task?.name = newName
    }

    fun getTasks(): List<Task> = tasks

    fun findTaskByName(taskName: String): Task? = tasks.find { it.name == taskName }

    // Function to generate the adjacency matrix for the project
    fun getAdjacencyMatrix(): Array<Array<Int>> {
        val matrix = Array(tasks.size) { Array(tasks.size) { 0 } }
        for (i in tasks.indices) {
            for (j in tasks.indices) {
                if (tasks[i].isDirectSuccessor(tasks[j])) {
                    matrix[i][j] = 1
                }
            }
        }
        return matrix
    }
}
