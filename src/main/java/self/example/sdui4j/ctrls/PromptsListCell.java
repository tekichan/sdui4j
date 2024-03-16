package self.example.sdui4j.ctrls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import self.example.sdui4j.models.TextPrompt;

import java.io.IOException;

/**
 * Cell of List View for Text Prompts
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public class PromptsListCell extends ListCell<TextPrompt> {
    private static final String CELL_FXML_PATH = "fxml/prompts_list_cell.fxml";
    @FXML
    private HBox hbox;
    @FXML
    private TextField textText;
    @FXML
    private Slider weightSlider;
    @FXML
    private TextField weightText;

    /**
     * Create a new instance of PromptsListCell
     * @return  PromptsListCell instance
     * @throws IOException
     */
    public static PromptsListCell newInstance() throws IOException {
        FXMLLoader loader = new FXMLLoader(PromptsListCell.class.getClassLoader().getResource(CELL_FXML_PATH));
        loader.load();
        return loader.getController();
    }

    /**
     * Initialize to add listeners of UI objects
     */
    public void initialize() {
        textText.textProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (!oldValue.equals(newValue)) {
                        getItem().setText(textText.getText());
                    }
                }
        );

        weightSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (!oldValue.equals(newValue)) {
                        setTextFieldValue(weightText, weightSlider.getValue());
                        getItem().setWeight(weightSlider.getValue());
                    }
                }
        );
    }

    @Override
    protected void updateItem(TextPrompt item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            textText.setText(item.getText());
            setTextFieldValue(weightText, item.getWeight());
            weightSlider.setValue(item.getWeight());
            setGraphic(hbox);
        }
    }

    /**
     * Handler when pressing Minus button
     * @param actionEvent   Action Event
     */
    public void handleMinusPromptButton(ActionEvent actionEvent) {
        if (getListView().getItems().size() > 1) {
            // At least to remain one item in the list
            getListView().getItems().remove(getItem());
        }
    }

    private void setTextFieldValue(TextField textField, double value) {
        textField.setText(String.format("%.2f", value));
    }
}
