package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.ComposicaoTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FrontendController {

    private final TimeRepository timeRepository;
    private final IntegranteRepository integranteRepository;
    private final ComposicaoTimeRepository composicaoTimeRepository;
    private final ApiService apiService;


    public FrontendController(TimeRepository timeRepository, IntegranteRepository integranteRepository, ComposicaoTimeRepository composicaoTimeRepository, ApiService apiService) {
        this.timeRepository = timeRepository;
        this.integranteRepository = integranteRepository;
        this.composicaoTimeRepository = composicaoTimeRepository;
        this.apiService = apiService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/times")
    public String times(Model model) {
        List<Time> times = timeRepository.findAll();
        List<Integrante> integrantes = integranteRepository.findAll(); // Adicionar os integrantes disponíveis
        model.addAttribute("times", times);
        model.addAttribute("integrantes", integrantes); // Enviar a lista de integrantes ao template
        return "times";
    }

    @PostMapping("/times/save")
    public String saveTime(@RequestParam("data") String data, @RequestParam("integrantes[]") List<Long> integranteIds) {
        // Criar novo time
        Time time = new Time();
        time.setData(LocalDate.parse(data));
        timeRepository.save(time);

        // Adicionar os integrantes selecionados ao time
        List<Integrante> integrantes = integranteRepository.findAllById(integranteIds);
        List<ComposicaoTime> composicoes = integrantes.stream()
                .map(integrante -> new ComposicaoTime(null, time, integrante))
                .collect(Collectors.toList());
        composicaoTimeRepository.saveAll(composicoes);

        return "redirect:/times";
    }


    @GetMapping("/integrantes")
    public String integrantes(Model model) {
        List<Integrante> integrantes = integranteRepository.findAll();
        model.addAttribute("integrantes", integrantes);
        return "integrantes";
    }

    @PostMapping("/integrantes/save")
    public String saveIntegrante(
            @RequestParam("nome") String nome,
            @RequestParam("franquia") String franquia,
            @RequestParam("funcao") String funcao) {
        Integrante integrante = new Integrante();
        integrante.setNome(nome);
        integrante.setFranquia(franquia);
        integrante.setFuncao(funcao);
        integranteRepository.save(integrante);
        return "redirect:/integrantes";
    }

    @GetMapping("/integrantes/delete/{id}")
    public String deleteIntegrante(@PathVariable Long id) {
        integranteRepository.deleteById(id);
        return "redirect:/integrantes";
    }

    @PostMapping("/times/delete/{id}")
    public String deleteTime(@PathVariable Long id) {
        // Excluir o time pelo ID
        timeRepository.deleteById(id);
        return "redirect:/times";
    }

    @GetMapping("/integrantes/mais-usado")
    public String getIntegranteMaisUsado(
            @RequestParam(value = "dataInicial", required = false) String dataInicial,
            @RequestParam(value = "dataFinal", required = false) String dataFinal,
            Model model) {

        boolean resultadoPresente = dataInicial != null && dataFinal != null;
        model.addAttribute("resultadoPresente", resultadoPresente);

        if (resultadoPresente) {
            LocalDate dataInicio = LocalDate.parse(dataInicial);
            LocalDate dataFim = LocalDate.parse(dataFinal);

            // Carregar todos os times
            List<Time> todosOsTimes = timeRepository.findAll();

            // Chamar o método integranteMaisUsado
            Integrante integranteMaisUsado = apiService.integranteMaisUsado(dataInicio, dataFim, todosOsTimes);

            // Adicionar apenas o integrante mais usado ao modelo
            model.addAttribute("integranteMaisUsado", integranteMaisUsado);
        }

        return "integrantes";
    }


    @GetMapping("/integrantes/time-mais-comum")
    public String getIntegrantesDoTimeMaisComum(
            @RequestParam("dataInicial") String dataInicial,
            @RequestParam("dataFinal") String dataFinal,
            Model model) {

        LocalDate dataInicio = LocalDate.parse(dataInicial);
        LocalDate dataFim = LocalDate.parse(dataFinal);

        // Carregar todos os times
        List<Time> todosOsTimes = timeRepository.findAll();

        // Chamar o método integrantesDoTimeMaisComum
        List<String> integrantesTimeMaisComum = apiService.integrantesDoTimeMaisComum(dataInicio, dataFim, todosOsTimes);

        // Adicionar os resultados ao modelo
        model.addAttribute("integrantesTimeMaisComum", integrantesTimeMaisComum);
        model.addAttribute("resultadoPresente", true);

        return "integrantes";
    }

    @GetMapping("/times/filter-date")
    public String filterTimeByDate(@RequestParam("data") String data, Model model) {
        LocalDate date = LocalDate.parse(data);

        // Carregar todos os times
        List<Time> todosOsTimes = timeRepository.findAll();

        // Usar o método timeDaData
        Time timeEncontrado = apiService.timeDaData(date, todosOsTimes);

        // Adicionar ao modelo
        model.addAttribute("timeEncontrado", timeEncontrado);
        model.addAttribute("buscaRealizada", true); // Indica que o filtro foi usado
        model.addAttribute("times", timeRepository.findAll()); // Atualiza lista de times
        model.addAttribute("integrantes", integranteRepository.findAll()); // Atualiza lista de integrantes
        return "times";
    }



}
