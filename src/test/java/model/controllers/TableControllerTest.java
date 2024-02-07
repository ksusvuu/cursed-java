package model.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dao.TableFileManager;
import model.tables.Table;

import model.Settings;

public class TableControllerTest {
    private static TableController tableController;
    private static TableFileManager fileManager;

    @BeforeAll
    public static void init() {
        tableController = new TableController(Settings.testFolder);
        fileManager = new TableFileManager(Settings.testFolder);
    }

    @BeforeEach
    public void setTable() throws IOException {
        Table table = tableController.loadTable("Cybersecurity-2150.txt");
        fileManager.saveToTextFile(table, "Cybersecurity-1234.txt");
    }

    @Test
    public void runActionsTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Table actual = tableController.loadTable("Cybersecurity-1234.txt");
        String addRow = "addRowBack()";
        String setCell = "setCellContent(1, 1, test)";
        String addColumn = "addColumnBack()";
        String clearCell = "clearCell(0, 0)";

        ArrayList<String> actions = new ArrayList<>();
        actions.add(addRow);
        actions.add(addColumn);
        actions.add(setCell);
        actions.add(clearCell);

        tableController.run("Cybersecurity-1234.txt", actions);
        
        actual.addRowBack();
        actual.addColumnBack();
        actual.setCellContent(1, 1, "test");
        actual.clearCell(0, 0);

        Table expected = tableController.loadTable("Cybersecurity-1234.txt");

        assertEquals(expected, actual);
    }
}
