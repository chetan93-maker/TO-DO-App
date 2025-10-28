package com.td;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EnhancedTodoApp {
 private JFrame frame;
 private DefaultListModel<String> listModel;
 private JList<String> taskList;
 private JTextField taskInput;
 private JLabel statusLabel;
 private int totalTasks = 0;
 private int completedTasks = 0;

 public EnhancedTodoApp() {
     initialize();
 }

 private void initialize() {
     listModel = new DefaultListModel<>();
     
     frame = new JFrame("Enhanced ToDo App");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(500, 600);
     frame.setLocationRelativeTo(null);

     // Create main panel with box layout
     JPanel mainPanel = new JPanel();
     mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
     mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

     // Add title
     JLabel titleLabel = new JLabel("My ToDo List");
     titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
     titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

     // Create input panel
     JPanel inputPanel = createInputPanel();
     
     // Create task list with scroll pane
     taskList = new JList<>(listModel);
     taskList.setFont(new Font("Arial", Font.PLAIN, 14));
     taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     
     JScrollPane scrollPane = new JScrollPane(taskList);
     scrollPane.setPreferredSize(new Dimension(400, 300));
     scrollPane.setBorder(BorderFactory.createTitledBorder(
         BorderFactory.createLineBorder(Color.GRAY), 
         "Tasks", 
         TitledBorder.LEFT, 
         TitledBorder.TOP
     ));

     // Create button panel
     JPanel buttonPanel = createButtonPanel();

     // Create status panel
     JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
     statusLabel = new JLabel("Total: 0 | Completed: 0");
     statusPanel.add(statusLabel);

     // Add components to main panel
     mainPanel.add(titleLabel);
     mainPanel.add(inputPanel);
     mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
     mainPanel.add(scrollPane);
     mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
     mainPanel.add(buttonPanel);
     mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
     mainPanel.add(statusPanel);

     frame.add(mainPanel);
 }

 private JPanel createInputPanel() {
     JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
     
     JLabel inputLabel = new JLabel("New Task:");
     taskInput = new JTextField(20);
     JButton addButton = new JButton("Add");

     // Styling
     addButton.setBackground(new Color(70, 130, 180));
     addButton.setForeground(Color.WHITE);
     addButton.setFocusPainted(false);

     addButton.addActionListener(e -> addTask());
     taskInput.addActionListener(e -> addTask());

     inputPanel.add(inputLabel, BorderLayout.WEST);
     inputPanel.add(taskInput, BorderLayout.CENTER);
     inputPanel.add(addButton, BorderLayout.EAST);

     return inputPanel;
 }

 private JPanel createButtonPanel() {
     JPanel buttonPanel = new JPanel(new FlowLayout());
     
     JButton deleteButton = createStyledButton("Delete", new Color(220, 20, 60));
     JButton completeButton = createStyledButton("Complete", new Color(34, 139, 34));
     JButton clearButton = createStyledButton("Clear All", new Color(255, 140, 0));
     JButton editButton = createStyledButton("Edit", new Color(138, 43, 226));

     deleteButton.addActionListener(e -> deleteTask());
     completeButton.addActionListener(e -> markComplete());
     clearButton.addActionListener(e -> clearAll());
     editButton.addActionListener(e -> editTask());

     buttonPanel.add(deleteButton);
     buttonPanel.add(completeButton);
     buttonPanel.add(editButton);
     buttonPanel.add(clearButton);

     return buttonPanel;
 }

 private JButton createStyledButton(String text, Color color) {
     JButton button = new JButton(text);
     button.setBackground(color);
     button.setForeground(Color.WHITE);
     button.setFocusPainted(false);
     button.setBorder(BorderFactory.createRaisedBevelBorder());
     return button;
 }

 private void addTask() {
     String task = taskInput.getText().trim();
     if (!task.isEmpty()) {
         listModel.addElement(task);
         taskInput.setText("");
         totalTasks++;
         updateStatus();
     } else {
         showMessage("Please enter a task!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 private void deleteTask() {
     int selectedIndex = taskList.getSelectedIndex();
     if (selectedIndex != -1) {
         String task = listModel.get(selectedIndex);
         if (task.startsWith("✓ ")) {
             completedTasks--;
         }
         listModel.remove(selectedIndex);
         totalTasks--;
         updateStatus();
     } else {
         showMessage("Please select a task to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 private void markComplete() {
     int selectedIndex = taskList.getSelectedIndex();
     if (selectedIndex != -1) {
         String task = listModel.get(selectedIndex);
         if (!task.startsWith("✓ ")) {
             listModel.set(selectedIndex, "✓ " + task);
             completedTasks++;
             updateStatus();
             showMessage("Task marked as complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
         } else {
             showMessage("Task is already completed!", "Information", JOptionPane.INFORMATION_MESSAGE);
         }
     } else {
         showMessage("Please select a task!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 private void editTask() {
     int selectedIndex = taskList.getSelectedIndex();
     if (selectedIndex != -1) {
         String currentTask = listModel.get(selectedIndex);
         if (currentTask.startsWith("✓ ")) {
             currentTask = currentTask.substring(2);
         }
         
         String newTask = JOptionPane.showInputDialog(frame, "Edit task:", currentTask);
         if (newTask != null && !newTask.trim().isEmpty()) {
             String taskToSet = listModel.get(selectedIndex).startsWith("✓ ") ? 
                 "✓ " + newTask.trim() : newTask.trim();
             listModel.set(selectedIndex, taskToSet);
         }
     } else {
         showMessage("Please select a task to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 private void clearAll() {
     if (!listModel.isEmpty()) {
         int result = JOptionPane.showConfirmDialog(frame, 
             "Are you sure you want to clear all tasks?", 
             "Confirm Clear", JOptionPane.YES_NO_OPTION);
         
         if (result == JOptionPane.YES_OPTION) {
             listModel.clear();
             totalTasks = 0;
             completedTasks = 0;
             updateStatus();
         }
     } else {
         showMessage("No tasks to clear!", "Information", JOptionPane.INFORMATION_MESSAGE);
     }
 }

 private void updateStatus() {
     statusLabel.setText(String.format("Total: %d | Completed: %d | Pending: %d", 
         totalTasks, completedTasks, totalTasks - completedTasks));
 }

 private void showMessage(String message, String title, int messageType) {
     JOptionPane.showMessageDialog(frame, message, title, messageType);
 }

 public void show() {
     frame.setVisible(true);
 }

 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> new EnhancedTodoApp().show());
 }
}