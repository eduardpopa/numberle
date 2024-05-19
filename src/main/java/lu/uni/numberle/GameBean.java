package lu.uni.numberle;

import java.io.Serializable;
import java.util.Objects;
import org.apache.logging.log4j.*;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lu.uni.numberle.ejb.SolutionGeneratorBean;
import lu.uni.numberle.ejb.SolutionGeneratorBeanLocal;

@SessionScoped
@Named("game")
public class GameBean implements Serializable {
    // TODO: Update faces-config and add page to use init state before start
    private final String STATE_INIT = "play";
    // TODO: Update faces-config and add play page when init is in place
    private final String STATE_PLAY = "play";
    private final String STATE_WIN = "win";
    private final String STATE_OVER = "over";
    // TODO: Update faces-config and add error page to show errors
    private final String STATE_ERROR = "error";
    // TODO: In the init page set this value
    private final int solutionSize = 6;
    // TODO: In the init page set this value
    private final int maxAttempts = 6;
    private static final Logger logger = LogManager.getLogger(GameBean.class);

    private int noAttempts;
    private Integer[] solution;
    private Digit[][] input;

    public Integer getSolutionSize() {
        return solutionSize;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public Integer getNoAttempts() {
        return noAttempts;
    }

    public Digit[][] getInput() {
        return input;
    }

    public Integer[] getSolution() {
        return solution;
    }

    public void setNoAttempts(Integer noAttempts) {
        this.noAttempts = noAttempts;
    }

    public void setInput(Digit[][] input) {
        this.input = input;
    }

    // TODO: Set WEB-INF beans.xml to map the concrete implementation
    @EJB
    private SolutionGeneratorBeanLocal solutionGenerator;

    public GameBean() {

    }

    public String play() {
        String next = STATE_PLAY;
        noAttempts = 0;
        try {
            // TODO: Remove this when beans.xml is set correctly, now the instance is null
            solutionGenerator = new SolutionGeneratorBean();
            solution = solutionGenerator.generate(solutionSize);
            input = new Digit[maxAttempts][solutionSize];
            for (int i = 0; i < maxAttempts; i++) {
                for (int j = 0; j < solutionSize; j++)
                    input[i][j] = new Digit();
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, e);
            // TODO: return error when STATE_ERROR is in place
        }
        return next;
    }

    public String exit() {
        String next = STATE_INIT;
        solution = null;
        input = null;
        noAttempts = 0;
        return next;
    }

    public String verify() {
        String next = STATE_PLAY;

        Digit[] guess = input[noAttempts];

        // TODO: not the most efficient - multiple traverse, also clone the guess
        verifyValidDigits(guess, solution);
        verifyInvalidDigits(guess, solution);
        verifyMultipleDigits(guess, solution);
        verifyLeftDigits(guess, solution);
        verifyRightDigits(guess, solution);

        noAttempts++;

        if (isValid(guess)) {
            next = STATE_WIN;
        } else if (isOver()) {
            next = STATE_OVER;
        }

        return next;
    }

    private void verifyValidDigits(Digit[] guess, Integer[] solution) {
        for (int i = 0; i < guess.length; i++) {
            if (guess[i].getState() != State.UNKNOWN) {
                continue;
            }
            if (Objects.equals(guess[i].getValue(), solution[i])) {
                guess[i].setState(State.VALID);
            }

        }
    }

    private void verifyInvalidDigits(Digit[] guess, Integer[] solution) {
        for (Digit digit : guess) {
            if (digit.getState() != State.UNKNOWN) {
                continue;
            }
            int count = 0;
            for (int j = 0; j < solution.length; j++) {
                if (!Objects.equals(digit.getValue(), solution[j])
                        || Objects.equals(guess[j].getValue(), solution[j])) {
                    count++;
                }
            }
            if (count >= solution.length) {
                digit.setState(State.INVALID);
            }
        }
    }

    private void verifyMultipleDigits(Digit[] guess, Integer[] solution) {
        for (Digit digit : guess) {
            if (digit.getState() != State.UNKNOWN) {
                continue;
            }
            int count = 0;
            for (int j = 0; j < solution.length; j++) {
                // check multiple digits found in the wrong possitions
                if (Objects.equals(digit.getValue(), solution[j])
                        && !Objects.equals(guess[j].getValue(), solution[j])) {
                    count++;
                }
            }
            if (count > 1) {
                digit.setState(State.MULTIPLE);
            }
        }
    }

    private void verifyLeftDigits(Digit[] guess, Integer[] solution) {
        for (int i = 1; i < guess.length; i++) {
            if (guess[i].getState() != State.UNKNOWN) {
                continue;
            }
            for (int j = 0; j < i; j++) {
                if (Objects.equals(guess[i].getValue(), solution[j])
                        && !Objects.equals(guess[j].getValue(), solution[j])) {
                    guess[i].setState(State.LEFT);
                    break;
                }
            }
        }
    }

    private void verifyRightDigits(Digit[] guess, Integer[] solution) {
        for (int i = 0; i < guess.length - 1; i++) {
            if (guess[i].getState() != State.UNKNOWN) {
                continue;
            }
            for (int j = i; j < solution.length; j++) {
                if (Objects.equals(guess[i].getValue(), solution[j])
                        && !Objects.equals(guess[j].getValue(), solution[j])) {
                    guess[i].setState(State.RIGHT);
                    break;
                }
            }
        }
    }

    private boolean isValid(Digit[] guess) {
        boolean success = true;
        for (Digit digit : guess) {
            if (digit.getState() != State.VALID) {
                success = false;
                break;
            }
        }
        return success;
    }

    private boolean isOver() {
        return noAttempts >= maxAttempts;
    }
}
