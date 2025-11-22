package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.awt.Desktop;
import java.io.File;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PdfInvoiceGenerator {

    public static void exportMovieTicketPdf(
            String filePath,
            String tenPhim,
            String ngayChieu,
            String gioChieu,
            String thoiLuong,
            String rapPhim,
            String phongChieu,
            List<String> gheList,
            List<String> sanPhamList,
            double tienVe,
            double tienSanPham,
            double tienGiam,
            double tongTien
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
        text(content, "Ngày chiếu: " + ngayChieu, left, y);     y -= 13;
        text(content, "Giờ chiếu: " + gioChieu, left, y);       y -= 13;
        text(content, "Thời lượng: " + thoiLuong, left, y);     y -= 18;

//        y -= 10;
        text(content, "Rạp: " + rapPhim, left, y);               y -= 13;
        text(content, "Phòng chiếu: " + phongChieu, left, y);    y -= 18;

        String gheString = String.join(", ", gheList);
        text(content, "Ghế: " + gheString, left, y);             y -= 20;

        text(content, "Sản phẩm:", left, y);           y -= 13;

        if (sanPhamList.isEmpty()) {
            text(content, "- Không có", left + 12, y);
            y -= 15;
        } else {
            for (String sp : sanPhamList) {
                text(content, "- " + sp, left + 12, y);
                y -= 15;
            }
        }

        y -= 10;

        content.setFont(fontBold, 9);
        text(content, "TIỀN THANH TOÁN", left, y); y -= 18;

        content.setFont(font, 7);
        text(content, "Tiền vé: " + tienVe + " đ", left, y);             y -= 13;
        text(content, "Tiền sản phẩm: " + tienSanPham + " đ", left, y);  y -= 13;
        text(content, "Giảm giá: -" + tienGiam + " đ", left, y);         y -= 18;

        content.setFont(fontBold, 9);
        text(content, "TỔNG TIỀN: " + tongTien + " đ", left, y);

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
    
    public static void main(String[] args) {
        try {
            exportMovieTicketPdf(
                    "hoadon.pdf",
                    "Avengers: Endgame",
                    "26/04/2019",
                    "19:30 - 22:32",
                    "3 giờ 2 phút",
                    "CGV Vincom Gò Vấp",
                    "Cinema 3",
                    List.of("C4", "C3"),
                    List.of("Bắp lớn", "Nước siêu lớn"),
                    180000,
                    90000,
                    0,
                    270000
            );
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
 
}
