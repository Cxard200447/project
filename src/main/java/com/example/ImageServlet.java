package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/img/*")
public class ImageServlet extends HttpServlet {
    private static final String IMAGE_DIRECTORY = "D:/Tomcat_java/java myweb/kk/data/img/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getPathInfo().substring(1); // ดึงชื่อไฟล์จาก URL
        File file = new File(IMAGE_DIRECTORY + fileName);

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "รูปภาพไม่พบ");
            return;
        }

        response.setContentType(getServletContext().getMimeType(file.getName()));
        response.setContentLengthLong(file.length());

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
