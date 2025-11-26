package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfInvoiceGenerator {
    
    public static void exportTicketCGVPdf(
            String tenPhim,
            String gioChieu,
            String ngayChieu,
            String phongChieu,
            String listGhe,
            String ticketInfo
    ) throws IOException {
        String filePath = "ve.pdf";

        PDDocument doc = new PDDocument();
        float mm = 2.83465f;

        String[] gheArr = listGhe.split(",");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        for (String ghe : gheArr) {
            ghe = ghe.trim();

            PDRectangle ticketSize = new PDRectangle(75 * mm, 100 * mm);
            PDPage page = new PDPage(ticketSize);
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            InputStream fontPath = PdfInvoiceGenerator.class.getResourceAsStream("/view/fonts/DejaVuSans.ttf");
            InputStream fontBoldPath = PdfInvoiceGenerator.class.getResourceAsStream("/view/fonts/DejaVuSans-Bold.ttf");
            PDType0Font font = PDType0Font.load(doc, fontPath);
            PDType0Font fontBold = PDType0Font.load(doc, fontBoldPath);

            float y = ticketSize.getHeight() - 40;
            float left = 10;

            // ===== 210Cinema =====
            content.setNonStrokingColor(0, 0, 0);
            leftText(content, "210Cinema* Movie Ticket", fontBold, 6, left, y);
            y -= 15;

            // ===== Movie name =====
            leftText(content, tenPhim, fontBold, 9, left, y);
            y -= 30;

            // ===== Audi / Seats =====
            float colLeft = left;
            float colRight = ticketSize.getWidth() / 2 + 5;

            leftText(content, "Audi", font, 8, colLeft, y);
            leftText(content, "Seat", font, 8, colRight, y);
            y -= 20;

            leftText(content, phongChieu, fontBold, 13, colLeft, y);
            leftText(content, ghe, fontBold, 13, colRight, y);
            y -= 28;

            // ===== Suất chiếu =====
            String suat = "Date: " + ngayChieu + "   Time: " + gioChieu;

            float fontSize = 11;
            float textWidth = font.getStringWidth(suat) / 1000 * fontSize;
            float bgW = textWidth + 12;
            float bgH = 16;

            content.setNonStrokingColor(0, 0, 0);
            content.addRect(left - 2, y - 4, bgW, bgH);
            content.fill();

            content.setNonStrokingColor(255, 255, 255);
            leftText(content, suat, font, fontSize, left, y);
            content.setNonStrokingColor(0, 0, 0);


            // ===== Ticket Info =====
            y -= 20;
            leftText(content, ticketInfo, font, 7, left, y);
            
            // ===== Barcode random text =====
            y -= 20;
            String randomCode = "210CINEMA-" + (int) (Math.random() * 900000 + 100000);
            leftText(content, randomCode, font, 6, left, y);
            
            // ===== Barcode =====
            y -= 25;
            drawFakeBarcode(content, left, y, 130, 20);
            
            y -= 20;
            leftText(content, "210Cinema* King of all cinemas", fontBold, 6, left, y);
           
            y -= 10;
            leftText(content, "Address: 210 Binh Nguyen Vo Tan", font, 5, left, y);
            
            y -= 10;
            leftText(content, "(https://www.210cinema.com)", fontBold, 6, left, y);
            
            y -= 10;
            leftText(content, currentTime, font, 5, left, y);
            content.close();
        }

        doc.save(filePath);
        doc.close();

        openPdf(filePath);
    }
    
    public static void exportVATInvoice(
            List<String[]> items,
            String[] khachHangInfos,
            String[] thanhToanInfos
    ) throws Exception {
        String filePath = "hoadon.pdf";

        PDDocument doc = new PDDocument();
        float mm = 2.83465f;
        PDRectangle pageSize = new PDRectangle(230 * mm, 297 * mm);  // A4
        PDPage page = new PDPage(pageSize);
        doc.addPage(page);

        PDPageContentStream ct = new PDPageContentStream(doc, page);

        // Font
        InputStream fontPath = PdfInvoiceGenerator.class.getResourceAsStream("/view/fonts/DejaVuSans.ttf");
        InputStream fontBoldPath = PdfInvoiceGenerator.class.getResourceAsStream("/view/fonts/DejaVuSans-Bold.ttf");
        PDType0Font font = PDType0Font.load(doc, fontPath);
        PDType0Font fontBold = PDType0Font.load(doc, fontBoldPath);

        float left = 40;
        float y = pageSize.getHeight() - 50;

        // ===== Header =====
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("'Ngày' d 'tháng' M 'năm' yyyy");
        String formatted = now.toLocalDate().format(fmt);

        centerText(ct, "HÓA ĐƠN GIÁ TRỊ GIA TĂNG", fontBold, 16, pageSize.getWidth() / 2, y);
        y -= 20;
        centerText(ct, "(Bản thể hiện của hóa đơn điện tử)", font, 10, pageSize.getWidth() / 2, y);
        y -= 15;
        centerText(ct, formatted, font, 10, pageSize.getWidth() / 2, y);

        // QR Code
        y = pageSize.getHeight() - 140;
        BufferedImage qr = generateQR("https://www.youtube.com/watch?v=dQw4w9WgXcQ&list=RDdQw4w9WgXcQ&start_radio=1");
        PDImageXObject qrImage = LosslessFactory.createFromImage(doc, qr);

        y -= 20;
        float qrX = pageSize.getWidth() - 150;
        float qrY = y - 90;
        
        ct.drawImage(qrImage, qrX, qrY, 100, 100);
        
        // Thong tin Khach Hang
        y -= 10;
        leftText(ct, khachHangInfos[0], font, 11, left, y);
        y -= 20;
        leftText(ct, khachHangInfos[1], font, 11, left, y);
        y -= 20;
        leftText(ct, "Số điện thoại: " + khachHangInfos[2], font, 11, left, y);
        y -= 20;
        leftText(ct, khachHangInfos[3], font, 11, left, y);
        y -= 20;
        leftText(ct, "Hình thức thanh toán:  " + khachHangInfos[4], font, 11, left, y);

        float metaX = pageSize.getWidth() - 140;
        float metaY = pageSize.getHeight() - 50;

        leftText(ct, "Mẫu số: AMENIC/210", font, 10, metaX, metaY);
        leftText(ct, "Ký hiệu: RR/05E", font, 10, metaX, metaY - 15);
        leftText(ct, "Số: 0000123", font, 10, metaX, metaY - 30);
        
        // Bang hang hoa
        
        y -= 60;
        float tableLeft = left;
        float rowHeight = 25;

        float[] colWidths = {40, 200, 50, 50, 100, 120};

        drawTableHeader(ct, tableLeft, y, colWidths, rowHeight, fontBold);

        for (String[] row : items) {
            y -= rowHeight;
            drawTableRow(ct, tableLeft, y, colWidths, rowHeight, row, font);
        }

        // ===== Tổng tiền =====
        y -= 40;
        leftText(ct, "Tổng tiền: " + thanhToanInfos[0], font, 11, left, y);
        y -= 20;
        leftText(ct, thanhToanInfos[1], font, 11, left, y);
        y -= 20;
        leftText(ct, "Khách đưa:  " + thanhToanInfos[2], font, 11, left, y);
        y -= 20;
        leftText(ct, "Trả lại:  " + thanhToanInfos[3], font, 11, left, y);
        y -= 20;
        leftText(ct, "Đã thanh toán: " + thanhToanInfos[4], fontBold, 12, left, y);
        
        ct.close();
        doc.save(filePath);
        doc.close();
        
        openPdf(filePath);
    }


    private static void openPdf(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("File không tồn tại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void leftText(PDPageContentStream ct, String s, PDType0Font font, float size, float x, float y) throws IOException {
        ct.beginText();
        ct.setFont(font, size);
        ct.newLineAtOffset(x, y);
        ct.showText(s);
        ct.endText();
    }
    
    private static void centerText(PDPageContentStream ct, String s, PDType0Font font, float size, float centerX, float y) throws IOException {
        float width = font.getStringWidth(s) / 1000 * size;
        float x = centerX - width / 2;
        leftText(ct, s, font, size, x, y);
    }
    
    private static void drawFakeBarcode(PDPageContentStream ct, float x, float y, float width, float height) throws IOException {
        for (int i = 0; i < width; i += 3) {
            if ((i / 3) % 2 == 0) {
                ct.setNonStrokingColor(0, 0, 0);
            } else {
                ct.setNonStrokingColor(255, 255, 255);
            }
            ct.addRect(x + i, y, 3, height);
            ct.fill();
        }
        ct.setNonStrokingColor(0, 0, 0);
    }
    
    private static void drawTableHeader(PDPageContentStream ct, float x, float y, float[] widths, float h, PDType0Font font) throws IOException {
        float cursor = x;
        String[] headers = {"STT", "Tên hàng hóa, dịch vụ", "ĐVT", "SL", "Đơn giá", "Thành tiền"};

        for (int i = 0; i < widths.length; i++) {
            ct.addRect(cursor, y, widths[i], h);
            ct.stroke();
            leftText(ct, headers[i], font, 10, cursor + 3, y + 8);
            cursor += widths[i];
        }
    }

    private static void drawTableRow(PDPageContentStream ct, float x, float y, float[] widths, float h, String[] values, PDType0Font font) throws IOException {
        float cursor = x;

        for (int i = 0; i < widths.length; i++) {
            ct.addRect(cursor, y, widths[i], h);
            ct.stroke();
            leftText(ct, values[i], font, 10, cursor + 3, y + 8);
            cursor += widths[i];
        }
    }
    
    private static BufferedImage generateQR(String text) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300);
        return MatrixToImageWriter.toBufferedImage(m);
    }
    
    
}
