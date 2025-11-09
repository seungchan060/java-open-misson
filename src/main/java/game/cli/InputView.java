package game.cli;

import java.util.Scanner;

public final class InputView {
    private final Scanner scanner = new Scanner(System.in);

    public String input(String message) {
        System.out.print(message + " ");
        return scanner.nextLine().trim();
    }
}