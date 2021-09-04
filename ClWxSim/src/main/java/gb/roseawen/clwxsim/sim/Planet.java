package gb.roseawen.clwxsim.sim;

/**
 * A representation of a planet's terrain, climate and weather
 *     Planned Attributes:
 *         air_humidity (float array): Size defined by grid_size
 *         air_vel_u (tuple array): Array storing x vectors representing the wind velocity in each grid square, Size defined by grid_size
 *         air_vel_v (tuple array): Array storing v vectors representing the wind velocity in each grid square, Size defined by grid_size
 *         air_temp (float array): Size defined by grid_size
 *         air_pressure (float array): Array storing the air pressure at sea level in each grid square, measured in mbar, size defined by grid_size
 *         old_air_pressure (float array): Array storing a copy of air_pressure as it was before the last tick
 *         air_precip (float array): Size defined by grid_size
 *         ground_temp (float array): Size defined by grid_size
 *         ground_height (float array): Size defined by grid_size
 *         ground_water (float array): Size defined by grid_size
 *         grid_size (tuple): Height and Width of all 2D world data arrays
 *         grid_sq_size (int): Height and Width of each grid square, measured in km
 *         world_name (str): The name of the world
 *         atmos_height (float): Height of the World's atmosphere assuming a uniform density, measured in km
 *         grid_sq_vol (float): An esimation of the volume of air a grid square holds, measured in km^3
 *         angular_vel (float): Angular velocity of planet, measured in rad/s
 */
public class Planet {
    private double angular_vel;
    double starting_pressure = 1013.;

    private double[][] air_vel_u;
    private double[][] air_vel_v;
    private double[][] air_pressure;

    private int planet_grid_width;
    private int planet_grid_height;
    private int grid_width;
    private int grid_height;

    public Planet(int width, int height, double ang_vel) {
        angular_vel = ang_vel;

        planet_grid_height = height;
        planet_grid_width = width;
        grid_height = height + 2;
        grid_width = width + 2;

        // setup arrays
        air_vel_u = new double[grid_width][grid_height];
        air_vel_v = new double[grid_width][grid_height];
        air_pressure = new double[grid_width][grid_height];

        fill2DArray(air_vel_u, 0);
        fill2DArray(air_vel_v, 0);
        fill2DArray(air_pressure, starting_pressure);
    }

    public void reset() {
        air_vel_u = new double[grid_width][grid_height];
        air_vel_v = new double[grid_width][grid_height];
        air_pressure = new double[grid_width][grid_height];

        fill2DArray(air_vel_u, 0);
        fill2DArray(air_vel_v, 0);
        fill2DArray(air_pressure, starting_pressure);
    }

    private void fill2DArray(double[][] arr, double val) {
        for (int i = 0; i < arr[0].length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = val;
            }
        }
    }
}
