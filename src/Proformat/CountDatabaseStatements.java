package Proformat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class CountDatabaseStatements {
    static int numberOfDatabaseStatements = 0;

    public static int main(File inputMain) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputMain)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (isDatabaseStatement(line))
                    numberOfDatabaseStatements++;
            }
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
        return numberOfDatabaseStatements;
    }

    static boolean isDatabaseStatement(final String statement) {
        final String lowerCaseStatement = statement.toLowerCase();
        final boolean isDatabaseStatement;
        // for Select Statement
        if (Pattern.matches(".*select.*from.*", lowerCaseStatement))
            isDatabaseStatement = true;
        else if (Pattern.matches(".*delete.*from.*", lowerCaseStatement))
            isDatabaseStatement = true;
        else if (Pattern.matches(".*update.*set.*", lowerCaseStatement))
            isDatabaseStatement = true;
        else if (Pattern.matches(".*insert.*into.*", lowerCaseStatement))
            isDatabaseStatement = true;
        else
            isDatabaseStatement = false;
        return isDatabaseStatement;
    }

}
