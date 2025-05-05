package fr.formationacademy.scpiinvestplusapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDocumentDTO;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSearchCriteriaDto;
import fr.formationacademy.scpiinvestplusapi.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScpiIndexService {

        private final ElasticsearchClient elasticsearchClient;
        private static final String INDEX_NAME = "scpi";

        public ScpiIndexService(ElasticsearchClient elasticsearchClient) {
                this.elasticsearchClient = elasticsearchClient;
        }

        public List<ScpiDocumentDTO> searchScpi(ScpiSearchCriteriaDto criteria) throws IOException {
                List<ScpiDocumentDTO> scpiList = new ArrayList<>();

                if (criteria.hasNoFilters()) {
                        SearchResponse<ScpiDocumentDTO> response = elasticsearchClient.search(s -> s
                                        .index(INDEX_NAME)
                                        .size(Constants.DEFAULT_RESULT_SIZE)
                                        .query(q -> q.matchAll(m -> m)),
                                        ScpiDocumentDTO.class);

                        if (response.hits() != null) {
                                scpiList = response.hits().hits().stream()
                                                .map(hit -> hit.source())
                                                .filter(Objects::nonNull)
                                                .sorted(Comparator.comparing(ScpiDocumentDTO::getDistributionRate)
                                                                .reversed())
                                                .collect(Collectors.toList());
                        }
                        return scpiList;
                }

                BoolQuery.Builder boolQuery = new BoolQuery.Builder();

                if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
                        log.info("Searching with name: {}", criteria.getName());
                        boolQuery.must(s -> s
                                        .bool(b -> b
                                                        .should(sh -> sh
                                                                        .multiMatch(m -> m
                                                                                        .query(criteria.getName())
                                                                                        .fields("name")
                                                                                        .fuzziness("2")
                                                                                        .operator(Operator.And)))
                                                        .should(sh -> sh
                                                                        .wildcard(w -> w
                                                                                        .field("name.keyword")
                                                                                        .value("*" + criteria.getName()
                                                                                                        + "*")
                                                                                        .caseInsensitive(true)))));
                }

                if (criteria.getDistributionRate() != null) {
                        log.info("Searching with distributionRate: {}", criteria.getDistributionRate());
                        boolQuery.filter(f -> f.range(r -> r
                                        .term(t -> t.field("distributionRate")
                                                        .gte(String.valueOf(criteria.getDistributionRate())))));
                }

                if (criteria.getMinimumSubscription() != null) {
                        log.info("Searching with minimumSubscription: {}", criteria.getMinimumSubscription());
                        boolQuery.filter(f -> f.range(r -> r
                                        .term(t -> t.field("minimumSubscription")
                                                        .lte(String.valueOf(criteria.getMinimumSubscription())))));
                }

                if (criteria.getSubscriptionFees() != null) {
                        log.info("Searching with subscriptionFees: {}", criteria.getSubscriptionFees());
                        if (criteria.getSubscriptionFees()) {
                                boolQuery.filter(f -> f.range(r -> r
                                                .term(t -> t.field("subscriptionFeesBigDecimal")
                                                                .gt(String.valueOf(0))

                                                )));
                        } else {
                                boolQuery.filter(f -> f.term(t -> t
                                                .field("subscriptionFeesBigDecimal")
                                                .value(0)));
                        }
                }

                if (criteria.getFrequencyPayment() != null && !criteria.getFrequencyPayment().trim().isEmpty()) {
                        log.info("Searching with frequencyPayment: {}", criteria.getFrequencyPayment());
                        boolQuery.must(m -> m.match(t -> t
                                        .field("frequencyPayment")
                                        .query(criteria.getFrequencyPayment().trim().toLowerCase())));
                }

                if (criteria.getLocations() != null && !criteria.getLocations().isEmpty()) {
                        log.info("Searching with locations: {}", criteria.getLocations());
                        for (String location : criteria.getLocations()) {
                                log.info("Searching with location: {}", location);
                                boolQuery.filter(n -> n
                                                .nested(nq -> nq
                                                                .path("locations")
                                                                .query(q -> q
                                                                                .match(m -> m
                                                                                                .field("locations.country")
                                                                                                .query(location.trim()
                                                                                                                .toLowerCase())))));
                        }
                }

                if (criteria.getSectors() != null && !criteria.getSectors().isEmpty()) {
                        log.info("Searching with sectors: {}", criteria.getSectors());
                        for (String sector : criteria.getSectors()) {
                                boolQuery.filter(s -> s
                                                .nested(nq -> nq
                                                                .path("sectors")
                                                                .query(q -> q
                                                                                .match(m -> m
                                                                                                .field("sectors.name")
                                                                                                .query(sector.trim()
                                                                                                                .toLowerCase())))));
                        }
                }

                SearchResponse<ScpiDocumentDTO> response = elasticsearchClient.search(s -> s
                                .index(INDEX_NAME)
                                .size(Constants.DEFAULT_RESULT_SIZE)
                                .query(q -> q.bool(boolQuery.build())),
                                ScpiDocumentDTO.class);

                if (response.hits() != null) {
                        scpiList = response.hits().hits().stream()
                                        .map(hit -> hit.source())
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());
                }
                return scpiList;
        }

        public List<ScpiDocumentDTO> getScpisWithScheduledPayment() throws IOException {
                List<ScpiDocumentDTO> scpiList = new ArrayList<>();

                SearchResponse<ScpiDocumentDTO> response = elasticsearchClient.search(s -> s
                                .index(INDEX_NAME)
                                .size(Constants.DEFAULT_RESULT_SIZE)
                                .query(q -> q
                                                .term(t -> t
                                                                .field("scheduledPayment")
                                                                .value(false))),
                                ScpiDocumentDTO.class);

                if (response.hits() != null) {
                        scpiList = response.hits().hits().stream()
                                        .map(hit -> hit.source())
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());
                }

                return scpiList;
        }

}