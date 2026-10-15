import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { Search, Book, CheckCircle, XCircle } from 'lucide-react';

export default function Katalog() {
  const [kitaplar, setKitaplar] = useState([]);
  const [aramaMetni, setAramaMetni] = useState('');
  const [yukleniyor, setYukleniyor] = useState(true);

  const kitaplariGetir = async (kelime = '') => {
    setYukleniyor(true);
    try {
      const url = kelime ? `/kitaplar/ara?kelime=${encodeURIComponent(kelime)}` : '/kitaplar';
      const res = await api.get(url);
      setKitaplar(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setYukleniyor(false);
    }
  };

  useEffect(() => {
    kitaplariGetir();
  }, []);

  const handleArama = (e) => {
    e.preventDefault();
    kitaplariGetir(aramaMetni);
  };

  return (
    <div style={{ padding: 32, flex: 1, overflowY: 'auto' }}>
      {/* Üst Başlık & Arama Çubuğu */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 28, flexWrap: 'wrap', gap: 16 }}>
        <div>
          <h2 style={{ fontSize: 24, fontWeight: 700, color: 'var(--primary-navy)' }}>Kitap Kataloğu</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, marginTop: 4 }}>Kütüphanedeki tüm kitapları arayın ve inceleyin</p>
        </div>

        <form onSubmit={handleArama} style={{ display: 'flex', gap: 8, width: 340 }}>
          <div style={{ position: 'relative', flex: 1 }}>
            <Search size={18} style={{ position: 'absolute', left: 12, top: 12, color: 'var(--text-muted)' }} />
            <input
              type="text"
              placeholder="Kitap veya yazar adı ara..."
              className="input-field"
              style={{ paddingLeft: 38, height: 42 }}
              value={aramaMetni}
              onChange={(e) => setAramaMetni(e.target.value)}
            />
          </div>
          <button type="submit" className="btn-navy" style={{ height: 42 }}>Ara</button>
        </form>
      </div>

      {/* Kitap Kartları Izgarası */}
      {yukleniyor ? (
        <p style={{ color: 'var(--text-muted)' }}>Kitaplar yükleniyor...</p>
      ) : kitaplar.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: 48 }}>
          <Book size={48} style={{ color: 'var(--text-muted)', margin: '0 auto 12px' }} />
          <h3 style={{ fontSize: 16, color: 'var(--text-dark)' }}>Kayıtlı Kitap Bulunamadı</h3>
          <p style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>Yeni kitap eklemek için Kütüphaneci panelini kullanabilirsiniz.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 20 }}>
          {kitaplar.map((k) => (
            <div key={k.id} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', transition: 'transform 0.2s' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
                  <span style={{ fontSize: 11, fontWeight: 700, padding: '4px 8px', borderRadius: 6, backgroundColor: '#F1F5F9', color: '#475569' }}>
                    {k.kategoriAdi}
                  </span>
                  <span style={{ fontSize: 11, fontWeight: 600, color: 'var(--text-muted)' }}>
                    Raf: {k.rafKonumu || '-'}
                  </span>
                </div>

                <h3 style={{ fontSize: 16, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 6 }}>{k.baslik}</h3>
                <p style={{ fontSize: 13, color: 'var(--text-muted)', marginBottom: 4 }}>Yazar: <strong style={{ color: 'var(--text-dark)' }}>{k.yazarAdi}</strong></p>
                <p style={{ fontSize: 12, color: 'var(--text-muted)', marginBottom: 12 }}>ISBN: {k.isbn}</p>
              </div>

              <div style={{ paddingTop: 14, borderTop: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 13, fontWeight: 600, color: k.mevcutAdet > 0 ? 'var(--success)' : 'var(--danger)' }}>
                  {k.mevcutAdet > 0 ? <CheckCircle size={16} /> : <XCircle size={16} />}
                  <span>{k.mevcutAdet > 0 ? `${k.mevcutAdet} Adet Mevcut` : 'Stok Tükendi'}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}