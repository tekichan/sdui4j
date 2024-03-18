package self.example.sdui4j.ctrls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import self.example.sdui4j.models.*;
import self.example.sdui4j.services.ApiRequestService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static self.example.sdui4j.utils.UIConfigUtil.configureSliderTextField;

/**
 * UI Controller of Main Application
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private static String DEFAULT_IMAGE_URL = "images/no_image_yet.png";
    private ApiRequestService apiRequestService;
    public static final ObservableList<TextPrompt> textPromptList = FXCollections.observableArrayList();

    @FXML private ComboBox<String> engineIdCombo;

    @FXML private Slider cfgScaleSlider;
    @FXML private TextField cfgScaleText;
    @FXML private Slider samplesSlider;
    @FXML private TextField samplesText;
    @FXML private Slider seedSlider;
    @FXML private TextField seedText;
    @FXML private Slider stepsSlider;
    @FXML private TextField stepsText;
    @FXML private ListView<TextPrompt> textPromptsListView;
    @FXML private ComboBox<ClipGuidancePreset> clipGuidPresetCombo;
    @FXML private ComboBox<Sampler> samplerCombo;
    @FXML private ComboBox<StylePreset> stylePresetCombo;

    @FXML private Label statusBarLabel;
    @FXML private TabPane tabPane;
    @FXML private TextToImagePageController textToImagePageController;
    @FXML private ImageToImagePageController imageToImagePageController;

    @FXML private ImageView resultImageView;

    @FXML private Integer defaultCfgScale;
    @FXML private Integer defaultSamples;
    @FXML private Long defaultSeed;
    @FXML private Integer defaultSteps;

    private byte[] downloadedByteArray = null;
    private String downloadedFileType = "png";

    /**
     * Setter of ApiRequestService
     * @param apiRequestService ApiRequestService
     */
    public void setApiRequestService(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
    }

    /**
     * Initialize configuration of UI objects
     */
    public void initialize() {
        configureSliderTextFields();
        configureComboBoxes();
        configureTextPromptListView();
    }

    /**
     * Configure engine ID list
     */
    public void configureEngineList() {
        try {
            List<String> engineIdList = this.apiRequestService.getEngineList();
            this.engineIdCombo.setItems( FXCollections.observableArrayList(engineIdList) );
        } catch (Exception ex) {
            logger.error("Error in configuring Engine List", ex);
        }
    }

    private void configureSliderTextFields() {
        configureSliderTextField(cfgScaleSlider, cfgScaleText, 1);
        configureSliderTextField(samplesSlider, samplesText, 1);
        configureSliderTextField(seedSlider, seedText, 1L);
        configureSliderTextField(stepsSlider, stepsText, 1);
    }

    private void configureComboBoxes() {
        clipGuidPresetCombo.setItems(FXCollections.observableArrayList(ClipGuidancePreset.values()));
        samplerCombo.setItems(FXCollections.observableArrayList(Sampler.values()));
        stylePresetCombo.setItems(FXCollections.observableArrayList(StylePreset.values()));
    }

    private void configureTextPromptListView() {
        if (textPromptList.isEmpty()) {
            textPromptList.add(
                    new TextPrompt("", 0.5)
            );
        }
        textPromptsListView.setCellFactory(item -> {
            try {
                return PromptsListCell.newInstance();
            } catch (IOException ioe) {
                logger.error("Error in creating a new PromptsListCell", ioe);
                return null;
            }
        });
        textPromptsListView.setItems(textPromptList);
        textPromptsListView.refresh();
    }

    /**
     * Handler when pressing Plus button
     * @param actionEvent   Action Event
     */
    public void handlePlusPromptButton(ActionEvent actionEvent) {
        textPromptList.add(
                new TextPrompt("", 0.5)
        );
    }

    /**
     * Handle when pressing Reset button
     * @param actionEvent   Action Event
     */
    public void handleResetButton(ActionEvent actionEvent) {
        this.textToImagePageController.handleResetButton(actionEvent);
        this.imageToImagePageController.handleResetButton(actionEvent);
        this.resetComboBoxes();
        this.resetSliders();
        this.resetTextPromptListView();
        this.setStatusBarText("");
        // Reset image
        this.downloadedByteArray = null;
        resultImageView.setImage(
                new Image(
                        getClass().getClassLoader().getResource(DEFAULT_IMAGE_URL).toExternalForm()
                )
        );
    }

    private void resetTextPromptListView() {
        textPromptList.clear();
        textPromptList.add(
                new TextPrompt("", 0.5)
        );
        textPromptsListView.setItems(textPromptList);
        textPromptsListView.refresh();
    }

    private void resetSliders() {
        cfgScaleSlider.adjustValue(defaultCfgScale);
        samplesSlider.adjustValue(defaultSamples);
        seedSlider.adjustValue(defaultSeed);
        stepsSlider.adjustValue(defaultSteps);
    }

    private void resetComboBoxes() {
        clipGuidPresetCombo.setValue(ClipGuidancePreset.getDefault());
        samplerCombo.setValue(Sampler.getDefault());
        stylePresetCombo.setValue(StylePreset.getDefault());
    }

    /**
     * Handle when pressing Submit button
     * @param actionEvent   Action Event
     */
    public void handleSubmitButton(ActionEvent actionEvent) {
        boolean isSelectedImageToImage = tabPane.getSelectionModel().getSelectedItem().getText().equalsIgnoreCase("Image To Image");
        try {
            List<ImageResponse> imageResponses = isSelectedImageToImage ?
                    this.apiRequestService.postImageToImage(
                            this.engineIdCombo.getValue(),
                            createImageToImageRequest()
                    ) :
                    this.apiRequestService.postTextToImage(
                        this.engineIdCombo.getValue(),
                            createTextToImageRequest()
                    );
            downloadedByteArray = imageResponses.get(0).imageByteArray();
            downloadedFileType = imageResponses.get(0).filetype();
            if (downloadedByteArray != null) {
                resultImageView.setImage(fromByteArrayToImage(downloadedByteArray));
            } else {
                resultImageView.setImage(new Image(DEFAULT_IMAGE_URL));
            }
        } catch (Exception ex) {
            logger.error("Error in posting Text To Image", ex);
            setStatusBarText(ex.toString());
        }
    }

    private ImageToImageRequest createImageToImageRequest() {
        return new ImageToImageRequest(
                textPromptList,
                imageToImagePageController.getInitImageFile(),
                imageToImagePageController.getInitImageMode(),
                Double.valueOf(
                        imageToImagePageController.getImageStrengthText()
                ),
                Double.valueOf(
                        imageToImagePageController.getStepScheduleStartText()
                ),
                Double.valueOf(
                        imageToImagePageController.getStepScheduleEndText()
                ),
                Integer.valueOf(cfgScaleText.getText()),
                clipGuidPresetCombo.getValue(),
                samplerCombo.getValue(),
                Integer.valueOf(samplesText.getText()),
                Long.valueOf(seedText.getText()),
                Integer.valueOf(stepsText.getText()),
                stylePresetCombo.getValue()
        );
    }

    private TextToImageRequest createTextToImageRequest() {
        return new TextToImageRequest(
                Integer.valueOf(
                        textToImagePageController.getHeightText()
                ),
                Integer.valueOf(
                        textToImagePageController.getWidthText()
                ),
                textPromptList,
                Integer.valueOf(cfgScaleText.getText()),
                clipGuidPresetCombo.getValue(),
                samplerCombo.getValue(),
                Integer.valueOf(samplesText.getText()),
                Long.valueOf(seedText.getText()),
                Integer.valueOf(stepsText.getText()),
                stylePresetCombo.getValue()
        );
    }

    private Image fromByteArrayToImage(byte[] byteArray) {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(byteArray)) {
            return new Image(bis);
        } catch (IOException e) {
            return new Image(DEFAULT_IMAGE_URL);
        }
    }

    /**
     * Handle when pressing Download button
     * @param actionEvent   Action Event
     */
    public void handleDownloadAction(ActionEvent actionEvent) {
        FileChooser downloadFileChooser = new FileChooser();
        downloadFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*." + downloadedFileType
        ));
        File file = downloadFileChooser.showSaveDialog(resultImageView.getScene().getWindow());
        if (file != null) {
            try(InputStream imageIs = new ByteArrayInputStream(downloadedByteArray)) {
                Files.copy(imageIs, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                setStatusBarText("File " + file.getName() + " is saved.");
            } catch (Exception ex) {
                logger.error("Error in saving Image to the local", ex);
            }

        }
    }

    /**
     * Set the text to be displayed in Status Bar
     * @param labelText     Text to be displayed
     */
    public void setStatusBarText(String labelText) {
        this.statusBarLabel.setText(labelText);
    }
}