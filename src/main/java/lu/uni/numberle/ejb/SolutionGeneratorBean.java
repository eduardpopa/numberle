package lu.uni.numberle.ejb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import jakarta.ejb.Stateless;

import org.apache.logging.log4j.*;

@Stateless
public class SolutionGeneratorBean implements SolutionGeneratorBeanLocal {

    private final String NUMBERS_FILE_PATH = "/numbers.txt";
    private final int NUMBERS_FILE_TOTAL_LINES = 5414;
    private static final Logger logger = LogManager.getLogger(SolutionGeneratorBean.class);

    @Override
    public Integer[] generate(int size) throws Exception {
        Random random = new Random();
        int lineNumber = random.nextInt(NUMBERS_FILE_TOTAL_LINES);
        logger.log(Level.INFO, "Random line number = " + lineNumber);
        Integer[] solution = new Integer[size];
        InputStream inputStream = getClass().getResourceAsStream(NUMBERS_FILE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int currentLine = 0;
        while ((line = reader.readLine()) != null && currentLine < lineNumber) {
            currentLine++;
        }
        reader.close();
        while (line.length() < size) {
            line += random.nextInt(9);
        }
        for (int i = 0; i < size; i++) {
            solution[i] = Character.getNumericValue(line.charAt(i));
        }
        logger.log(Level.INFO, "Generated solution = " + arrayToString(solution));
        return solution;
    }

    private String arrayToString(Integer[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
