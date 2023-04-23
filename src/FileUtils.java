import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

public class FileUtils {

    /**
     * path of file of person to be import
     */
    private final static String PATH_TO_PERSON_IMPORT = "resources/persons.csv";
    /**
     * path of file of statistic to be saved
     */
    private final static String PATH_TO_STATISTIC = "resources/statistic.txt";

    /**
     * Read list of Persons per line from CSV
     * 
     * @return person list from the csv file
     */

    public static List<Person> readPersonsFromCsv() {
        try {
            String line;
            List<Person> personList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(PATH_TO_PERSON_IMPORT));
            while ((line = br.readLine()) != null) {
                String[] split = line.split(";");
                if (split.length == 4) { // need 4 params to create person
                    try {
                        Person person = new Person(Long.parseLong(split[0]), split[1], split[2],
                                Integer.parseInt(split[3]));
                        personList.add(person);
                    } catch (NumberFormatException e) {
                        System.out.printf("Skip the process of the line [%s] because " +
                                "parameters are not valide \n", line);
                    }
                } else {
                    System.out.printf("Skip the process of the line [%s] because " +
                            "don't have 4 params \n", line);
                }
            }
            br.close();
            return personList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write statistic of the Workout-Tracker to file
     * 
     * @param persons  list of person to track
     * @param workouts list of workout
     */
    public static void writeStatisticToFile(List<Person> persons, List<Workout> workouts) {
        if (persons != null && workouts != null) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(PATH_TO_STATISTIC, false));
                // start write person to the file
                for (int i = 0; i < persons.size(); i++) {
                    Person person = persons.get(i);
                    try {
                        String sb = "--- Person " + (i + 1) + " ---\n" +
                                "Name: " + person.getLastName() + " " + person.getFirstName() + " (" + person.getAge()
                                + ")\n" +
                                "Number of running exercises: "
                                + getNumberOfBikingWorkoutByPerson(person.getId(), workouts) + "\n" +
                                "Number of swimming exercises: "
                                + getNumberOfSwimmingWorkoutByPerson(person.getId(), workouts) + "\n" +
                                "Average duration: " + getAverageDurationOfWorkoutByPerson(person.getId(), workouts)
                                + "min \n";
                        bw.write(sb);
                    } catch (Exception e) {
                        System.out.printf("Error! Skip to write the Person with id=%s", person.getId());
                    }
                }
                // start write statistic to the file
                String sb = "--- Biking  ---\n" +
                        "Average distance: " + getAverageDistanceByWorkout(workouts, BikingWorkout.class) + "m\n" +
                        "Average duration: " + getAverageDurationByWorkout(workouts, BikingWorkout.class) + "mim\n" +
                        "# mountain: " + getNumberOfBikingWorkoutByType(workouts, BikingType.MOUNTAIN) + "\n" +
                        "# road: " + getNumberOfBikingWorkoutByType(workouts, BikingType.ROAD) + "\n";
                // write statistic of Biking
                bw.write(sb);

                sb = "--- Swimming  ---\n" +
                        "Average distance: " + getAverageDistanceByWorkout(workouts, SwimmingWorkout.class) + "m\n" +
                        "Average duration: " + getAverageDurationByWorkout(workouts, SwimmingWorkout.class) + "mim\n" +
                        "# backstroke: " + getNumberOfSwimmingWorkoutByType(workouts, SwimmingType.BACKSTROKE) + "\n" +
                        "# butterfly: " + getNumberOfSwimmingWorkoutByType(workouts, SwimmingType.BUTTERFLY) + "\n";
                // write statistic of Swimming
                bw.write(sb);
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Get average distance by workout
     * 
     * @param workouts list of workout
     * @param workout  workout class use to filter
     * @return Average distance
     */
    private static double getAverageDistanceByWorkout(List<Workout> workouts, Class<?> workout) {
        if (workouts != null && !workouts.isEmpty() && workout != null) {
            return getAverageDistance(workouts.stream().filter(workout::isInstance).toList());
        }
        return 0.0;
    }

    /**
     * Get average duration by workout
     * 
     * @param workouts list of workout
     * @param workout  workout class use to filter
     * @return Average duration
     */
    private static double getAverageDurationByWorkout(List<Workout> workouts, Class<?> workout) {
        if (workouts != null && !workouts.isEmpty() && workout != null) {
            return getAverageDuration(workouts.stream().filter(workout::isInstance).toList());
        }
        return 0.0;
    }

    /**
     * Get number of biking workout by type
     * 
     * @param workouts list of workout
     * @param type     type of BikingWorkout
     * @return number of Biking
     */
    private static int getNumberOfBikingWorkoutByType(List<Workout> workouts, BikingType type) {
        if (workouts != null && !workouts.isEmpty() && type != null) {
            return workouts.stream()
                    .filter(BikingWorkout.class::isInstance)
                    .map(BikingWorkout.class::cast)
                    .filter(p -> type == p.getType())
                    .toList().size();
        }
        return 0;
    }

    /**
     * Get number of swimming workout by type
     * 
     * @param workouts list of workout
     * @param type     type of SwimmingWorkout
     * @return number of Biking
     */
    private static int getNumberOfSwimmingWorkoutByType(List<Workout> workouts, SwimmingType type) {
        if (workouts != null && !workouts.isEmpty() && type != null) {
            return workouts.stream()
                    .filter(SwimmingWorkout.class::isInstance)
                    .map(SwimmingWorkout.class::cast)
                    .filter(p -> type == p.getType())
                    .toList().size();
        }
        return 0;
    }

    /**
     * Get average duration of workout by person
     * 
     * @param personId    id of the person
     * @param workoutList list of workout
     * @return average duration of workout by person
     */
    private static double getAverageDurationOfWorkoutByPerson(Long personId, List<Workout> workoutList) {
        if (personId != null && workoutList != null && !workoutList.isEmpty()) {
            return getAverageDuration(workoutList.stream()
                    .filter(p -> p.getPersonId().equals(personId))
                    .toList());
        }
        return 0;
    }

    /**
     * Get number of biking workout by person
     * 
     * @param personId    id of the person
     * @param workoutList list of workout
     * @return number of biking workout by person
     */
    private static int getNumberOfBikingWorkoutByPerson(Long personId, List<Workout> workoutList) {
        if (personId != null && workoutList != null && !workoutList.isEmpty()) {
            return workoutList.stream()
                    .filter(p -> p.getPersonId().equals(personId))
                    .filter(BikingWorkout.class::isInstance)
                    .toList().size();
        }
        return 0;
    }

    /**
     * Get number of swimming workout by person
     * 
     * @param personId    id of the person
     * @param workoutList list of workout
     * @return number of swimming workout by person
     */
    private static int getNumberOfSwimmingWorkoutByPerson(Long personId, List<Workout> workoutList) {
        if (personId != null && workoutList != null && !workoutList.isEmpty()) {
            return workoutList.stream()
                    .filter(p -> p.getPersonId().equals(personId))
                    .filter(SwimmingWorkout.class::isInstance)
                    .toList().size();
        }
        return 0;
    }

    /**
     * Get average distance
     * 
     * @param workouts
     * @return average distance of workout
     */
    private static double getAverageDistance(List<? extends Workout> workouts) {
        if (workouts != null && !workouts.isEmpty()) {
            return workouts.stream()

                    .mapToDouble(Workout::getDistance)
                    .average().orElse(0.0);
        }
        return 0.0;
    }

    /**
     * Get average duration
     * 
     * @param workouts
     * @return average duration of workout
     */
    private static double getAverageDuration(List<? extends Workout> workouts) {
        if (workouts != null && !workouts.isEmpty()) {
            return workouts.stream()
                    .mapToDouble(Workout::getDuration)
                    .average().orElse(0.0);
        }
        return 0.0;
    }
}
