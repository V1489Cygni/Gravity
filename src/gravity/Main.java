package gravity;

import gravity.simulation.GObject;
import gravity.simulation.Simulation;
import gravity.solvers.AdamsSolver;
import gravity.solvers.DESolver;
import gravity.solvers.EulerSolver;
import gravity.solvers.RKSolver;
import gravity.systems.DESystem;
import gravity.visualisation.Visualiser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Map<String, String> parameters = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String fileName;
        if (args.length == 0) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter simulation file name: ");
            fileName = sc.next() + ".txt";
        } else {
            if (args.length > 1) {
                System.err.println("Warning: extra arguments will be ignored!");
            }
            fileName = args[0];
        }
        parseOptions(fileName);
        int num = Integer.parseInt(get("num"));
        double G = Double.parseDouble(get("G"));
        double t = Double.parseDouble(get("t"));
        double step = Double.parseDouble(get("step"));
        double[] y = new double[4 * num];
        GObject[] objects = new GObject[num];
        for (int i = 0; i < num; i++) {
            String key = Integer.toString(i + 1) + ".";
            y[4 * i] = Double.parseDouble(get(key + "x"));
            y[4 * i + 1] = Double.parseDouble(get(key + "y"));
            y[4 * i + 2] = Double.parseDouble(get(key + "vx"));
            y[4 * i + 3] = Double.parseDouble(get(key + "vy"));
            objects[i] = new GObject(Double.parseDouble(get(key + "m")), Double.parseDouble(get(key + "r")));
        }
        if (Boolean.parseBoolean(get("normalize"))) {
            normalize(y, objects);
        }
        Simulation simulation = new Simulation(G, objects);
        DESolver solver = getSolver(get("solver"), simulation, step, t, y);
        String mode = get("mode");
        switch (mode) {
            case "v":
                double cx = Double.parseDouble(get("camera_x")), cy = Double.parseDouble(get("camera_y")), scale = Double.parseDouble(get("camera_scale"));
                Visualiser.run(solver, objects, y, cx, cy, scale);
                break;
            case "c":
                String file = get("file");
                int stepsNum = Integer.parseInt(get("steps_num"));
                Writer writer = new BufferedWriter(new FileWriter(new File(file)));
                writer.write(arrayToString(y) + "\n");
                double[] result = new double[4 * num];
                for (int i = 0; i < stepsNum; i++) {
                    solver.next(result);
                    writer.write(arrayToString(result) + "\n");
                }
                writer.close();
                break;
            default:
                System.err.println("Error: unrecognized mode!");
                break;
        }
    }

    private static void parseOptions(String fileName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (!line.equals("") && line.charAt(0) != '#') {
                int d = line.indexOf('=');
                parameters.put(line.substring(0, d), line.substring(d + 1));
            }
        }
    }

    private static String get(String key) {
        if (!parameters.containsKey(key)) {
            System.err.println("Error: undefined option " + key);
            System.exit(0);
        }
        return parameters.get(key);
    }

    private static DESolver getSolver(String name, DESystem system, double step, double x, double[] y) {
        switch (name) {
            case "E":
                return new EulerSolver(system, step, x, y);
            case "R":
                return new RKSolver(system, step, x, y);
            case "A":
                return new AdamsSolver(system, step, x, y, new EulerSolver(system, step, x, y));
        }
        System.err.println("Error: unrecognized solver!");
        System.exit(0);
        throw new AssertionError();
    }

    private static String arrayToString(double[] y) {
        String result = "";
        for (double d : y) {
            result += d + " ";
        }
        return result;
    }

    private static void normalize(double[] y, GObject[] objects) {
        double px = 0, py = 0, m = 0;
        for (int i = 0; i < objects.length; i++) {
            m += objects[i].m;
            px += y[4 * i + 2] * objects[i].m;
            py += y[4 * i + 3] * objects[i].m;
        }
        px /= m;
        py /= m;
        for (int i = 0; i < objects.length; i++) {
            y[4 * i + 2] -= px;
            y[4 * i + 3] -= py;
        }
    }
}
