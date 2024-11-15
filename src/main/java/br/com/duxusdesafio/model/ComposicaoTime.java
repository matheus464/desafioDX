package br.com.duxusdesafio.model;

import javax.persistence.*;

@Entity
@Table(name = "COMPOSICAO_TIME ")
public class ComposicaoTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relacionamento com Time
    @ManyToOne
    @JoinColumn(name = "id_time", nullable = false)
    private Time time;

    //Relacionamento com Integrante
    @ManyToOne
    @JoinColumn(name = "id_integrante", nullable = false)
    private Integrante integrante;

    public ComposicaoTime(Object o, Time time1, Integrante integrante2) {
        this.time = time1;
        this.integrante = integrante2;
    }

    public ComposicaoTime() {}


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Integrante getIntegrante() {
        return integrante;
    }

    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }
}
