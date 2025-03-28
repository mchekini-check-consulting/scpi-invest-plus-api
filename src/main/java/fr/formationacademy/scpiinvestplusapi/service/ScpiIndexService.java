package fr.formationacademy.scpiinvestplusapi.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScpiIndexService {


    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "scpi";


    public ScpiIndexService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    private void createIndexWithMapping() throws IOException {

        String mappingJson = """
                    {
                      "mappings": {
                        "properties": {
                          "id": { "type": "keyword" },
                          "name": { "type": "text" },
                          "minimumSubscription": { "type": "integer" },
                          "subscriptionFees": { "type": "double" },
                          "managementCosts": { "type": "double" },
                          "frequencyPayment": { "type": "keyword" },
                          "minimumInvestmentAmount": { "type": "float" },
                          "locations": { "type": "keyword" },
                          "sectors": { "type": "keyword" }
                        }
                      }
                    }
                """;

        elasticsearchClient.indices().create(c -> c
                .index(INDEX_NAME)
                .withJson(new ByteArrayInputStream(mappingJson.getBytes(StandardCharsets.UTF_8)))
        );
    }


    public boolean saveMultipleScpiIndex(List<ScpiIndex> scpiList) throws IOException {

        createIndexIfNotExists();

        List<BulkOperation> bulkOperations = scpiList.stream()
                .map(scpi -> BulkOperation.of(b -> b
                        .index(idx -> idx
                                .index(INDEX_NAME)
                                .id(scpi.getId())
                                .document(scpi)
                        )
                ))
                .collect(Collectors.toList());

        BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(bulkOperations));

        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);

        return !bulkResponse.errors();
    }


    private void createIndexIfNotExists() throws IOException {
        BooleanResponse indexExistsResponse = elasticsearchClient.indices().exists(b -> b.index(INDEX_NAME));
        boolean indexExists = indexExistsResponse.value();

        if (!indexExists) {
            createIndexWithMapping();
        }
    }


    public List<ScpiIndex> searchScpi(String name, Integer minimumSubscription) throws IOException {

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();


        if (name != null && !name.trim().isEmpty()) {
            String normalizedName = Normalizer.normalize(name, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            boolBuilder.must(m -> m
                    .fuzzy(f -> f
                            .field("name")
                            .value(normalizedName)
                            .fuzziness("2")
                    )
            );
        }

        if (minimumSubscription != null) {
            boolBuilder.must(m -> m
                    .term(t -> t
                            .field("minimumSubscription")
                            .value(minimumSubscription)
                    )
            );
        }


        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX_NAME)
                .query(q -> q
                        .bool(boolBuilder.build())
                )
        );


        SearchResponse<ScpiIndex> response = elasticsearchClient.search(searchRequest, ScpiIndex.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }


    //==========================================================

    public ScpiIndex getScpiByName(String name) throws IOException {
        SearchResponse<ScpiIndex> response = elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .size(1)
                        .query(q -> q
                                .fuzzy(m -> m
                                        .field("name")
                                        .value(name)
                                        .fuzziness("2")
                                )
                        )
                , ScpiIndex.class);

        return response.hits().hits().stream()
                .findFirst()
                .map(Hit::source)
                .orElse(null);
    }

    public List<ScpiIndex> getFuzzyScpiByName(String name) throws IOException {
        String normalizedName = removeAccents(name);
        log.info("Normalized name: {}", normalizedName);

        SearchResponse<ScpiIndex> searchResponse = elasticsearchClient.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q
                        .match(m -> m
                                .field("name")
                                .query(normalizedName)
                                .fuzziness("2")
                        )
                ), ScpiIndex.class);

        log.info("Search response: {}", searchResponse);
        return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }


    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
