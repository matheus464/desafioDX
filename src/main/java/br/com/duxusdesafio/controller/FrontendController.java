package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FrontendController {

    private final TimeRepository timeRepository;
    private final IntegranteRepository integranteRepository;

    public FrontendController(TimeRepository timeRepository, IntegranteRepository integranteRepository) {
        this.timeRepository = timeRepository;
        this.integranteRepository = integranteRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/times")
    public String times(Model model) {
        List<Time> times = timeRepository.findAll();
        model.addAttribute("times", times);
        return "times";
    }

    @GetMapping("/integrantes")
    public String integrantes(Model model) {
        List<Integrante> integrantes = integranteRepository.findAll();
        model.addAttribute("integrantes", integrantes);
        return "integrantes";
    }
}
