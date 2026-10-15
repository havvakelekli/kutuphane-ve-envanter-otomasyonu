package com.library.library_backend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.library.library_backend.entity.OduncKaydi;
import com.library.library_backend.exception.KaynakBulunamadiException;
import com.library.library_backend.repository.OduncKaydiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final OduncKaydiRepository oduncKaydiRepository;

    public ByteArrayInputStream oduncFisiUret(UUID oduncKayitId) {
        OduncKaydi kayit = oduncKaydiRepository.findById(oduncKayitId)
                .orElseThrow(() -> new KaynakBulunamadiException("Ödünç kaydı bulunamadı: " + oduncKayitId));

        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Türkçe karakter desteği için font ayarı
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, "CP1254", BaseFont.NOT_EMBEDDED);
            Font baslikFont = new Font(bf, 16, Font.BOLD, new BaseColor(15, 32, 67)); // Koyu Lacivert #0F2043
            Font altBaslikFont = new Font(bf, 11, Font.BOLD, BaseColor.DARK_GRAY);
            Font normalFont = new Font(bf, 10, Font.NORMAL, BaseColor.BLACK);

            PdfWriter.getInstance(document, out);
            document.open();

            // Başlık
            Paragraph baslik = new Paragraph("KÜTÜPHANE VE ENVANTER OTOMASYONU", baslikFont);
            baslik.setAlignment(Element.ALIGN_CENTER);
            document.add(baslik);

            Paragraph fisBasligi = new Paragraph("RESMİ KİTAP ÖDÜNÇ ALMA FİŞİ\n\n", altBaslikFont);
            fisBasligi.setAlignment(Element.ALIGN_CENTER);
            document.add(fisBasligi);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            // Bilgi Tablosu
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            tabloSatiriEkle(table, "Fiş No (ID):", kayit.getId().toString(), normalFont);
            tabloSatiriEkle(table, "Üye Adı Soyadı:", kayit.getKullanici().getAdSoyad(), normalFont);
            tabloSatiriEkle(table, "Üye E-Posta:", kayit.getKullanici().getEposta(), normalFont);
            tabloSatiriEkle(table, "Kitap Adı:", kayit.getKitap().getBaslik(), normalFont);
            tabloSatiriEkle(table, "ISBN No:", kayit.getKitap().getIsbn(), normalFont);
            tabloSatiriEkle(table, "Ödünç Alma Tarihi:", kayit.getOduncTarihi().format(formatter), normalFont);
            tabloSatiriEkle(table, "Son Teslim Tarihi:", kayit.getSonTeslimTarihi().format(formatter), normalFont);

            document.add(table);

            // Alt Bilgilendirme Notu
            Paragraph not = new Paragraph("\n* Önemli Not: Kitabın son teslim tarihinden sonra iade edilmesi durumunda günlük 10.00 TL gecikme cezası tahakkuk ettirilecektir.", new Font(bf, 8, Font.ITALIC, BaseColor.RED));
            document.add(not);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("PDF fiş oluşturulurken hata meydana geldi: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void tabloSatiriEkle(PdfPTable table, String etiket, String deger, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(etiket, new Font(font.getBaseFont(), 10, Font.BOLD)));
        cell1.setBackgroundColor(new BaseColor(245, 247, 250));
        cell1.setPadding(6);

        PdfPCell cell2 = new PdfPCell(new Phrase(deger, font));
        cell2.setPadding(6);

        table.addCell(cell1);
        table.addCell(cell2);
    }
}