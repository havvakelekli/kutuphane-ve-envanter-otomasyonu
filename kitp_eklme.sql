-- 1. Kategoriler
INSERT INTO kategoriler (ad, aciklama) VALUES
('Dünya Klasikleri', 'Başyapıtlar'),
('Bilim Kurgu', 'Felsefe ve gelecek'),
('Tarih', 'Milli mücadele')
ON CONFLICT (ad) DO NOTHING;

-- 2. Yazarlar
INSERT INTO yazarlar (ad_soyad, biyografi) VALUES
('Yaşar Kemal', 'Türk edebiyatı ustası'),
('George Orwell', 'İngiliz yazar'),
('Mustafa Kemal Atatürk', 'Gazi Mustafa Kemal')
ON CONFLICT DO NOTHING;

-- 3. Yayınevleri
INSERT INTO yayinevleri (ad, adres) VALUES
('Yapı Kredi Yayınları', 'İstanbul'),
('Can Yayınları', 'İstanbul')
ON CONFLICT (ad) DO NOTHING;

-- 4. Kitaplar
INSERT INTO kitaplar (isbn, baslik, kategori_id, yazar_id, yayinevi_id, yayin_yili, toplam_adet, mevcut_adet, raf_konumu)
VALUES
('9789750719387', 'İnce Memed 1', (SELECT id FROM kategoriler WHERE ad='Dünya Klasikleri' LIMIT 1), (SELECT id FROM yazarlar WHERE ad_soyad='Yaşar Kemal' LIMIT 1), (SELECT id FROM yayinevleri WHERE ad='Yapı Kredi Yayınları' LIMIT 1), 2020, 8, 8, 'A-101'),
('9789750718533', '1984', (SELECT id FROM kategoriler WHERE ad='Bilim Kurgu' LIMIT 1), (SELECT id FROM yazarlar WHERE ad_soyad='George Orwell' LIMIT 1), (SELECT id FROM yayinevleri WHERE ad='Can Yayınları' LIMIT 1), 2021, 10, 10, 'B-201'),
('9789751600000', 'Nutuk', (SELECT id FROM kategoriler WHERE ad='Tarih' LIMIT 1), (SELECT id FROM yazarlar WHERE ad_soyad='Mustafa Kemal Atatürk' LIMIT 1), (SELECT id FROM yayinevleri WHERE ad='Yapı Kredi Yayınları' LIMIT 1), 2023, 12, 12, 'T-301')
ON CONFLICT (isbn) DO NOTHING;