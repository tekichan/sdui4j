package self.example.sdui4j.ctrls;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import self.example.sdui4j.models.InitImageMode;

import java.io.File;

import static self.example.sdui4j.utils.UIConfigUtil.configureSliderDoubleTextField;

/**
 * UI Controller for Image-To-Image Page
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public class ImageToImagePageController {
    private static final int DECIMAL_PLACES = 2;
    private static String DEFAULT_IMAGE_URL = "images/no_image_yet.png";

    @FXML private ComboBox<InitImageMode> initImageModeCombo;
    @FXML private Slider imageStrengthSlider;
    @FXML private TextField imageStrengthText;
    @FXML private Slider stepScheduleStartSlider;
    @FXML private TextField stepScheduleStartText;
    @FXML private Slider stepScheduleEndSlider;
    @FXML private TextField stepScheduleEndText;
    @FXML private Double defaultImageStrength;
    @FXML private Double defaultStepScheduleStart;
    @FXML private Double defaultStepScheduleEnd;
    @FXML private Label lblImageStrength;
    @FXML private HBox hbxImageStrength;
    @FXML private Label lblStepScheduleStart;
    @FXML private HBox hbxStepScheduleStart;
    @FXML private Label lblStepScheduleEnd;
    @FXML private HBox hbxStepScheduleEnd;
    @FXML private ImageView uploadImageView;

    /**
     * Initalize configuration of UI objects
     */
    public void initialize() {
        configureSliderTextFields();
        configureComboBoxes();
        configureOptionalPanes();
    }

    private void configureSliderTextFields() {
        configureSliderDoubleTextField(imageStrengthSlider, imageStrengthText, DECIMAL_PLACES);
        configureSliderDoubleTextField(stepScheduleStartSlider, stepScheduleStartText, DECIMAL_PLACES);
        configureSliderDoubleTextField(stepScheduleEndSlider, stepScheduleEndText, DECIMAL_PLACES);
    }

    private void configureComboBoxes() {
        initImageModeCombo.setItems(FXCollections.observableArrayList(InitImageMode.values()));
        initImageModeCombo.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    switch (initImageModeCombo.getValue()) {
                        case IMAGE_STRENGTH -> {
                            lblImageStrength.setVisible(true);
                            hbxImageStrength.setVisible(true);
                            lblStepScheduleStart.setVisible(false);
                            hbxStepScheduleStart.setVisible(false);
                            lblStepScheduleEnd.setVisible(false);
                            hbxStepScheduleEnd.setVisible(false);
                        }
                        case STEP_SCHEDULE -> {
                            lblImageStrength.setVisible(false);
                            hbxImageStrength.setVisible(false);
                            lblStepScheduleStart.setVisible(true);
                            hbxStepScheduleStart.setVisible(true);
                            lblStepScheduleEnd.setVisible(true);
                            hbxStepScheduleEnd.setVisible(true);
                        }
                        default -> {
                            lblImageStrength.setVisible(false);
                            hbxImageStrength.setVisible(false);
                            lblStepScheduleStart.setVisible(false);
                            hbxStepScheduleStart.setVisible(false);
                            lblStepScheduleEnd.setVisible(false);
                            hbxStepScheduleEnd.setVisible(false);
                        }
                    }
                }
        );
    }

    private void configureOptionalPanes() {
        lblImageStrength.setVisible(false);
        hbxImageStrength.setVisible(false);
        lblStepScheduleStart.setVisible(false);
        hbxStepScheduleStart.setVisible(false);
        lblStepScheduleEnd.setVisible(false);
        hbxStepScheduleEnd.setVisible(false);
    }

    /**
     * Handler when pressing Reset button
     * @param actionEvent   Action Event
     */
    public void handleResetButton(ActionEvent actionEvent) {
        initImageModeCombo.setValue(null);
        imageStrengthSlider.adjustValue(defaultImageStrength);
        stepScheduleStartSlider.adjustValue(defaultStepScheduleStart);
        stepScheduleEndSlider.adjustValue(defaultStepScheduleEnd);
        // Reset image
        uploadImageView.setImage(
                new Image(
                        getClass().getClassLoader().getResource(DEFAULT_IMAGE_URL).toExternalForm()
                )
        );
    }

    /**
     * Handler when pressing Upload Init Image button
     * @param actionEvent   Action Event
     */
    public void handleUploadInitImage(ActionEvent actionEvent) {
        FileChooser uploadFileChooser = new FileChooser();
        uploadFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png"
        ));
        File file = uploadFileChooser.showOpenDialog(uploadImageView.getScene().getWindow());
        if (file != null) uploadImageView.setImage(new Image("file:" + file.getAbsolutePath()));
    }
}
