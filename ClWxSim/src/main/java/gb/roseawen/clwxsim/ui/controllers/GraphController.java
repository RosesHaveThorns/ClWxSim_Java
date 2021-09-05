package gb.roseawen.clwxsim.ui.controllers;

import ch.qos.logback.classic.Level;
import gb.roseawen.clwxsim.sim.Planet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import org.jetbrains.skija.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Timer;

public class GraphController extends SceneController {

    private static final Logger logger = LoggerFactory.getLogger("Main.GraphController");

    public ImageView mainGraph;

    private static final double SURFACE_WIDTH_SCALE = 0.8;
    private static final double SURFACE_HEIGHT_SCALE = 0.8;

    private static final int TICK_AMNT = 10;

    private static final Typeface typeface = FontMgr.getDefault().matchFamilyStyle("Arial", FontStyle.NORMAL);
    private static final Font font = new Font(typeface, 14);

    @FXML
    public void reloadGraph(ActionEvent actionEvent) {
        double angvel = 0.00007292115;

        long startMillis = 0;
        if(System.getenv("DEVMODE") != null) {
            startMillis = System.currentTimeMillis();
        }

        // todo: use main planet not a new one
        Surface surface = drawNewGraph(new Planet(100, 100, angvel));
        updateGraphImg(surface);

        if(startMillis != 0) {
            logger.debug("Drew graph in " + (System.currentTimeMillis() - startMillis) + "ms");
        }
    }

    @FXML
    public void testGraph(ActionEvent actionEvent) {

    }

    private Surface drawNewGraph(Planet planet) {
        int surfaceWidth = (int) Math.round(mainGraph.getScene().getWidth() * SURFACE_WIDTH_SCALE);
        int surfaceHeight = (int) Math.round(mainGraph.getScene().getHeight() * SURFACE_HEIGHT_SCALE);


        Surface surface = Surface.makeRasterN32Premul(surfaceWidth, surfaceHeight);
        Canvas canvas = surface.getCanvas();

        int padding = 50;
        float[] origin = {padding, surfaceHeight - padding};
        float graph_width = surfaceWidth - padding*2;
        float graph_height = surfaceHeight - padding*2;

        float graph_cell_width = graph_width / planet.getWidth();
        float graph_cell_height = graph_height / planet.getHeight();

        drawAxis(canvas, origin, graph_width, graph_height, planet);
        drawPressure(canvas, origin, graph_cell_width, graph_cell_height, planet);

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

    private void drawAxis(Canvas canvas, float[] origin, float graph_width, float graph_height, Planet planet) {
        Paint paint = new Paint();

        // draw axis lines
        canvas.drawLine(origin[0], origin[1], origin[0] + graph_width, origin[1], paint);
        canvas.drawLine(origin[0], origin[1], origin[0], origin[1] - graph_height, paint);

        // draw x axis ticks
        for (int i=0; i <= TICK_AMNT; i++) {
            int tick_x_pos = Math.round(origin[0] + (graph_width / TICK_AMNT) * i);

            String tick_val = String.valueOf((planet.getWidth() / TICK_AMNT) * i);
            float str_width = font.measureText(tick_val, paint).getWidth();
            float str_height = font.measureText(tick_val, paint).getHeight();
            canvas.drawString(tick_val, tick_x_pos - (str_width / 2), origin[1] + (15 + str_height), font, paint);

            canvas.drawLine(tick_x_pos, origin[1], tick_x_pos, origin[1]+10, paint);
        }

        // draw y axis ticks
        for (int i=0; i <= TICK_AMNT; i++) {
            int tick_y_pos = Math.round(origin[1] - (graph_height / TICK_AMNT) * i);

            String tick_val = String.valueOf((planet.getHeight() / TICK_AMNT) * i);
            float str_width = font.measureText(tick_val, paint).getWidth();
            float str_height = font.measureText(tick_val, paint).getHeight();
            canvas.drawString(tick_val, origin[0] - (15 + str_width), tick_y_pos + (str_height / 2), font, paint);

            canvas.drawLine(origin[0], tick_y_pos, origin[0]-10, tick_y_pos, paint);
        }
    }

    private void drawPressure(Canvas canvas, float[] origin, float graph_cell_width, float graph_cell_height, Planet planet) {
        double[][] p = planet.getAirPressure();
        int h = planet.getHeight();
        int w = planet.getWidth();

        double expected_max_p = 1030;
        double expected_min_p = 1000;

        Paint paint = new Paint();
        Rect rect;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float cell_btm = origin[1] - (y * graph_cell_height);
                float cell_lft = origin[0] + (x * graph_cell_width);
                float cell_top = origin[1] - ((y+1) * graph_cell_height);
                float cell_rgt = origin[0] + ((x+1) * graph_cell_width);
                rect = Rect.makeLTRB(cell_lft, cell_top, cell_rgt, cell_btm);

                double rgb_val = ((p[x][y] - expected_min_p) / (expected_max_p - expected_min_p)) * 256;
                int r = (int)Math.round(rgb_val);
                int b = 255 - (int)Math.round(rgb_val);

                String hex = String.format("0xFF%02X%02X%02X", r, 0, b);
                long val = Long.decode(hex);
                paint.setColor((int)val);
                canvas.drawRect(rect, paint);
            }
        }
    }
}
