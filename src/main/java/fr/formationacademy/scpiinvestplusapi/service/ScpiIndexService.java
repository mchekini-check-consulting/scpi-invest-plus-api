package fr.formationacademy.scpiinvestplusapi.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
                          "minimumInvestmentAmount": { "type": "float" }
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

}
