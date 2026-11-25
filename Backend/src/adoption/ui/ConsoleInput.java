package adoption.ui;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);

    public String prompt(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public int promptInt(String label) {
        return Integer.parseInt(prompt(label));
    }

    public <T> T chooseOption(String label, T[] options, T current) {
        while (true) {
            System.out.println(label + ":");
            for (int i = 0; i < options.length; i++) {
                System.out.println("  " + (i + 1) + ". " + options[i]);
            }
            String answer = prompt(current == null
                    ? "Choose a number"
                    : "Choose a number (Enter to keep " + current + ")");

            if (answer.isBlank() && current != null) {
                return current;
            }
            try {
                int number = Integer.parseInt(answer);
                if (number >= 1 && number <= options.length) {
                    return options[number - 1];
                }
            } catch (NumberFormatException e) {
                // falls through to the message below
            }
            System.out.println("Please choose a number from the list.");
        }
    }

    public <T> T chooseOptionOrAny(String label, T[] options) {
        while(true) {
            System.out.println(label + ":");
            System.out.println("0. Any");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            String answer = prompt("Choose a number (Enter for any)");

            if (answer.isBlank()) {
                return null;
            }

            try {
                int number = Integer.parseInt(answer);
                if (number == 0) {
                    return null;
                } if (number >= 1 && number <= options.length) {
                    return options[number - 1];
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Please choose a number from the list.");
        }
    }

    public boolean promptYesNo(String label, Boolean current) {
        while (true) {
            String hint = current == null ? " (y/n)" : " (y/n, Enter to keep " + (current ? "yes" : "no") + ")";
            String answer = prompt(label + hint).toLowerCase();

            if (answer.isBlank() && current != null) {
                return current;
            }
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }

    public <T> T chooseOptionOptional(String label, T[] options) {
        while (true) {
            System.out.println(label + ":");
            for (int i = 0; i < options.length; i++) {
                System.out.println("  " + (i + 1) + ". " + options[i]);
            }
            String answer = prompt("Choose a number (Enter to skip)");

            if (answer.isBlank()) {
                return null;
            }
            try {
                int number = Integer.parseInt(answer);
                if (number >= 1 && number <= options.length) {
                    return options[number - 1];
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Please choose a number from the list.");
        }
    }

    /** Returns null when the user just presses Enter. Throws NumberFormatException for non-numbers. */
    public Integer promptOptionalInt(String label) {
        String answer = prompt(label + " (Enter to skip)");
        return answer.isBlank() ? null : Integer.valueOf(answer);
    }

    /** Returns null when the user just presses Enter. Throws NumberFormatException for non-numbers. */
    public Double promptOptionalDouble(String label) {
        String answer = prompt(label + " (Enter to skip)");
        return answer.isBlank() ? null : Double.valueOf(answer);
    }

    /** Returns null when the user just presses Enter. */
    public Boolean promptOptionalYesNo(String label) {
        while (true) {
            String answer = prompt(label + " (y/n, Enter to skip)").toLowerCase();
            if (answer.isBlank()) {
                return null;
            }
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }
}