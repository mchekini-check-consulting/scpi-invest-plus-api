package fr.formationacademy.scpiinvestplusapi.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDocumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScpiIndexService {

    private final ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "scpi";

    public ScpiIndexService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<ScpiDocumentDTO> searchScpi(String name, BigDecimal distributionRate, Integer minimumSubscription, Boolean subscriptionFees, String frequencyPayment, List<String> locations, List<String> sectors) throws IOException {

        List<ScpiDocumentDTO> scpiList  = new ArrayList<>();

        boolean noFilter = (name == null || name.trim().isEmpty()) &&
                distributionRate == null &&
                minimumSubscription == null &&
                subscriptionFees == null &&
                (frequencyPayment == null || frequencyPayment.trim().isEmpty()) &&
                (locations == null || locations.isEmpty()) &&
                (sectors == null || sectors.isEmpty());

        if (noFilter) {

            SearchResponse<ScpiDocumentDTO> response = elasticsearchClient.search(s -> s
                            .index(INDEX_NAME)
                            .size(52)
                            .query(q -> q.matchAll(m -> m)),
                    ScpiDocumentDTO.class);

            if (response.hits() != null) {
                scpiList = response.hits().hits().stream()
                        .map(hit -> hit.source())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            return scpiList;
        }

        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        if (name != null && !name.trim().isEmpty()) {
            log.info("Searching with name: {}", name);

            boolQuery.must(s -> s
                    .bool(b -> b
                            .should(sh -> sh
                                    .fuzzy(f -> f
                                            .field("name")
                                            .value(name)
                                            .fuzziness("2")
                                    )
                            )
                            .should(sh -> sh
                                    .wildcard(w -> w
                                            .field("name")
                                            .value("*" + name + "*")
                                    )
                            )
                    )
            );
        }

        if (distributionRate != null) {
            log.info("Searching with minimumSubscription: {}", distributionRate);
            boolQuery.filter(f -> f.range(r -> r
                    .term(t -> t.field("distributionRate")
                            .lte(String.valueOf(distributionRate))
                    )
            ));
        }

        if (minimumSubscription != null) {
            log.info("Searching with minimumSubscription: {}", minimumSubscription);
            boolQuery.filter(f -> f.range(r -> r
                    .term(t -> t.field("minimumSubscription")
                            .lte(String.valueOf(minimumSubscription))
                    )
            ));
        }

        if (subscriptionFees != null) {
            log.info("Searching with subscriptionFees: {}", subscriptionFees);
            if (subscriptionFees) {

                boolQuery.filter(f -> f.range(r -> r
                        .term(t -> t.field("subscriptionFeesBigDecimal")
                        .gt(String.valueOf(0))
                        )
                ));

            } else {

                boolQuery.filter(f -> f.term(t -> t
                        .field("subscriptionFeesBigDecimal")
                        .value(0)
                ));
            }
        }

        if (frequencyPayment != null && !frequencyPayment.trim().isEmpty()) {
            log.info("Searching with frequencyPayment: {}", frequencyPayment);
            boolQuery.must(m -> m.match(t -> t
                    .field("frequencyPayment")
                    .query(frequencyPayment.trim().toLowerCase())
            ));
        }

        if (locations != null && !locations.isEmpty()) {
            log.info("Searching with locations: {}", locations);
            for (String location : locations) {
                log.info("Searching with location: {}", location);
                boolQuery.filter(n -> n
                        .nested(nq -> nq
                                .path("locations")
                                .query(q -> q
                                        .match(m -> m
                                                .field("locations.country")
                                                .query(location.trim().toLowerCase())
                                        )
                                )
                        )
                );
            }
        }

        if (sectors != null && !sectors.isEmpty()) {
            log.info("Searching with sectors: {}", sectors);
            for (String sector : sectors) {
                boolQuery.filter(s -> s
                        .nested(nq -> nq
                                .path("sectors")
                                .query(q -> q
                                        .match(m -> m
                                                .field("sectors.name")
                                                .query(sector.trim().toLowerCase())
                                        )
                                )
                        )
                );
            }
        }
        SearchResponse<ScpiDocumentDTO> response = elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.bool(boolQuery.build()))
                , ScpiDocumentDTO.class);

        if (response.hits() != null) {
            scpiList = response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return scpiList;
    }

    public Object getAllScpi() {
        try {
            return elasticsearchClient.search(s -> s
                                    .index(INDEX_NAME)
                                    .query(q -> q.matchAll(m -> m)),
                            ScpiDocumentDTO.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(hit -> hit.source())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Erreur lors de la récupération des SCPI", e);
            return "Erreur lors de la récupération des données : " + e.getMessage();
        }
    }
}
