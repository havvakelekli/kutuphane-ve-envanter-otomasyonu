import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import { BookPlus, CheckCircle, AlertCircle } from 'lucide-react';

export default function KitapEkle() {
  const [isbn, setIsbn] = useState('');
  const [baslik, setBaslik] = useState('');
  const [yayinYili, setYayinYili] = useState(2024);
  const [toplamAdet, setToplamAdet] = useState(5);
  const [rafKonumu, setRafKonumu] = useState('A-101');
  const [mesaj, setMesaj] = useState('');
  const [hata, setHata] = useState('');
  const [yukleniyor, setYukleniyor] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setHata('');
    setMesaj('');
    setYukleniyor(true);

    try {
      await api.post('/kitaplar', {
        isbn,
        baslik,
        yayinYili: parseInt(yayinYili),
        toplamAdet: parseInt(toplamAdet),
        rafKonumu,
      });
      setMesaj('Kitap başarıyla envantere eklendi!');
      setTimeout(() => navigate('/katalog'), 1500);
    } catch (err) {
      setHata(err.response?.data?.mesaj || 'Kitap eklenirken hata oluştu.');
    } finally {
      setYukleniyor(false);
    }
  };

  return (
    <div style={{ padding: 32, flex: 1, overflowY: 'auto' }}>
      <div style={{ maxWidth: 600 }}>
        <h2 style={{ fontSize: 24, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 6 }}>Yeni Kitap Kaydı</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: 13, marginBottom: 24 }}>Kütüphane envanterine yeni kitap ve stok girişi yapın</p>

        {mesaj && (
          <div style={{ background: '#D1FAE5', border: '1px solid #34D399', color: '#065F46', padding: '12px 16px', borderRadius: 8, fontSize: 14, display: 'flex', alignItems: 'center', gap: 8, marginBottom: 20 }}>
            <CheckCircle size={18} />
            <span>{mesaj}</span>
          </div>
        )}

        {hata && (
          <div style={{ background: '#FEE2E2', border: '1px solid #F87171', color: '#991B1B', padding: '12px 16px', borderRadius: 8, fontSize: 14, display: 'flex', alignItems: 'center', gap: 8, marginBottom: 20 }}>
            <AlertCircle size={18} />
            <span>{hata}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="card" style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div>
            <label style={{ display: 'block', fontSize: 13, fontWeight: 600, marginBottom: 6 }}>ISBN / Barkod Numarası *</label>
            <input type="text" required placeholder="978-975-07-1938-7" className="input-field" value={isbn} onChange={(e) => setIsbn(e.target.value)} />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 13, fontWeight: 600, marginBottom: 6 }}>Kitap Başlığı *</label>
            <input type="text" required placeholder="İnce Memed 1" className="input-field" value={baslik} onChange={(e) => setBaslik(e.target.value)} />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 12 }}>
            <div>
              <label style={{ display: 'block', fontSize: 13, fontWeight: 600, marginBottom: 6 }}>Yayın Yılı</label>
              <input type="number" className="input-field" value={yayinYili} onChange={(e) => setYayinYili(e.target.value)} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: 13, fontWeight: 600, marginBottom: 6 }}>Toplam Adet *</label>
              <input type="number" min="1" required className="input-field" value={toplamAdet} onChange={(e) => setToplamAdet(e.target.value)} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: 13, fontWeight: 600, marginBottom: 6 }}>Raf Konumu</label>
              <input type="text" className="input-field" placeholder="A-101" value={rafKonumu} onChange={(e) => setRafKonumu(e.target.value)} />
            </div>
          </div>

          <button type="submit" disabled={yukleniyor} className="btn-gold" style={{ height: 46, marginTop: 8 }}>
            <BookPlus size={18} />
            <span>{yukleniyor ? 'Kaydediliyor...' : 'Envantere Kaydet'}</span>
          </button>
        </form>
      </div>
    </div>
  );
}