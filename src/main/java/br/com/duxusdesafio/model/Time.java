package br.com.duxusdesafio.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComposicaoTime> composicoes;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ComposicaoTime> getComposicoes() {
        return composicoes;
    }

    public void setComposicoes(List<ComposicaoTime> composicoes) {
        this.composicoes = composicoes;
    }
}
