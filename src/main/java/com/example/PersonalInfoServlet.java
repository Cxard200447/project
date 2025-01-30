package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Paths;

@WebServlet("/save-info")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,   // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class PersonalInfoServlet extends HttpServlet {
    private static final String FILE_PATH = "D:/Tomcat_java/java myweb/kk/data/food-info.txt";
    private static final String IMAGE_PATH = "D:/Tomcat_java/java myweb/kk/data/img/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // รับข้อมูลจากฟอร์ม
        String foodName = request.getParameter("foodName");
        String foodType = request.getParameter("foodType");
        String price = request.getParameter("price");
        Part foodImagePart = request.getPart("foodImage");

        // ตรวจสอบว่าข้อมูลไม่ว่างเปล่า
        if (foodName == null || foodType == null || price == null || foodImagePart == null ||
                foodName.isEmpty() || foodType.isEmpty() || price.isEmpty() || foodImagePart.getSize() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "กรุณากรอกข้อมูลให้ครบถ้วนและเลือกรูปภาพ");
            return;
        }

        // จัดการไฟล์รูปภาพ
        String fileName = Paths.get(foodImagePart.getSubmittedFileName()).getFileName().toString(); // ชื่อไฟล์
        String imagePath = IMAGE_PATH + fileName;

        // ตรวจสอบโฟลเดอร์เก็บรูปภาพ
        File imageDir = new File(IMAGE_PATH);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        // บันทึกรูปภาพลงโฟลเดอร์
        try (InputStream inputStream = foodImagePart.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(imagePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "เกิดข้อผิดพลาดในการบันทึกรูปภาพ");
            e.printStackTrace();
            return;
        }

        // เขียนข้อมูลอาหารลงไฟล์
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(foodName + "," + foodType + "," + price + "," + fileName); // เพิ่มชื่อไฟล์รูปภาพในข้อมูล
            writer.newLine();
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "เกิดข้อผิดพลาดในการบันทึกข้อมูล");
            e.printStackTrace();
            return;
        }

        // แสดงผลตอบกลับ
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<style>");
            out.println("body {");
            out.println("    font-family: Arial, sans-serif;");
            out.println("    background-color: #121212;");
            out.println("    color: white;");
            out.println("    text-align: center;");
            out.println("    padding: 50px;");
            out.println("}");
            out.println("h1 {");
            out.println("    font-size: 28px;");
            out.println("    margin-bottom: 20px;");
            out.println("}");
            out.println("a {");
            out.println("    color: #FFD700;");
            out.println("    font-size: 18px;");
            out.println("    text-decoration: none;");
            out.println("    padding: 10px 20px;");
            out.println("    border: 2px solid #FFD700;");
            out.println("    border-radius: 5px;");
            out.println("    transition: background-color 0.3s, color 0.3s;");
            out.println("}");
            out.println("a:hover {");
            out.println("    background-color: #FFD700;");
            out.println("    color: #121212;");
            out.println("}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>ข้อมูลอาหารถูกบันทึกเรียบร้อยแล้ว</h1>");
            out.println("<a href=\"home.html\">กลับไปหน้าเพิ่มข้อมูล</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
