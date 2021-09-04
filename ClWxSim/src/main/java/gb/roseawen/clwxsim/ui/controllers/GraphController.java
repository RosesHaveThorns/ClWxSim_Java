package gb.roseawen.clwxsim.ui.controllers;

import gb.roseawen.clwxsim.sim.Planet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import org.jetbrains.skija.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class GraphController extends SceneController {

    public ImageView mainGraph;

    private static final Logger logger = LoggerFactory.getLogger("Main.GraphController");

    @FXML
    public void reloadGraph(ActionEvent actionEvent) {
        double angvel = 0.00007292115;
        Surface surface = drawNewGraph(new Planet(100, 100, angvel));
        updateGraphImg(surface);
    }

    public void testGraph(ActionEvent actionEvent) {

    }

    private Surface drawNewGraph(Planet planet) {
        Surface surface = Surface.makeRasterN32Premul(250, 250);
        Canvas canvas = surface.getCanvas();

        Paint paint = new Paint();
        canvas.drawCircle(50, 50, 30, paint);

        return surface;
    }

    private void updateGraphImg(Surface surface) {
        Image image = surface.makeImageSnapshot();
        Data data = image.encodeToData(EncodedImageFormat.PNG);
        assert data != null;
        byte[] pngBytes = data.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngBytes);

        javafx.scene.image.Image imageFx = new javafx.scene.image.Image(inputStream);
        mainGraph.setImage(imageFx);

        logger.info("Reloaded graph");
    }
}
