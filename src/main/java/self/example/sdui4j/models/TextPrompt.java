package self.example.sdui4j.models;

/**
 * Representation of Text Prompt to use for generation.
 * <p>
 *     Prompt is the description in natural language about the desired image.
 *
 * </p>
 */
public class TextPrompt{
    public static final Double DEFAULT_WEIGHT = 0.5;
    private String text;
    private Double weight;

    public TextPrompt() {
        text = "";
        weight = DEFAULT_WEIGHT;
    }

    public TextPrompt(String t, Double w) {
        text = t;
        weight = w;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Text: " + text + " (Weight: " + String.format("%.2f", weight) + ")";
    }
}
