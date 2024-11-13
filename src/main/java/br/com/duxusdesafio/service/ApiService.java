package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service que possuirá as regras de negócio para o processamento dos dados solicitados no desafio!
 */
@Service
public class ApiService {

    /**
     * Vai retornar o integrante que estiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return todosOsTimes.stream()
                .filter(time -> isDataDentroDoPeriodo(time.getData(), dataInicial, dataFinal))
                .flatMap(time -> time.getComposicoes().stream())
                .map(ComposicaoTime::getIntegrante)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais comum
     * dentro do período
     */
    public List<String> integrantesDoTimeMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return todosOsTimes.stream()
                .filter(time -> isDataDentroDoPeriodo(time.getData(), dataInicial, dataFinal))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(time -> time.getComposicoes().stream()
                        .map(composicao -> composicao.getIntegrante().getNome())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        return todosOsTimes.stream()
                .filter(time -> isDataDentroDoPeriodo(time.getData(), dataInicial, dataFinal))
                .flatMap(time -> time.getComposicoes().stream())
                .map(composicao -> composicao.getIntegrante().getFranquia())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    /**
     * Vai retornar o número (quantidade) de Franquias dentro do período
     */
    public Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return todosOsTimes.stream()
                .filter(time -> isDataDentroDoPeriodo(time.getData(), dataInicial, dataFinal))
                .flatMap(time -> time.getComposicoes().stream())
                .map(composicao -> composicao.getIntegrante().getFranquia())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return todosOsTimes.stream()
                .filter(time -> isDataDentroDoPeriodo(time.getData(), dataInicial, dataFinal))
                .flatMap(time -> time.getComposicoes().stream())
                .map(composicao -> composicao.getIntegrante().getFuncao())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private boolean isDataDentroDoPeriodo(LocalDate data, LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial != null && data.isBefore(dataInicial)) {
            return false;
        }
        return dataFinal == null || !data.isAfter(dataFinal);
    }
}
