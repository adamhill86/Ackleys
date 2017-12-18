package edu.odu.cs480;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        double min = -5.0, max = 5.0;
        Point initial = new Point(ThreadLocalRandom.current().nextDouble(min, max), ThreadLocalRandom.current().nextDouble(min, max));
        Ackleys ackleys = new Ackleys(min, max, initial);

        System.out.println("Hill Climbing:");
        List<Point> minima = hillClimbing(ackleys, min, max);
        ackleys = new Ackleys(min, max, initial);
        System.out.println("\n\nDifferential Evolution:");
        List<Point> diffEvoMinima = differentialEvolution(ackleys);
    }

    /**
     * Run the hill climbing algorithm 100 times and output the results
     * @param ackleys
     * @param min
     * @param max
     * @return
     */
    public static List<Point> hillClimbing(Ackleys ackleys, double min, double max) {
        List<Point> minima = new ArrayList<>();
        System.out.println("Local minima found:");

        for (int i = 0; i < 100; i++) {
            Point minimum = ackleys.hillClimb();
            System.out.println(ackleys.ackleys(minimum));
            minima.add(ackleys.hillClimb());
            Point initial = new Point(ThreadLocalRandom.current().nextDouble(min, max), ThreadLocalRandom.current().nextDouble(min, max));
            ackleys.setCurrentPosition(initial);
        }

        return minima;
    }

    /**
     * Run the differential evolution algorithm 100 times and output the results
     * @param ackleys
     * @return
     */
    public static List<Point> differentialEvolution(Ackleys ackleys) {
        List<Point> minima = new ArrayList<>();

        System.out.println("Minima found:");

        for (int i = 0; i < 100; i++) {
            Point minimum = ackleys.diffEvo();
            minima.add(minimum);
        }

        return minima;
    }
}
