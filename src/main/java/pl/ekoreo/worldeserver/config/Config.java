package pl.ekoreo.worldeserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.ekoreo.worldeserver.services.GameManager;

@Configuration
public class Config implements WebMvcConfigurer {
    @Bean
    public GameManager gameManager(){
        return new GameManager(10, true);
    }
    @Override
    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").exposedHeaders("Access-Control-Allow-Origin");
    }
}
