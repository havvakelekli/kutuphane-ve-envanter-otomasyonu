import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Sidebar from './components/Sidebar';
import Login from './pages/Login';
import Register from './pages/Register';
import Katalog from './pages/Katalog';
import KitapEkle from './pages/KitapEkle';
import OduncIslemleri from './pages/OduncIslemleri';
import Uzerimdekiler from './pages/Uzerimdekiler';

function ProtectedLayout({ children, requiredRole }) {
  const { user, loading } = useAuth();

  if (loading) return null;
  if (!user) return <Navigate to="/login" replace />;

  if (requiredRole && user.rol !== 'ROL_ADMIN' && user.rol !== requiredRole) {
    return <Navigate to="/katalog" replace />;
  }

  return (
    <div style={{ display: 'flex', minHeight: '100vh', width: '100vw' }}>
      <Sidebar />
      <main style={{ flex: 1, backgroundColor: 'var(--bg-light)', display: 'flex', flexDirection: 'column' }}>
        {children}
      </main>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/katalog" element={<ProtectedLayout><Katalog /></ProtectedLayout>} />
          <Route path="/uzerimdekiler" element={<ProtectedLayout><Uzerimdekiler /></ProtectedLayout>} />
          
          <Route path="/kitap-ekle" element={<ProtectedLayout requiredRole="ROL_KUTUPHANECI"><KitapEkle /></ProtectedLayout>} />
          <Route path="/odunc-islemleri" element={<ProtectedLayout requiredRole="ROL_KUTUPHANECI"><OduncIslemleri /></ProtectedLayout>} />

          <Route path="*" element={<Navigate to="/katalog" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}