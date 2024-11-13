package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.ComposicaoTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    public String getTimes(Model model) {
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
            model.addAttribute("resultadoPresente", true);
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
        model.addAttribute("timeMaisComumPresente", true);

        return "integrantes";
    }

    @GetMapping("/times/filter-by-date")
    public String filterTimesByDate(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            Model model) {
        List<Time> timesFiltrados = timeRepository.findByDataBetween(dataInicial, dataFinal);
        model.addAttribute("times", timesFiltrados); // Adiciona os times filtrados ao modelo
        model.addAttribute("buscaPorData", true); // Indica que a busca foi realizada
        return "times";
    }



    //Metódo para buscar função mais comum dos times em um período.
    @GetMapping("/times/filter-most-common-role")
    public String filtrarFuncaoMaisComum(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            Model model) {
        // Buscar todos os times no período
        List<Time> timesFiltrados = timeRepository.findByDataBetween(dataInicial, dataFinal);

        // Calcular a função mais comum
        List<ComposicaoTime> composicoes = timesFiltrados.stream()
                .flatMap(time -> time.getComposicoes().stream())
                .toList();

        String funcaoMaisComum = composicoes.stream()
                .map(composicao -> composicao.getIntegrante().getFuncao())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nenhuma função encontrada");

        model.addAttribute("funcaoMaisComum", funcaoMaisComum);
        model.addAttribute("exibirContagemFuncoes", false); // Não exibe o totalizador
        model.addAttribute("exibirFuncaoMaisComum", true); // Exibe a função mais comum
        return "times";
    }

    //Metódo para buscar a franquia mais famosa dentro de um perido.
    @GetMapping("/integrantes/filter-most-famous-franchise")
    public String filterMostFamousFranchise(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            Model model) {
        // Buscar todos os times no período
        List<Time> todosOsTimes = timeRepository.findByDataBetween(dataInicial, dataFinal);

        // Determinar a franquia mais famosa
        String franquiaMaisFamosa = apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes);

        // Adicionar ao modelo
        model.addAttribute("franquiaMaisFamosa", franquiaMaisFamosa);
        model.addAttribute("buscaPorFranquia", true);
        return "integrantes";
    }


    //Metódo que Vai retornar o número (quantidade) de Franquias dentro do período.
    @GetMapping("/integrantes/count-franchises")
    public String countFranchises(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            Model model) {
        // Buscar todos os times no período
        List<Time> todosOsTimes = timeRepository.findByDataBetween(dataInicial, dataFinal);

        // Contar franquias
        Map<String, Long> contagemFranquias = apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes);

        // Adicionar os resultados ao modelo
        model.addAttribute("contagemFranquias", contagemFranquias);
        model.addAttribute("buscaPorContagem", true);
        return "integrantes";
    }


    //Metódo que Vai retornar o número (quantidade) de Funções dentro do período.
    @GetMapping("/times/count-functions")
    public String countFunctions(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            Model model) {
        // Buscar todos os times no período
        List<Time> todosOsTimes = timeRepository.findByDataBetween(dataInicial, dataFinal);

        // Contar funções
        Map<String, Long> contagemFuncoes = apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);

        // Adicionar os resultados ao modelo
        model.addAttribute("contagemFuncoes", contagemFuncoes); // Contagem das funções
        model.addAttribute("exibirContagemFuncoes", true); // Indica que a busca foi realizada
        model.addAttribute("exibirFuncaoMaisComum", false);
        return "times"; // Retorna para o template `times.html`
    }


}
