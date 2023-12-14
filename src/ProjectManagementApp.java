import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ProjectManagementApp extends JFrame implements ActionListener, ListSelectionListener {
    JMenuItem addProjectItem, editProjectItem, deleteProjectItem;
    JMenuItem addTaskItem, editTaskItem, deleteTaskItem, addSuccessorItem,removeSuccessorItem, viewAdjMatrixItem;
    HashMap<String, ArrayList<String>> projectTasks;
    ArrayList<String> projects;
    DefaultListModel<String> projectListModel, taskListModel;
    JList<String> projectList, taskList;
    ProjectManager projectManager;

    ProjectManagementApp() {
        projects = new ArrayList<>();
        projectTasks = new HashMap<>();
        projectListModel = new DefaultListModel<>();
        taskListModel = new DefaultListModel<>();
        projectList = new JList<>(projectListModel);
        taskList = new JList<>(taskListModel);
        projectManager = new ProjectManager();


        JMenuBar menuBar = new JMenuBar();
        JMenu projectMenu = new JMenu("Project");
        JMenu taskMenu = new JMenu("Task");

        // Project Menu Items
        addProjectItem = new JMenuItem("Add Project");
        addProjectItem.addActionListener(this);
        projectMenu.add(addProjectItem);

        editProjectItem = new JMenuItem("Edit Project");
        editProjectItem.addActionListener(this);
        projectMenu.add(editProjectItem);

        deleteProjectItem = new JMenuItem("Delete Project");
        deleteProjectItem.addActionListener(this);
        projectMenu.add(deleteProjectItem);

        // Task Menu Items
        addTaskItem = new JMenuItem("Add Task");
        addTaskItem.addActionListener(this);
        taskMenu.add(addTaskItem);

        editTaskItem = new JMenuItem("Edit Task");
        editTaskItem.addActionListener(this);
        taskMenu.add(editTaskItem);

        deleteTaskItem = new JMenuItem("Delete Task");
        deleteTaskItem.addActionListener(this);
        taskMenu.add(deleteTaskItem);

        menuBar.add(projectMenu);
        menuBar.add(taskMenu);
        this.setJMenuBar(menuBar);

        projectList.addListSelectionListener(this);

        addSuccessorItem = new JMenuItem("Add Successor");
        removeSuccessorItem = new JMenuItem("Remove Successor");

        taskMenu.add(addSuccessorItem);
        taskMenu.add(removeSuccessorItem);

        addSuccessorItem.addActionListener(this);
        removeSuccessorItem.addActionListener(this);

        viewAdjMatrixItem = new JMenuItem("View Adjacency Matrix");
        viewAdjMatrixItem.addActionListener(this);
        taskMenu.add(viewAdjMatrixItem);


        // Layout setup
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(projectList),
                new JScrollPane(taskList));
        splitPane.setDividerLocation(250);
        this.add(splitPane, BorderLayout.CENTER);

        this.setSize(800, 800);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addProjectItem) {
            addNewProject();
        } else if (e.getSource() == editProjectItem) {
            editProject();
        } else if (e.getSource() == deleteProjectItem) {
            deleteProject();
        } else if (e.getSource() == addTaskItem) {
            addTask();
        } else if (e.getSource() == editTaskItem) {
            editTask();
        } else if (e.getSource() == deleteTaskItem) {
            deleteTask();
        }else if (e.getSource() == addSuccessorItem) {
            manageSuccessor(true);
        } else if (e.getSource() == removeSuccessorItem) {
            manageSuccessor(false);
        }else if (e.getSource() == viewAdjMatrixItem) {
            viewAdjacencyMatrix();
        }
    }
    private void viewAdjacencyMatrix() {
        int selectedProjectIndex = projectList.getSelectedIndex();
        if (selectedProjectIndex != -1) {
            String projectName = projectListModel.getElementAt(selectedProjectIndex);
            Project selectedProject = projectManager.findProjectByName(projectName);
            if (selectedProject != null) {
                Integer[][] matrix = selectedProject.getAdjacencyMatrix(); // Use Integer[][] here
                displayMatrix(matrix, projectName);
            }
        }
    }



    private void displayMatrix(Integer[][] matrix, String projectName) {
        StringBuilder matrixDisplay = new StringBuilder("Adjacency Matrix for " + projectName + ":\n");
        for (Integer[] row : matrix) {
            for (Integer cell : row) {
                matrixDisplay.append(cell).append(" ");
            }
            matrixDisplay.append("\n");
        }
        JOptionPane.showMessageDialog(this, matrixDisplay.toString());
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (projectList.getSelectedIndex() != -1) {
                String projectName = projectListModel.getElementAt(projectList.getSelectedIndex());
                Project selectedProject = projectManager.findProjectByName(projectName);
                if (selectedProject != null) {
                    updateTaskList(selectedProject);
                }
            }
        }
    }


    private void addNewProject() {
        String projectname = JOptionPane.showInputDialog(this, "Enter Project Name:");
        projectManager.addProject(projectname);
        refreshProjectList();
    }

    private void editProject() {
        int selectedIdx = projectList.getSelectedIndex();
        if (selectedIdx != -1) {
            String currentName = projectListModel.getElementAt(selectedIdx);
            String newName = JOptionPane.showInputDialog(this, "Edit Project Name:", currentName);

            if (newName != null && !newName.trim().isEmpty() && !newName.equals(currentName)) {
                projectManager.editProject(currentName, newName);
                refreshProjectList();
            }
        }
    }

    private void deleteProject() {
        int selectedIdx = projectList.getSelectedIndex();
        if (selectedIdx != -1) {
            String projectName = projectListModel.getElementAt(selectedIdx);
            projectManager.removeProject(projectName);
            refreshProjectList();
            taskListModel.clear();

//            String projectName = projects.remove(selectedIdx);
//            projectListModel.remove(selectedIdx);
//            projectTasks.remove(projectName);
//            taskListModel.clear(); // Clear tasks list when project is deleted
        }
    }

    private void addTask() {
        int selectedProjectIndex = projectList.getSelectedIndex();
        if (selectedProjectIndex != -1) {
            String projectName = projectListModel.getElementAt(selectedProjectIndex);
            Project selectedProject = projectManager.findProjectByName(projectName);
            if (selectedProject != null) {
                String taskIdStr = JOptionPane.showInputDialog(this, "Enter Task Id:");
                String taskName = JOptionPane.showInputDialog(this, "Enter Task Name:");
                String durationStr = JOptionPane.showInputDialog(this, "Enter Task Duration:");

                if (taskIdStr != null && !taskIdStr.trim().isEmpty() &&
                        taskName != null && !taskName.trim().isEmpty() &&
                        durationStr != null && !durationStr.trim().isEmpty()) {
                    try {
                        // Convert taskId and duration strings to integers
                        int taskId = Integer.parseInt(taskIdStr);
                        int duration = Integer.parseInt(durationStr);

                        // Assuming the Task class has a constructor with id, name, and duration
                        Task newTask = new Task(taskId, taskName, duration);
                        selectedProject.addTask(newTask);
                        updateTaskList(selectedProject);
                    } catch (NumberFormatException ex) {
                        // Handle the case where taskId or duration is not a valid integer
                        JOptionPane.showMessageDialog(this, "Please enter valid numbers for the Task ID and duration.");
                    }
                }
            }
        }
    }


    private void editTask() {
        int selectedProjectIndex = projectList.getSelectedIndex();
        int selectedTaskIndex = taskList.getSelectedIndex();
        if (selectedProjectIndex != -1 && selectedTaskIndex != -1) {
            String projectName = projectListModel.getElementAt(selectedProjectIndex);
            Project selectedProject = projectManager.findProjectByName(projectName);
            if (selectedProject != null) {
                Task taskToEdit = selectedProject.getTasks().get(selectedTaskIndex);
                String newTaskName = JOptionPane.showInputDialog(this, "Edit Task Name:", taskToEdit.getName());
                if (newTaskName != null && !newTaskName.trim().isEmpty()) {
                    selectedProject.editTask(taskToEdit.getName(), newTaskName);
                    updateTaskList(selectedProject);
                }
            }
        }
    }

    private void deleteTask() {
        int selectedProjectIndex = projectList.getSelectedIndex();
        int selectedTaskIndex = taskList.getSelectedIndex();
        if (selectedProjectIndex != -1 && selectedTaskIndex != -1) {
            String projectName = projectListModel.getElementAt(selectedProjectIndex);
            Project selectedProject = projectManager.findProjectByName(projectName);
            if (selectedProject != null && selectedTaskIndex < selectedProject.getTasks().size()) {
                // Get the task to be deleted
                Task taskToDelete = selectedProject.getTasks().get(selectedTaskIndex);
                // Delete the task using the deleteTask method
                selectedProject.deleteTask(taskToDelete);
                // Update the task list in the GUI
                updateTaskList(selectedProject);
            }
        }
    }
    private void manageSuccessor(boolean isAdding) {
        int selectedProjectIndex = projectList.getSelectedIndex();
        int selectedTaskIndex = taskList.getSelectedIndex();
        if (selectedProjectIndex != -1 && selectedTaskIndex != -1) {
            String projectName = projectListModel.getElementAt(selectedProjectIndex);
            Project selectedProject = projectManager.findProjectByName(projectName);
            if (selectedProject != null) {
                Task selectedTask = selectedProject.getTasks().get(selectedTaskIndex);
                if (isAdding) {
                    // Code to add successor
                    addSuccessor(selectedProject, selectedTask);
                } else {
                    // Code to remove successor
                    removeSuccessor(selectedProject, selectedTask);
                }
            }
        }
    }

    private void addSuccessor(Project project, Task task) {
        // Show a dialog to select the successor task
        Task successor = selectSuccessorTask(project, task);
        if (successor != null) {
            try {
                task.addSuccessor(successor);  // Assuming Task class has addSuccessor method
                JOptionPane.showMessageDialog(this, "Successor added successfully.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding successor: " + e.getMessage());
            }
        }
    }

    private void removeSuccessor(Project project, Task task) {
        // Show a dialog to select the successor task
        Task successor = selectSuccessorTask(project, task);
        if (successor != null) {
            try {
                task.removeSuccessor(successor);  // Assuming Task class has removeSuccessor method
                JOptionPane.showMessageDialog(this, "Successor removed successfully.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error removing successor: " + e.getMessage());
            }
        }
    }

    private Task selectSuccessorTask(Project project, Task currentTask) {
        List<Task> possibleSuccessors = project.getTasks().stream()
                .filter(t -> !t.equals(currentTask))
                .collect(Collectors.toList());

        Task[] successorArray = new Task[possibleSuccessors.size()];
        possibleSuccessors.toArray(successorArray);

        Task selected = (Task) JOptionPane.showInputDialog(
                this,
                "Select Successor Task:",
                "Select Task",
                JOptionPane.QUESTION_MESSAGE,
                null,
                successorArray,
                successorArray[0]
        );

        return selected;
    }






    private void updateTaskList(Project project) {
        taskListModel.clear();
        for (Task task : project.getTasks()) {
            // Format the main task details
            String mainTaskDetails = String.format("ID: %d, Name: %s, Duration: %d", task.getId(), task.getName(), task.getDuration());
            taskListModel.addElement(mainTaskDetails);

            // Add the successors of the task with indentation for better readability
            for (Task successor : task.getSuccessors()) {
                String successorDetails = String.format("   Successor - ID: %d, Name: %s, Duration: %d", successor.getId(), successor.getName(), successor.getDuration());
                taskListModel.addElement(successorDetails);
            }
        }
    }



    private void refreshProjectList() {
        projectListModel.clear();
        for (String projectName : projectManager.getProjectNames()) {
            projectListModel.addElement(projectName);
        }
    }

}

