//package ru.practicum.config;
//
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.util.DefaultUriBuilderFactory;
//import ru.practicum.ewm.client.stats.StatsClient;
//
//@Configuration
//public class StatsClientConfig {
//
//    @Bean
//    public StatsClient statsClient() {
//        RestTemplateBuilder builder = new RestTemplateBuilder();
//        builder.uriTemplateHandler(new DefaultUriBuilderFactory("${stats-server.url}"))
//                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                .build();
//        return new StatsClient("${stats-server.url}", builder);
//    }
//}
