package model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.users.Student;
import model.users.Teacher;
import model.users.UsersBase;

import model.Settings;

public class UsersFileManagerTest {
    private static UsersFileManager fileManager;
    private static Teacher teacher;
    private static Student student;
    private static ArrayList<String> statements;
    private static ArrayList<String> subjects;
    private static UsersBase ub;

    @BeforeAll
    public static void init() {
        fileManager = new UsersFileManager(Settings.testFolder);
        statements = new ArrayList<>();
        statements.add("Some-file.txt");
        statements.add("New-Some-file.txt");

        subjects = new ArrayList<>();
        subjects.add("Demo Subjects");
        subjects.add("Veeery useful subject");

        student = new Student("demouser", "1234", "Demo", "Demov", "Demovich", "1234");
        teacher = new Teacher("testuser", "4321", "Demo", "Demova", "Demovna", subjects);

        student.setStatements(statements);
        teacher.setStatements(statements);

        ub = new UsersBase();
        ub.addUser(student);
        ub.addUser(teacher);
    }

    @Test // Проверить название файлов перед проверкой
    public void saveTeacherInfoTest() {
        try {
            fileManager.saveUserInfo(teacher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveStudentInfoTest() {
        try {
            fileManager.saveUserInfo(student);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadTeacherInfoByLoginTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        Teacher expected = teacher;
        Teacher actual = (Teacher) fileManager.loadUserInfoByLogin(teacher.getLogin());

        assertEquals(expected, actual);
    }

    @Test
    public void loadStudentInfoByLoginTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        Student expected = student;
        Student actual = (Student) fileManager.loadUserInfoByLogin(student.getLogin());

        assertEquals(expected, actual);
    }

    @Test
    public void saveUsersBaseTest() {
        try {
            fileManager.saveUsersBase(ub);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadUsersBaseTest() throws IOException {
        UsersBase expected = ub;
        UsersBase actual = fileManager.loadUsersBase();

        assertEquals(expected, actual);
    }
}
