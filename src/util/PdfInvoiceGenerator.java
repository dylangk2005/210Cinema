package util;

import java.awt.Component;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Desktop;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PdfInvoiceGenerator {

    public static void exportMovieTicketPdf(
            String filePath,
            String tenPhim,
            String gioChieu,
            String phongChieu,
            String gheList,
            JPanel listPanelSanPham,
            String tienVe,
            String tienSanPham,
            String tongTien, 
            String tienGiam,
            String tienSauGiamGia,
            String khachDua,
            String traLai
    ) throws IOException {

        PDDocument doc = new PDDocument();
        float mm = 2.83465f;
        PDRectangle ticketSize = new PDRectangle(80 * mm, 160 * mm); 
        PDPage page = new PDPage(ticketSize);
        doc.addPage(page);

        PDPageContentStream content = new PDPageContentStream(doc, page);
        
        InputStream fontPath = PdfInvoiceGenerator.class.getResourceAsStream("/view/fonts/DejaVuSans.ttf");     
        InputStream fontBoldPath = PdfInvoiceGenerator.class.getResourceAsStream("/view/fonts/DejaVuSans-Bold.ttf");     
        PDType0Font font = PDType0Font.load(doc, fontPath);
        PDType0Font fontBold = PDType0Font.load(doc, fontBoldPath);
        
        float y = ticketSize.getHeight() - 30;
        float left = 10;

        String title = "VÉ XEM PHIM";
        float fsTitle = 16;
        float titleW = fontBold.getStringWidth(title) / 1000 * fsTitle;
        float centerX = (ticketSize.getWidth() - titleW) / 2;

        content.beginText();
        content.setFont(fontBold, fsTitle);
        content.newLineAtOffset(centerX, y);
        content.showText(title);
        content.endText();

        y -= 25;
        
        content.setFont(fontBold, 9);
        text(content, "Phim: " + tenPhim, left, y);    y -= 18;

        content.setFont(font, 7);
        text(content, "Suất chiếu: " + gioChieu, left, y);       y -= 13;

//        y -= 10;
        text(content, "Rạp chiếu: 210Cinema", left, y);               y -= 13;
        text(content, "Phòng chiếu: " + phongChieu, left, y);    y -= 18;

        text(content, "Ghế: " + gheList, left, y);             y -= 20;

        text(content, "Sản phẩm:", left, y);           y -= 13;

        Boolean check = false;
        for (Component c : listPanelSanPham.getComponents()) {
            if (c instanceof JPanel productPanel) {

                JLabel lb = (JLabel) productPanel.getComponent(0);      
                JTextField tf = (JTextField) productPanel.getComponent(2);

                int soLuong = Integer.parseInt(tf.getText().trim());
                if (soLuong > 0) {
                    text(content, "- " + lb.getText() + " x" + soLuong, left + 12, y); y -= 15;
                }
                check = true;
            }
        }
        if (!check) {
            text(content, "- Không có", left + 12, y); y -= 15;
        }

        y -= 10;

        content.setFont(fontBold, 9);
        text(content, "TIỀN THANH TOÁN", left, y); y -= 18;

        content.setFont(font, 7);
        text(content, "Tiền vé: " + tienVe, left, y);             y -= 13;
        text(content, "Tiền sản phẩm: " + tienSanPham, left, y);  y -= 13;
        text(content, "Tổng tiền: " + tongTien, left, y);  y -= 13;
        text(content, "Giảm giá: -" + tienGiam, left, y);         y -= 13;
        text(content, "Tiền phải trả: " + tienSauGiamGia, left, y); y -= 13;
        text(content, "Khách đưa: " + khachDua, left, y);   y -= 13;
        text(content, "Trả lại: " + traLai, left, y);

        content.close();
        doc.save(filePath);
        doc.close();
        
        openPdf(filePath); 
    }

    private static void text(PDPageContentStream ct, String s, float x, float y) throws IOException {
        ct.beginText();
        ct.newLineAtOffset(x, y);
        ct.showText(s);
        ct.endText();
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
 
}
