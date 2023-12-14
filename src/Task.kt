class Task(val id: Int,var name: String, val duration: Int) {


    // List of successor tasks
    val successors: MutableList<Task> = mutableListOf()
    override fun toString(): String {
        return name
    }

    // Adds a successor task
    fun addSuccessor(task: Task) {
        successors.add(task)
    }
    fun removeSuccessor(task: Task){
        successors.remove(task)
    }

    // Removes a successor task by its name
    fun removeSuccessorByName(successorName: String) {
        successors.removeIf { it.name == successorName }
    }
    fun isDirectSuccessor(task: Task): Boolean {
        return successors.contains(task)
    }

}


