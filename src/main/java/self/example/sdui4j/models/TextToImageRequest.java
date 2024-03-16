package self.example.sdui4j.models;

import java.util.List;

/**
 * Request body of Text-To-Image API
 * @param height                Height of the image to generate, in pixels
 * @param width                 Width of the image to generate, in pixels
 * @param textPrompts           A list of Prompt text and weight value
 * @param cfgScale              How strictly the diffusion process adheres to the prompt text (higher values keep your image closer to your prompt)
 * @param clipGuidancePreset    Clip Guidance Preset
 * @param sampler               Which sampler to use for the diffusion process. If this value is omitted we'll automatically select an appropriate sampler for you.
 * @param samples               Number of images to generate
 * @param seed                  Random noise seed (omit this option or use 0 for a random seed)
 * @param steps                 Number of diffusion steps to run.
 * @param stylePreset           Pass in a style preset to guide the image model towards a particular style. This list of style presets is subject to change.
 */
public record TextToImageRequest(
        Integer height,
        Integer width,
        List<TextPrompt> textPrompts,
        Integer cfgScale,
        ClipGuidancePreset clipGuidancePreset,
        Sampler sampler,
        Integer samples,
        Long seed,
        Integer steps,
        StylePreset stylePreset
) {
}
