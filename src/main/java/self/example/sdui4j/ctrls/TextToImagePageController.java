package self.example.sdui4j.ctrls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import static self.example.sdui4j.utils.UIConfigUtil.configureSliderTextField;

/**
 * UI Controller for Text-To-Image Page
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public class TextToImagePageController {
    @FXML private Slider heightSlider;
    @FXML private TextField heightText;
    @FXML private Slider widthSlider;
    @FXML private TextField widthText;
    @FXML private Integer defaultSize;
    @FXML private Integer unitSize;

    /**
     * Initalize configuration of UI objects
     */
    public void initialize() {
        configureSliderTextFields();
    }

    private void configureSliderTextFields() {
        configureSliderTextField(heightSlider, heightText, unitSize);
        configureSliderTextField(widthSlider, widthText, unitSize);
    }

    /**
     * Handler when pressing Reset button
     * @param actionEvent   Action Event
     */
    public void handleResetButton(ActionEvent actionEvent) {
        heightSlider.adjustValue(defaultSize);
        widthSlider.adjustValue(defaultSize);
    }

    /**
     * Get the value of Height TextField
     * @return  height value
     */
    public String getHeightText() {
        return heightText.getText();
    }

    /**
     * Get the value of Width TextField
     * @return  width value
     */
    public String getWidthText() {
        return widthText.getText();
    }
}