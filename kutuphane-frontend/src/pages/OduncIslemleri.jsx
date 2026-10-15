import React, { useState } from 'react';
import api from '../services/api';
import { Repeat, FileDown, CheckCircle, AlertCircle } from 'lucide-react';

export default function OduncIslemleri() {
  const [kullaniciId, setKullaniciId] = useState('');
  const [kitapId, setKitapId] = useState('');
  const [gunSayisi, setGunSayisi] = useState(15);

  const [iadeKayitId, setIadeKayitId] = useState('');

  const [sonOdunc, setSonOdunc] = useState(null);
  const [mesaj, setMesaj] = useState('');
  const [hata, setHata] = useState('');

  const handleOduncVer = async (e) => {
    e.preventDefault();
    setHata('');
    setMesaj('');
    try {
      const res = await api.post('/odunc/al', {
        kullaniciId,
        kitapId,
        gunSayisi: parseInt(gunSayisi),
      });
      setSonOdunc(res.data);
      setMesaj('Kitap başarıyla ödünç verildi! Fişi aşağıdan indirebilirsiniz.');
    } catch (err) {
      setHata(err.response?.data?.mesaj || 'Ödünç verme işlemi başarısız.');
    }
  };

  const handleIadeAl = async (e) => {
    e.preventDefault();
    setHata('');
    setMesaj('');
    try {
      await api.post(`/odunc/iade/${iadeKayitId}`);
      setMesaj('Kitap başarıyla iade alındı ve stok +1 artırıldı.');
      setIadeKayitId('');
    } catch (err) {
      setHata(err.response?.data?.mesaj || 'İade işlemi başarısız.');
    }
  };

  const pdfIndir = async (id) => {
    try {
      const response = await api.get(`/odunc/fis/${id}`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `odunc_fisi_${id}.pdf`);
      document.body.appendChild(link);
      link.click();
    } catch (err) {
      alert('PDF indirilirken hata oluştu.');
    }
  };

  return (
    <div style={{ padding: 32, flex: 1, overflowY: 'auto' }}>
      <h2 style={{ fontSize: 24, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 6 }}>Ödünç & İade Masası</h2>
      <p style={{ color: 'var(--text-muted)', fontSize: 13, marginBottom: 24 }}>Kütüphaneci bankosu ödünç verme ve iade teslim alma ekranı</p>

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

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        {/* Ödünç Verme Formu */}
        <div className="card">
          <h3 style={{ fontSize: 16, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 16 }}>📖 Kitap Ödünç Ver</h3>
          <form onSubmit={handleOduncVer} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 600, marginBottom: 4 }}>Kullanıcı UUID ID</label>
              <input type="text" required placeholder="Örn: b171da5c-794c-4440-bc8c-..." className="input-field" value={kullaniciId} onChange={(e) => setKullaniciId(e.target.value)} />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 600, marginBottom: 4 }}>Kitap UUID ID</label>
              <input type="text" required placeholder="Örn: 9f8a12bc-3341-..." className="input-field" value={kitapId} onChange={(e) => setKitapId(e.target.value)} />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 600, marginBottom: 4 }}>Süre (Gün)</label>
              <input type="number" min="1" max="30" className="input-field" value={gunSayisi} onChange={(e) => setGunSayisi(e.target.value)} />
            </div>

            <button type="submit" className="btn-gold" style={{ height: 44, marginTop: 6 }}>
              Ödünç Kaydı Aç
            </button>
          </form>

          {sonOdunc && (
            <div style={{ marginTop: 18, padding: 14, background: '#F8FAFC', borderRadius: 8, border: '1px solid var(--border-color)' }}>
              <p style={{ fontSize: 13, fontWeight: 600, color: 'var(--primary-navy)' }}>Son İşlem: {sonOdunc.kitapBaslik}</p>
              <p style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 2 }}>Teslim Tarihi: {new Date(sonOdunc.sonTeslimTarihi).toLocaleDateString('tr-TR')}</p>
              <button onClick={() => pdfIndir(sonOdunc.id)} className="btn-navy" style={{ marginTop: 10, padding: '8px 14px', fontSize: 12 }}>
                <FileDown size={15} />
                <span>Resmi PDF Fişini İndir</span>
              </button>
            </div>
          )}
        </div>

        {/* İade Alma Formu */}
        <div className="card">
          <h3 style={{ fontSize: 16, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 16 }}>📥 Kitap İade Al</h3>
          <form onSubmit={handleIadeAl} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
            <div>
              <label style={{ display: 'block', fontSize: 12, fontWeight: 600, marginBottom: 4 }}>Ödünç Kayıt UUID ID</label>
              <input type="text" required placeholder="Ödünç fişindeki veya listedeki ID" className="input-field" value={iadeKayitId} onChange={(e) => setIadeKayitId(e.target.value)} />
            </div>

            <button type="submit" className="btn-navy" style={{ height: 44, marginTop: 6 }}>
              <Repeat size={18} />
              <span>İadeyi Onayla & Stoğa Ekle</span>
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}