package self.example.sdui4j.models;

import java.util.List;

/**
 * Array of Artifacts (responded images)
 * @param artifacts     a list of responded images
 */
public record ArtifactsResponse(
        List<Artifact> artifacts
) {
}
