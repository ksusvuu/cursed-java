package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.controllers.NamedUserController;
import model.users.NamedUser;
import model.users.Teacher;

import model.Settings;

@WebServlet(urlPatterns = {"/home"})
public class HomePageServlet extends HttpServlet {
    private NamedUserController userController;
    private String mainFolder;

    @Override
    public void init() throws ServletException {
        mainFolder = Settings.mainFolder;
        userController = new NamedUserController(mainFolder);
    }

    @Override
    protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        HttpSession session = arg0.getSession();
        if (session.getAttribute("login") != null) {
            super.service(arg0, arg1);
        } else {
            arg1.sendRedirect("/digital-statement/static/auth-error.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String login = (String) session.getAttribute("login");

        boolean isTeacher = false;
        NamedUser currentUser = null;
        ArrayList<String> guiElements = new ArrayList<>();

        currentUser = (NamedUser) userController.getUserInfo(login);

        Gson gson = new Gson();

        if (currentUser instanceof Teacher) {
            guiElements.add("Ведомости");
            guiElements.add("Создать ведомость");
            isTeacher = true;
        } else {
            userController.refreshStatements(currentUser);
            for (String v : currentUser.getStatements()) {
                guiElements.add(v);
            }
        }

        session.setAttribute("isTeacher", isTeacher);
        Cookie isTeacherCookie = new Cookie("isTeacher", String.valueOf(isTeacher));

        String guiElementsBase64 = Base64.getEncoder().encodeToString(gson.toJson(guiElements).getBytes("UTF-8"));
        Cookie guiElementsCookie = new Cookie("guiElements", guiElementsBase64);

        resp.addCookie(isTeacherCookie);
        resp.addCookie(guiElementsCookie);

        req.getRequestDispatcher("/static/home.html").forward(req, resp);
    }
}
