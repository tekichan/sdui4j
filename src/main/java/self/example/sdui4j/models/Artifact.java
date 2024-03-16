package self.example.sdui4j.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a responded image object
 * @param base64        Image encoded in base64
 * @param finishReason  Finish Reason
 * @param seed          The seed associated with this image
 */
public record Artifact(
        @JsonProperty(value = "base64")
        String base64,
        @JsonProperty(value = "finishReason")
        String finishReason,
        @JsonProperty(value = "seed")
        Long seed
) {
}
