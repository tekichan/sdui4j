package self.example.sdui4j.models;

/**
 * Enum of Clip Guidance Preset
 * <p>
 *     CLIP Guidance is a technique that uses the CLIP neural network to guide the generation of images to be more
 *     in-line with your included prompt, which often results in improved coherency.
 * </p>
 */
public enum ClipGuidancePreset {
    FAST_BLUE,
    FAST_GREEN,
    NONE,
    SIMPLE,
    SLOW,
    SLOWER,
    SLOWEST;

    public static ClipGuidancePreset getDefault() {
        return NONE;
    }
}
