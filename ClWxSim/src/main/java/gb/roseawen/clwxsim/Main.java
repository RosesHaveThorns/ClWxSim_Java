package gb.roseawen.clwxsim;

import ch.qos.logback.classic.Level;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

// if env var DEVMODE exists during startup, will run in debug/developer mode

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger("Main");

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage rootStage) throws Exception {
        // Set logger level
        ch.qos.logback.classic.Logger parentLogger =
                (ch.qos.logback.classic.Logger) logger;
        if(System.getenv("DEVMODE") != null) {
            parentLogger.setLevel(Level.TRACE);
        }
        else {
            parentLogger.setLevel(Level.WARN);
        }

        // Load base scene
        logger.info("Starting application");

        rootStage.setTitle("ClWxSim");
        rootStage.setScene(loadScene("graphs"));

        rootStage.show();
    }

    private Scene loadScene(String fxmlFileName) {
        try {
            return new Scene(loadFXML(fxmlFileName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Parent loadFXML(String fileName) throws IOException {
        URL url = getClass().getResource("/fxml/" + fileName + ".fxml");
        if (url == null) {
            throw new FileNotFoundException("Missing fxml file in resources/fxml/");
        }

        logger.info("Loaded " + fileName + " fxml file");
        return FXMLLoader.load(url);
    }

    public static void quit() {
        Platform.exit();
        System.exit(0);
    }
}