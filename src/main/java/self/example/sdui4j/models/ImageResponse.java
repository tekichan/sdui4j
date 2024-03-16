package self.example.sdui4j.models;

/**
 * Representation of Image Response
 * @param imageByteArray    Byte array of image file
 * @param filetype          File type (e.g. png)
 * @param finishReason      Finish reason
 * @param seed              Seed value
 */
public record ImageResponse(
        byte[] imageByteArray,
        String filetype,
        FinishReason finishReason,
        Long seed
) {
}
