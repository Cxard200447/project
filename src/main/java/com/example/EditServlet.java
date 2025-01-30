package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.util.*;

@WebServlet("/edit")
@MultipartConfig
public class EditServlet extends HttpServlet {
    private static final String FILE_PATH = "D:/Tomcat_java/java myweb/kk/data/food-info.txt";
    private static final String IMAGE_DIRECTORY = "D:/Tomcat_java/java myweb/kk/data/img/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        response.setContentType("text/html;charset=UTF-8");

        String[] recordToEdit = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length > 0 && record[0].trim().equals(id)) {
                    recordToEdit = record;
                    break;
                }
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>แก้ไขข้อมูล</title>");
            out.println("<style>");
            out.println("body { font-family: 'Roboto', Arial, sans-serif; margin: 0; padding: 0; background-color: #20232a; color: #ffffff; line-height: 1.6; }");
            out.println("h1 { text-align: center; color: #61dafb; margin-top: 20px; }");
            out.println("form { width: 60%; margin: 20px auto; background-color: #282c34; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
            out.println("label { display: block; margin: 10px 0; color: #61dafb; }");
            out.println("input[type='text'], input[type='file'] { width: 100%; padding: 10px; margin-top: 5px; border: none; border-radius: 4px; }");
            out.println("button { background-color: #61dafb; color: #20232a; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }");
            out.println("button:hover { background-color: #4faadf; }");
            out.println(".link { display: block; text-align: center; margin: 20px auto; color: #61dafb; text-decoration: none; font-size: 18px; }");
            out.println(".link:hover { text-decoration: underline; color: #4faadf; }");
            out.println("img { max-width: 200px; display: block; margin: 10px 0; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            if (recordToEdit == null) {
                out.println("<h1>ไม่พบข้อมูลที่ต้องการแก้ไข</h1>");
                out.println("<a href='home.html' class='link'>กลับไปหน้าหลัก</a>");
            } else {
                out.println("<h1>แก้ไขข้อมูล</h1>");
                out.println("<form action='edit' method='post' enctype='multipart/form-data'>");
                out.println("<input type='hidden' name='oldId' value='" + recordToEdit[0] + "'>");
                out.println("<input type='hidden' name='oldImage' value='" + (recordToEdit.length > 3 ? recordToEdit[3] : "") + "'>");
                out.println("<label>ชื่อ: <input type='text' name='name' value='" + recordToEdit[0] + "' required></label>");
                out.println("<label>หมวดหมู่: <input type='text' name='type' value='" + recordToEdit[1] + "' required></label>");
                out.println("<label>ราคา: <input type='text' name='price' value='" + recordToEdit[2] + "' required></label>");
                if (recordToEdit.length > 3 && !recordToEdit[3].isEmpty()) {
                    out.println("<label>รูปภาพปัจจุบัน:</label>");
                    out.println("<img src='img/" + recordToEdit[3] + "' alt='รูปภาพอาหาร'>");
                }
                out.println("<label>อัปโหลดรูปภาพใหม่: <input type='file' name='image'></label>");
                out.println("<button type='submit'>บันทึก</button>");
                out.println("</form>");
                out.println("<a href='home.html' class='link'>กลับไปหน้าหลัก</a>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldId = request.getParameter("oldId");
        String oldImage = request.getParameter("oldImage");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String price = request.getParameter("price");

        Part imagePart = request.getPart("image");
        String newImageName = oldImage;

        // หากมีการอัปโหลดรูปภาพใหม่
        if (imagePart != null && imagePart.getSize() > 0) {
            // ลบรูปภาพเก่า
            if (oldImage != null && !oldImage.isEmpty()) {
                File oldImageFile = new File(IMAGE_DIRECTORY + oldImage);
                if (oldImageFile.exists()) {
                    oldImageFile.delete();
                }
            }

            // บันทึกรูปภาพใหม่
            newImageName = UUID.randomUUID() + "_" + imagePart.getSubmittedFileName();
            imagePart.write(IMAGE_DIRECTORY + newImageName);
        }

        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length > 0 && record[0].trim().equals(oldId)) {
                    record[0] = name;
                    record[1] = type;
                    record[2] = price;
                    record[3] = newImageName;
                }
                records.add(record);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        }

        response.sendRedirect("home.html");
    }
}
