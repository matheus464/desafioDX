package br.com.duxusdesafio.config;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.ComposicaoTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Configuration
@Profile("dev") // Essa configuração será executada apenas no perfil 'dev'
public class DataInitializer implements CommandLineRunner {

    private final IntegranteRepository integranteRepository;
    private final TimeRepository timeRepository;
    private final ComposicaoTimeRepository composicaoTimeRepository;

    public DataInitializer(IntegranteRepository integranteRepository,
                           TimeRepository timeRepository,
                           ComposicaoTimeRepository composicaoTimeRepository) {
        this.integranteRepository = integranteRepository;
        this.timeRepository = timeRepository;
        this.composicaoTimeRepository = composicaoTimeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Cria Integrantes
        Integrante integrante1 = new Integrante(null, "Futebol", "João Silva", "Atacante");
        Integrante integrante2 = new Integrante(null, "Futebol", "Carlos Santos", "Meia");
        Integrante integrante3 = new Integrante(null, "Futebol", "Rafael Souza", "Goleiro");
        Integrante integrante4 = new Integrante(null, "Valorant", "Ana Pereira", "Sniper");
        Integrante integrante5 = new Integrante(null, "Free Fire", "Beatriz Lima", "Atirador");

        integranteRepository.save(integrante1);
        integranteRepository.save(integrante2);
        integranteRepository.save(integrante3);
        integranteRepository.save(integrante4);
        integranteRepository.save(integrante5);

        // Cria Times
        Time time1 = new Time(null, LocalDate.of(2024, 11, 11));
        Time time2 = new Time(null, LocalDate.of(2024, 11, 12));

        timeRepository.save(time1);
        timeRepository.save(time2);

        // Cria Composições de Time
        composicaoTimeRepository.save(new ComposicaoTime(null, time1, integrante1));
        composicaoTimeRepository.save(new ComposicaoTime(null, time1, integrante2));
        composicaoTimeRepository.save(new ComposicaoTime(null, time1, integrante3));
        composicaoTimeRepository.save(new ComposicaoTime(null, time2, integrante4));
        composicaoTimeRepository.save(new ComposicaoTime(null, time2, integrante5));
    }
}
