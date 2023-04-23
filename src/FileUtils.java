import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

public class FileUtils {

    /**
     * Pfad zur CSV-Datei mit den Personen-Daten.
     */
    private final static String PATH_TO_PERSON_IMPORT = "resources/persons.csv";
    /**
     * Pfad zur Textdatei, in der die Statistik gespeichert wird.
     */
    private final static String PATH_TO_STATISTIC = "resources/statistic.txt";

    /**
     * Liest eine Liste von Personen aus der CSV-Datei und gibt sie zurück.
     * 
     * @return Liste von Personen aus der CSV-Datei
     */

    public static List<Person> readPersonsFromCsv() {
        try {
            String line;
            List<Person> personList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(PATH_TO_PERSON_IMPORT));
            while ((line = br.readLine()) != null) {
                String[] split = line.split(";");
                if (split.length == 4) { // es werden 4 Parameter benötigt, um eine Person zu erstellen
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
     * Schreibt die Statistik des Workout-Trackers in die Datei.
     * 
     * @param persons  Liste der Personen, deren Workouts getrackt werden sollen
     * @param workouts Liste der Workouts
     */

    public static void writeStatisticToFile(List<Person> persons, List<Workout> workouts) {
        if (persons != null && workouts != null) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(PATH_TO_STATISTIC, false));
                // schreibe Personen in die Datei
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
                // schreibet Statistik in die Datei
                String sb = "--- Biking  ---\n" +
                        "Average distance: " + getAverageDistanceByWorkout(workouts, BikingWorkout.class) + "m\n" +
                        "Average duration: " + getAverageDurationByWorkout(workouts, BikingWorkout.class) + "mim\n" +
                        "# mountain: " + getNumberOfBikingWorkoutByType(workouts, BikingType.MOUNTAIN) + "\n" +
                        "# road: " + getNumberOfBikingWorkoutByType(workouts, BikingType.ROAD) + "\n";
                // schreibet Statistik für Biking
                bw.write(sb);

                sb = "--- Swimming  ---\n" +
                        "Average distance: " + getAverageDistanceByWorkout(workouts, SwimmingWorkout.class) + "m\n" +
                        "Average duration: " + getAverageDurationByWorkout(workouts, SwimmingWorkout.class) + "mim\n" +
                        "# backstroke: " + getNumberOfSwimmingWorkoutByType(workouts, SwimmingType.BACKSTROKE) + "\n" +
                        "# butterfly: " + getNumberOfSwimmingWorkoutByType(workouts, SwimmingType.BUTTERFLY) + "\n";
                // schreibet Statistik für Swimming
                bw.write(sb);
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Durchschnittliche Distanz des Workouts abrufen
     *
     * @param workouts Liste der Workouts
     * @param workout  Workout-Klasse zur Filterung
     * @return Durchschnittliche Distanz
     */
    private static double getAverageDistanceByWorkout(List<Workout> workouts, Class<?> workout) {
        if (workouts != null && !workouts.isEmpty() && workout != null) {
            return getAverageDistance(workouts.stream().filter(workout::isInstance).toList());
        }
        return 0.0;
    }

    /**
     * Durchschnittliche Dauer des Workouts abrufen
     *
     * @param workouts Liste der Workouts
     * @param workout  Workout-Klasse zur Filterung
     * @return Durchschnittliche Dauer
     */
    private static double getAverageDurationByWorkout(List<Workout> workouts, Class<?> workout) {
        if (workouts != null && !workouts.isEmpty() && workout != null) {
            return getAverageDuration(workouts.stream().filter(workout::isInstance).toList());
        }
        return 0.0;
    }

    /**
     * Anzahl der Radfahr-Workouts nach Typ abrufen
     *
     * @param workouts Liste der Workouts
     * @param type     Typ des Radfahr-Workouts
     * @return Anzahl der Radfahr-Workouts
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
     * Anzahl der Schwimm-Workouts nach Typ abrufen
     * 
     * @param workouts Liste der Workouts
     * @param type     Typ des Schwimm-Workouts
     * @return Anzahl der Schwimm-Workouts
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
     * Durchschnittliche Dauer des Workouts pro Person abrufen
     *
     * @param personId    ID der Person
     * @param workoutList Liste der Workouts
     * @return Durchschnittliche Dauer des Workouts pro Person
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
     * Anzahl der Radfahr-Workouts pro Person
     *
     * @param personId    ID der Person
     * @param workoutList Liste der Workouts
     * @return Anzahl der Radfahr-Workouts pro Person
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
     * Anzahl der Schwimm-Workouts pro Person
     * 
     * @param personId    ID der Person
     * @param workoutList Liste der Workouts
     * @return Anzahl der Schwimm-Workouts pro Person
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
     * Durchschnittliche Distanz des Workouts abrufen
     *
     * @param workouts Liste der Workouts
     * @return Durchschnittliche Distanz des Workouts
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
     * Durchschnittliche Dauer des Workouts abrufen
     *
     * @param workouts Liste der Workouts
     * @return Durchschnittliche Dauer des Workouts
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
