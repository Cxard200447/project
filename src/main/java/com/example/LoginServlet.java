package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // รับค่าจากฟอร์ม
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // ตั้งค่าการตอบกลับ
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                // หากล็อกอินสำเร็จ
              response.sendRedirect("home.html");
            } else {
                // หากล็อกอินล้มเหลว
                response.sendRedirect("index.html");
            }
        }
    }
}