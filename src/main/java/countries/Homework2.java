package countries;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.util.stream.Collectors.*;

import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Homework2 {

    private List<Country> countries;

    public Homework2() {
        countries = new CountryRepository().getAll();
    }

    static int charCount(String s, char c){

        int count = 0;
        for (char character: s.toLowerCase().toCharArray()){
            if (character == c){
                count++;
            }
        }
        return count;
    }

    static int vowelCount(String s){

        int count = 0;
        String vowels = "aeiou";
        for (char character: s.toLowerCase().toCharArray()){

            if (vowels.contains(Character.toString(character))){
                count++;
            }
        }
        return count;
    }

    static double populationDensity(Country country){
        if (country.getArea() == null){
            return Double.NaN;
        }else{
            return BigDecimal.valueOf(country.getPopulation()).divide(country.getArea(), RoundingMode.FLOOR).doubleValue();
        }
    }

    /**
     * Returns the longest country name translation.
     */
    public Optional<String> streamPipeline1() {
        return countries.stream().flatMap(country -> country.getTranslations().values().stream())
                .max(Comparator.comparing(String::length));
    }

    /**
     * Returns the longest Italian (i.e., {@code "it"}) country name translation.
     */
    public Optional<String> streamPipeline2() {
        return countries.stream().flatMap(country -> country.getTranslations().entrySet().stream()
                .filter(t -> t.getKey().equals("it"))).map(Map.Entry::getValue)
                .max(Comparator.comparing(String::length));
    }

    /**
     * Prints the longest country name translation together with its language code in the form language=translation.
     */
    public void streamPipeline3() {
        System.out.println(countries.stream().flatMap(country -> country.getTranslations().entrySet().stream())
                .max(Comparator.comparing(t -> t.getValue().length())).get());
    }

    /**
     * Prints single word country names (i.e., country names that do not contain any space characters).
     */
    public void streamPipeline4() {
        countries.stream().map(Country::getName).filter(name -> !name.contains(" ")).forEach(System.out::println);
    }

    /**
     * Returns the country name with the most number of words.
     */
    public Optional<String> streamPipeline5() {
        return countries.stream().map(Country::getName).max(Comparator.comparing(name -> name.split(" ").length));
    }

    /**
     * Returns whether there exists at least one capital that is a palindrome.
     */
    public boolean streamPipeline6() {
        return countries.stream().map(Country::getCapital).anyMatch(name -> name.equals(new StringBuilder(name).reverse().toString()));
    }

    /**
     * Returns the country name with the most number of {@code 'e'} characters ignoring case.
     */
    public Optional<String> streamPipeline7() {
        return countries.stream().map(Country::getName).max(Comparator.comparingInt(name -> charCount(name, 'e')));
    }

    /**
     *  Returns the capital with the most number of English vowels (i.e., {@code 'a'}, {@code 'e'}, {@code 'i'}, {@code 'o'}, {@code 'u'}).
     */
    public Optional<String> streamPipeline8() {
        return countries.stream().map(Country::getName).max(Comparator.comparingInt(Homework2::vowelCount));
    }

    /**
     * Returns a map that contains for each character the number of occurrences in country names ignoring case.
     */
    public Map<Character, Long> streamPipeline9() {
        return Stream.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z')
                .collect(Collectors
                .toMap(character -> character,
                        character -> Long.valueOf(charCount(countries.stream().map(Country::getName).collect(Collectors.joining()), character))));
    }

    /**
     * Returns a map that contains the number of countries for each possible timezone.
     */
    public Map<ZoneId, Long> streamPipeline10() {
        return countries.stream().flatMap(country -> country.getTimezones().stream())
                .distinct()
                .collect(Collectors.toMap(timezone -> timezone,
                        timezone -> countries.stream().flatMap(country -> country.getTimezones().stream()).filter(timezone::equals).count()));
    }

    /**
     * Returns the number of country names by region that starts with their two-letter country code ignoring case.
     */
    public Map<Region, Long> streamPipeline11() {
        return countries.stream().filter(country -> country.getCode().toLowerCase().equals(country.getName().toLowerCase().substring(0, 2)))
                .collect(Collectors.groupingBy(Country::getRegion, Collectors.counting()));
    }

    /**
     * Returns a map that contains the number of countries whose population is greater or equal than the population average versus the the number of number of countries with population below the average.
     */
    public Map<Boolean, Long> streamPipeline12() {
        return countries.stream()
                .collect(Collectors.partitioningBy(country -> (double) country.getPopulation() >=
                        countries.stream().mapToLong(Country::getPopulation).average().getAsDouble(), Collectors.counting()));
    }

    /**
     * Returns a map that contains for each country code the name of the corresponding country in Portuguese ({@code "pt"}).
     */
    public Map<String, String> streamPipeline13() {
        return countries.stream().collect(Collectors.toMap(Country::getCode, country -> country.getTranslations().get("pt")));
    }

    /**
     * Returns the list of capitals by region whose name is the same is the same as the name of their country.
     */
    public Map<Region, List<String>> streamPipeline14() {
        return countries.stream().filter(country -> country.getName().equals(country.getCapital()))
                .collect(Collectors.groupingBy(Country::getRegion, Collectors.mapping(Country::getCapital, Collectors.toList())));
    }

    /**
     *  Returns a map of country name-population density pairs.
     */
    public Map<String, Double> streamPipeline15() {
        return countries.stream().collect(Collectors.toMap(Country::getName, Homework2::populationDensity));
    }

}
