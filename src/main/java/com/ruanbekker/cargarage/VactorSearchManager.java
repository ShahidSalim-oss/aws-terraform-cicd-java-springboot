package com.ruanbekker.cargarage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class VactorSearchManager {

    @Autowired
    private RestTemplate restTemplate;

    public String store(String shopId, String itemId, String text) {
        setMapping();
        TestItem item = new TestItem();
        item.setShopId(shopId);
        item.setId(itemId);
        item.setEmbedding(generateRandomDoubles());
        // Set yourModel properties

        // Create the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP request entity with your model as the request body
        HttpEntity<TestItem> requestEntity = new HttpEntity<>(item, headers);

        // Send the HTTP POST request to Elasticsearch to add the document
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:9200" + "/your_index_name/your_type", requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "Record added to Elasticsearch";
        } else {
            return "Failed to add record to Elasticsearch";
        }
    }

    private void setMapping() {
        String mapping = "{\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"id\": { \"type\": \"text\" },\n" +
                "      \"embedding\": { \"type\": \"dense_vector\", \"dims\": 5 }  \n" +
                "    }\n" +
                "  }\n" +
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(mapping, headers);

        // Send the HTTP POST request to Elasticsearch for cosine similarity search
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:9200" + "/your_index_name/your_type", requestEntity, String.class);

    }

    public String cosineSearch(String shopId, String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put("queryVector", generateRandomDoubles());

        Map<String, Object> scriptObject = new HashMap<>();
        scriptObject.put("inline", "cosineSimilarity(params.queryVector, doc['embedding']) + 1.0");
        scriptObject.put("lang", "painless");
        scriptObject.put("params", scriptParams);

        Map<String, Object> scriptScore = new HashMap<>();
        scriptScore.put("script_score", Collections.singletonMap("script", scriptObject));

        Map<String, Object> functionScore = new HashMap<>();
        functionScore.put("query", Collections.singletonMap("match_all", new HashMap<>()));
        functionScore.put("functions", Collections.singletonList(scriptScore));

        Map<String, Object> query = new HashMap<>();
        query.put("query", Collections.singletonMap("function_score", functionScore));

        ObjectMapper objectMapper = new ObjectMapper();
        String queryString;
        try {
            queryString = objectMapper.writeValueAsString(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Failed to serialize query";
        }

        System.out.println(queryString);
        HttpEntity<String> requestEntity = new HttpEntity<>(queryString, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:9200/your_index_name/_search",
                requestEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return "Cosine similarity search failed";
        }
    }



    public String listToJSONArray(List<Double> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            // Handle or log the exception as needed
            return "[]"; // Return an empty JSON array as a default
        }
    }

    public List<Double> generateRandomDoubles() {
        return generateRandomDoubles(5, 10, 1000);
    }

    public List<Double> generateRandomDoubles(int count, double min, double max) {
        List<Double> randomDoubles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            double randomValue = min + (max - min) * random.nextDouble();
            randomDoubles.add(randomValue);
        }

        return randomDoubles;
    }
}
