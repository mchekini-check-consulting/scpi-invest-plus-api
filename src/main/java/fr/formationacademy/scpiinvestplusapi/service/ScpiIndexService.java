package fr.formationacademy.scpiinvestplusapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import fr.formationacademy.scpiinvestplusapi.dto.CriteriaIn;
import fr.formationacademy.scpiinvestplusapi.dto.CriteriaPoperties;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDocumentDTO;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSearchCriteriaDto;
import fr.formationacademy.scpiinvestplusapi.utils.Constants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScpiIndexService {

        private final ElasticsearchClient elasticsearchClient;
        private Map<String, Double> optimalValuesMap = new HashMap<>();
        private Map<String, CriteriaPoperties> CriteriaMap = new HashMap<>();
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

        /* Service de scoring */

        public void initOtimalValueMap() {
                optimalValuesMap.put("distributionRate", 8.35);
                optimalValuesMap.put("enjoymentDelay", 0.0);
                optimalValuesMap.put("managementCosts", 7.2);
                optimalValuesMap.put("subscriptionFeesBigDecimal", 0.0);
                optimalValuesMap.put("capitalization", 6200000000.0);
        }

        public void initCriteriaMap() {
                CriteriaMap.put("distributionRate", CriteriaPoperties.builder().ScoringType("FunctionScore")
                                .modifier(FieldValueFactorModifier.Sqrt).factor(2.0).build());
                CriteriaMap.put("enjoymentDelay",
                                CriteriaPoperties.builder().ScoringType("DecayFunctionScore").scale(4).decay(0.5)
                                                .build());
                CriteriaMap.put("managementCosts",
                                CriteriaPoperties.builder().ScoringType("FunctionScore")
                                                .modifier(FieldValueFactorModifier.Reciprocal).factor(1.5).build());
                CriteriaMap.put("subscriptionFeesBigDecimal",
                                CriteriaPoperties.builder().ScoringType("FunctionScore")
                                                .modifier(FieldValueFactorModifier.Reciprocal).factor(1.5).build());
                CriteriaMap.put("capitalization",
                                CriteriaPoperties.builder().ScoringType("RangeBonus").weight(1.5).limit(740000000L)
                                                .build());
        }

        @PostConstruct
        public void initData() {
                initCriteriaMap();
                initOtimalValueMap();
        }

        public List<ScpiDocumentDTO> searchScoredScpi(List<CriteriaIn> criterias) throws IOException {

                SearchRequest searchRequest = buildSearchRequest(criterias);
                System.out.println("la requete est : " + searchRequest.toString());
                SearchResponse<ScpiDocumentDTO> searchResponse = elasticsearchClient.search(searchRequest,
                                ScpiDocumentDTO.class);
                System.out.println("la reponse est : " + searchResponse.toString());

                System.out.println("je suis ici");
                return extractScpiFromResponse(searchResponse, criterias);
        }

        private SearchRequest buildSearchRequest(List<CriteriaIn> criterias) {
                return SearchRequest.of(s -> s
                                .index("scpi")
                                .query(buildFunctionScoreQuery(criterias))
                                .sort(SortOptions.of(so -> so.field(f -> f.field("_score").order(SortOrder.Desc))))
                                .size(100));
        }

        private Query buildFunctionScoreQuery(List<CriteriaIn> criterias) {
                return Query.of(q -> q.functionScore(fs -> fs
                                .query(qb -> qb.matchAll(ma -> ma))
                                .functions(getFunctionScores(criterias))
                                .boostMode(FunctionBoostMode.Sum)));
        }

        private List<FunctionScore> getFunctionScores(List<CriteriaIn> criterias) {

                List<FunctionScore> scores = new ArrayList<>();
                for (CriteriaIn criteria : criterias) {
                        System.out.println("voilaa" + criteria.getName());
                        switch (CriteriaMap.get(criteria.getName()).getScoringType()) {
                                case "FunctionScore":
                                        scores.add(createFunctionScore(criteria.getName(),
                                                        CriteriaMap.get(criteria.getName()).getModifier(),
                                                        CriteriaMap.get(criteria.getName()).getFactor(),
                                                        criteria.getFactor()));
                                        break;

                                case "DecayFunctionScore":
                                        scores.add(createDecayFunctionScore(criteria.getName(),
                                                        CriteriaMap.get(criteria.getName()).getScale(),
                                                        CriteriaMap.get(criteria.getName()).getDecay(),
                                                        criteria.getFactor()));
                                        break;

                                case "RangeBonus":
                                        scores.add(createRangeBonus(criteria.getName(),
                                                        CriteriaMap.get(criteria.getName()).getWeight(),
                                                        CriteriaMap.get(criteria.getName()).getLimit(),
                                                        criteria.getFactor()));
                                        break;

                                default:
                                        log.warn("Scoring type non pris en charge : "
                                                        + CriteriaMap.get(criteria.getName()).getScoringType());
                                        break;
                        }
                }
                return scores;
        }

        private FunctionScore createFunctionScore(String field, FieldValueFactorModifier modifier, double factor,
                        double userFactor) {
                return FunctionScore.of(f -> f.fieldValueFactor(fn -> fn
                                .field(field)
                                .modifier(modifier)
                                .factor(factor * userFactor)));
        }

        private FunctionScore createDecayFunctionScore(String field, double scale,
                        double decay, double userFactor) {

                try {
                        FunctionScore fs = FunctionScore.of(f -> f
                                        .weight(userFactor)
                                        .exp(e -> e.numeric(
                                                        a -> a.field(field).placement(p -> p.scale(scale)
                                                                        .origin(0.0).decay(decay)))));

                        return fs;
                } catch (Exception e) {
                        log.info("erreur : ", e);
                }
                return null;
        }

        private FunctionScore createRangeBonus(String field, double baseWeight, long limit, double multiplier) {
                return FunctionScore.of(f -> f
                                .scriptScore(ss -> ss
                                                .script(Script.of(s -> s
                                                                .source("doc[params.field].value >= params.limit ? params.baseWeight * params.multiplier : params.multiplier")
                                                                .params("baseWeight", JsonData.of(baseWeight))
                                                                .params("multiplier", JsonData.of(multiplier))
                                                                .params("field", JsonData.of(field))
                                                                .params("limit", JsonData.of(limit))))));
        }

        private List<ScpiDocumentDTO> extractScpiFromResponse(SearchResponse<ScpiDocumentDTO> searchResponse,
                        List<CriteriaIn> criteriaList) {
                List<ScpiDocumentDTO> result = new ArrayList<>();
                List<Double> scores = new ArrayList<>();

                for (Hit<ScpiDocumentDTO> hit : searchResponse.hits().hits()) {
                        ScpiDocumentDTO scpi = hit.source();
                        if (scpi != null) {
                                result.add(scpi);
                                System.out.println("le score ajoue : " + hit.score());
                                scores.add(hit.score());
                        }
                }

                return calculateMashedScore(result, scores, criteriaList);
        }

        public List<ScpiDocumentDTO> calculateMashedScore(List<ScpiDocumentDTO> scpiList,
                        List<Double> elasticScores,
                        List<CriteriaIn> criteriaList) {
                System.out.println("je suis dans calculatedmashedscore : ");
                double optimalScore = calculateElasticLikeScore(criteriaList);

                for (int i = 0; i < scpiList.size(); i++) {
                        ScpiDocumentDTO scpi = scpiList.get(i);
                        double score = elasticScores.get(i);

                        double mashedScore = optimalScore != 0 ? Math.min(score / optimalScore, 1.0) : 0.0;
                        System.out.println("mashedScore : " + mashedScore);
                        scpi.setMatchedScore(mashedScore);
                }

                return scpiList;
        }

        public double calculateElasticLikeScore(List<CriteriaIn> criteriaList) {
                System.out.println("je suis dans calculated elastic like score");
                double totalScore = 0.0;

                for (CriteriaIn criteria : criteriaList) {
                        String criteriaName = criteria.getName();
                        double userFactor = criteria.getFactor();

                        Double optimalValue = optimalValuesMap.get(criteriaName);
                        CriteriaPoperties properties = CriteriaMap.get(criteriaName);

                        if (optimalValue != null && properties != null) {
                                double modifiedValue = optimalValue;

                                switch (properties.getScoringType()) {
                                        case "FunctionScore":
                                                // Seulement sqrt est utilisé
                                                modifiedValue = Math.sqrt(optimalValue);
                                                modifiedValue *= properties.getFactor() * userFactor;
                                                break;

                                        case "DecayFunctionScore":
                                                // Reproduction d’une decay function d’Elasticsearch
                                                double decay = properties.getDecay();
                                                double scale = properties.getScale();
                                                double decayScore = Math
                                                                .exp(-Math.pow(optimalValue / scale, 2 * decay));
                                                modifiedValue = decayScore * userFactor;
                                                break;

                                        case "RangeBonus":
                                                if (optimalValue >= properties.getLimit()) {
                                                        modifiedValue = properties.getWeight() * userFactor;
                                                } else {
                                                        modifiedValue = userFactor;
                                                }
                                                break;

                                        default:
                                                // Au cas où un scoringType inconnu arrive
                                                modifiedValue = userFactor;
                                                break;
                                }

                                totalScore += modifiedValue;
                        } else {
                                log.warn("Missing optimal value or properties for criteria: {}", criteriaName);
                        }
                }

                return totalScore;
        }

}