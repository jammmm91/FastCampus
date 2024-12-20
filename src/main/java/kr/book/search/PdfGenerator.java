package kr.book.search;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.*;
import java.util.*;
import java.net.MalformedURLException;

public class PdfGenerator {
    public static void generateBookListPdf(List<Book> books, String fileName) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setFontSize(12);
        // 폰트 생성 및 추가
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("Files/malgunsl.ttf", PdfEncodings.IDENTITY_H, true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        document.setFont(font);
        // 타이틀 추가
        Paragraph titleParagraph = new Paragraph("도서 목록");
        titleParagraph.setFontSize(24);
        titleParagraph.setTextAlignment(TextAlignment.CENTER);
        titleParagraph.setBold();
        document.add(titleParagraph);

        // 도서 정보 테이블 생성
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginTop(20);

        // 테이블 헤더 추가
        table.addHeaderCell(createCell("제목", true));
        table.addHeaderCell(createCell("저자", true));
        table.addHeaderCell(createCell("출판사", true));
        table.addHeaderCell(createCell("도서정가", true));
        table.addHeaderCell(createCell("도서판매가", true));
        table.addHeaderCell(createCell("이미지", true));

        // 도서 정보를 테이블에 추가
        for (Book book : books) {
            table.addCell(createCell(book.getTitle(), false));
            table.addCell(createCell(book.getAuthor(), false));
            table.addCell(createCell(book.getPublisher(), false));
            table.addCell(createCell(String.valueOf(book.getPrice()), false));
            table.addCell(createCell(String.valueOf(book.getSale_price()), false));

            // 이미지 추가
            try {
                String thumbnail = book.getThumbnail();
//                System.out.println("thumbnail = " + thumbnail);
                if (thumbnail == null || thumbnail.isEmpty()) {
                    table.addCell(createCell("이미지없음", false));
                } else {
                    File file = new File(thumbnail);
                    ImageData imageData = ImageDataFactory.create(thumbnail);
                    Image image = new Image(imageData);
                    image.setAutoScale(true);
                    table.addCell(new Cell().add(image).setPadding(5));
                }
            } catch (MalformedURLException e) {
                table.addCell(createCell("URL 형식이 잘못되었습니다", false));
            } catch (IOException e) {
                table.addCell(createCell("이미지 불러오기 실패", false));
            }
        }
        document.add(table);
        document.close();
    }

    private static Cell createCell(String content, boolean isHeader) {
        Paragraph paragraph = new Paragraph(content);
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            cell.setFontSize(14);
            cell.setBold();
        }
        return cell;
    }
}
