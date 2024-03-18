package self.example.sdui4j.models;

import java.io.File;
import java.util.List;

/**
 * Request body of Image-To-Image API
 * @param textPrompts           A list of Prompt text and weight value
 * @param initImage             Image used to initialize the diffusion process, in lieu of random noise
 * @param initImageMode         Whether to use image_strength or step_schedule_* to control how much influence the init_image has on the result.
 * @param imageStrength         How much influence the init_image has on the diffusion process. Values close to 1 will yield images very similar to the init_image while values close to 0 will yield images wildly different than the init_image
 * @param stepScheduleStart     Skips a proportion of the start of the diffusion steps, allowing the init_image to influence the final generated image. Lower values will result in more influence from the init_image, while higher values will result in more influence from the diffusion steps. (e.g. a value of 0 would simply return you the init_image, where a value of 1 would return you a completely different image.)
 * @param stepScheduleEnd       Skips a proportion of the end of the diffusion steps, allowing the init_image to influence the final generated image. Lower values will result in more influence from the init_image, while higher values will result in more influence from the diffusion steps.
 * @param cfgScale              How strictly the diffusion process adheres to the prompt text (higher values keep your image closer to your prompt)
 * @param clipGuidancePreset    Clip Guidance Preset
 * @param sampler               Which sampler to use for the diffusion process. If this value is omitted we'll automatically select an appropriate sampler for you.
 * @param samples               Number of images to generate
 * @param seed                  Random noise seed (omit this option or use 0 for a random seed)
 * @param steps                 Number of diffusion steps to run.
 * @param stylePreset           Pass in a style preset to guide the image model towards a particular style. This list of style presets is subject to change.
 */
public record ImageToImageRequest(
        List<TextPrompt> textPrompts,
        File initImage,
        InitImageMode initImageMode,
        Double imageStrength,
        Double stepScheduleStart,
        Double stepScheduleEnd,
        Integer cfgScale,
        ClipGuidancePreset clipGuidancePreset,
        Sampler sampler,
        Integer samples,
        Long seed,
        Integer steps,
        StylePreset stylePreset
) {
}
