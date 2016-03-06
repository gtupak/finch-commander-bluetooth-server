/**
 * Created by gabriel on 06/03/16.
 */
public class Utilities {
    public enum Commands {
        MOVE_FORWARD,
        MOVE_BACKWARDS,
        TURN_RIGHT,
        TURN_LEFT,
        SPEAK
    }

    /**
     * Converts a literal number to its numerical form. Must be between 1 and 10
     * @return the converted number or 0 if not a valid literal number
     */
    public int parseLiteralNumber(String litNum) {
        switch (litNum) {
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            case "nine":
                return 9;
            case "ten":
                return 10;
        }
        return 0;
    }
}
