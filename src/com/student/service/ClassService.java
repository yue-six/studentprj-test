package com.student.service;

import com.student.entity.SchoolClass;
import com.student.entity.Group;
import com.student.entity.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ClassService {
    private final List<SchoolClass> classes;
    private static ClassService instance;
    private static final String DATA_FILE = "classes.dat";
    private final Random random;

    private ClassService() {
        this.classes = new ArrayList<>();
        this.random = new Random();
        loadData();
    }

    public static ClassService getInstance() {
        if (instance == null) {
            instance = new ClassService();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    List<SchoolClass> loadedClasses = (List<SchoolClass>) ois.readObject();
                    classes.clear();
                    classes.addAll(loadedClasses);
                    System.out.println("Loaded " + classes.size() + " classes");
                    for (SchoolClass cls : classes) {
                        System.out.println("Class: " + cls.getName() + ", Groups: " + cls.getGroups().size());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            classes.clear();
        }
    }

    public void saveData() {
        try {
            System.out.println("Saving data...");
            System.out.println("Number of classes: " + classes.size());
            for (SchoolClass cls : classes) {
                System.out.println("Saving class: " + cls.getName() + ", Groups: " + cls.getGroups().size());
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                oos.writeObject(new ArrayList<>(classes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SchoolClass> getAllClasses() {
        System.out.println("Getting all classes, count: " + classes.size());
        return new ArrayList<>(classes);
    }

    public void addClass(SchoolClass schoolClass) {
        if (schoolClass != null) {
            classes.add(schoolClass);
            saveData();
        }
    }

    public void removeClass(SchoolClass schoolClass) {
        if (schoolClass != null) {
            classes.remove(schoolClass);
            saveData();
        }
    }

    public SchoolClass getClassById(String id) {
        if (id != null) {
            Optional<SchoolClass> result = classes.stream()
                    .filter(c -> id.equals(c.getId()))
                    .findFirst();

            if (result.isPresent()) {
                SchoolClass foundClass = result.get();
                System.out.println("Found class: " + foundClass.getName());
                return foundClass;
            }
        }
        System.out.println("Class not found for id: " + id);
        return null;
    }

    public boolean isClassNameExists(String className) {
        if (className != null) {
            return classes.stream()
                    .anyMatch(c -> className.equals(c.getName()));
        }
        return false;
    }

    public boolean isGroupNameExists(String classId, String groupName) {
        if (classId != null && groupName != null) {
            SchoolClass schoolClass = getClassById(classId);
            if (schoolClass != null) {
                return schoolClass.getGroups().stream()
                        .anyMatch(g -> groupName.equals(g.getName()));
            }
        }
        return false;
    }

    public Group getRandomGroup(String classId) {
        SchoolClass schoolClass = getClassById(classId);
        if (schoolClass != null) {
            List<Group> groups = schoolClass.getGroups();
            if (!groups.isEmpty()) {
                int index = random.nextInt(groups.size());
                Group selectedGroup = groups.get(index);
                System.out.println("Randomly selected group: " + selectedGroup.getName());
                return selectedGroup;
            }
        }
        return null;
    }

    public Student getRandomStudent(String classId) {
        SchoolClass schoolClass = getClassById(classId);
        if (schoolClass != null) {
            List<Student> students = schoolClass.getStudents();
            if (!students.isEmpty()) {
                int index = random.nextInt(students.size());
                Student selectedStudent = students.get(index);
                System.out.println("Randomly selected student: " + selectedStudent.getName());
                return selectedStudent;
            }
        }
        return null;
    }

    public Student getRandomStudentFromGroup(String classId, String groupId) {
        if (classId != null && groupId != null) {
            SchoolClass schoolClass = getClassById(classId);
            if (schoolClass != null) {
                Optional<Group> groupOpt = schoolClass.getGroups().stream()
                        .filter(g -> groupId.equals(g.getId()))
                        .findFirst();

                if (groupOpt.isPresent()) {
                    Group group = groupOpt.get();
                    List<Student> students = group.getStudents();
                    if (!students.isEmpty()) {
                        int index = random.nextInt(students.size());
                        Student selectedStudent = students.get(index);
                        System.out.println("Randomly selected student from group " +
                                group.getName() + ": " + selectedStudent.getName());
                        return selectedStudent;
                    }
                }
            }
        }
        return null;
    }

    public boolean isStudentIdExists(String classId, String studentId) {
        if (classId != null && studentId != null) {
            SchoolClass schoolClass = getClassById(classId);
            if (schoolClass != null) {
                return schoolClass.getStudents().stream()
                        .anyMatch(s -> studentId.equals(s.getId()));
            }
        }
        return false;
    }
}