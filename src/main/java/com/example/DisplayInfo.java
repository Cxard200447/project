package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/display-info")
public class DisplayInfo extends HttpServlet {
    // เก็บข้อมูลอาหาร
    private static final String FILE_PATH = "D:/Tomcat_java/java myweb/kk/data/food-info.txt";
    private static final String IMAGE_PATH = "D:/Tomcat_java/java myweb/kk/data/img/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // อ่านข้อมูลจากไฟล์
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line.split(",")); // แยกข้อมูลด้วยเครื่องหมาย ","
            }
        }

        // สร้างหน้า HTML เพื่อตอบกลับ
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>จัดการข้อมูลอาหาร</title>");
            out.println("<style>");
            out.println("body { font-family: 'Roboto', Arial, sans-serif; margin: 0; padding: 0; background-color: #20232a; color: #ffffff; line-height: 1.6; }");
            out.println(".container { max-width: 800px; margin: 50px auto; padding: 20px; background-color: #282c34; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
            out.println("h1 { text-align: center; color: #61dafb; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #444; }");
            out.println("th { background-color: #333; color: #61dafb; text-transform: uppercase; }");
            out.println("tr:nth-child(even) { background-color: #2a2a2a; }");
            out.println("tr:hover { background-color: #3a3a3a; }");
            out.println("img { max-width: 100px; max-height: 100px; border-radius: 8px; }");
            out.println(".btn-edit { background-color: #4CAF50; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; }");
            out.println(".btn-edit:hover { background-color: #45a049; }");
            out.println(".btn-delete { background-color: #ff5c5c; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; }");
            out.println(".btn-delete:hover { background-color: #e63939; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>จัดการข้อมูลอาหาร</h1>");
            out.println("<table>");
            out.println("<thead><tr><th>ชื่ออาหาร</th><th>ประเภท</th><th>ราคา</th><th>รูปภาพ</th><th>การกระทำ</th></tr></thead>");
            out.println("<tbody>");

            // เพิ่มข้อมูลในตาราง
            for (String[] record : records) {
                String foodName = record[0];
                String foodType = record[1];
                String price = record[2];
                String imageName = record.length > 3 ? record[3] : ""; // ชื่อไฟล์รูปภาพ

                out.println("<tr>");
                out.println("<td>" + foodName + "</td>"); // ชื่ออาหาร
                out.println("<td>" + foodType + "</td>"); // ประเภท
                out.println("<td>" + price + "</td>"); // ราคา
                out.println("<td>");
                if (!imageName.isEmpty()) {
                    out.println("<img src='img/" + imageName + "' alt='รูปภาพอาหาร'>"); // แสดงรูปภาพ
                } else {
                    out.println("ไม่มีรูปภาพ");
                }
                out.println("</td>");
                out.println("<td>");
                out.println("<button class='btn-edit' onclick=\"window.location.href='edit?id=" + foodName + "'\">แก้ไข</button>");
                out.println("<button class='btn-delete' onclick=\"window.location.href='delete?id=" + foodName + "'\">ลบ</button>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
