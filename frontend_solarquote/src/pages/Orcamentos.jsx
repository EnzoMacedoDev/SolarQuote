import { useEffect, useState } from 'react';
import {
  Plus,
  FileText,
  Download,
  Trash2,
  ChevronDown,
  ChevronUp,
  MapPin,
  PanelsTopLeft,
  Zap,
  CalendarDays,
  UserRound,
  CircleDollarSign,
} from 'lucide-react';
import http from '../api/http';
import { useLayout } from '../context/LayoutContext';
 
export default function Orcamentos() {
  const [orcamentos, setOrcamentos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [expandidoId, setExpandidoId] = useState(null);
 
  const { abrirNovoOrcamento, versaoOrcamentos } = useLayout();
 
  useEffect(() => {
    carregar();
  }, [versaoOrcamentos]);
 
  async function carregar() {
    setCarregando(true);
    const resp = await http.get('/orcamentos');
    setOrcamentos(resp.data);
    setCarregando(false);
  }
 
  function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  }
 
  function formatarData(data) {
    return new Date(data).toLocaleDateString('pt-BR');
  }
 
  async function baixarPdf(id) {
    const resp = await http.get(`/orcamentos/${id}/pdf`, {
      responseType: 'blob',
    });
 
    const url = URL.createObjectURL(resp.data);
    window.open(url, '_blank');
  }
 
  async function excluir(id) {
    if (!window.confirm('Excluir este orçamento?')) return;
 
    await http.delete(`/orcamentos/${id}`);
 
    setOrcamentos((atual) =>
      atual.filter((o) => o.id !== id)
    );
  }
 
  if (carregando) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-amber-500" />
          <p className="text-sm text-slate-500">
            Carregando orçamentos...
          </p>
        </div>
      </div>
    );
  }
 
  const listaOrdenada = [...orcamentos].reverse();
 
  return (
    <div className="min-h-screen bg-slate-50">
      <div className="mx-auto max-w-7xl px-6 py-8 lg:px-10">
 
        {/* Cabeçalho */}
        <header className="mb-8 flex flex-col justify-between gap-5 md:flex-row md:items-center">
          <div>
            <p className="mb-1 text-sm font-medium text-amber-600">
              Gestão comercial
            </p>
 
            <h1 className="text-3xl font-bold tracking-tight text-slate-900">
              Orçamentos
            </h1>
 
            <p className="mt-1 text-sm text-slate-500">
              Consulte e gerencie os orçamentos realizados.
            </p>
          </div>
 
          <button
            onClick={abrirNovoOrcamento}
            className="inline-flex items-center justify-center gap-2 rounded-xl bg-amber-500 px-5 py-3 text-sm font-semibold text-slate-950 shadow-lg shadow-amber-500/20 transition-all hover:bg-amber-400 hover:shadow-xl active:scale-[0.98]"
          >
            <Plus size={19} strokeWidth={2.5} />
            Novo orçamento
          </button>
        </header>
 
        {/* Indicador */}
        <div className="mb-6 flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-white text-amber-600 shadow-sm ring-1 ring-slate-200">
            <FileText size={19} />
          </div>
 
          <div>
            <p className="text-sm font-semibold text-slate-800">
              {orcamentos.length}{' '}
              {orcamentos.length === 1
                ? 'orçamento realizado'
                : 'orçamentos realizados'}
            </p>
 
            <p className="text-xs text-slate-500">
              Histórico de propostas cadastradas
            </p>
          </div>
        </div>
 
        {/* Lista */}
        {listaOrdenada.length === 0 ? (
          <div className="rounded-2xl border border-slate-200 bg-white px-6 py-20 text-center shadow-sm">
 
            <div className="mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-2xl bg-slate-100 text-slate-400">
              <FileText size={28} />
            </div>
 
            <h2 className="text-lg font-semibold text-slate-800">
              Nenhum orçamento cadastrado
            </h2>
 
            <p className="mx-auto mt-2 max-w-md text-sm text-slate-500">
              Você ainda não possui nenhum orçamento. Crie o primeiro para
              começar a registrar suas propostas.
            </p>
 
            <button
              onClick={abrirNovoOrcamento}
              className="mt-6 inline-flex items-center gap-2 rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition-colors hover:bg-slate-800"
            >
              <Plus size={18} />
              Criar primeiro orçamento
            </button>
          </div>
        ) : (
          <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">
 
            {/* Cabeçalho da tabela */}
            <div className="hidden grid-cols-[minmax(220px,1.5fr)_1fr_130px_150px_48px] gap-4 border-b border-slate-100 bg-slate-50/70 px-6 py-4 text-[11px] font-semibold uppercase tracking-wider text-slate-400 lg:grid">
              <span>Cliente</span>
              <span>Sistema</span>
              <span>Data</span>
              <span>Valor</span>
              <span />
            </div>
 
            {/* Orçamentos */}
            <div>
              {listaOrdenada.map((o) => {
                const expandido = expandidoId === o.id;
 
                return (
                  <div
                    key={o.id}
                    className="border-b border-slate-100 last:border-b-0"
                  >
 
                    {/* Linha principal */}
                    <div className="grid grid-cols-1 gap-4 px-6 py-5 transition-colors hover:bg-slate-50/50 lg:grid-cols-[minmax(220px,1.5fr)_1fr_130px_150px_48px] lg:items-center lg:gap-4">
 
                      {/* Cliente */}
                      <div className="flex min-w-0 items-center gap-3">
                        <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-slate-100 text-sm font-bold text-slate-600">
                          {o.nomeCliente
                            ?.charAt(0)
                            ?.toUpperCase() || '?'}
                        </div>
 
                        <div className="min-w-0">
                          <p className="truncate text-sm font-semibold text-slate-800">
                            {o.nomeCliente}
                          </p>
 
                          <div className="mt-1 flex items-center gap-1.5 text-xs text-slate-500">
                            <MapPin size={12} />
                            <span className="truncate">
                              {o.localCliente}
                            </span>
                          </div>
                        </div>
                      </div>
 
                      {/* Sistema */}
                      <div>
                        <div className="flex items-center gap-2">
                          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-amber-50 text-amber-600">
                            <PanelsTopLeft size={15} />
                          </div>
 
                          <div>
                            <p className="text-sm font-medium text-slate-700">
                              {o.quantidadePaineis} painéis
                            </p>
 
                            <p className="text-xs text-slate-400">
                              {Number(o.potenciaKwp).toFixed(3)} kWp
                            </p>
                          </div>
                        </div>
                      </div>
 
                      {/* Data */}
                      <div className="flex items-center gap-2 text-sm text-slate-500">
                        <CalendarDays
                          size={15}
                          className="text-slate-400"
                        />
 
                        {formatarData(o.dataCriacao)}
                      </div>
 
                      {/* Valor */}
                      <div>
                        <p className="text-sm font-bold text-slate-800">
                          {formatarMoeda(o.valorTotal)}
                        </p>
 
                        <p className="mt-0.5 text-xs text-slate-400">
                          Valor total
                        </p>
                      </div>
 
                      {/* Expandir */}
                      <div className="flex justify-start lg:justify-end">
                        <button
                          onClick={() =>
                            setExpandidoId(
                              expandido ? null : o.id
                            )
                          }
                          className="flex h-9 w-9 items-center justify-center rounded-lg text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-700"
                          title={
                            expandido
                              ? 'Ocultar detalhes'
                              : 'Visualizar detalhes'
                          }
                        >
                          {expandido ? (
                            <ChevronUp size={18} />
                          ) : (
                            <ChevronDown size={18} />
                          )}
                        </button>
                      </div>
                    </div>
 
                    {/* Detalhes */}
                    {expandido && (
                      <div className="border-t border-slate-100 bg-slate-50/70 px-6 py-6">
 
                        <div className="mb-5 flex items-center gap-2">
                          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-white text-slate-600 shadow-sm ring-1 ring-slate-200">
                            <FileText size={16} />
                          </div>
 
                          <div>
                            <h3 className="text-sm font-semibold text-slate-800">
                              Detalhes do orçamento
                            </h3>
 
                            <p className="text-xs text-slate-500">
                              Informações completas da proposta
                            </p>
                          </div>
                        </div>
 
                        {/* Detalhes */}
                        <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
 
                          {/* Cliente */}
                          <div className="rounded-xl border border-slate-200 bg-white p-4">
                            <div className="mb-3 flex items-center gap-2 text-slate-400">
                              <UserRound size={16} />
                              <span className="text-xs font-medium">
                                Cliente
                              </span>
                            </div>
 
                            <p className="text-sm font-semibold text-slate-800">
                              {o.nomeCliente}
                            </p>
 
                            <p className="mt-1 text-xs text-slate-500">
                              {o.localCliente}
                            </p>
                          </div>
 
                          {/* Painel */}
                          <div className="rounded-xl border border-slate-200 bg-white p-4">
                            <div className="mb-3 flex items-center gap-2 text-slate-400">
                              <PanelsTopLeft size={16} />
                              <span className="text-xs font-medium">
                                Painel
                              </span>
                            </div>
 
                            <p className="text-sm font-semibold text-slate-800">
                              {o.painel.modelo}
                            </p>
 
                            <p className="mt-1 text-xs text-slate-500">
                              {o.painel.fabricante}
                            </p>
                          </div>
 
                          {/* Geração */}
                          <div className="rounded-xl border border-slate-200 bg-white p-4">
                            <div className="mb-3 flex items-center gap-2 text-slate-400">
                              <Zap size={16} />
                              <span className="text-xs font-medium">
                                Geração estimada
                              </span>
                            </div>
 
                            <p className="text-sm font-semibold text-slate-800">
                              {Number(o.geracaoKwh).toFixed(2)} kWh
                            </p>
 
                            <p className="mt-1 text-xs text-slate-500">
                              {Number(o.potenciaKwp).toFixed(3)} kWp
                            </p>
                          </div>
 
                          {/* Investimento */}
                          <div className="rounded-xl border border-slate-200 bg-white p-4">
                            <div className="mb-3 flex items-center gap-2 text-slate-400">
                              <CircleDollarSign size={16} />
                              <span className="text-xs font-medium">
                                Investimento
                              </span>
                            </div>
 
                            <p className="text-sm font-semibold text-slate-800">
                              {formatarMoeda(o.valorTotal)}
                            </p>
 
                            <p className="mt-1 text-xs text-slate-500">
                              Total da proposta
                            </p>
                          </div>
                        </div>
 
                        {/* Itens do orçamento */}
                        {o.itens && o.itens.length > 0 && (
                          <div className="mt-3 overflow-hidden rounded-xl border border-slate-200 bg-white">
                            <div className="border-b border-slate-100 bg-slate-50 px-4 py-2 text-xs font-medium text-slate-500">
                              Equipamentos
                            </div>
                            <div className="divide-y divide-slate-100">
                              {o.itens.map((item) => (
                                <div key={item.id} className="flex justify-between px-4 py-2 text-sm">
                                  <span className="text-slate-700">{item.descricao}</span>
                                  <span className="text-slate-500">{item.quantidade}</span>
                                </div>
                              ))}
                            </div>
                          </div>
                        )}
 
                        {/* Valores */}
                        <div className="mt-3 grid grid-cols-1 gap-3 sm:grid-cols-2">
 
                          <div className="rounded-xl border border-slate-200 bg-white px-4 py-3">
                            <p className="text-xs text-slate-500">
                              Entrada
                            </p>
 
                            <p className="mt-1 text-sm font-semibold text-slate-800">
                              {formatarMoeda(o.precoEntrada)}
                            </p>
                          </div>
 
                          <div className="rounded-xl border border-slate-200 bg-white px-4 py-3">
                            <p className="text-xs text-slate-500">
                              Instalação
                            </p>
 
                            <p className="mt-1 text-sm font-semibold text-slate-800">
                              {formatarMoeda(o.precoInstalacao)}
                            </p>
                          </div>
                        </div>
 
                        {/* Ações */}
                        <div className="mt-5 flex flex-wrap gap-3">
 
                          <button
                            onClick={() => baixarPdf(o.id)}
                            className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2.5 text-sm font-medium text-white transition-colors hover:bg-slate-800"
                          >
                            <Download size={16} />
                            Gerar / visualizar PDF
                          </button>
 
                          <button
                            onClick={() => excluir(o.id)}
                            className="inline-flex items-center gap-2 rounded-xl border border-red-200 bg-white px-4 py-2.5 text-sm font-medium text-red-600 transition-colors hover:bg-red-50"
                          >
                            <Trash2 size={16} />
                            Excluir orçamento
                          </button>
 
                        </div>
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        )}
 
      </div>
    </div>
  );
}