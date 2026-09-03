import { useEffect, useState } from 'react';

import {
  PanelsTopLeft,
  Plus,
  Pencil,
  Power,
  X,
  Save,
} from 'lucide-react';

import http from '../api/http';

const painelVazio = {
  modelo: '',
  fabricante: '',
  potenciaWp: '',
  preco: '',
};

export default function Paineis() {
  const [paineis, setPaineis] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [novoPainel, setNovoPainel] = useState(painelVazio);
  const [erro, setErro] = useState('');
  const [editandoId, setEditandoId] = useState(null);
  const [edicao, setEdicao] = useState(painelVazio);

  useEffect(() => {
    carregar();
  }, []);

  async function carregar() {
    setCarregando(true);

    const resp = await http.get('/paineis');

    setPaineis(resp.data);
    setCarregando(false);
  }

  function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  }

  async function cadastrar(e) {
    e.preventDefault();

    setErro('');

    try {
      await http.post('/paineis', {
        modelo: novoPainel.modelo,
        fabricante: novoPainel.fabricante,
        potenciaWp: Number(novoPainel.potenciaWp),
        preco: Number(novoPainel.preco),
        ativo: true,
      });

      setNovoPainel(painelVazio);
      carregar();
    } catch (err) {
      setErro('Não foi possível cadastrar. Confira os dados.');
    }
  }

  function iniciarEdicao(p) {
    setEditandoId(p.id);

    setEdicao({
      modelo: p.modelo,
      fabricante: p.fabricante,
      potenciaWp: p.potenciaWp,
      preco: p.preco,
    });
  }

  async function salvarEdicao(p) {
    await http.put(`/paineis/${p.id}`, {
      modelo: edicao.modelo,
      fabricante: edicao.fabricante,
      potenciaWp: Number(edicao.potenciaWp),
      preco: Number(edicao.preco),
      ativo: p.ativo,
    });

    setEditandoId(null);
    carregar();
  }

  async function alternarAtivo(p) {
    if (p.ativo) {
      await http.delete(`/paineis/${p.id}`);
    } else {
      await http.put(`/paineis/${p.id}`, { ...p, ativo: true });
    }

    carregar();
  }

  if (carregando) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-amber-500" />
          <p className="text-sm text-slate-500">
            Carregando painéis...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="mx-auto max-w-7xl px-6 py-8 lg:px-10">

        {/* Cabeçalho */}
        <header className="mb-8">
          <p className="mb-1 text-sm font-medium text-amber-600">
            Catálogo de produtos
          </p>

          <div className="flex flex-col justify-between gap-4 md:flex-row md:items-center">
            <div>
              <h1 className="text-3xl font-bold tracking-tight text-slate-900">
                Painéis
              </h1>

              <p className="mt-1 text-sm text-slate-500">
                Cadastre e gerencie os painéis disponíveis para seus orçamentos.
              </p>
            </div>

            <div className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-4 py-3 shadow-sm">
              <PanelsTopLeft
                size={19}
                className="text-amber-500"
              />

              <div>
                <p className="text-xs text-slate-500">
                  Painéis cadastrados
                </p>

                <p className="text-lg font-bold text-slate-900">
                  {paineis.length}
                </p>
              </div>
            </div>
          </div>
        </header>

        {/* Cadastro */}
        <section className="mb-8 rounded-2xl border border-slate-200 bg-white shadow-sm">

          <div className="border-b border-slate-100 px-6 py-5">
            <div className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-amber-100 text-amber-600">
                <Plus size={20} strokeWidth={2.5} />
              </div>

              <div>
                <h2 className="font-semibold text-slate-900">
                  Cadastrar painel
                </h2>

                <p className="text-sm text-slate-500">
                  Adicione um novo painel ao catálogo.
                </p>
              </div>
            </div>
          </div>

          <form
            onSubmit={cadastrar}
            className="grid grid-cols-1 gap-5 p-6 md:grid-cols-2"
          >
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Modelo
              </label>

              <input
                type="text"
                value={novoPainel.modelo}
                onChange={(e) =>
                  setNovoPainel({
                    ...novoPainel,
                    modelo: e.target.value,
                  })
                }
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                placeholder="Ex.: Mono 550W"
                required
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Fabricante
              </label>

              <input
                type="text"
                value={novoPainel.fabricante}
                onChange={(e) =>
                  setNovoPainel({
                    ...novoPainel,
                    fabricante: e.target.value,
                  })
                }
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                placeholder="Ex.: Exemplo Solar"
                required
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Potência (W)
              </label>

              <input
                type="number"
                value={novoPainel.potenciaWp}
                onChange={(e) =>
                  setNovoPainel({
                    ...novoPainel,
                    potenciaWp: e.target.value,
                  })
                }
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                placeholder="Ex.: 550"
                required
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Preço (R$)
              </label>

              <input
                type="number"
                step="0.01"
                value={novoPainel.preco}
                onChange={(e) =>
                  setNovoPainel({
                    ...novoPainel,
                    preco: e.target.value,
                  })
                }
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                placeholder="Ex.: 899.90"
                required
              />
            </div>

            {erro && (
              <p className="md:col-span-2 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
                {erro}
              </p>
            )}

            <div className="flex justify-end md:col-span-2">
              <button
                type="submit"
                className="inline-flex items-center gap-2 rounded-xl bg-amber-500 px-5 py-3 text-sm font-semibold text-slate-950 shadow-lg shadow-amber-500/20 transition hover:bg-amber-400 hover:shadow-xl active:scale-[0.98]"
              >
                <Plus size={18} strokeWidth={2.5} />
                Cadastrar painel
              </button>
            </div>
          </form>
        </section>

        {/* Lista */}
        <section>
          <div className="mb-4 flex items-center justify-between">
            <div>
              <h2 className="text-lg font-bold text-slate-900">
                Painéis cadastrados
              </h2>

              <p className="text-sm text-slate-500">
                Gerencie os produtos disponíveis no sistema.
              </p>
            </div>
          </div>

          <div className="grid grid-cols-1 gap-5 lg:grid-cols-2">
            {paineis.map((p) => (
              <div
                key={p.id}
                className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
              >
                {editandoId === p.id ? (
                  <div>
                    <div className="mb-5 flex items-center justify-between">
                      <div>
                        <h3 className="font-semibold text-slate-900">
                          Editar painel
                        </h3>

                        <p className="text-sm text-slate-500">
                          Atualize os dados do produto.
                        </p>
                      </div>

                      <button
                        type="button"
                        onClick={() => setEditandoId(null)}
                        className="rounded-lg p-2 text-slate-400 transition hover:bg-slate-100 hover:text-slate-700"
                      >
                        <X size={19} />
                      </button>
                    </div>

                    <div className="space-y-4">
                      <input
                        value={edicao.modelo}
                        onChange={(e) =>
                          setEdicao({
                            ...edicao,
                            modelo: e.target.value,
                          })
                        }
                        className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                        placeholder="Modelo"
                      />

                      <input
                        value={edicao.fabricante}
                        onChange={(e) =>
                          setEdicao({
                            ...edicao,
                            fabricante: e.target.value,
                          })
                        }
                        className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                        placeholder="Fabricante"
                      />

                      <input
                        type="number"
                        value={edicao.potenciaWp}
                        onChange={(e) =>
                          setEdicao({
                            ...edicao,
                            potenciaWp: e.target.value,
                          })
                        }
                        className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                        placeholder="Potência (W)"
                      />

                      <input
                        type="number"
                        step="0.01"
                        value={edicao.preco}
                        onChange={(e) =>
                          setEdicao({
                            ...edicao,
                            preco: e.target.value,
                          })
                        }
                        className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                        placeholder="Preço"
                      />

                      <div className="flex gap-2 pt-1">
                        <button
                          onClick={() => salvarEdicao(p)}
                          className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-slate-800"
                        >
                          <Save size={17} />
                          Salvar
                        </button>

                        <button
                          onClick={() => setEditandoId(null)}
                          className="inline-flex items-center gap-2 rounded-xl border border-slate-200 px-4 py-2.5 text-sm font-medium text-slate-600 transition hover:bg-slate-50"
                        >
                          <X size={17} />
                          Cancelar
                        </button>
                      </div>
                    </div>
                  </div>
                ) : (
                  <div>
                    <div className="mb-5 flex items-start justify-between gap-4">
                      <div className="flex items-center gap-3">
                        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-amber-100 text-amber-600">
                          <PanelsTopLeft size={21} />
                        </div>

                        <div>
                          <h3 className="font-bold text-slate-900">
                            {p.modelo}
                          </h3>

                          <p className="text-sm text-slate-500">
                            {p.fabricante}
                          </p>
                        </div>
                      </div>

                      <span
                        className={`rounded-full px-3 py-1 text-xs font-semibold ${
                          p.ativo
                            ? 'bg-emerald-50 text-emerald-600'
                            : 'bg-slate-100 text-slate-500'
                        }`}
                      >
                        {p.ativo ? 'Ativo' : 'Inativo'}
                      </span>
                    </div>

                    <div className="mb-5 grid grid-cols-2 gap-3">
                      <div className="rounded-xl bg-slate-50 p-4">
                        <p className="mb-1 text-xs font-medium text-slate-500">
                          Potência
                        </p>

                        <p className="text-lg font-bold text-slate-900">
                          {p.potenciaWp} W
                        </p>
                      </div>

                      <div className="rounded-xl bg-slate-50 p-4">
                        <p className="mb-1 text-xs font-medium text-slate-500">
                          Preço
                        </p>

                        <p className="text-lg font-bold text-slate-900">
                          {formatarMoeda(p.preco)}
                        </p>
                      </div>
                    </div>

                    <div className="flex gap-2 border-t border-slate-100 pt-4">
                      <button
                        onClick={() => iniciarEdicao(p)}
                        className="inline-flex items-center gap-2 rounded-xl border border-slate-200 px-4 py-2.5 text-sm font-medium text-slate-600 transition hover:bg-slate-50 hover:text-slate-900"
                      >
                        <Pencil size={17} />
                        Editar
                      </button>

                      <button
                        onClick={() => alternarAtivo(p)}
                        className={`inline-flex items-center gap-2 rounded-xl px-4 py-2.5 text-sm font-medium transition ${
                          p.ativo
                            ? 'bg-red-50 text-red-600 hover:bg-red-100'
                            : 'bg-emerald-50 text-emerald-600 hover:bg-emerald-100'
                        }`}
                      >
                        <Power size={17} />
                        {p.ativo ? 'Desativar' : 'Ativar'}
                      </button>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </section>
      </div>
    </div>
  );
}