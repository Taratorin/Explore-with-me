package ru.practicum.ewm.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.ewm.dto.stats.ViewStatsDto(app, uri, count(ip)) " +
            "from EndpointHit " +
            "where uri in (?1) " +
            "and ts between ?2 and ?3 " +
            "group by app, uri order by count(ip) desc")
    List<ViewStatsDto> findByUriAllIp(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.dto.stats.ViewStatsDto(app, uri, count(ip)) " +
            "from EndpointHit " +
            "where ts between ?1 and ?2 " +
            "group by app, uri order by count(ip) desc")
    List<ViewStatsDto> findByEmptyUriAllIp(LocalDateTime start, LocalDateTime end);


    @Query("select new ru.practicum.ewm.dto.stats.ViewStatsDto(app, uri, count(distinct ip)) " +
            "from EndpointHit " +
            "where uri = ?1 " +
            "and ts between ?2 and ?3 " +
            "group by app, uri order by count(distinct ip) desc")
    List<ViewStatsDto> findByUriDistinctIp(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.dto.stats.ViewStatsDto(app, uri, count(distinct ip)) " +
            "from EndpointHit " +
            "where ts between ?1 and ?2 " +
            "group by app, uri order by count(distinct ip) desc")
    List<ViewStatsDto> findByEmptyUriDistinctIp(LocalDateTime start, LocalDateTime end);

}
