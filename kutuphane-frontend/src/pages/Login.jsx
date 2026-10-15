import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import { BookOpen, Lock, Mail, AlertCircle } from 'lucide-react';

export default function Login() {
  const [eposta, setEposta] = useState('');
  const [sifre, setSifre] = useState('');
  const [hata, setHata] = useState('');
  const [yukleniyor, setYukleniyor] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setHata('');
    setYukleniyor(true);
    try {
      await login(eposta, sifre);
      navigate('/katalog');
    } catch (err) {
      setHata(err.response?.data?.mesaj || 'Giriş başarısız! E-posta veya şifre hatalı.');
    } finally {
      setYukleniyor(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, #0F2043 0%, #1A365D 100%)', padding: 20 }}>
      <div className="card" style={{ width: '100%', maxWidth: 420, padding: 36, boxShadow: '0 20px 40px rgba(0,0,0,0.3)' }}>
        <div style={{ textAlign: 'center', marginBottom: 28 }}>
          <div style={{ width: 56, height: 56, borderRadius: 16, background: 'rgba(212, 175, 55, 0.15)', display: 'inline-flex', alignItems: 'center', justifyContent: 'center', color: 'var(--accent-gold)', marginBottom: 12 }}>
            <BookOpen size={30} />
          </div>
          <h2 style={{ color: 'var(--primary-navy)', fontSize: 22, fontWeight: 700 }}>Kütüphane Otomasyonu</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: 13, marginTop: 4 }}>Hesabınıza giriş yapın</p>
        </div>

        {hata && (
          <div style={{ background: '#FEE2E2', border: '1px solid #F87171', color: '#991B1B', padding: '10px 14px', borderRadius: 8, fontSize: 13, display: 'flex', alignItems: 'center', gap: 8, marginBottom: 18 }}>
            <AlertCircle size={16} />
            <span>{hata}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div>
            <label style={{ display: 'block', fontSize: 13, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 6 }}>E-Posta Adresi</label>
            <div style={{ position: 'relative' }}>
              <Mail size={18} style={{ position: 'absolute', left: 14, top: 14, color: 'var(--text-muted)' }} />
              <input
                type="email"
                required
                className="input-field"
                style={{ paddingLeft: 42 }}
                placeholder="ornek@kutuphane.com"
                value={eposta}
                onChange={(e) => setEposta(e.target.value)}
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: 13, fontWeight: 600, color: 'var(--text-dark)', marginBottom: 6 }}>Şifre</label>
            <div style={{ position: 'relative' }}>
              <Lock size={18} style={{ position: 'absolute', left: 14, top: 14, color: 'var(--text-muted)' }} />
              <input
                type="password"
                required
                className="input-field"
                style={{ paddingLeft: 42 }}
                placeholder="••••••••"
                value={sifre}
                onChange={(e) => setSifre(e.target.value)}
              />
            </div>
          </div>

          <button type="submit" disabled={yukleniyor} className="btn-gold" style={{ marginTop: 8, height: 46 }}>
            {yukleniyor ? 'Giriş Yapılıyor...' : 'Giriş Yap'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: 24, fontSize: 13, color: 'var(--text-muted)' }}>
          Hesabınız yok mu?{' '}
          <Link to="/register" style={{ color: 'var(--accent-gold)', fontWeight: 600, textDecoration: 'none' }}>
            Hemen Kayıt Ol
          </Link>
        </div>
      </div>
    </div>
  );
}