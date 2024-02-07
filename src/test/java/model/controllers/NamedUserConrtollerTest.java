package model.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.dao.UsersFileManager;
import model.users.Student;
import model.users.Teacher;

import model.Settings;

public class NamedUserConrtollerTest {
    private static NamedUserController controller;
    private static Teacher teacher;
    private static Student student;
    private static String mainFolder;
    private static UsersFileManager fileManager;

    @BeforeAll
    public static void init() {
        mainFolder = Settings.testFolder;

        ArrayList<String> subjects = new ArrayList<>();
        subjects.add("Demo Subjects");
        subjects.add("Veeery useful subject");

        student = new Student("demouser", "1234", "Demo", "Demov", "Demovich", "1234");
        teacher = new Teacher("testuser", "4321", "Demo", "Demova", "Demovna", subjects);
        controller = new NamedUserController(mainFolder);
        fileManager = new UsersFileManager(mainFolder);

        try {
            ArrayList<String> statements = ((Teacher) fileManager.loadUserInfoByLogin(teacher.getLogin())).getStatements();
            teacher.setStatements(statements);

            statements = ((Student) fileManager.loadUserInfoByLogin(student.getLogin())).getStatements();
            student.setStatements(statements);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getUserInfoTestTeacher() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        Teacher expected = (Teacher) new UsersFileManager(mainFolder).loadUserInfoByLogin(teacher.getLogin());
        Teacher actual = (Teacher) controller.getUserInfo(teacher.getLogin());
        assertEquals(expected, actual);
    }

    @Test
    public void getUserClassTestTeacher() {
        Class<?> expected = Teacher.class;
        Class<?> actual = controller.getUserClass(teacher);

        assertEquals(expected, actual);
    }

    @Test
    public void getUserClassTestStudent() {
        Class<?> expected = Student.class;
        Class<?> actual = controller.getUserClass(student);

        assertEquals(expected, actual);
    }
}
