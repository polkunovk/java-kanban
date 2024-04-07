import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasksEndpointTest {
    private static final String BASE_URL = "http://localhost:8080/tasks";
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetTasks_Success() throws IOException, InterruptedException {
        stubFor(get(urlEqualTo("/tasks"))
                .willReturn(aResponse().withStatus(200)));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testAddTask_Success() throws IOException, InterruptedException {
        stubFor(post(urlEqualTo("/tasks"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(aResponse().withStatus(201)));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"Test Task\",\"description\":\"Test Description\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    void testDeleteTask_Success() throws IOException, InterruptedException {
        stubFor(delete(urlEqualTo("/tasks/1"))
                .willReturn(aResponse().withStatus(200)));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
}
