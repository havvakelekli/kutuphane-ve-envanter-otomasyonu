import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const kayitliKullanici = localStorage.getItem('kutuphane_kullanici');
    const token = localStorage.getItem('kutuphane_token');
    if (kayitliKullanici && token) {
      setUser(JSON.parse(kayitliKullanici));
    }
    setLoading(false);
  }, []);

  const login = async (eposta, sifre) => {
    const res = await api.post('/auth/giris-yap', { eposta, sifre });
    const { token, ...kullaniciBilgisi } = res.data;
    localStorage.setItem('kutuphane_token', token);
    localStorage.setItem('kutuphane_kullanici', JSON.stringify(kullaniciBilgisi));
    setUser(kullaniciBilgisi);
    return kullaniciBilgisi;
  };

  const register = async (adSoyad, eposta, sifre, telefonNo, rol) => {
    const res = await api.post('/auth/kayit-ol', { adSoyad, eposta, sifre, telefonNo, rol });
    const { token, ...kullaniciBilgisi } = res.data;
    localStorage.setItem('kutuphane_token', token);
    localStorage.setItem('kutuphane_kullanici', JSON.stringify(kullaniciBilgisi));
    setUser(kullaniciBilgisi);
    return kullaniciBilgisi;
  };

  const logout = () => {
    localStorage.removeItem('kutuphane_token');
    localStorage.removeItem('kutuphane_kullanici');
    setUser(null);
    window.location.href = '/login';
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);