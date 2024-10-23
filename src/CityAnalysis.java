
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CityAnalysis {
    private List<City> cities;

    public CityAnalysis(String filename) {
        try {
            cities = Files.lines(Paths.get(filename))
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseCity)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            cities = new ArrayList<>();
        }
    }

    private City parseCity(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                return new City(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].replaceAll("[^0-9]", ""))
                );
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing population: " + line);
        }
        return null;
    }

    public void printTopThreeCitiesByState() {
        System.out.println("\n=== Top Three Cities by State ===");

        cities.stream()
                .collect(Collectors.groupingBy(City::getState))
                .forEach((state, stateCities) -> {
                    System.out.println("\n" + state + ":");
                    stateCities.stream()
                            .sorted((c1, c2) -> c2.getPopulation() - c1.getPopulation())
                            .limit(3)
                            .forEach(city -> System.out.println("  " + city));
                });
    }

    public void printLowestCityByState() {
        System.out.println("\n=== Lowest Population City by State ===");

        cities.stream()
                .collect(Collectors.groupingBy(
                        City::getState,
                        Collectors.minBy(Comparator.comparingInt(City::getPopulation))
                ))
                .forEach((state, cityOpt) -> {
                    cityOpt.ifPresent(city -> System.out.println(state + ": " + city));
                });
    }

    public void printCitiesWithSameName() {
        System.out.println("\n===Cities Sharing Names===");

        cities.stream()
                .collect(Collectors.groupingBy(City::getName))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .forEach(entry -> {
                    System.out.println("\n" + entry.getKey() + ":");
                    entry.getValue().forEach(city ->
                            System.out.println("  " + city));
                });
    }

    public void printArkansasCities() {
        System.out.println("\n===Cities in Arkansas===");

        List<City> arCities = cities.stream()
                .filter(city -> city.getState().equals("AR"))
                .sorted((c1, c2) -> c2.getPopulation() - c1.getPopulation())
                .collect(Collectors.toList());

        if (arCities.isEmpty()) {
            System.out.println("No cities found in Arkansas");
        } else {
            System.out.println("Found " + arCities.size() + " cities in Arkansas:");
            arCities.forEach(System.out::println);
        }
    }

    public void printArkansasLargestCityRank() {
        System.out.println("\n=== Arkansas Largest City National Rank ===");

        List<City> sortedCities = cities.stream()
                .sorted((c1, c2) -> c2.getPopulation() - c1.getPopulation())
                .collect(Collectors.toList());

        //find the cities in arkansas
        for (int i = 0; i < sortedCities.size(); i++) {
            City city = sortedCities.get(i);
            if (city.getState().equals("AR")) {
                System.out.printf("%s, AR ranks #%d nationally with population: %,d%n",
                        city.getName(), i + 1, city.getPopulation());
                break;
            }
        }
    }
    }