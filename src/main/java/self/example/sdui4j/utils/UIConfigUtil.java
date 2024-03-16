package self.example.sdui4j.utils;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

/**
 * UI Config Utility
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public final class UIConfigUtil {
    /**
     * Configure Slider with Text Field
     * @param slider        Slider object
     * @param textField     TextField object
     * @param multipleOf    Multiple value (unit value) in long
     */
    public static void configureSliderTextField(Slider slider, TextField textField, long multipleOf) {
        slider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (!oldValue.equals(newValue)) {
                        textField.setText(
                                String.valueOf(getIntegerByMultiple(slider.getValue(), multipleOf))
                        );
                    }
                }
        );
    }

    private static long getIntegerByMultiple(double originalValue, long multipleOf) {
        return BigDecimal.valueOf(originalValue / multipleOf).longValue() * multipleOf;
    }

    /**
     * Configure Slider with Text Field
     * @param slider        Slider object
     * @param textField     TextField object
     * @param decimalPlaces Decimal Places used in TextField
     */
    public static void configureSliderDoubleTextField(Slider slider, TextField textField, int decimalPlaces) {
        final String stringFormat = "%." + decimalPlaces + "f";
        slider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (!oldValue.equals(newValue)) {
                        textField.setText(
                                String.format(stringFormat, slider.getValue())
                        );
                    }
                }
        );
    }
}