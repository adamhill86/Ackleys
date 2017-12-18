package edu.odu.cs480;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Ackleys {
    private double domainMin, domainMax; // hold the minimum and maximum range values
    private Point currentPosition, initialPosition;
    private Random random;

    public Ackleys(double min, double max, Point initialPosition) {
        domainMin = min;
        domainMax = max;
        this.initialPosition = initialPosition;
        currentPosition = new Point(initialPosition.getX(), initialPosition.getY());
        random = new Random();
    }

    /**
     * This function was for testing purposes only.
     * It calculates f(x,y) for all x,y in [domainMin, domainMax] in step increments of 0.1.
     */
    public void ackleys() {
        for (double y = domainMin; y <= domainMax; y += 0.1) {
            for (double x = domainMin; x <= domainMax; x += 0.1) {
                System.out.print((-20 * Math.exp(-0.2 * Math.sqrt(0.5 * ((x*x) + (y*y)))))
                        - (Math.exp(0.5 * (Math.cos(2*Math.PI*x) + Math.cos(2*Math.PI*y)))));
                System.out.print(" ");
                if (x == domainMax || (x > 4.9 && x < 5.0)) {
                    System.out.print("\n");
                }
            }
        }
    }

    /**
     * Calculates the value of the Ackleys function at f(x,y)
     * @param x The current x value
     * @param y The current y value
     * @return The value of the Ackleys function at f(x,y)
     */
    public double ackleys(double x, double y) {
        return ((-20 * Math.exp(-0.2 * Math.sqrt(0.5 * ((x*x) + (y*y)))))
                - (Math.exp(0.5 * (Math.cos(2*Math.PI*x) + Math.cos(2*Math.PI*y)))));
    }

    public double ackleys(Point p) {
        return ackleys(p.getX(), p.getY());
    }

    /**
     * The hill climbing algorithm described in the assignment notes.
     * From the current position, it generates a new x and y position.
     * Then, it computes the Ackleys(x',y') and compares the value to where we started.
     * If the new value is less than the previous value, store the result.
     * Otherwise, increase the step account and generate a new position.
     * Once we've gone 100 steps without finding a better value, return the best we'd found.
     * @return The best local minimum found
     */
    public Point hillClimb() {
        int steps = 0;
        double previousAckleys = ackleys(currentPosition.getX(), currentPosition.getY());
        Point minimum = new Point(currentPosition.getX(), currentPosition.getY());

        while (steps != 100) {
            newPosition();
            double newAckleys = ackleys(currentPosition.getX(), currentPosition.getY());
            if (newAckleys < previousAckleys) {
                minimum = new Point(currentPosition.getX(), currentPosition.getY());
                steps = 0;
                previousAckleys = newAckleys;
            } else {
                steps++;
            }
        }

        return minimum;
    }

    /**
     * A differential evolution algorithm as described in the class notes.
     * First it initializes its parameters.
     * Then it initializes its population of agents
     * It then selects a candidate agent (currentAgent) from the list.
     * It also randomly selects 3 other agents a, b, c, none of which can be the same or equal currentAgent
     * Next, it computes the mutant vector V = a + F(b-c)
     * It then generates a random number rand from [0-1)
     * Check to see if rand < Cr for each coordinate in the agent. If it is, swap the coordinate with the mutant vector
     * If Ackleys(mutantAgent) < Ackleys(currentAgent) set currentAgent to the mutant
     * When this has been done numCycles times, find the best point and return it
     * @return The best minimum the function could find
     */
    public Point diffEvo() {
        int populationSize = 20; // Np
        int numCycles = 100; // Nc
        double crossoverRecombProb = 0.1; // Cr, crossover and recombination probability
        double differentialWeight = 0.8; // F
        int steps = 0;
        Point minimum = generateRandomPoint();
        List<Point> agents = new ArrayList<>();

        // initialize agents
        for (int i = 0; i < populationSize; i++) {
            agents.add(generateRandomPoint());
        }

        while (steps < numCycles) {
            for (int i = 0; i < populationSize; i++) {
                Point currentAgent = agents.get(i);
                List<Integer> availableIndices = new ArrayList<>();
                Point a = new Point(), b = new Point(), c = new Point();

                // get a list of all available indices (not including i)
                for (int j = 0; j < populationSize; j++) {
                    if (j != i) {
                        availableIndices.add(j);
                        //System.out.println("Adding " + j);
                    }
                }

                // randomly select 3 indices from the list
                for (int k = 0; k < 3; k++) {
                    int rand = random.nextInt(availableIndices.size());
                    int index = availableIndices.get(rand);

                    // create three agents from the randomly select indices
                    if (k == 0) {
                        a = new Point(agents.get(index));
                        //System.out.println("A: " + a + ", " + index);
                    } else if (k == 1) {
                        b = new Point(agents.get(index));
                        //System.out.println("B: " + b + ", " + index);
                    } else {
                        c = new Point(agents.get(index));
                        //System.out.println("C: " + c + ", " + index);
                    }
                    availableIndices.remove(rand);
                }

                // compute mutant vector
                Point difference = Point.difference(b, c);
                Point mutant = Point.add(a, Point.multiply(difference, differentialWeight));
                Point mutantAgent = new Point(currentAgent);

                // check for crossover
                for (int j = 0; j < 2; j++) {
                    double rand = random.nextDouble();
                    if (rand < crossoverRecombProb) {
                        if (j == 0) {
                            // replace the x-coordinate in currentAgent
                            mutantAgent.setX(mutant.getX());
                        } else {
                            // replace the y-coordinate
                            mutantAgent.setY(mutant.getY());
                        }
                    }
                }

                // evaluate function values
                double currentMutantScore = ackleys(mutantAgent);
                double currentAgentScore = ackleys(currentAgent);

                // if we found a better score, save it
                if (currentMutantScore < currentAgentScore) {
                    currentAgent.setX(mutantAgent.getX());
                    currentAgent.setY(mutantAgent.getY());
                    steps = 0; // reset step count since we found a better minimum
                } else {
                    steps++; // keep looking
                }
            }
        }

        // find the point with the lowest Ackleys score
        Point first = agents.get(0);
        double minScore = ackleys(first);

        for (int i = 1; i < agents.size(); i++) {
            Point agent = agents.get(i);
            double score = ackleys(agent);
            if (score < minScore) {
                minimum = agent;
                minScore = score;
            }
        }
        //System.out.println("Min: " + minimum + ", " + minScore);
        System.out.println(minScore);

        return minimum;
    }

    /**
     * Generates a new random position (x,y) in (domainMin, domainMax)
     * @return The new random point
     */
    private Point generateRandomPoint() {
        return new Point(ThreadLocalRandom.current().nextDouble(domainMin, domainMax),
                ThreadLocalRandom.current().nextDouble(domainMin, domainMax));
    }

    /**
     * Calculates a new position based on the assignment notes.
     * x' = (rand() - 0.5)*0.1 + x
     * y’ = (rand() – 0.5)*0.1 + y
     */
    public void newPosition() {
        double newX = ((random.nextDouble() - 0.5) * 0.1) + currentPosition.getX();
        double newY = ((random.nextDouble() - 0.5) * 0.1) + currentPosition.getY();
        currentPosition = new Point(newX, newY);
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }
}
