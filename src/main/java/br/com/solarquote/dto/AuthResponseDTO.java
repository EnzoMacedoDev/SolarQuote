package br.com.solarquote.dto;

public class AuthResponseDTO {

    private String mensagem;
    private Long usuarioId;
    private String nome;
    private String email;
    private String token;

    public AuthResponseDTO(
            String mensagem,
            Long usuarioId,
            String nome,
            String email,
            String token) {

        this.mensagem = mensagem;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
        this.token = token;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}