import { BrowserRouter, Routes, Route } from 'react-router-dom';
 
import { AuthProvider } from './context/AuthContext';
 
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
 
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Orcamentos from './pages/Orcamentos';
import Paineis from './pages/Paineis';
 
export default function App() {
  return (
    <AuthProvider>
 
      <BrowserRouter>
 
        <Routes>
 
          <Route
            path="/"
            element={<Login />}
          />
 
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Layout>
                  <Dashboard />
                </Layout>
              </ProtectedRoute>
            }
          />
 
          <Route
            path="/orcamentos"
            element={
              <ProtectedRoute>
                <Layout>
                  <Orcamentos />
                </Layout>
              </ProtectedRoute>
            }
          />
 
          <Route
            path="/paineis"
            element={
              <ProtectedRoute>
                <Layout>
                  <Paineis />
                </Layout>
              </ProtectedRoute>
            }
          />
 
        </Routes>
 
      </BrowserRouter>
 
    </AuthProvider>
  );
}