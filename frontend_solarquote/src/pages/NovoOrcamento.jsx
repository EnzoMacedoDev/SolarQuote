import { useEffect, useMemo, useState } from 'react';
import {
  X,
  UserRound,
  Sun,
  Calculator,
  CircleDollarSign,
  Zap,
  MapPin,
  PanelsTopLeft,
  ArrowRight,
  LoaderCircle,
  ListPlus,
  Plus,
  Trash2,
} from 'lucide-react';
import http from '../api/http';
import { useLayout } from '../context/LayoutContext';
 
export default function NovoOrcamento() {
 
  const [paineis, setPaineis] = useState([]);
  const [nomeCliente, setNomeCliente] = useState('');
  const [localCliente, setLocalCliente] = useState('');
  const [painelId, setPainelId] = useState('');
  const [quantidadePaineis, setQuantidadePaineis] = useState('');
  const [precoEntrada, setPrecoEntrada] = useState('');
  const [precoInstalacao, setPrecoInstalacao] = useState('');
  const [itens, setItens] = useState([]);
  const [erro, setErro] = useState('');
  const [enviando, setEnviando] = useState(false);
 
  const { fecharNovoOrcamento, notificarOrcamentoCriado } = useLayout();
 
  useEffect(() => {
    http.get('/paineis').then((resp) => {
      setPaineis(resp.data.filter((p) => p.ativo));
    });
  }, []);
 
  const painelSelecionado = paineis.find(
    (p) => p.id === Number(painelId)
  );
 
  const potenciaKwp = useMemo(() => {
    if (!painelSelecionado || !quantidadePaineis) return 0;
 
    return (
      (painelSelecionado.potenciaWp *
        Number(quantidadePaineis)) /
      1000
    );
  }, [painelSelecionado, quantidadePaineis]);
 
  const geracaoKwh = potenciaKwp * 127;
 
  const valorTotal =
    (Number(precoEntrada) || 0) +
    (Number(precoInstalacao) || 0);
 
  function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  }
 
  function adicionarItem() {
    setItens([...itens, { descricao: '', quantidade: '' }]);
  }
 
  function removerItem(index) {
    setItens(itens.filter((_, i) => i !== index));
  }
 
  function atualizarItem(index, campo, valor) {
    const novos = [...itens];
    novos[index] = { ...novos[index], [campo]: valor };
    setItens(novos);
  }
 
  async function handleSubmit(e) {
    e.preventDefault();
    setErro('');
    setEnviando(true);
 
    const itensValidos = itens.filter(
      (item) => item.descricao.trim() && item.quantidade.trim()
    );
 
    try {
      await http.post('/orcamentos', {
        nomeCliente,
        localCliente,
        painelId: Number(painelId),
        quantidadePaineis: Number(quantidadePaineis),
        precoEntrada: Number(precoEntrada),
        precoInstalacao: Number(precoInstalacao),
        itens: itensValidos,
      });
 
      notificarOrcamentoCriado();
      fecharNovoOrcamento();
    } catch (err) {
      setErro(
        'Não foi possível gerar o orçamento. Confira os dados.'
      );
 
      setEnviando(false);
    }
  }
 
  function fechar() {
    fecharNovoOrcamento();
  }
 
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/70 p-4 backdrop-blur-sm">
 
      {/* Modal */}
      <div className="flex max-h-[92vh] w-full max-w-5xl flex-col overflow-hidden rounded-3xl bg-white shadow-2xl">
 
        {/* Cabeçalho */}
        <div className="flex shrink-0 items-center justify-between border-b border-slate-200 px-6 py-5 lg:px-8">
 
          <div className="flex items-center gap-4">
            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-amber-50 text-amber-600">
              <Sun size={22} />
            </div>
 
            <div>
              <h1 className="text-lg font-bold text-slate-900">
                Novo orçamento
              </h1>
 
              <p className="text-sm text-slate-500">
                Preencha os dados para criar uma nova proposta.
              </p>
            </div>
          </div>
 
          <button
            type="button"
            onClick={fechar}
            className="flex h-10 w-10 items-center justify-center rounded-xl text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-700"
            title="Fechar"
          >
            <X size={21} />
          </button>
        </div>
 
        {/* Conteúdo com scroll */}
        <div className="overflow-y-auto">
          <form onSubmit={handleSubmit}>
 
            <div className="grid grid-cols-1 lg:grid-cols-[1fr_330px]">
 
              {/* Formulário */}
              <div className="space-y-8 p-6 lg:p-8">
 
                {/* Cliente */}
                <section>
                  <div className="mb-5 flex items-center gap-3">
                    <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-slate-100 text-slate-600">
                      <UserRound size={18} />
                    </div>
 
                    <div>
                      <h2 className="text-sm font-semibold text-slate-900">
                        Dados do cliente
                      </h2>
 
                      <p className="text-xs text-slate-500">
                        Informações básicas da proposta
                      </p>
                    </div>
                  </div>
 
                  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
 
                    {/* Nome */}
                    <div>
                      <label className="mb-2 block text-xs font-semibold text-slate-600">
                        Nome do cliente
                      </label>
 
                      <div className="relative">
                        <UserRound
                          size={17}
                          className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                        />
 
                        <input
                          type="text"
                          value={nomeCliente}
                          onChange={(e) =>
                            setNomeCliente(e.target.value)
                          }
                          placeholder="Ex.: João da Silva"
                          className="h-11 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-4 text-sm text-slate-800 outline-none transition-all placeholder:text-slate-400 focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                          required
                        />
                      </div>
                    </div>
 
                    {/* Local */}
                    <div>
                      <label className="mb-2 block text-xs font-semibold text-slate-600">
                        Local da instalação
                      </label>
 
                      <div className="relative">
                        <MapPin
                          size={17}
                          className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                        />
 
                        <input
                          type="text"
                          value={localCliente}
                          onChange={(e) =>
                            setLocalCliente(e.target.value)
                          }
                          placeholder="Ex.: Goiânia - GO"
                          className="h-11 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-4 text-sm text-slate-800 outline-none transition-all placeholder:text-slate-400 focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                          required
                        />
                      </div>
                    </div>
                  </div>
                </section>
 
                {/* Sistema */}
                <section>
                  <div className="mb-5 flex items-center gap-3">
                    <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-amber-50 text-amber-600">
                      <PanelsTopLeft size={18} />
                    </div>
 
                    <div>
                      <h2 className="text-sm font-semibold text-slate-900">
                        Sistema solar
                      </h2>
 
                      <p className="text-xs text-slate-500">
                        Configure os painéis do sistema
                      </p>
                    </div>
                  </div>
 
                  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
 
                    {/* Painel */}
                    <div>
                      <label className="mb-2 block text-xs font-semibold text-slate-600">
                        Painel fotovoltaico
                      </label>
 
                      <select
                        value={painelId}
                        onChange={(e) =>
                          setPainelId(e.target.value)
                        }
                        className="h-11 w-full rounded-xl border border-slate-200 bg-white px-3 text-sm text-slate-700 outline-none transition-all focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                        required
                      >
                        <option value="">
                          Selecione um painel
                        </option>
 
                        {paineis.map((p) => (
                          <option key={p.id} value={p.id}>
                            {p.modelo} — {p.potenciaWp}W
                          </option>
                        ))}
                      </select>
 
                      {painelSelecionado && (
                        <div className="mt-2 flex items-center gap-1.5 text-xs text-slate-400">
                          <span>
                            {painelSelecionado.fabricante}
                          </span>
 
                          <span>•</span>
 
                          <span>
                            {painelSelecionado.potenciaWp} W
                          </span>
                        </div>
                      )}
                    </div>
 
                    {/* Quantidade */}
                    <div>
                      <label className="mb-2 block text-xs font-semibold text-slate-600">
                        Quantidade de painéis
                      </label>
 
                      <input
                        type="number"
                        min="1"
                        value={quantidadePaineis}
                        onChange={(e) =>
                          setQuantidadePaineis(e.target.value)
                        }
                        placeholder="Ex.: 10"
                        className="h-11 w-full rounded-xl border border-slate-200 bg-white px-4 text-sm text-slate-800 outline-none transition-all placeholder:text-slate-400 focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                        required
                      />
                    </div>
                  </div>
                </section>
 
                {/* Dimensionamento */}
                <section>
                  <div className="mb-5 flex items-center gap-3">
                    <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-blue-50 text-blue-600">
                      <Calculator size={18} />
                    </div>
 
                    <div>
                      <h2 className="text-sm font-semibold text-slate-900">
                        Dimensionamento
                      </h2>
 
                      <p className="text-xs text-slate-500">
                        Estimativa calculada automaticamente
                      </p>
                    </div>
                  </div>
 
                  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
 
                    {/* Potência */}
                    <div className="rounded-2xl border border-slate-200 bg-slate-50 p-5">
                      <div className="mb-3 flex items-center justify-between">
                        <span className="text-xs font-medium text-slate-500">
                          Potência do sistema
                        </span>
 
                        <Zap
                          size={17}
                          className="text-amber-500"
                        />
                      </div>
 
                      <p className="text-2xl font-bold tracking-tight text-slate-900">
                        {potenciaKwp.toFixed(3)}
                        <span className="ml-1 text-sm font-medium text-slate-500">
                          kWp
                        </span>
                      </p>
 
                      <p className="mt-1 text-xs text-slate-400">
                        Capacidade instalada
                      </p>
                    </div>
 
                    {/* Geração */}
                    <div className="rounded-2xl border border-slate-200 bg-slate-50 p-5">
                      <div className="mb-3 flex items-center justify-between">
                        <span className="text-xs font-medium text-slate-500">
                          Geração estimada
                        </span>
 
                        <Sun
                          size={17}
                          className="text-amber-500"
                        />
                      </div>
 
                      <p className="text-2xl font-bold tracking-tight text-slate-900">
                        {geracaoKwh.toFixed(2)}
                        <span className="ml-1 text-sm font-medium text-slate-500">
                          kWh
                        </span>
                      </p>
 
                      <p className="mt-1 text-xs text-slate-400">
                        Estimativa de geração
                      </p>
                    </div>
                  </div>
                </section>
 
                {/* Itens / Equipamentos */}
                <section>
                  <div className="mb-5 flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-slate-100 text-slate-600">
                        <ListPlus size={18} />
                      </div>
 
                      <div>
                        <h2 className="text-sm font-semibold text-slate-900">
                          Itens do orçamento
                        </h2>
 
                        <p className="text-xs text-slate-500">
                          Equipamentos e materiais inclusos (opcional)
                        </p>
                      </div>
                    </div>
 
                    <button
                      type="button"
                      onClick={adicionarItem}
                      className="inline-flex items-center gap-1.5 rounded-lg border border-slate-200 px-3 py-1.5 text-xs font-medium text-slate-600 transition-colors hover:bg-slate-50"
                    >
                      <Plus size={14} />
                      Adicionar item
                    </button>
                  </div>
 
                  {itens.length === 0 && (
                    <p className="text-xs text-slate-400">
                      Nenhum item adicionado ainda.
                    </p>
                  )}
 
                  <div className="space-y-2">
                    {itens.map((item, index) => (
                      <div key={index} className="flex items-center gap-2">
                        <input
                          type="text"
                          value={item.descricao}
                          onChange={(e) =>
                            atualizarItem(index, 'descricao', e.target.value)
                          }
                          placeholder="Ex.: Estrutura fixação - PERFIL"
                          className="h-10 flex-1 rounded-lg border border-slate-200 bg-white px-3 text-sm text-slate-800 outline-none transition-all focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                        />
 
                        <input
                          type="text"
                          value={item.quantidade}
                          onChange={(e) =>
                            atualizarItem(index, 'quantidade', e.target.value)
                          }
                          placeholder="Ex.: 3 ou 10 Kit"
                          className="h-10 w-32 rounded-lg border border-slate-200 bg-white px-3 text-sm text-slate-800 outline-none transition-all focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                        />
 
                        <button
                          type="button"
                          onClick={() => removerItem(index)}
                          className="flex h-10 w-10 items-center justify-center rounded-lg text-slate-400 transition-colors hover:bg-red-50 hover:text-red-600"
                          title="Remover item"
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    ))}
                  </div>
                </section>
 
                {/* Valores */}
                <section>
                  <div className="mb-5 flex items-center gap-3">
                    <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-emerald-50 text-emerald-600">
                      <CircleDollarSign size={18} />
                    </div>
 
                    <div>
                      <h2 className="text-sm font-semibold text-slate-900">
                        Investimento
                      </h2>
 
                      <p className="text-xs text-slate-500">
                        Defina os valores da proposta
                      </p>
                    </div>
                  </div>
 
                  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
 
                    {/* Entrada */}
                    <div>
                      <label className="mb-2 block text-xs font-semibold text-slate-600">
                        Entrada
                      </label>
 
                      <div className="relative">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-sm font-medium text-slate-400">
                          R$
                        </span>
 
                        <input
                          type="number"
                          step="0.01"
                          min="0"
                          value={precoEntrada}
                          onChange={(e) =>
                            setPrecoEntrada(e.target.value)
                          }
                          placeholder="0,00"
                          className="h-11 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-4 text-sm text-slate-800 outline-none transition-all placeholder:text-slate-400 focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                          required
                        />
                      </div>
                    </div>
 
                    {/* Instalação */}
                    <div>
                      <label className="mb-2 block text-xs font-semibold text-slate-600">
                        Instalação
                      </label>
 
                      <div className="relative">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-sm font-medium text-slate-400">
                          R$
                        </span>
 
                        <input
                          type="number"
                          step="0.01"
                          min="0"
                          value={precoInstalacao}
                          onChange={(e) =>
                            setPrecoInstalacao(e.target.value)
                          }
                          placeholder="0,00"
                          className="h-11 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-4 text-sm text-slate-800 outline-none transition-all placeholder:text-slate-400 focus:border-amber-400 focus:ring-4 focus:ring-amber-500/10"
                          required
                        />
                      </div>
                    </div>
                  </div>
                </section>
 
                {/* Erro */}
                {erro && (
                  <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
                    {erro}
                  </div>
                )}
              </div>
 
              {/* Resumo lateral */}
              <aside className="border-t border-slate-200 bg-slate-50 p-6 lg:border-l lg:border-t-0 lg:p-7">
 
                <div className="sticky top-0">
 
                  <div className="mb-6">
                    <p className="text-xs font-semibold uppercase tracking-wider text-slate-400">
                      Resumo
                    </p>
 
                    <h2 className="mt-1 text-lg font-bold text-slate-900">
                      Proposta comercial
                    </h2>
                  </div>
 
                  {/* Sistema */}
                  <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
 
                    <div className="flex items-center gap-3">
                      <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-amber-50 text-amber-600">
                        <Sun size={19} />
                      </div>
 
                      <div className="min-w-0">
                        <p className="text-xs text-slate-400">
                          Sistema
                        </p>
 
                        <p className="truncate text-sm font-semibold text-slate-800">
                          {painelSelecionado
                            ? painelSelecionado.modelo
                            : 'Nenhum painel selecionado'}
                        </p>
                      </div>
                    </div>
 
                    <div className="my-5 h-px bg-slate-100" />
 
                    <div className="space-y-3">
 
                      <div className="flex items-center justify-between">
                        <span className="text-xs text-slate-500">
                          Painéis
                        </span>
 
                        <span className="text-sm font-semibold text-slate-800">
                          {quantidadePaineis || '—'}
                        </span>
                      </div>
 
                      <div className="flex items-center justify-between">
                        <span className="text-xs text-slate-500">
                          Potência
                        </span>
 
                        <span className="text-sm font-semibold text-slate-800">
                          {potenciaKwp.toFixed(3)} kWp
                        </span>
                      </div>
 
                      <div className="flex items-center justify-between">
                        <span className="text-xs text-slate-500">
                          Geração
                        </span>
 
                        <span className="text-sm font-semibold text-slate-800">
                          {geracaoKwh.toFixed(2)} kWh
                        </span>
                      </div>
 
                      <div className="flex items-center justify-between">
                        <span className="text-xs text-slate-500">
                          Itens extras
                        </span>
 
                        <span className="text-sm font-semibold text-slate-800">
                          {itens.filter((i) => i.descricao.trim()).length}
                        </span>
                      </div>
                    </div>
                  </div>
 
                  {/* Total */}
                  <div className="mt-4 rounded-2xl bg-slate-900 p-5 text-white">
 
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-slate-400">
                        Investimento total
                      </span>
 
                      <CircleDollarSign
                        size={19}
                        className="text-amber-400"
                      />
                    </div>
 
                    <p className="mt-3 text-3xl font-bold tracking-tight">
                      {formatarMoeda(valorTotal)}
                    </p>
 
                    <p className="mt-1 text-xs text-slate-500">
                      Entrada + instalação
                    </p>
                  </div>
 
                  {/* Botão */}
                  <button
                    type="submit"
                    disabled={enviando}
                    className="mt-5 flex w-full items-center justify-center gap-2 rounded-xl bg-amber-500 px-5 py-3.5 text-sm font-bold text-slate-950 shadow-lg shadow-amber-500/20 transition-all hover:bg-amber-400 hover:shadow-xl disabled:cursor-not-allowed disabled:opacity-60"
                  >
                    {enviando ? (
                      <>
                        <LoaderCircle
                          size={18}
                          className="animate-spin"
                        />
                        Gerando orçamento...
                      </>
                    ) : (
                      <>
                        Gerar orçamento
                        <ArrowRight size={18} />
                      </>
                    )}
                  </button>
 
                  <button
                    type="button"
                    onClick={fechar}
                    disabled={enviando}
                    className="mt-2 w-full rounded-xl px-5 py-3 text-sm font-medium text-slate-500 transition-colors hover:bg-slate-200 hover:text-slate-700 disabled:opacity-50"
                  >
                    Cancelar
                  </button>
                </div>
              </aside>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}