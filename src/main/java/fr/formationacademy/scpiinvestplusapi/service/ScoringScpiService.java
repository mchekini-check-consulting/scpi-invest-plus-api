package fr.formationacademy.scpiinvestplusapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.JsonData;
import fr.formationacademy.scpiinvestplusapi.dto.CriteriaIn;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDocumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ScoringScpiService {

    private static final String INDEX_NAME = "scpi";
    private static final String INDEX_SCORING_NAME = "scpi_optimal";
    private static final String MAPPING_FILE_PATH = "src/main/resources/mappings/scpi-optimal-mapping.json";
    private final ElasticsearchClient elasticsearchClient;
    private final Map<String, Double> optimalValuesMap;
    private boolean InitDone = true;
    public ScoringScpiService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
        this.optimalValuesMap = initOptimalValueMap();
    }

    private Map<String, Double> initOptimalValueMap() {
        Map<String, Double> map = new HashMap<>();
        map.put("distributionRate", 8.35);
        map.put("enjoymentDelay", 0.0);
        map.put("managementCosts", 7.2);
        map.put("subscriptionFeesBigDecimal", 0.0);
        map.put("capitalization", 6294000000.0);
        return map;
    }

    private boolean indexExists(String indexName) throws IOException {
        return elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
    }

    private void createOptimalIndex() throws IOException {
        try (InputStream mappingStream = Files.newInputStream(Paths.get(MAPPING_FILE_PATH))) {
            CreateIndexResponse response = elasticsearchClient.indices()
                    .create(c -> c.index(INDEX_SCORING_NAME).withJson(mappingStream));

            if (response.acknowledged()) {
                log.info("Index '{}' created successfully", INDEX_SCORING_NAME);
            } else {
                log.error("Error creating index '{}'", INDEX_SCORING_NAME);
            }
        } catch (IOException e) {
            log.error("Error reading mapping file '{}'", MAPPING_FILE_PATH, e);
            throw e;
        }
    }

    public List<ScpiDocumentDTO> searchScoredScpi(List<CriteriaIn> criterias) throws IOException {

        SearchRequest searchRequest = buildSearchRequest(criterias);
        SearchResponse<ScpiDocumentDTO> response = elasticsearchClient.search(searchRequest,
                ScpiDocumentDTO.class);
        return extractScpiFromResponse(response, criterias);
    }

    private SearchRequest buildSearchRequest(List<CriteriaIn> criterias) {
        return SearchRequest.of(s -> s
                .index(INDEX_NAME)
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
            switch (criteria.getName()) {

                case "capitalization":
                    scores.add(createScoringFunction(criteria.getName(), 9.261e-11, 0.6845, criteria.getFactor(),
                            "(params.a * doc[params.field].value + params.b) * params.multiplier"));
                    break;
                case "distributionRate":
                    scores.add(createScoringFunction(criteria.getName(), 0.1478, -0.233, criteria.getFactor(),
                            "(params.a * doc[params.field].value + params.b) * params.multiplier"));
                    break;
                case "managementCosts":
                    scores.add(createScoringFunction(criteria.getName(), 164.67, 0.085, criteria.getFactor(),
                            "(params.a / doc[params.field].value + params.b) * params.multiplier"));
                    break;
                case "enjoymentDelay":
                    scores.add(createScoringFunction(criteria.getName(), 1.05, -0.05, criteria.getFactor(),
                            "(params.a / (doc[params.field].value + 1) + params.b) * params.multiplier"));
                    break;
                case "subscriptionFeesBigDecimal":
                    scores.add(createScoringFunction(criteria.getName(), 15.66, -0.08, criteria.getFactor(),
                            "(params.a / (doc[params.field].value +1 ) + params.b) * params.multiplier"));
                    break;
                default:
                    log.warn("Critere non pris en charge : "
                            + criteria.getName());
                    break;
            }
        }
        return scores;
    }

    private FunctionScore createScoringFunction(String field, double a, double b, double multiplier, String function) {
        return FunctionScore.of(f -> f
                .scriptScore(ss -> ss
                        .script(Script.of(s -> s
                                .source(function)
                                .params("field", JsonData.of(field))
                                .params("a", JsonData.of(a))
                                .params("b", JsonData.of(b))
                                .params("multiplier", JsonData.of(multiplier))))));
    }

    private List<ScpiDocumentDTO> extractScpiFromResponse(SearchResponse<ScpiDocumentDTO> searchResponse,
            List<CriteriaIn> criteriaList) throws IOException {
        List<ScpiDocumentDTO> result = new ArrayList<>();
        List<Double> scores = new ArrayList<>();

        for (Hit<ScpiDocumentDTO> hit : searchResponse.hits().hits()) {
            ScpiDocumentDTO scpi = hit.source();
            if (scpi != null) {
                result.add(scpi);
                scores.add(hit.score());
            }
        }

        return calculateMatchedScore(result, scores, criteriaList);
    }

    public List<ScpiDocumentDTO> calculateMatchedScore(List<ScpiDocumentDTO> scpiList,
            List<Double> elasticScores,
            List<CriteriaIn> criteriaList) throws IOException {
        if(InitDone){
            if (!indexExists(INDEX_SCORING_NAME)) {
                createOptimalIndex();
                InitDone = false;
            }
        }


        for (int i = 0; i < scpiList.size(); i++) {
            ScpiDocumentDTO scpi = scpiList.get(i);
            double score = elasticScores.get(i);
            double optimal_score = calculateOptimalScoreForScpi(scpi, criteriaList);
            double mashedScore = score / optimal_score;
            scpi.setMatchedScore(mashedScore);
        }

        return scpiList;
    }

    private double calculateOptimalScoreForScpi(ScpiDocumentDTO originalScpi, List<CriteriaIn> criterias)
            throws IOException {
        ScpiDocumentDTO optimalScpi = getOptimalSCPI(originalScpi, criterias);
        String tempId = persistOptimalSCPI(optimalScpi);
        SearchResponse<ScpiDocumentDTO> searchResponse = getSearchResponse(criterias);
        double optimalScore = getOptimalScore(searchResponse);
        deleteOptimalSCPI(tempId);
        return optimalScore;
    }

    private ScpiDocumentDTO getOptimalSCPI(ScpiDocumentDTO originalScpi, List<CriteriaIn> criterias) {
        ScpiDocumentDTO optimalScpi = originalScpi.cloneScpi(originalScpi);

        for (CriteriaIn criteria : criterias) {
            Double optimalValue = optimalValuesMap.get(criteria.getName());
            if (optimalValue != null) {
                setFieldValue(optimalScpi, criteria.getName(), optimalValue.floatValue());
            }
        }
        return optimalScpi;
    }

    private String persistOptimalSCPI(ScpiDocumentDTO optimalScpi) throws IOException {

        String tempId = "optimal-temp-" + UUID.randomUUID();
        elasticsearchClient.index(i -> i
                .index(INDEX_SCORING_NAME)
                .id(tempId)
                .document(optimalScpi)
                .refresh(Refresh.True));
        return tempId;

    }

    private void deleteOptimalSCPI(String tempId) throws IOException {
        elasticsearchClient.delete(d -> d.index(INDEX_SCORING_NAME).id(tempId));

    }

    private SearchResponse<ScpiDocumentDTO> getSearchResponse(List<CriteriaIn> criterias) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX_SCORING_NAME)
                .query(buildFunctionScoreQuery(criterias))
                .sort(SortOptions.of(so -> so.field(f -> f.field("_score").order(SortOrder.Desc))))
                .size(1));

        return elasticsearchClient.search(searchRequest,
                ScpiDocumentDTO.class);

    }

    private double getOptimalScore(SearchResponse<ScpiDocumentDTO> searchResponse) {
        double optimalScore = 0.0;
        if (!searchResponse.hits().hits().isEmpty()) {
            optimalScore = searchResponse.hits().hits().get(0).score();
        }
        return optimalScore;
    }

    private void setFieldValue(ScpiDocumentDTO scpi, String fieldName, Float value) {
        switch (fieldName) {
            case "distributionRate" -> scpi.setDistributionRate(value);
            case "enjoymentDelay" -> scpi.setEnjoymentDelay(Float.floatToIntBits(value));
            case "managementCosts" -> scpi.setManagementCosts(value);
            case "subscriptionFeesBigDecimal" -> scpi.setSubscriptionFeesBigDecimal(value);
            case "capitalization" -> scpi.setCapitalization(value.longValue());
            default -> log.warn("Champ inconnu à modifier : {}", fieldName);
        }
    }

}
