//package main;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.client.RestTemplate;
//import ru.practicum.ewm.client.stats.StatsClient;
//import ru.practicum.ewm.dto.stats.EndpointHitDto;
//import ru.practicum.ewm.dto.stats.HttpRequestDto;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.util.Map;
//
//public class Main {
//
//    //класс для проверки работы клиента StatsClient
//    // todo удалить после окончания работы над main-service
//    public static void main(String[] args) {
//        StatsClient client = new StatsClient(new RestTemplate());
//        HttpRequestDto httpRequestDto = new HttpRequestDto();
//        HttpServletRequest request = new HttpServletRequestWrapper(new Http);
//
//        client.saveHit("http://localhost:9090/hit", );
//
//        String start = "2020-05-05 00:00:00";
//        String end = "2035-05-05 00:00:00";
//        String unique = "true";
//        String[] uris = new String[]{"/events"};
//        Map<String, Object> parameters = Map.of(
//                "start", start,
//                "end", end,
//                "uris", uris,
//                "unique", unique
//        );
//        ResponseEntity<Object> responseEntity = client.get("http://localhost:9090/stats?start={start}&uris={uris}&end={end}&unique={unique}", parameters);
//        System.out.println(responseEntity);
//    }
//}
