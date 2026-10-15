CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS kullanicilar(

id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
ad_soyad VARCHAR(100) NOT NULL,
eposta VARCHAR(100) UNIQUE NOT NULL,
sifre VARCHAR(255) NOT NULL,
telefon_no VARCHAR(20),
rol VARCHAR(30) NOT NULL DEFAULT 'ROL_UYE',
aktif_mi BOOLEAN DEFAULT TRUE,
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
guncellenme_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP

);


CREATE TABLE IF NOT EXISTS kategoriler(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
ad VARCHAR(100) UNIQUE NOT NULL,
acıklama TEXT,
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE IF NOT EXISTS yazarlar(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
ad_soyad VARCHAR(150) NOT NULL,
biyografi TEXT,
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS yayinevleri(

id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
ad VARCHAR(150) UNIQUE NOT NULL,
adres TEXT,
iletisim_telefonu VARCHAR(20),
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


-- 6. KİTAPLAR TABLOSU
CREATE TABLE IF NOT EXISTS kitaplar (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    isbn VARCHAR(20) UNIQUE NOT NULL,               -- Barkod / ISBN numarası (benzersiz)
    baslik VARCHAR(200) NOT NULL,                    -- Kitap adı
    kategori_id UUID REFERENCES kategoriler(id) ON DELETE SET NULL,
    yazar_id UUID REFERENCES yazarlar(id) ON DELETE SET NULL,
    yayinevi_id UUID REFERENCES yayinevleri(id) ON DELETE SET NULL,
    yayin_yili INT,
    toplam_adet INT NOT NULL DEFAULT 1,              -- Kütüphanedeki toplam fiziksel kopya sayısı
    mevcut_adet INT NOT NULL DEFAULT 1,              -- Şu an rafta ödünç verilebilir kopya sayısı
    raf_konumu VARCHAR(50),                          -- Örn: 'A-12', 'Salon 2 / Raf 3'
    olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    guncellenme_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_stok CHECK (mevcut_adet >= 0 AND mevcut_adet <= toplam_adet)
);


CREATE TABLE IF NOT EXISTS odunc_kayitlari(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
kullanici_id UUID NOT NULL REFERENCES kullanicilar(id) ON DELETE CASCADE,
kitap_id UUID NOT NULL REFERENCES kitaplar(id) ON DELETE CASCADE,
odunc_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
son_teslim_tarihi TIMESTAMP WITH TIME ZONE NOT NULL,
iade_tarihi TIMESTAMP WITH TIME ZONE,
durum VARCHAR(30) NOT NULL DEFAULT 'ODUNC_ALINDI',
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS rezervasyonlar(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
kullanici_id UUID NOT NULL REFERENCES kullanicilar(id) ON DELETE CASCADE,
kitap_id UUID NOT NULL REFERENCES kitaplar(id) ON DELETE CASCADE,
rezervasyon_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
sira_no INT NOT NULL DEFAULT 1,
durum VARCHAR(30) NOT NULL DEFAULT 'BEKLEMEDE',
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS ceza_odemeleri(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
odunc_kayit_id UUID REFERENCES odunc_kayitlari(id) ON DELETE CASCADE,
kullanici_id UUID NOT NULL REFERENCES kullanicilar(id) ON DELETE CASCADE,
tutar NUMERIC(10,2) NOT NULL DEFAULT 0.00,
odendi_mi BOOLEAN DEFAULT FALSE,
odeme_tarihi TIMESTAMP WITH TIME ZONE,
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS envanter_loglari(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
kitap_id UUID NOT NULL REFERENCES kitaplar(id) ON DELETE CASCADE,
islem_turu VARCHAR(50) NOT NULL,
adet_degisimi INT NOT NULL,
acıklama TEXT,
olusturulma_tarihi TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);



