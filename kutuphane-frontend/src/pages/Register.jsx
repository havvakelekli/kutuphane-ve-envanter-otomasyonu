import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import { BookOpen, Lock, Mail, User, Phone, AlertCircle } from 'lucide-react';

export default function Register() {
  const [adSoyad, setAdSoyad] = useState('');
  const [eposta, setEposta] = useState('');
  const [sifre, setSifre] = useState('');
  const [telefonNo, setTelefonNo] = useState('');
  const [rol, setRol] = useState('ROL_UYE');
  const [hata, setHata] = useState('');
  const [yukleniyor, setYukleniyor] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setHata('');
    setYukleniyor(true);
    try {
      await register(adSoyad, eposta, sifre, telefonNo, rol);
      navigate('/katalog');
    } catch (err) {
      setHata(err.response?.data?.mesaj || 'Kayıt oluşturulamadı. Bilgilerinizi kontrol edin.');
    } finally {
      setYukleniyor(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, #0F2043 0%, #1A365D 100%)', padding: 20 }}>
      <div className="card" style={{ width: '100%', maxWidth: 460, padding: 36, boxShadow: '0 20px 40px rgba(0,0,0,0.3)' }}>
        <div style={{ textAlign: 'center', marginBottom: 24 }}>
          <div style={{ width: 56, height: 56, borderRadius: 16, background: 'rgba(212, 175, 55, 0.15)', display: 'inline-flex', alignItems: 'center', justifyContent: 'center', color: 'var(--accent-gold)', marginBottom: 10 }}>
            <BookOpen size={30} />
          </div>
          <h2 style={{ color: 'var(--primary-navy)', fontSize: 22, fontWeight: 700 }}>Yeni Üye Kaydı</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, marginTop: 4 }}>Kütüphane sistemine katılın</p>
        </div>

        {hata && (
          <div style={{ background: '#FEE2E2', border: '1px solid #F87171', color: '#991B1B', padding: '10px 14px', borderRadius: 8, fontSize: 13, display: 'flex', alignItems: 'center', gap: 8, marginBottom: 16 }}>
            <AlertCircle size={16} />
            <span>{hata}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 4 }}>Ad Soyad</label>
            <div style={{ position: 'relative' }}>
              <User size={18} style={{ position: 'absolute', left: 14, top: 12, color: 'var(--text-muted)' }} />
              <input type="text" required className="input-field" style={{ paddingLeft: 42, padding: 10 }} placeholder="Ahmet Yılmaz" value={adSoyad} onChange={(e) => setAdSoyad(e.target.value)} />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 4 }}>E-Posta Adresi</label>
            <div style={{ position: 'relative' }}>
              <Mail size={18} style={{ position: 'absolute', left: 14, top: 12, color: 'var(--text-muted)' }} />
              <input type="email" required className="input-field" style={{ paddingLeft: 42, padding: 10 }} placeholder="ahmet@gmail.com" value={eposta} onChange={(e) => setEposta(e.target.value)} />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 4 }}>Şifre (Min 6 Karakter)</label>
            <div style={{ position: 'relative' }}>
              <Lock size={18} style={{ position: 'absolute', left: 14, top: 12, color: 'var(--text-muted)' }} />
              <input type="password" required minLength={6} className="input-field" style={{ paddingLeft: 42, padding: 10 }} placeholder="••••••••" value={sifre} onChange={(e) => setSifre(e.target.value)} />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 4 }}>Telefon Numarası</label>
            <div style={{ position: 'relative' }}>
              <Phone size={18} style={{ position: 'absolute', left: 14, top: 12, color: 'var(--text-muted)' }} />
              <input type="tel" className="input-field" style={{ paddingLeft: 42, padding: 10 }} placeholder="0555 123 4567" value={telefonNo} onChange={(e) => setTelefonNo(e.target.value)} />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 4 }}>Rol Seçimi</label>
            <select className="input-field" value={rol} onChange={(e) => setRol(e.target.value)} style={{ padding: 10 }}>
              <option value="ROL_UYE">Üye (Öğrenci / Okuyucu)</option>
              <option value="ROL_KUTUPHANECI">Kütüphane Görevlisi</option>
              <option value="ROL_ADMIN">Sistem Yöneticisi (Admin)</option>
            </select>
          </div>

          <button type="submit" disabled={yukleniyor} className="btn-gold" style={{ marginTop: 8, height: 44 }}>
            {yukleniyor ? 'Kaydediliyor...' : 'Hesap Oluştur'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: 18, fontSize: 13, color: 'var(--text-muted)' }}>
          Zaten hesabınız var mı?{' '}
          <Link to="/login" style={{ color: 'var(--accent-gold)', fontWeight: 600, textDecoration: 'none' }}>
            Giriş Yap
          </Link>
        </div>
      </div>
    </div>
  );
}