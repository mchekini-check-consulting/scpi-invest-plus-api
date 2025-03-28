package fr.formationacademy.scpiinvestplusapi.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScpiIndexService {


    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "scpi";


    public ScpiIndexService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    public boolean saveMultipleScpiIndex(List<ScpiIndex> scpiList) throws IOException {
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

}
