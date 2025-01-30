package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private static final String FILE_PATH = "D:/Tomcat_java/java myweb/kk/data/food-info.txt";
    private static final String IMAGE_DIRECTORY = "D:/Tomcat_java/java myweb/kk/data/img/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        response.setContentType("text/html;charset=UTF-8");

        List<String[]> matchedRecords = new ArrayList<>();

        // อ่านไฟล์และค้นหา
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record[0].toLowerCase().contains(query.toLowerCase())) {
                    matchedRecords.add(record);
                }
            }
        }

        // เขียน HTML ผลลัพธ์การค้นหา
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>ผลลัพธ์การค้นหา</title>");
            out.println("<style>");
            out.println("body { font-family: 'Roboto', Arial, sans-serif; margin: 0; padding: 0; background-color: #20232a; color: #ffffff; line-height: 1.6; }");
            out.println("h1 { text-align: center; color: #61dafb; margin-top: 20px; }");
            out.println("table { width: 90%; margin: 20px auto; border-collapse: collapse; background-color: #282c34; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
            out.println("th, td { padding: 15px; text-align: center; border-bottom: 1px solid #444; }");
            out.println("th { background-color: #333; color: #61dafb; text-transform: uppercase; }");
            out.println("tr:nth-child(even) { background-color: #2a2a2a; }");
            out.println("tr:hover { background-color: #3a3a3a; }");
            out.println("img { max-width: 100px; max-height: 100px; border-radius: 8px; }");
            out.println("a { color: #61dafb; text-decoration: none; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println(".btn-delete { color: #ff5c5c; font-weight: bold; }");
            out.println(".btn-delete:hover { color: #e63939; text-decoration: underline; }");
            out.println(".link { display: block; text-align: center; margin: 20px auto; color: #61dafb; text-decoration: none; font-size: 18px; }");
            out.println(".link:hover { text-decoration: underline; color: #4faadf; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>ผลลัพธ์การค้นหา</h1>");

            if (matchedRecords.isEmpty()) {
                out.println("<p style='text-align: center;'>ไม่พบข้อมูลที่ตรงกับคำค้นหา</p>");
            } else {
                out.println("<table>");
                out.println("<thead>");
                out.println("<tr><th>ชื่ออาหาร</th><th>หมวดหมู่</th><th>ราคา</th><th>รูปภาพ</th><th>การดำเนินการ</th></tr>");
                out.println("</thead>");
                out.println("<tbody>");

                for (String[] record : matchedRecords) {
                    String imageName = record[0] + ".jpg"; // ตั้งชื่อนามสกุลรูปภาพเป็น .jpg
                    File imageFile = new File(IMAGE_DIRECTORY, imageName);
                    String imagePath = imageFile.exists() ? "img/" + imageName : "img/no-image.jpg";

                    out.println("<tr>");
                    out.println("<td>" + record[0] + "</td>");
                    out.println("<td>" + record[1] + "</td>");
                    out.println("<td>" + record[2] + "</td>");
                    out.println("<td><img src='" + imagePath + "' alt='รูปภาพ'></td>");
                    out.println("<td>");
                    out.println("<a href='edit?id=" + record[0] + "'>แก้ไข</a> | ");
                    out.println("<a href='delete?id=" + record[0] + "' class='btn-delete'>ลบ</a>");
                    out.println("</td>");
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
            }

            out.println("<a href='search.html' class='link'>ค้นหาใหม่</a>");
            out.println("<a href='home.html' class='link'>กลับหน้าหลัก</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
