import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { BookOpen, PlusCircle, Repeat, UserCheck, LogOut, Shield } from 'lucide-react';

export default function Sidebar() {
  const { user, logout } = useAuth();

  const isLibrarianOrAdmin = user?.rol === 'ROL_ADMIN' || user?.rol === 'ROL_KUTUPHANECI';

  const linkStyle = ({ isActive }) => ({
    display: 'flex',
    alignItems: 'center',
    gap: 12,
    padding: '12px 16px',
    borderRadius: 8,
    textDecoration: 'none',
    fontSize: 14,
    fontWeight: 500,
    color: isActive ? '#0F2043' : '#94A3B8',
    background: isActive ? '#D4AF37' : 'transparent',
    transition: 'all 0.2s',
    marginBottom: 6,
  });

  return (
    <aside style={{ width: 260, backgroundColor: '#0F2043', color: '#FFF', display: 'flex', flexDirection: 'column', padding: '24px 16px', minHeight: '100vh', boxShadow: '4px 0 16px rgba(0,0,0,0.1)' }}>
      {/* Logo */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 12, paddingBottom: 24, borderBottom: '1px solid rgba(255,255,255,0.1)', marginBottom: 20 }}>
        <div style={{ width: 40, height: 40, borderRadius: 10, backgroundColor: 'rgba(212,175,55,0.2)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#D4AF37' }}>
          <BookOpen size={24} />
        </div>
        <div>
          <h1 style={{ fontSize: 16, fontWeight: 700, letterSpacing: -0.5 }}>KÜTÜPHANE</h1>
          <span style={{ fontSize: 11, color: '#D4AF37', fontWeight: 600 }}>Envanter Sistemi</span>
        </div>
      </div>

      {/* Menü Linkleri */}
      <nav style={{ flex: 1 }}>
        <NavLink to="/katalog" style={linkStyle}>
          <BookOpen size={18} />
          <span>Kitap Kataloğu</span>
        </NavLink>

        <NavLink to="/uzerimdekiler" style={linkStyle}>
          <UserCheck size={18} />
          <span>Üzerimdeki Kitaplar</span>
        </NavLink>

        {isLibrarianOrAdmin && (
          <>
            <div style={{ fontSize: 11, fontWeight: 700, color: '#64748B', textTransform: 'uppercase', margin: '20px 0 8px 12px', letterSpacing: 0.5 }}>
              Yönetim Paneli
            </div>

            <NavLink to="/kitap-ekle" style={linkStyle}>
              <PlusCircle size={18} />
              <span>Yeni Kitap Ekle</span>
            </NavLink>

            <NavLink to="/odunc-islemleri" style={linkStyle}>
              <Repeat size={18} />
              <span>Ödünç & İade Masası</span>
            </NavLink>
          </>
        )}
      </nav>

      {/* Kullanıcı Kartı & Çıkış Butonu */}
      <div style={{ paddingTop: 16, borderTop: '1px solid rgba(255,255,255,0.1)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 12 }}>
          <div style={{ width: 36, height: 36, borderRadius: '50%', backgroundColor: '#1A365D', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#D4AF37', fontWeight: 700, fontSize: 13 }}>
            {user?.adSoyad?.charAt(0) || 'U'}
          </div>
          <div style={{ overflow: 'hidden' }}>
            <p style={{ fontSize: 13, fontWeight: 600, whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>{user?.adSoyad}</p>
            <span style={{ fontSize: 10, display: 'inline-block', padding: '2px 6px', borderRadius: 4, backgroundColor: 'rgba(212,175,55,0.2)', color: '#D4AF37', fontWeight: 700 }}>
              {user?.rol?.replace('ROL_', '')}
            </span>
          </div>
        </div>

        <button onClick={logout} style={{ width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 8, padding: '10px', borderRadius: 8, backgroundColor: 'rgba(239,68,68,0.15)', color: '#FCA5A5', border: 'none', cursor: 'pointer', fontSize: 13, fontWeight: 600 }}>
          <LogOut size={16} />
          <span>Güvenli Çıkış</span>
        </button>
      </div>
    </aside>
  );
}