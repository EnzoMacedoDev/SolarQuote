import { NavLink } from 'react-router-dom';

import {
  LayoutDashboard,
  FileText,
  PanelsTopLeft,
  Sun,
  LogOut,
} from 'lucide-react';

import { useAuth } from '../context/AuthContext';

const links = [
  { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
  { name: 'Orçamentos', path: '/orcamentos', icon: FileText },
  { name: 'Painéis', path: '/paineis', icon: PanelsTopLeft },
];

export default function Sidebar() {
  const { logout } = useAuth();

  return (
    <aside className="fixed left-0 top-0 z-40 flex h-screen w-64 flex-col border-r border-slate-800 bg-slate-950 text-white">

      {/* Logo */}
      <div className="flex h-20 items-center border-b border-slate-800 px-6">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-amber-500 text-slate-950">
            <Sun size={22} strokeWidth={2.5} />
          </div>

          <div>
            <h1 className="text-lg font-bold tracking-tight">
              Solar<span className="text-amber-400">Quote</span>
            </h1>

            <p className="text-[11px] text-slate-500">
              Gestão de orçamentos
            </p>
          </div>
        </div>
      </div>

      {/* Navegação */}
      <nav className="flex-1 px-3 py-6">
        <p className="mb-3 px-3 text-[11px] font-semibold uppercase tracking-wider text-slate-500">
          Menu
        </p>

        <div className="space-y-1">
          {links.map((link) => {
            const Icon = link.icon;

            return (
              <NavLink
                key={link.path}
                to={link.path}
                className={({ isActive }) =>
                  `group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-medium transition-all ${
                    isActive
                      ? 'bg-amber-500 text-slate-950 shadow-lg shadow-amber-500/10'
                      : 'text-slate-400 hover:bg-slate-900 hover:text-white'
                  }`
                }
              >
                <Icon size={19} strokeWidth={2} />
                <span>{link.name}</span>
              </NavLink>
            );
          })}
        </div>
      </nav>

      {/* Rodapé */}
      <div className="border-t border-slate-800 p-4">

        {/* Usuário */}
        <div className="mb-3 flex items-center gap-3 rounded-xl bg-slate-900 p-3">
          <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-700 text-sm font-semibold text-white">
            U
          </div>

          <div className="min-w-0">
            <p className="truncate text-sm font-medium text-white">
              Usuário
            </p>

            <p className="truncate text-xs text-slate-500">
              SolarQuote
            </p>
          </div>
        </div>

        {/* Sair */}
        <button
          onClick={logout}
          className="flex w-full items-center gap-3 rounded-xl px-3 py-3 text-sm font-medium text-slate-400 transition-all hover:bg-red-500/10 hover:text-red-400"
        >
          <LogOut size={19} strokeWidth={2} />
          <span>Sair</span>
        </button>

      </div>
    </aside>
  );
}