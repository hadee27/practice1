class ProjectManager {
    private var projects: MutableList<Project> = mutableListOf()

    fun addProject(name: String) {
        if (projects.none { it.name == name }) {
            projects.add(Project(name))
        }
    }

    fun removeProject(name: String) {
        projects.removeIf { it.name == name }
    }

    fun editProject(oldName: String, newName: String) {
        projects.find { it.name == oldName }?.apply { name = newName }
    }


    fun getProjectNames(): List<String> {
        return projects.map { it.name }
    }

    // Find a project by name
    fun findProjectByName(name: String): Project? {
        return projects.find { it.name == name }
    }


}