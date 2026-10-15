# 📚 Gelişmiş Kütüphane ve Envanter Otomasyonu

Spring Boot 3.x ve React v18 (Vite) ile geliştirilmiş, kurumsal mimari standartlarına sahip tam kapsamlı kütüphane ve envanter yönetim sistemi.

---

## 🛠️ Teknoloji Yığını (Tech Stack)

- **Backend:** Java 21, Spring Boot 3.x, Spring Data JPA, Spring Security 6
- **Veritabanı:** PostgreSQL (128-bit UUID Primary Key mimarisi)
- **Güvenlik:** Durumsuz (Stateless) JWT Bearer Token, RBAC (Rol Bazlı Yetkilendirme), BCrypt
- **İş Mantığı:** `@Transactional` veri bütünlüğü, `@Scheduled` Gece Cron Job (Otomatik Ceza İşletimi)
- **Raporlama:** iTextPDF ile Türkçe karakter destekli resmi Ödünç Alma Fişi üretimi
- **Dokümantasyon:** Swagger UI / OpenAPI 3.1 (`/swagger-ui/index.html`)
- **Frontend:** React 18, Vite, Axios Interceptors, React Router v6, Lucide Icons

---

## 🏛️ Mimari Özellikler

1. **ID Enumeration Koruması:** Sıralı ID (1, 2, 3...) yerine 128-bit `UUID` kullanılarak URL tahmin saldırıları engellenmiştir.
2. **Merkezi Hata Yönetimi:** `@RestControllerAdvice` ile tüm sistem hataları kullanıcıya temiz ve standart JSON nesnesi olarak döner.
3. **Rol Bazlı Menü (RBAC):** Admin, Kütüphaneci ve Üye rolleri için dinamik React ekranları.
4. **Axios Interceptor:** JWT Token tüm HTTP isteklerinin Header'ına otomatik eklenir; oturum süresi dolduğunda otomatik login sayfasına yönlendirir.
5. **Otomatik Gece Cron Job:** Her gece 00:00'da geciken kitaplar otomatik tespit edilir ve günlük gecikme cezası yansıtılır.
6. **Resmi PDF Fişi:** Her ödünç alma işlemi için iText ile anlık indirilip basılabilen resmi kütüphane fişi üretilir.

---

## 🚀 Kurulum ve Çalıştırma

### 1. Backend:
```bash
cd library_backend
mvn clean install
mvn spring-boot:run
```
- **Port:** `http://localhost:8081`
- **Swagger UI:** `http://localhost:8081/swagger-ui/index.html`

### 2. Frontend:
```bash
cd kutuphane-frontend
npm install
npm run dev
```
- **Port:** `http://localhost:5173`
