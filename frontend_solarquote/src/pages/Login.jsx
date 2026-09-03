import { useState } from "react";
import { useNavigate } from "react-router-dom";
import http from "../api/http";
import { useAuth } from "../context/AuthContext";
import { Sun, User, Lock, ArrowRight } from "lucide-react";
export default function Login() {
  const [username, setUsername] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const navigate = useNavigate();
  const { login } = useAuth();
  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    try {
      const resposta = await http.post("/auth/login", { username, senha });
      login(resposta.data.token);
      navigate("/dashboard");
    } catch (err) {
      setErro("Usuário ou senha inválidos");
    }
  }
  return (
    <div className="flex min-h-screen bg-slate-950">
      {" "}
      {/* Lado esquerdo */}{" "}
      <div className="hidden flex-1 flex-col justify-between p-12 lg:flex">
        {" "}
        <div className="flex items-center gap-3">
          {" "}
          <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-amber-500 text-slate-950">
            {" "}
            <Sun size={24} strokeWidth={2.5} />{" "}
          </div>{" "}
          <h1 className="text-xl font-bold tracking-tight text-white">
            {" "}
            Solar<span className="text-amber-400">Quote</span>{" "}
          </h1>{" "}
        </div>{" "}
        <div className="max-w-lg">
          {" "}
          <p className="mb-4 text-sm font-medium text-amber-400">
            {" "}
            Gestão de orçamentos{" "}
          </p>{" "}
          <h2 className="text-5xl font-bold leading-tight tracking-tight text-white">
            {" "}
            Simplifique seus <br />{" "}
            <span className="text-slate-400"> orçamentos solares. </span>{" "}
          </h2>{" "}
          <p className="mt-6 max-w-md text-base leading-relaxed text-slate-400">
            {" "}
            Gerencie seus painéis, clientes e orçamentos de forma simples e
            organizada.{" "}
          </p>{" "}
        </div>{" "}
        <p className="text-xs text-slate-600">
          {" "}
          SolarQuote • Gestão de orçamentos{" "}
        </p>{" "}
      </div>{" "}
      {/* Lado direito */}{" "}
      <div className="flex w-full items-center justify-center bg-slate-50 px-6 py-10 lg:w-[520px] lg:px-12">
        {" "}
        <div className="w-full max-w-sm">
          {" "}
          {/* Logo mobile */}{" "}
          <div className="mb-10 flex items-center gap-3 lg:hidden">
            {" "}
            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-amber-500 text-slate-950">
              {" "}
              <Sun size={24} strokeWidth={2.5} />{" "}
            </div>{" "}
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              {" "}
              Solar<span className="text-amber-500">Quote</span>{" "}
            </h1>{" "}
          </div>{" "}
          {/* Título */}{" "}
          <div className="mb-8">
            {" "}
            <p className="mb-2 text-sm font-medium text-amber-600">
              {" "}
              Bem-vindo de volta{" "}
            </p>{" "}
            <h2 className="text-3xl font-bold tracking-tight text-slate-900">
              {" "}
              Entrar na sua conta{" "}
            </h2>{" "}
            <p className="mt-2 text-sm text-slate-500">
              {" "}
              Informe seus dados para acessar o sistema.{" "}
            </p>{" "}
          </div>{" "}
          {/* Card */}{" "}
          <div className="rounded-2xl border border-slate-200 bg-white p-7 shadow-sm">
            {" "}
            <form onSubmit={handleSubmit} className="space-y-5">
              {" "}
              {/* Usuário */}{" "}
              <div>
                {" "}
                <label className="mb-2 block text-sm font-medium text-slate-700">
                  {" "}
                  Usuário{" "}
                </label>{" "}
                <div className="relative">
                  {" "}
                  <User
                    size={19}
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
                  />{" "}
                  <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className="w-full rounded-xl border border-slate-200 bg-slate-50 py-3 pl-11 pr-4 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                    placeholder="Digite seu usuário"
                  />{" "}
                </div>{" "}
              </div>{" "}
              {/* Senha */}{" "}
              <div>
                {" "}
                <label className="mb-2 block text-sm font-medium text-slate-700">
                  {" "}
                  Senha{" "}
                </label>{" "}
                <div className="relative">
                  {" "}
                  <Lock
                    size={19}
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
                  />{" "}
                  <input
                    type="password"
                    value={senha}
                    onChange={(e) => setSenha(e.target.value)}
                    className="w-full rounded-xl border border-slate-200 bg-slate-50 py-3 pl-11 pr-4 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-amber-500 focus:bg-white focus:ring-4 focus:ring-amber-500/10"
                    placeholder="Digite sua senha"
                  />{" "}
                </div>{" "}
              </div>{" "}
              {/* Erro */}{" "}
              {erro && (
                <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm font-medium text-red-600">
                  {" "}
                  {erro}{" "}
                </div>
              )}{" "}
              {/* Botão */}{" "}
              <button
                type="submit"
                className="flex w-full items-center justify-center gap-2 rounded-xl bg-amber-500 px-5 py-3.5 text-sm font-semibold text-slate-950 shadow-lg shadow-amber-500/20 transition-all hover:bg-amber-400 hover:shadow-xl hover:shadow-amber-500/25 active:scale-[0.98]"
              >
                {" "}
                Entrar <ArrowRight size={18} strokeWidth={2.5} />{" "}
              </button>{" "}
            </form>{" "}
          </div>{" "}
          <p className="mt-6 text-center text-xs text-slate-400">
            {" "}
            Acesso seguro ao SolarQuote{" "}
          </p>{" "}
        </div>{" "}
      </div>{" "}
    </div>
  );
}
