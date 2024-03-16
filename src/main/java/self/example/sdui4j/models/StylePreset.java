package self.example.sdui4j.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Style Preset
 * <p>
 *     Pass in a style preset to guide the image model towards a particular style. This list of style presets is
 *     subject to change.
 * </p>
 */
public enum StylePreset {
    THREE_D_MODEL("3d-model"),
    ANALOG_FILM("analog-film"),
    ANIME("anime"),
    CINEMATIC("cinematic"),
    COMIC_BOOK("comic-book"),
    DIGITAL_ART("digital-art"),
    ENHANCE("enhance"),
    FANTASY_ART("fantasy-art"),
    ISOMETRIC("isometric"),
    LINE_ART("line-art"),
    LOW_POLY("low-poly"),
    MODELING_COMPOUND("modeling-compound"),
    NEON_PUNK("neon-punk"),
    ORIGAMI("origami"),
    PHOTOGRAPHIC("photographic"),
    PIXEL_ART("pixel-art"),
    TILE_TEXTURE("tile-texture");

    public final String label;
    StylePreset(String label) {
        this.label = label;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.label;
    }

    public static StylePreset getDefault() {
        return THREE_D_MODEL;
    }
}
