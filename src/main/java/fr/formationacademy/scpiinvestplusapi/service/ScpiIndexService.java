package fr.formationacademy.scpiinvestplusapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
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
import java.math.BigDecimal;
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
                        .sorted(Comparator.comparing(ScpiDocumentDTO::getDistributionRate).reversed())
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
                                            .operator(Operator.And)
                                    )
                            )
                            .should(sh -> sh
                                    .wildcard(w -> w
                                            .field("name.keyword")
                                            .value("*" + criteria.getName() + "*")
                                            .caseInsensitive(true)
                                    )
                            )
                    )
            );
        }



        if (criteria.getDistributionRate() != null) {
            log.info("Searching with distributionRate: {}", criteria.getDistributionRate());
            boolQuery.filter(f -> f.range(r -> r
                    .term(t -> t.field("distributionRate")
                            .gte(String.valueOf(criteria.getDistributionRate()))
                    )
            ));
        }

        if (criteria.getMinimumSubscription() != null) {
            log.info("Searching with minimumSubscription: {}", criteria.getMinimumSubscription());
            boolQuery.filter(f -> f.range(r -> r
                    .term(t -> t.field("minimumSubscription")
                            .lte(String.valueOf(criteria.getMinimumSubscription()))
                    )
                    ));
        }

        if (criteria.getSubscriptionFees() != null) {
            log.info("Searching with subscriptionFees: {}", criteria.getSubscriptionFees());
            if (criteria.getSubscriptionFees()) {
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

        if (criteria.getFrequencyPayment() != null && !criteria.getFrequencyPayment().trim().isEmpty()) {
            log.info("Searching with frequencyPayment: {}", criteria.getFrequencyPayment());
            boolQuery.must(m -> m.match(t -> t
                    .field("frequencyPayment")
                    .query(criteria.getFrequencyPayment().trim().toLowerCase())
            ));
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
                                                .query(location.trim().toLowerCase())
                                        )
                                )
                        )
                );
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
                                                .query(sector.trim().toLowerCase())
                                        )
                                )
                        )
                );
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
                                .value(false)
                        )
                ), ScpiDocumentDTO.class);

        if (response.hits() != null) {
            scpiList = response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return scpiList;
    }

        /* Service de scoring */

        // public void createScpiIndex(ElasticsearchClient client) throws IOException {
        // client.indices().create(req -> req
        // .index("scpi")
        // .settings(s -> s
        // .numberOfShards("1")
        // .numberOfReplicas("0")
        // .analysis(a -> a
        // .analyzer("edge_ngram_analyzer", c -> c
        // .custom(ca -> ca
        // .tokenizer("standard")
        // .filter("lowercase",
        // "asciifolding",
        // "edge_ngram")))))
        // .mappings(m -> m
        // .properties("id", Property.of(p -> p.keyword(k -> k)))
        // .properties("name", Property.of(p -> p
        // .text(t -> t
        // .analyzer("edge_ngram_analyzer")
        // .fields("keyword", f -> f
        // .keyword(k -> k)))))
        // .properties("distributionRate", Property
        // .of(p -> p.scaledFloat(sf -> sf.scalingFactor(100.0))))
        // .properties("sharePrice", Property
        // .of(p -> p.scaledFloat(sf -> sf.scalingFactor(100.0))))
        // .properties("subscriptionFeesBigDecimal", Property
        // .of(p -> p.scaledFloat(sf -> sf.scalingFactor(100.0))))
        // .properties("managementCosts", Property
        // .of(p -> p.scaledFloat(sf -> sf.scalingFactor(100.0))))
        // .properties("capitalization", Property.of(p -> p.long_(l -> l)))
        // .properties("enjoymentDelay", Property.of(p -> p.integer(i -> i)))
        // .properties("frequencyPayment", Property.of(p -> p
        // .text(t -> t
        // .fields("keyword", f -> f
        // .keyword(k -> k)))))
        // .properties("minimumSubscription", Property.of(p -> p.integer(i -> i)))

        // .properties("countryDominant", Property.of(p -> p
        // .object(o -> o
        // .properties("country",
        // Property.of(p2 -> p2
        // .text(t -> t
        // .fields("keyword",
        // f -> f.keyword(k -> k)))))
        // .properties("countryPercentage",
        // Property.of(p2 -> p2
        // .float_(f -> f))))))

        // .properties("sectorDominant", Property.of(p -> p
        // .object(o -> o
        // .properties("name", Property.of(p2 -> p2
        // .text(t -> t
        // .fields("keyword",
        // f -> f.keyword(k -> k)))))
        // .properties("sectorPercentage", Property
        // .of(p2 -> p2.scaledFloat(
        // sf -> sf.scalingFactor(
        // 100.0)))))))

        // .properties("locations", Property.of(p -> p
        // .nested(n -> n
        // .properties("country",
        // Property.of(p2 -> p2
        // .text(t -> t
        // .fields("keyword",
        // f -> f.keyword(k -> k)))))
        // .properties("countryPercentage",
        // Property.of(p2 -> p2
        // .float_(f -> f))))))

        // .properties("sectors", Property.of(p -> p
        // .nested(n -> n
        // .properties("name", Property.of(p2 -> p2
        // .text(t -> t
        // .fields("keyword",
        // f -> f.keyword(k -> k)))))
        // .properties("sectorPercentage", Property
        // .of(p2 -> p2.float_(
        // f -> f))))))));
        // }

        public void initOtimalValueMap() {
                optimalValuesMap.put("distributionRate", 8.35);
                optimalValuesMap.put("enjoymentDelay", 0.0);
                optimalValuesMap.put("managementCosts", 7.2);
                optimalValuesMap.put("subscriptionFees", 0.0);
                optimalValuesMap.put("capitalization", 6200000000.0);
        }

        public void initCriteriaMap() {
                CriteriaMap.put("distributionRate", CriteriaPoperties.builder().ScoringType("FunctionScore")
                                .modifier(FieldValueFactorModifier.Sqrt).factor(2.0).build());
                CriteriaMap.put("enjoymentDelay",
                                CriteriaPoperties.builder().ScoringType("DecayFunctionScore").scale(4).decay(0.5)
                                                .build());
                CriteriaMap.put("managementCosts",
                                CriteriaPoperties.builder().ScoringType("DecayFunctionScore").scale(10).decay(0.5)
                                                .build());
                CriteriaMap.put("subscriptionFees",
                                CriteriaPoperties.builder().ScoringType("DecayFunctionScore").scale(5).decay(0.5)
                                                .build());
                CriteriaMap.put("capitalization",
                                CriteriaPoperties.builder().ScoringType("RangeBonus").weight(1.5).limit(740000000L)
                                                .build());
        }

        @PostConstruct
        public void initData() {
                initCriteriaMap();
                initOtimalValueMap();
                // log.info(("Le service est bien executé"));
                // try {
                // if (!indexExists("scpi")) {
                // createScpiIndex(elasticsearchClient);
                // log.info("L'index SCPI a ete cree avec succes !");
                // } else {
                // log.info("L'index SCPI existe deja, aucune creation necessaire.");
                // }
                // } catch (IOException e) {
                // log.error("Erreur lors de la verification ou creation de l'index SCPI", e);
                // }

                // if (getAllScpi().isEmpty()) {
                // List<ScpiDocumentDTO> scpiIndexes = InitIndexDataForTest();
                // scpiIndexes.forEach(scpi -> {
                // try {
                // saveScpi(scpi);
                // } catch (IOException e) {
                // log.error("Erreur lors de l'indexation de la SCPI : " + scpi.getName(),
                // e);
                // }
                // });
                // log.info("Donnees SCPI indexees avec succes !");
                // } else {
                // log.info("Les donnees SCPI existent deja, aucune insertion necessaire.");
                // }
        }

        // public boolean indexExists(String indexName) throws IOException {
        // return elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
        // }

        // public void saveScpi(ScpiDocumentDTO scpi) throws IOException {
        // IndexResponse response = elasticsearchClient.index(i -> i
        // .index("scpi")
        // .id(scpi.getId())
        // .document(scpi));
        // log.info("SCPI indexé : " + response.id());
        // }

        public List<ScpiDocumentDTO> searchScoredScpi(List<CriteriaIn> criterias) throws IOException {

                SearchRequest searchRequest = buildSearchRequest(criterias);
                SearchResponse<ScpiDocumentDTO> searchResponse = elasticsearchClient.search(searchRequest,
                                ScpiDocumentDTO.class);
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
                for (FunctionScore score : scores) {
                        log.info("Leeeeeeeees scooores", score);
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
                System.err.println(" Reponse de Elastic  " + searchResponse.hits().hits());
                List<ScpiDocumentDTO> result = searchResponse.hits().hits().stream()
                                .map(hit -> hit.source())
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                return calculateMashedScore(result, criteriaList);
        }

        public List<ScpiDocumentDTO> calculateMashedScore(List<ScpiDocumentDTO> scpiList,
                        List<CriteriaIn> criteriaList) {
                for (ScpiDocumentDTO scpi : scpiList) {
                        double totalScore = 0.0;

                        for (CriteriaIn criteria : criteriaList) {
                                String criteriaName = criteria.getName();
                                double factor = criteria.getFactor();

                                double attributeValue = getAttributeValue(scpi, criteriaName);

                                Double optimalValue = optimalValuesMap.get(criteriaName);

                                if (optimalValue != null) {
                                        double rate = attributeValue / optimalValue;
                                        totalScore += rate * factor;
                                } else {
                                        log.warn("No optimal value found for criteria: {}", criteriaName);
                                }
                        }

                        scpi.setMashedScore((float) totalScore);
                }
                return scpiList;
        }

        private double getAttributeValue(ScpiDocumentDTO scpi, String criteriaName) {
                switch (criteriaName) {
                        case "distributionRate":
                                return scpi.getDistributionRate();
                        case "subscriptionFees":
                                return scpi.getSubscriptionFeesBigDecimal().doubleValue();
                        case "capitalization":
                                return scpi.getCapitalization().doubleValue();
                        case "enjoymentDelay":
                                return scpi.getEnjoymentDelay().doubleValue();
                        case "managementCosts":
                                return scpi.getManagementCosts().doubleValue();
                        case "minimumSubscription":
                                return scpi.getMinimumSubscription();
                        default:
                                throw new IllegalArgumentException("Unknown criteria name: " + criteriaName);
                }
        }

        // private List<ScpiDocumentDTO> InitIndexDataForTest() {
        // return List.of(
        // ScpiDocumentDTO.builder()
        // .id(UUID.randomUUID().toString())
        // .name("Transitions Europe")
        // .distributionRate(8.35f)
        // .subscriptionFeesBigDecimal(10.00f)
        // .managementCosts(10.00f)
        // .capitalization(45000000L)
        // .enjoymentDelay(4)
        // .frequencyPayment("Trimestrielle")
        // .minimumSubscription(5000)
        // .countryDominant(new ScpiDocumentDTO.CountryDominant("Pays-Bas", 47.0f))
        // .sectorDominant(new ScpiDocumentDTO.SectorDominant("Bureaux", 46.0f))
        // .locations(List.of(
        // new ScpiDocumentDTO.Location("Pays-Bas", 47.0f),
        // new ScpiDocumentDTO.Location("Espagne", 24.0f),
        // new ScpiDocumentDTO.Location("Irlande", 12.0f),
        // new ScpiDocumentDTO.Location("Pologne", 11.0f),
        // new ScpiDocumentDTO.Location("Allemagne", 6.0f)))
        // .sectors(List.of(
        // new ScpiDocumentDTO.Sector("Bureaux", 46.0f),
        // new ScpiDocumentDTO.Sector("Hotels", 18.0f),
        // new ScpiDocumentDTO.Sector("Logistique", 9.0f),
        // new ScpiDocumentDTO.Sector("Sante", 18.0f),
        // new ScpiDocumentDTO.Sector("Commerce", 9.0f)))
        // .build(),

        // ScpiDocumentDTO.builder()
        // .id(UUID.randomUUID().toString())
        // .name("Elevation Tertiom")
        // .distributionRate(8.00f)
        // .subscriptionFeesBigDecimal(24.00f)
        // .managementCosts(24.00f)
        // .capitalization(45000000L)
        // .enjoymentDelay(4)
        // .frequencyPayment("Mensuelle")
        // .minimumSubscription(2850)
        // .countryDominant(new ScpiDocumentDTO.CountryDominant("France", 100.0f))
        // .sectorDominant(new ScpiDocumentDTO.SectorDominant("Autre", 100.0f))
        // .locations(List.of(new ScpiDocumentDTO.Location("France", 100.0f)))
        // .sectors(List.of(new ScpiDocumentDTO.Sector("Autre", 100.0f)))
        // .build(),

        // ScpiDocumentDTO.builder()
        // .id(UUID.randomUUID().toString())
        // .name("Upéka")
        // .distributionRate(8.00f)
        // .subscriptionFeesBigDecimal(16.00f)
        // .managementCosts(16.00f)
        // .capitalization(45000000L)
        // .enjoymentDelay(4)
        // .frequencyPayment("Trimestrielle")
        // .minimumSubscription(1000)
        // .countryDominant(new ScpiDocumentDTO.CountryDominant("Pays-Bas", 56.0f))
        // .sectorDominant(new ScpiDocumentDTO.SectorDominant("Commerces", 44.0f))
        // .locations(List.of(
        // new ScpiDocumentDTO.Location("Pays-Bas", 56.0f),
        // new ScpiDocumentDTO.Location("Espagne", 25.0f),
        // new ScpiDocumentDTO.Location("France", 19.0f)))
        // .sectors(List.of(
        // new ScpiDocumentDTO.Sector("Commerces", 44.0f),
        // new ScpiDocumentDTO.Sector("Bureaux", 29.0f),
        // new ScpiDocumentDTO.Sector("Logistique", 27.0f)))
        // .build(),

        // ScpiDocumentDTO.builder()
        // .id(UUID.randomUUID().toString())
        // .name("Comète")
        // .distributionRate(8.00f)
        // .subscriptionFeesBigDecimal(11.00f)
        // .managementCosts(11.00f)
        // .capitalization(45000000L)
        // .enjoymentDelay(4)
        // .frequencyPayment("Trimestrielle")
        // .minimumSubscription(5000)
        // .countryDominant(new ScpiDocumentDTO.CountryDominant("Italie", 44.0f))
        // .sectorDominant(new ScpiDocumentDTO.SectorDominant("Autre", 56.0f))
        // .locations(List.of(
        // new ScpiDocumentDTO.Location("Italie", 44.0f),
        // new ScpiDocumentDTO.Location("Pays-Bas", 44.0f),
        // new ScpiDocumentDTO.Location("Espagne", 12.0f)))
        // .sectors(List.of(
        // new ScpiDocumentDTO.Sector("Autre", 56.0f),
        // new ScpiDocumentDTO.Sector("Bureaux", 44.0f)))
        // .build(),

        // ScpiDocumentDTO.builder()
        // .id(UUID.randomUUID().toString())
        // .name("Remake Live")
        // .distributionRate(7.79f)
        // .subscriptionFeesBigDecimal(18.00f)
        // .managementCosts(18.00f)
        // .capitalization(45000000L)
        // .enjoymentDelay(4)
        // .frequencyPayment("Mensuelle")
        // .minimumSubscription(1020)
        // .countryDominant(
        // new ScpiDocumentDTO.CountryDominant("Allemagne", 43.0f))
        // .sectorDominant(new ScpiDocumentDTO.SectorDominant("Bureaux", 50.0f))
        // .locations(List.of(
        // new ScpiDocumentDTO.Location("France", 37.0f),
        // new ScpiDocumentDTO.Location("Espagne", 20.0f),
        // new ScpiDocumentDTO.Location("Allemagne", 43.0f)))
        // .sectors(List.of(
        // new ScpiDocumentDTO.Sector("Bureaux", 50.0f),
        // new ScpiDocumentDTO.Sector("Logistique", 16.0f),
        // new ScpiDocumentDTO.Sector("Sante", 15.0f),
        // new ScpiDocumentDTO.Sector("Commerces", 12.0f),
        // new ScpiDocumentDTO.Sector("Reste", 4.0f)))
        // .build());
        // }

}
