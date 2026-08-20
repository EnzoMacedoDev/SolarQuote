package br.com.solarquote.dto;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String local;

    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(Long id, String nome, String local) {
        this.id = id;
        this.nome = nome;
        this.local = local;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLocal() {
        return local;
    }
}
