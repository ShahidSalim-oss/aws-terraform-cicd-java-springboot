package com.ruanbekker.cargarage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyTest {

    @InjectMocks
    private VactorSearchManager vactorSearchManager;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void shouldStoreItem() throws Exception {
        // Create a mock response entity with a successful status code (2xx)
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Success message", HttpStatus.OK);
        // Mock the postForEntity method of RestTemplate to return the mock response
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockResponse);
        // Execute the store method
        String result = vactorSearchManager.store("shop1", UUID.randomUUID().toString(), "my text");
        // Verify the result
        assertEquals("Record added to Elasticsearch", result);
    }

    @Test
    public void testCosineSearch() {
        // Create a mock response entity with a successful status code (2xx)
        ResponseEntity<String> mockResponse = new ResponseEntity<>("{'result': 'cosine similarity result'}", HttpStatus.OK);
        // Mock the postForEntity method of RestTemplate to return the mock response
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockResponse);
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        // Mock the Elasticsearch server response for the cosineSearch method
        server.expect(requestTo("http://localhost:9200/your_index_name/_search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{'result': 'cosine similarity result'}", MediaType.APPLICATION_JSON));
        String result = vactorSearchManager.cosineSearch("shop1", "my text");
        // Verify that the cosineSearch method sent a request to Elasticsearch and received a successful response
        verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
        assertEquals("{'result': 'cosine similarity result'}", result);
    }
}
