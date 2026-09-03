import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLayout } from '../context/LayoutContext';

import {
  FileText,
  PanelsTopLeft,
  Wallet,
  Plus,
  ArrowUpRight,
  CalendarDays,
  ChevronRight,
} from 'lucide-react';

import http from '../api/http';

export default function Dashboard() {
  const [orcamentos, setOrcamentos] = useState([]);
  const [totalPaineis, setTotalPaineis] = useState(0);
  const [carregando, setCarregando] = useState(true);

  const { abrirNovoOrcamento, versaoOrcamentos } = useLayout();

  const navigate = useNavigate();

  useEffect(() => {
    async function carregarDados() {
      try {
        const [respOrcamentos, respPaineis] = await Promise.all([
          http.get('/orcamentos'),
          http.get('/paineis'),
        ]);

        setOrcamentos(respOrcamentos.data);
        setTotalPaineis(respPaineis.data.length);
      } catch (erro) {
        console.error('Erro ao carregar dashboard:', erro);
      } finally {
        setCarregando(false);
      }
    }

    carregarDados();
  }, [versaoOrcamentos]);

  if (carregando) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-amber-500" />

          <p className="text-sm text-slate-500">
            Carregando dashboard...
          </p>
        </div>
      </div>
    );
  }

  const totalOrcamentos = orcamentos.length;

  const valorTotalGeral = orcamentos.reduce(
    (soma, o) => soma + Number(o.valorTotal || 0),
    0
  );

  const ultimosOrcamentos = [...orcamentos].reverse().slice(0, 5);

  function formatarMoeda(valor) {
    return Number(valor || 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  }

  function formatarData(data) {
    return new Date(data).toLocaleDateString('pt-BR');
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="mx-auto max-w-7xl px-6 py-8 lg:px-10">

        {/* Cabeçalho */}
        <header className="mb-8 flex flex-col justify-between gap-5 md:flex-row md:items-center">
          <div>
            <p className="mb-1 text-sm font-medium text-amber-600">
              Visão geral
            </p>

            <h1 className="text-3xl font-bold tracking-tight text-slate-900">
              Dashboard
            </h1>

            <p className="mt-1 text-sm text-slate-500">
              Acompanhe seus orçamentos e sistemas cadastrados.
            </p>
          </div>

          <button
            onClick={abrirNovoOrcamento}
            className="inline-flex items-center justify-center gap-2 rounded-xl bg-amber-500 px-5 py-3 text-sm font-semibold text-slate-950 shadow-lg shadow-amber-500/20 transition-all hover:bg-amber-400 hover:shadow-xl hover:shadow-amber-500/25 active:scale-[0.98]"
          >
            <Plus size={19} strokeWidth={2.5} />
            Novo orçamento
          </button>
        </header>

        {/* Cards */}
        <section className="mb-8 grid grid-cols-1 gap-5 md:grid-cols-3">

          {/* Orçamentos */}
          <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md">
            <div className="flex items-start justify-between">
              <div>
                <p className="text-sm font-medium text-slate-500">
                  Orçamentos realizados
                </p>

                <h2 className="mt-3 text-3xl font-bold tracking-tight text-slate-900">
                  {totalOrcamentos}
                </h2>
              </div>

              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-amber-50 text-amber-600">
                <FileText size={21} />
              </div>
            </div>

            <div className="mt-5 flex items-center gap-1 text-xs text-slate-400">
              <ArrowUpRight size={14} />
              Total registrado no sistema
            </div>
          </div>

          {/* Painéis */}
          <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md">
            <div className="flex items-start justify-between">
              <div>
                <p className="text-sm font-medium text-slate-500">
                  Painéis cadastrados
                </p>

                <h2 className="mt-3 text-3xl font-bold tracking-tight text-slate-900">
                  {totalPaineis}
                </h2>
              </div>

              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-50 text-blue-600">
                <PanelsTopLeft size={21} />
              </div>
            </div>

            <div className="mt-5 flex items-center gap-1 text-xs text-slate-400">
              <ArrowUpRight size={14} />
              Modelos disponíveis
            </div>
          </div>

          {/* Valor */}
          <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md">
            <div className="flex items-start justify-between">
              <div className="min-w-0">
                <p className="text-sm font-medium text-slate-500">
                  Valor em orçamentos
                </p>

                <h2 className="mt-3 truncate text-2xl font-bold tracking-tight text-slate-900">
                  {formatarMoeda(valorTotalGeral)}
                </h2>
              </div>

              <div className="ml-3 flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-emerald-50 text-emerald-600">
                <Wallet size={21} />
              </div>
            </div>

            <div className="mt-5 flex items-center gap-1 text-xs text-slate-400">
              <ArrowUpRight size={14} />
              Soma dos valores registrados
            </div>
          </div>
        </section>

        {/* Área principal */}
        <section className="rounded-2xl border border-slate-200 bg-white shadow-sm">

          {/* Cabeçalho da tabela */}
          <div className="flex flex-col gap-3 border-b border-slate-100 px-6 py-5 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h2 className="font-semibold text-slate-900">
                Últimos orçamentos
              </h2>

              <p className="mt-1 text-xs text-slate-500">
                Os 5 orçamentos mais recentes
              </p>
            </div>

            {ultimosOrcamentos.length > 0 && (
              <button
                onClick={() => navigate('/orcamentos')}
                className="inline-flex items-center gap-1 text-sm font-medium text-amber-600 transition-colors hover:text-amber-700"
              >
                Ver todos
                <ChevronRight size={16} />
              </button>
            )}
          </div>

          {/* Conteúdo */}
          {ultimosOrcamentos.length === 0 ? (
            <div className="flex flex-col items-center justify-center px-6 py-16 text-center">
              <div className="mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-slate-100 text-slate-400">
                <FileText size={25} />
              </div>

              <h3 className="font-semibold text-slate-800">
                Nenhum orçamento ainda
              </h3>

              <p className="mt-1 max-w-sm text-sm text-slate-500">
                Crie seu primeiro orçamento para começar a acompanhar seus
                projetos por aqui.
              </p>

              <button
                onClick={abrirNovoOrcamento}
                className="mt-5 inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2.5 text-sm font-medium text-white transition-colors hover:bg-slate-800"
              >
                <Plus size={17} />
                Criar orçamento
              </button>
            </div>
          ) : (
            <>
              {/* Cabeçalho */}
              <div className="hidden grid-cols-[1fr_150px_150px_40px] gap-4 border-b border-slate-100 bg-slate-50/70 px-6 py-3 text-xs font-semibold uppercase tracking-wider text-slate-400 md:grid">
                <span>Cliente</span>
                <span>Data</span>
                <span>Valor</span>
                <span />
              </div>

              {/* Linhas */}
              <div>
                {ultimosOrcamentos.map((o) => (
                  <div
                    key={o.id}
                    className="grid grid-cols-1 gap-3 border-b border-slate-100 px-6 py-5 transition-colors last:border-b-0 hover:bg-slate-50/50 md:grid-cols-[1fr_150px_150px_40px] md:items-center md:gap-4"
                  >

                    {/* Cliente */}
                    <div className="min-w-0">
                      <div className="flex items-center gap-3">
                        <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-slate-100 text-sm font-bold text-slate-600">
                          {o.nomeCliente?.charAt(0)?.toUpperCase() || '?'}
                        </div>

                        <div className="min-w-0">
                          <p className="truncate text-sm font-semibold text-slate-800">
                            {o.nomeCliente}
                          </p>

                          <p className="mt-0.5 truncate text-xs text-slate-500">
                            {o.quantidadePaineis} painéis ·{' '}
                            {Number(o.potenciaKwp).toFixed(3)} kWp
                          </p>
                        </div>
                      </div>
                    </div>

                    {/* Data */}
                    <div className="flex items-center gap-2 text-sm text-slate-500">
                      <CalendarDays
                        size={16}
                        className="text-slate-400"
                      />

                      {formatarData(o.dataCriacao)}
                    </div>

                    {/* Valor */}
                    <div>
                      <p className="text-sm font-semibold text-slate-800">
                        {formatarMoeda(o.valorTotal)}
                      </p>

                      <p className="mt-0.5 text-xs text-slate-400">
                        Orçamento
                      </p>
                    </div>

                    {/* Ação */}
                    <div className="flex justify-end">
                      <button
                        onClick={() => navigate('/orcamentos')}
                        className="flex h-9 w-9 items-center justify-center rounded-lg text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-700"
                        title="Ver orçamentos"
                      >
                        <ChevronRight size={18} />
                      </button>
                    </div>

                  </div>
                ))}
              </div>
            </>
          )}
        </section>
      </div>
    </div>
  );
}