/**
 * Created by gabriel on 06/03/16.
 */
public class Utilities {

    /**
     * Converts a literal number to its numerical form. Must be between 1 and 10
     * @return The converted number or 0 if not a valid literal number
     */
    public static double parseLiteralNumber(String litNum) {
        switch (litNum) {
            case "one":
                return 1.0;
            case "two":
                return 2.0;
            case "three":
                return 3.0;
            case "four":
                return 4.0;
            case "five":
                return 5.0;
            case "six":
                return 6.0;
            case "seven":
                return 7.0;
            case "eight":
                return 8.0;
            case "nine":
                return 9.0;
            case "ten":
                return 10.0;
        }
        return 0.0;
    }
}
