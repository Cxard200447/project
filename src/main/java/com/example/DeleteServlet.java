package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    private static final String FILE_PATH = "D:/Tomcat_java/java myweb/kk/data/food-info.txt";
    private static final String IMAGE_DIRECTORY = "D:/Tomcat_java/java myweb/kk/data/img/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        response.setContentType("text/html;charset=UTF-8");

        String[] recordToDelete = null;

        // ค้นหาข้อมูลที่ต้องการลบ
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record[0].equals(id)) {
                    recordToDelete = record;
                    break;
                }
            }
        }

        // แสดงข้อมูลก่อนลบ
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>ยืนยันการลบข้อมูล</title>");
            out.println("<style>");
            out.println("body { font-family: 'Roboto', Arial, sans-serif; margin: 0; padding: 0; background-color: #20232a; color: #ffffff; line-height: 1.6; }");
            out.println("h1 { text-align: center; color: #61dafb; margin-top: 20px; }");
            out.println("p { text-align: center; font-size: 18px; }");
            out.println("form { text-align: center; margin-top: 20px; }");
            out.println("button { background-color: #61dafb; color: #20232a; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; }");
            out.println("button:hover { background-color: #4faadf; }");
            out.println("a { color: #61dafb; text-decoration: none; display: block; text-align: center; margin-top: 20px; font-size: 18px; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            if (recordToDelete == null) {
                out.println("<h1>ไม่พบข้อมูลที่ต้องการลบ</h1>");
                out.println("<a href='search.html'>กลับไปหน้าค้นหา</a>");
            } else {
                out.println("<h1>คุณต้องการลบข้อมูลนี้หรือไม่?</h1>");
                out.println("<form action='delete' method='post'>");
                out.println("<input type='hidden' name='name' value='" + recordToDelete[0] + "'>");
                out.println("<input type='hidden' name='image' value='" + (recordToDelete.length > 3 ? recordToDelete[3] : "") + "'>");
                out.println("<p>ชื่อ: " + recordToDelete[0] + "</p>");
                out.println("<p>อีเมล: " + recordToDelete[1] + "</p>");
                out.println("<p>เบอร์โทร: " + recordToDelete[2] + "</p>");
                out.println("<button type='submit'>ยืนยันการลบ</button>");
                out.println("</form>");
                out.println("<a href='search.html'>ยกเลิกและกลับไปหน้าค้นหา</a>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nameToDelete = request.getParameter("name");
        String imageToDelete = request.getParameter("image");

        List<String[]> records = new ArrayList<>();

        // อ่านไฟล์และลบข้อมูลที่ตรงกับชื่อ
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (!record[0].equals(nameToDelete)) {
                    records.add(record);  // เก็บเฉพาะข้อมูลที่ไม่ได้ถูกลบ
                }
            }
        }

        // ลบไฟล์รูปภาพหากมี
        if (imageToDelete != null && !imageToDelete.isEmpty()) {
            File imageFile = new File(IMAGE_DIRECTORY + imageToDelete);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }

        // เขียนข้อมูลที่เหลือกลับไปที่ไฟล์
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        }

        // เปลี่ยนเส้นทางกลับไปที่หน้าค้นหา
        response.sendRedirect("home.html");
    }
}
