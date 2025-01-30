package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/register")
@MultipartConfig // รองรับการอัปโหลดไฟล์
public class register extends HttpServlet {

    private static final String FILE_PATH = "D:/Tomcat_java/java myweb/kk/data/personal_info.txt";
    private static final String UPLOAD_DIR = "D:/Apa/book/profile_img/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // รับข้อมูลจากฟอร์ม
        String username = request.getParameter("u_user");
        String password = request.getParameter("u_pass");
        String name = request.getParameter("u_name");
        String tel = request.getParameter("u_tel");
        Part imagePart = request.getPart("u_img"); // รับไฟล์รูปภาพ

        // ตั้งชื่อไฟล์รูปภาพ
        String imageFileName = username + ".jpg";

        // ตรวจสอบและสร้างโฟลเดอร์อัปโหลดรูปภาพหากยังไม่มี
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // บันทึกข้อมูลลงไฟล์
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(username + "," + password + "," + name + "," + tel + "," + imageFileName);
            writer.newLine();
        }

        // บันทึกไฟล์รูปภาพ
        if (imagePart != null) {
            imagePart.write(UPLOAD_DIR + imageFileName);
        }
        // แสดงผลตอบกลับ
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            response.sendRedirect("index.html");
        }
    }
}