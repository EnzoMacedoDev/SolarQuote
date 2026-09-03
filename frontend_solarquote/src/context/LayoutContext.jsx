import { createContext, useContext, useState } from 'react';
 
const LayoutContext = createContext(null);
 
export function LayoutProvider({ children }) {
  const [novoOrcamentoAberto, setNovoOrcamentoAberto] = useState(false);
  const [versaoOrcamentos, setVersaoOrcamentos] = useState(0);
 
  function abrirNovoOrcamento() {
    setNovoOrcamentoAberto(true);
  }
 
  function fecharNovoOrcamento() {
    setNovoOrcamentoAberto(false);
  }
 
  function notificarOrcamentoCriado() {
    setVersaoOrcamentos((v) => v + 1);
  }
 
  return (
    <LayoutContext.Provider
      value={{
        novoOrcamentoAberto,
        abrirNovoOrcamento,
        fecharNovoOrcamento,
        versaoOrcamentos,
        notificarOrcamentoCriado,
      }}
    >
      {children}
    </LayoutContext.Provider>
  );
}
 
export function useLayout() {
  return useContext(LayoutContext);
}