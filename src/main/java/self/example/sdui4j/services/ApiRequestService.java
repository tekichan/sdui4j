package self.example.sdui4j.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import self.example.sdui4j.models.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Service to handle API Request
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public class ApiRequestService {
    private static Logger logger = LoggerFactory.getLogger(ApiRequestService.class);
    private final String STABILITY_DOMAIN = "https://api.stability.ai";
    private final String PATH_V1_ENGINES = "/v1/engines/list";
    private final String PATH_V1_GEN_TXT_TO_IMG = "/v1/generation/{engine_id}/text-to-image";
    private final String PATH_V1_GEN_IMG_TO_IMG = "/v1/generation/{engine_id}/image-to-image";

    private String apiKey;
    private ObjectMapper jsonMapper;

    /**
     * ApiRequestService Constructor
     * @param apiKey        API Key
     * @param jsonMapper    Jackson JSON Mapper
     */
    public ApiRequestService(String apiKey, ObjectMapper jsonMapper) {
        this.apiKey = apiKey;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Get a list of model engines via Engine List API
     * @return  a list of model engines (engine ID)
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public List<String> getEngineList() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(STABILITY_DOMAIN + PATH_V1_ENGINES))
                .headers(
                        "Authorization", "Bearer " + this.apiKey,
                        "Accept", "application/json"
                )
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String body = response.body();
            return getEngineIdsFromJson(body);
        } else {
            return List.of();
        }
    }

    /**
     * Post a request to Text-To-Image API
     * @param engineId      selected engine ID
     * @param requestBody   Text-To-Image request body
     * @return              a list of Image Response objects
     * @throws Exception
     */
    public List<ImageResponse> postTextToImage(String engineId, TextToImageRequest requestBody)
            throws Exception {
        String url = STABILITY_DOMAIN + PATH_V1_GEN_TXT_TO_IMG.replace("{engine_id}", engineId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(
                        "Authorization", "Bearer " + this.apiKey,
                        "Content-Type", "application/json",
                        "Accept", "application/json"
                )
                .POST(
                        HttpRequest.BodyPublishers.ofString(
                                jsonMapper.writeValueAsString(requestBody)
                        )
                )
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ArtifactsResponse artifactsResponse = jsonMapper.readValue(response.body(), ArtifactsResponse.class);
            return convertImageResponse(artifactsResponse);
        } else {
            throw new Exception("Status Code: " + response.statusCode() + ", Body: " + response.body());
        }
    }

    private List<ImageResponse> convertImageResponse(ArtifactsResponse artifactsResponse) {
        return artifactsResponse.artifacts().stream().map(
                artifact -> new ImageResponse(
                        Base64.getDecoder().decode(artifact.base64()),
                        "png",
                        FinishReason.valueOf(artifact.finishReason().toUpperCase()),
                        artifact.seed()
                )
        ).toList();
    }

    private List<String> getEngineIdsFromJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> jsonList = mapper.readValue(jsonString, new TypeReference<>(){});
            return jsonList.stream()
                    .map(mapObject -> mapObject.getOrDefault("id", "").toString())
                    .toList();
        } catch (JsonProcessingException jpe) {
            logger.warn("Failed to deserialize engine list from API response", jpe);
            return List.of();
        }
    }

    /**
     * Post a request to Image-To-Image API
     * @param engineId      selected engine ID
     * @param requestBody   Image-To-Image request body
     * @return              a list of Image Response objects
     * @throws Exception
     */
    public List<ImageResponse> postImageToImage(String engineId, ImageToImageRequest requestBody)
            throws Exception {
        String url = STABILITY_DOMAIN + PATH_V1_GEN_IMG_TO_IMG.replace("{engine_id}", engineId);
        String boundary = ImageToImageBodyPublisherBuilder.getBoundary();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(
                        "Authorization", "Bearer " + this.apiKey,
                        "Content-Type", "multipart/form-data; boundary=" + boundary,
                        "Accept", "application/json"
                )
                .POST(
                        (new ImageToImageBodyPublisherBuilder())
                                .imageToImageRequest(requestBody)
                                .boundary(boundary)
                                .build()
                )
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ArtifactsResponse artifactsResponse = jsonMapper.readValue(response.body(), ArtifactsResponse.class);
            return convertImageResponse(artifactsResponse);
        } else {
            throw new Exception("Status Code: " + response.statusCode() + ", Body: " + response.body());
        }
    }
}
