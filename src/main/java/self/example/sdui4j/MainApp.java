package self.example.sdui4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import self.example.sdui4j.ctrls.MainController;
import self.example.sdui4j.services.ApiRequestService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Main Program of the JavaFX Application
 * @author Teki Chan
 * @since 16 Mar 2024
 */
public class MainApp extends Application {
    private static Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static final int APP_WIDTH = 1280;
    private static final int APP_HEIGHT = 768;
    private static final String APP_TITLE = "Stable Diffusion DEMO";
    private static final String APP_FXML_PATH = "fxml/main_app.fxml";
    private static final String PROPERTIES_FILE = "sdui4j.properties";
    private static final String STABILITY_API_KEY_FIELD = "STABILITY_API_KEY";
    private ApiRequestService apiRequestService;

    @Override
    public void start(Stage stage) throws IOException {
        // Initiate service objects
        if (apiRequestService == null) {
            String apiKey = this.getParameters().getRaw().get(0);
            apiRequestService = new ApiRequestService(apiKey, createObjectMapper());
        }

        // Initiate UI from FXML
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(APP_FXML_PATH));
        Parent root = loader.load();

        // Retrieve a list of engine IDs
        MainController mainController = loader.getController();
        mainController.setApiRequestService(apiRequestService);
        mainController.configureEngineList();

        // Display the UI
        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        String apiKey = getApiKey();
        if (apiKey == null) {
            throw new IOException("API Key must be provided.");
        }

        Application.launch(MainApp.class, apiKey);
    }

    private static String getApiKey() {
        // Retrieve API Key from Environment Variable
        String apiKey = System.getenv(STABILITY_API_KEY_FIELD);
        if (apiKey == null) {
            // Otherwise from the default properties file
            Properties appProps = new Properties();
            try ( FileInputStream fis = new FileInputStream(PROPERTIES_FILE) ) {
                appProps.load(fis);
            } catch(Exception ex) {
                logger.error("Unable to read API Key resource", ex);
            }
            return appProps.getProperty(STABILITY_API_KEY_FIELD);
        } else {
            return apiKey;
        }
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper;
    }
}