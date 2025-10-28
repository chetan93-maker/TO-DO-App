package com.td;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TodoApp {
 private JFrame frame;
 private JPanel mainPanel;
 private JTextField taskInput;
 private JButton addButton;
 private JButton deleteButton;
 private JList<String> taskList;
 private DefaultListModel<String> listModel;
 private ArrayList<String> tasks;

 public TodoApp() {
     initialize();
 }

 private void initialize() {
     // Initialize components
     tasks = new ArrayList<>();
     listModel = new DefaultListModel<>();
     
     // Create main frame
     frame = new JFrame("ToDo Application");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(400, 500);
     frame.setLocationRelativeTo(null); // Center the window

     // Create main panel with border layout
     mainPanel = new JPanel(new BorderLayout(10, 10));
     mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     // Create input panel
     JPanel inputPanel = createInputPanel();
     
     // Create task list
     taskList = new JList<>(listModel);
     taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     JScrollPane scrollPane = new JScrollPane(taskList);
     scrollPane.setPreferredSize(new Dimension(300, 300));

     // Create button panel
     JPanel buttonPanel = createButtonPanel();

     // Add components to main panel
     mainPanel.add(inputPanel, BorderLayout.NORTH);
     mainPanel.add(scrollPane, BorderLayout.CENTER);
     mainPanel.add(buttonPanel, BorderLayout.SOUTH);

     // Add main panel to frame
     frame.add(mainPanel);
 }

 private JPanel createInputPanel() {
     JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
     
     JLabel label = new JLabel("Enter Task:");
     taskInput = new JTextField();
     addButton = new JButton("Add Task");

     // Add action listener for add button
     addButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             addTask();
         }
     });

     // Add Enter key listener to input field
     taskInput.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             addTask();
         }
     });

     inputPanel.add(label, BorderLayout.WEST);
     inputPanel.add(taskInput, BorderLayout.CENTER);
     inputPanel.add(addButton, BorderLayout.EAST);

     return inputPanel;
 }

 private JPanel createButtonPanel() {
     JPanel buttonPanel = new JPanel(new FlowLayout());
     
     deleteButton = new JButton("Delete Selected Task");
     JButton clearButton = new JButton("Clear All Tasks");
     JButton markCompleteButton = new JButton("Mark Complete");

     // Add action listeners
     deleteButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             deleteSelectedTask();
         }
     });

     clearButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             clearAllTasks();
         }
     });

     markCompleteButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             markTaskComplete();
         }
     });

     buttonPanel.add(deleteButton);
     buttonPanel.add(clearButton);
     buttonPanel.add(markCompleteButton);

     return buttonPanel;
 }

 private void addTask() {
     String task = taskInput.getText().trim();
     if (!task.isEmpty()) {
         tasks.add(task);
         listModel.addElement(task);
         taskInput.setText("");
         taskInput.requestFocus();
     } else {
         JOptionPane.showMessageDialog(frame, "Please enter a task!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 private void deleteSelectedTask() {
     int selectedIndex = taskList.getSelectedIndex();
     if (selectedIndex != -1) {
         String task = listModel.getElementAt(selectedIndex);
         int confirm = JOptionPane.showConfirmDialog(frame, 
             "Are you sure you want to delete: " + task + "?", 
             "Confirm Delete", JOptionPane.YES_NO_OPTION);
         
         if (confirm == JOptionPane.YES_OPTION) {
             listModel.remove(selectedIndex);
             tasks.remove(selectedIndex);
         }
     } else {
         JOptionPane.showMessageDialog(frame, "Please select a task to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 private void clearAllTasks() {
     if (!listModel.isEmpty()) {
         int confirm = JOptionPane.showConfirmDialog(frame, 
             "Are you sure you want to delete all tasks?", 
             "Confirm Clear All", JOptionPane.YES_NO_OPTION);
         
         if (confirm == JOptionPane.YES_OPTION) {
             listModel.clear();
             tasks.clear();
         }
     } else {
         JOptionPane.showMessageDialog(frame, "No tasks to clear!", "Information", JOptionPane.INFORMATION_MESSAGE);
     }
 }

 private void markTaskComplete() {
     int selectedIndex = taskList.getSelectedIndex();
     if (selectedIndex != -1) {
         String task = listModel.getElementAt(selectedIndex);
         if (!task.startsWith("✓ ")) {
             listModel.set(selectedIndex, "✓ " + task);
             JOptionPane.showMessageDialog(frame, "Task marked as complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
         } else {
             JOptionPane.showMessageDialog(frame, "Task is already completed!", "Information", JOptionPane.INFORMATION_MESSAGE);
         }
     } else {
         JOptionPane.showMessageDialog(frame, "Please select a task to mark complete!", "Warning", JOptionPane.WARNING_MESSAGE);
     }
 }

 public void show() {
     frame.setVisible(true);
 }

 public static void main(String[] args) {
     // Use Event Dispatch Thread for thread-safe GUI operations
     SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
             new TodoApp().show();
         }
     });
 }
}