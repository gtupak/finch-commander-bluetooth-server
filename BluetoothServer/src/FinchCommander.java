import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 * Created by gabriel on 06/03/16.
 *
 * Used to set up the Finch and execute commands
 */

public class FinchCommander {
    Finch mFinch;

    public FinchCommander() {
        mFinch = new Finch();

        mFinch.saySomething("The finch is ready to go");

        // Always disconnect with
        // mFinch.quit();
    }

    /**
     * Method for move commands
     * @param command A move command
     * @param duration A number in seconds
     */
    public void processCommand(Command command, double duration) {
        Double toConvert = new Double(duration * 1000);
        int convertedDuration = toConvert.intValue();
        switch (command) {
            case MOVE_FORWARD:
                mFinch.setWheelVelocities(255, 255, convertedDuration);
                break;
            case MOVE_BACKWARDS:
                mFinch.setWheelVelocities(-255, -255, convertedDuration);
                break;
            case TURN_RIGHT:
                mFinch.setWheelVelocities(255, -255, convertedDuration);
                break;
            case TURN_LEFT:
                mFinch.setWheelVelocities(-255, 255, convertedDuration);
                break;
            default:
                System.err.println("ERROR: Could not process move command.");
        }
    }

    /**
     * Method for speak commands
     * @param command Command.SPEAK
     * @param toSay
     */
    public void processCommand(Command command, String toSay) {
        if (command == Command.SPEAK)
            mFinch.saySomething(toSay);
    }
}
