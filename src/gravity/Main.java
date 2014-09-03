package gravity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Simulation getSimulationFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        double width = scanner.nextDouble(), height = scanner.nextDouble();
        List<GravityObject> objects = new ArrayList<>();
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.length() == 0 || s.charAt(0) == '#') {
                continue;
            }
            Scanner sc = new Scanner(s);
            double x = sc.nextDouble(), y = sc.nextDouble(), m = sc.nextDouble(), r = sc.nextDouble();
            double vx = sc.nextDouble(), vy = sc.nextDouble();
            objects.add(new GravityObject(new Vector(x, y), m, r, new Vector(vx, vy)));
        }
        return new Simulation(width, height, objects);
    }

    public static void main(String[] args) {
        String filename = "simulation.in";
        try {
            GravityFrame frame = new GravityFrame(getSimulationFromFile(filename));
            while (true) {
                Thread.sleep(5);
                frame.repaint();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot load simulation from file: " + filename);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted");
        }
    }
}
