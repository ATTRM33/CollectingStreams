public class Main {
    public static void main(String[] args) {
        CityAnalysis analyzer = new CityAnalysis("cities.txt");

        analyzer.printTopThreeCitiesByState();
        analyzer.printLowestCityByState();
        analyzer.printCitiesWithSameName();
        analyzer.printArkansasCities();
        analyzer.printArkansasLargestCityRank();
    }
}
