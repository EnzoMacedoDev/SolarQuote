import Sidebar from './Sidebar';
import { LayoutProvider, useLayout } from '../context/LayoutContext';
import NovoOrcamento from '../pages/NovoOrcamento';

function LayoutContent({ children }) {
  const { novoOrcamentoAberto } = useLayout();

  return (
    <>
      <Sidebar />

      <main className="min-h-screen pl-64">
        {children}
      </main>

      {novoOrcamentoAberto && <NovoOrcamento />}
    </>
  );
}

export default function Layout({ children }) {
  return (
    <LayoutProvider>
      <div className="min-h-screen bg-slate-50">
        <LayoutContent>{children}</LayoutContent>
      </div>
    </LayoutProvider>
  );
}