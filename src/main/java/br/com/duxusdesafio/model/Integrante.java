package br.com.duxusdesafio.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Integrante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String franquia;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String funcao;

    // Relacionamento com ComposicaoTime
    @OneToMany(mappedBy = "integrante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComposicaoTime> composicoes;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFranquia() {
        return franquia;
    }

    public void setFranquia(String franquia) {
        this.franquia = franquia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public List<ComposicaoTime> getComposicoes() {
        return composicoes;
    }

    public void setComposicoes(List<ComposicaoTime> composicoes) {
        this.composicoes = composicoes;
    }
}
