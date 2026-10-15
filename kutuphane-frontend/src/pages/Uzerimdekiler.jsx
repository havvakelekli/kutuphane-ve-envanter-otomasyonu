import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Clock, AlertTriangle, CheckCircle } from 'lucide-react';

export default function Uzerimdekiler() {
  const { user } = useAuth();
  const [oduncler, setOduncler] = useState([]);
  const [cezalar, setCezalar] = useState([]);
  const [yukleniyor, setYukleniyor] = useState(true);

  const verileriGetir = async () => {
    if (!user?.id) return;
    setYukleniyor(true);
    try {
      const [oduncRes, cezaRes] = await Promise.all([
        api.get(`/odunc/kullanici/${user.id}`),
        api.get(`/ceza/kullanici/${user.id}`),
      ]);
      setOduncler(oduncRes.data);
      setCezalar(cezaRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setYukleniyor(false);
    }
  };

  useEffect(() => {
    verileriGetir();
  }, [user]);

  const cezaOde = async (cezaId) => {
    try {
      await api.post(`/ceza/ode/${cezaId}`);
      alert('Ceza borcu başarıyla ödendi!');
      verileriGetir();
    } catch (err) {
      alert('Ödeme başarısız.');
    }
  };

  return (
    <div style={{ padding: 32, flex: 1, overflowY: 'auto' }}>
      <h2 style={{ fontSize: 24, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 6 }}>Üzerimdeki Kitaplar & Cezalarım</h2>
      <p style={{ color: 'var(--text-muted)', fontSize: 13, marginBottom: 28 }}>Ödünç aldığınız kitapların kalan süreleri ve ceza durumunuz</p>

      {/* Ceza Borcu Varsa Uyarı Kartı */}
      {cezalar.filter((c) => !c.odendiMi).length > 0 && (
        <div style={{ background: '#FEE2E2', border: '1px solid #F87171', borderRadius: 12, padding: 18, marginBottom: 24, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <AlertTriangle size={24} style={{ color: '#DC2626' }} />
            <div>
              <h4 style={{ fontSize: 14, fontWeight: 700, color: '#991B1B' }}>Ödenmemiş Ceza Borcunuz Bulunmaktadır!</h4>
              <p style={{ fontSize: 12, color: '#B91C1C', marginTop: 2 }}>Yeni kitap alabilmek için borcunuzu ödemeniz gerekmektedir.</p>
            </div>
          </div>
          {cezalar.filter((c) => !c.odendiMi).map((c) => (
            <button key={c.id} onClick={() => cezaOde(c.id)} className="btn-gold" style={{ padding: '8px 16px', fontSize: 13 }}>
              {c.tutar} TL Borcu Öde
            </button>
          ))}
        </div>
      )}

      {/* Kitaplar Tablosu */}
      <div className="card">
        <h3 style={{ fontSize: 16, fontWeight: 700, color: 'var(--primary-navy)', marginBottom: 16 }}>Ödünç Aldığım Kitaplar</h3>
        {yukleniyor ? (
          <p style={{ color: 'var(--text-muted)' }}>Yükleniyor...</p>
        ) : oduncler.length === 0 ? (
          <p style={{ color: 'var(--text-muted)', fontSize: 13 }}>Şu an üzerinizde ödünç alınmış kitap bulunmamaktadır.</p>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 13 }}>
            <thead>
              <tr style={{ borderBottom: '2px solid var(--border-color)', color: 'var(--text-muted)' }}>
                <th style={{ padding: 12 }}>Kitap Başlığı</th>
                <th style={{ padding: 12 }}>Ödünç Tarihi</th>
                <th style={{ padding: 12 }}>Son Teslim</th>
                <th style={{ padding: 12 }}>Kalan Süre</th>
                <th style={{ padding: 12 }}>Durum</th>
              </tr>
            </thead>
            <tbody>
              {oduncler.map((o) => (
                <tr key={o.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                  <td style={{ padding: 12, fontWeight: 600 }}>{o.kitapBaslik}</td>
                  <td style={{ padding: 12 }}>{new Date(o.oduncTarihi).toLocaleDateString('tr-TR')}</td>
                  <td style={{ padding: 12 }}>{new Date(o.sonTeslimTarihi).toLocaleDateString('tr-TR')}</td>
                  <td style={{ padding: 12 }}>
                    {o.durum === 'ODUNC_ALINDI' ? (
                      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, color: o.kalanGun < 3 ? '#DC2626' : '#16A34A', fontWeight: 700 }}>
                        <Clock size={14} /> {o.kalanGun} Gün Kaldı
                      </span>
                    ) : (
                      '-'
                    )}
                  </td>
                  <td style={{ padding: 12 }}>
                    <span style={{ padding: '4px 8px', borderRadius: 4, fontSize: 11, fontWeight: 700, backgroundColor: o.durum === 'IADE_EDILDI' ? '#DCFCE7' : '#FEF3C7', color: o.durum === 'IADE_EDILDI' ? '#166534' : '#92400E' }}>
                      {o.durum}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}