package self.example.sdui4j.services;

import self.example.sdui4j.models.ImageToImageRequest;
import self.example.sdui4j.models.InitImageMode;
import self.example.sdui4j.models.TextPrompt;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Build HttpRequest.BodyPublisher for Image-To-Image API
 */
public class ImageToImageBodyPublisherBuilder {
    private ImageToImageRequest requestBody;
    private String boundary;

    public static String getBoundary() {
        char charToAppend = '-';
        char[] charArray = new char[26];
        Arrays.fill(charArray, charToAppend);
        String prefix = new String(charArray);
        String randomDigits = String.format("%024d", new BigInteger(75, new Random()));
        return prefix + randomDigits;
    }

    public ImageToImageBodyPublisherBuilder imageToImageRequest(ImageToImageRequest requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ImageToImageBodyPublisherBuilder boundary(String boundary) {
        this.boundary = boundary;
        return this;
    }

    public HttpRequest.BodyPublisher build() throws IOException {
        // Result request body
        List<byte[]> byteArrays = new ArrayList<>();

        // Separator with boundary
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);

        // Iterating over data parts
        for(int i=0; i<requestBody.textPrompts().size(); i++) {
            TextPrompt textPrompt = requestBody.textPrompts().get(i);

            byteArrays.add(separator);
            addByteArrays(byteArrays, "text_prompts[" + i + "][text]", textPrompt.getText());

            byteArrays.add(separator);
            addByteArrays(byteArrays, "text_prompts[" + i + "][weight]", String.format("%.2f", textPrompt.getWeight()));
        }

        byteArrays.add(separator);
        addByteArrays(byteArrays, "init_image", requestBody.initImage());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "init_image_mode", requestBody.initImageMode().toString());

        if (requestBody.initImageMode().equals(InitImageMode.STEP_SCHEDULE)) {
            byteArrays.add(separator);
            addByteArrays(byteArrays, "step_schedule_start", String.format("%.2f", requestBody.stepScheduleStart()));
            byteArrays.add(separator);
            addByteArrays(byteArrays, "step_schedule_end", String.format("%.2f", requestBody.stepScheduleEnd()));
        } else {
            // IMAGE_STRENGTH as default
            byteArrays.add(separator);
            addByteArrays(byteArrays, "image_strength", String.format("%.2f", requestBody.imageStrength()));
        }

        byteArrays.add(separator);
        addByteArrays(byteArrays, "cfg_scale", requestBody.cfgScale().toString());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "clip_guidance_preset", requestBody.clipGuidancePreset().toString());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "sampler", requestBody.sampler().toString());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "samples", requestBody.samples().toString());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "seed", requestBody.seed().toString());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "steps", requestBody.steps().toString());

        byteArrays.add(separator);
        addByteArrays(byteArrays, "style_preset", requestBody.stylePreset().toString());

        // Closing boundary
        byteArrays.add(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        // Serializing as byte array
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }


    private void addByteArrays(List<byte[]> byteArrays, String key, String value) {
        byteArrays.add(
                ("\"" + key + "\"\r\n\r\n" + value + "\r\n")
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    private void addByteArrays(List<byte[]> byteArrays, String key, File file) throws IOException {
        String mimeType = Files.probeContentType(file.toPath());
        byteArrays.add(
                ("\"" + key + "\"; filename=\"" + file.getName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8)
        );
        byteArrays.add(Files.readAllBytes(file.toPath()));
        byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
    }

}
