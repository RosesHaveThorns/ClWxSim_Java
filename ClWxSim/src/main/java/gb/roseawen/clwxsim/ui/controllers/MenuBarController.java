package gb.roseawen.clwxsim.ui.controllers;

import gb.roseawen.clwxsim.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MenuBarController {

    private static final String githubURL = "https://github.com/RosesHaveThorns/ClWxSim_Java";
    public static final String docsURL = "https://docs.rose-brindle.dev/md/clwxsim/clwxsim_java";

    private static final Logger logger = LoggerFactory.getLogger("Main.MenuBarController");

    // File Menu
    @FXML
    public void handleQuitClick(final ActionEvent actionEvent) {
        logger.info("Quit Clicked");
        Main.quit();
    }

    // Sim Menu
    @FXML
    public void handleSimStartClick(final ActionEvent actionEvent) {
        logger.info("Sim Start Clicked");
    }

    @FXML
    public void handleSimPausePlayClick(final ActionEvent actionEvent) {
        logger.info("Sim Pause/Play Clicked");
    }

    @FXML
    public void handleSimResetClick(final ActionEvent actionEvent) {
        logger.info("Sim Reset Clicked");
    }

    // Help Menu

    @FXML
    public void handleAboutClick(final ActionEvent actionEvent) {
        logger.info("About Clicked");

        // Open Docs webpage
        try {
            Desktop.getDesktop().browse(new URI(docsURL));
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    @FXML
    public void handleGithubClick(final ActionEvent actionEvent) {
        logger.info("Github Clicked");

        // Open Github webpage
        try {
            Desktop.getDesktop().browse(new URI(githubURL));
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    public void handleGraphDrawTest(ActionEvent actionEvent) {

    }
}
