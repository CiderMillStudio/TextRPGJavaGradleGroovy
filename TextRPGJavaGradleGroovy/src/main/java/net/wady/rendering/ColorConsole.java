package net.wady.rendering;

public class ColorConsole {
    // Reset code to restore default terminal colors/styles
    public static final String ANSI_RESET = "\u001B[0m";

    // Text Styles
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_ITALIC = "\u001B[3m";   // not supported by all terminals
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_STRIKETHROUGH = "\u001B[9m"; // bonus, widely supported

    // Standard Foreground Colors (30-37)
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Bright Foreground Colors (90-97)
    public static final String ANSI_BRIGHT_BLACK = "\u001B[90m"; // aka "gray"
    public static final String ANSI_BRIGHT_RED = "\u001B[91m";
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

    // Standard Background Colors (40-47)
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // Bright Background Colors (100-107)
    public static final String ANSI_BRIGHT_BLACK_BACKGROUND = "\u001B[100m";
    public static final String ANSI_BRIGHT_RED_BACKGROUND = "\u001B[101m";
    public static final String ANSI_BRIGHT_GREEN_BACKGROUND = "\u001B[102m";
    public static final String ANSI_BRIGHT_YELLOW_BACKGROUND = "\u001B[103m";
    public static final String ANSI_BRIGHT_BLUE_BACKGROUND = "\u001B[104m";
    public static final String ANSI_BRIGHT_PURPLE_BACKGROUND = "\u001B[105m";
    public static final String ANSI_BRIGHT_CYAN_BACKGROUND = "\u001B[106m";
    public static final String ANSI_BRIGHT_WHITE_BACKGROUND = "\u001B[107m";

    public static void main(String[] args) {
        // Simple colored text
        System.out.println(ANSI_RED + "This text is red!" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "This text is green!" + ANSI_RESET);

        // Bright colors
        System.out.println(ANSI_BRIGHT_CYAN + "This text is bright cyan!" + ANSI_RESET);

        // Background + foreground combo
        System.out.println(ANSI_BRIGHT_BLACK_BACKGROUND + ANSI_RED + ANSI_BOLD + ANSI_ITALIC + "Coocoo " + ANSI_RESET + ANSI_BLACK_BACKGROUND + ANSI_BLUE + " text!!" + ANSI_RESET);

        // Text styles
        System.out.println(ANSI_BOLD + "This text is bold!" + ANSI_RESET);
        System.out.println(ANSI_ITALIC + "This text is italic!" + ANSI_RESET);
        System.out.println(ANSI_UNDERLINE + "This text is underlined!" + ANSI_RESET);
        System.out.println(ANSI_STRIKETHROUGH + "This text is struck through!" + ANSI_RESET);

        // Styles can be stacked with colors (order doesn't matter, they don't overwrite each other)
        System.out.println(ANSI_BOLD + ANSI_UNDERLINE + ANSI_BRIGHT_YELLOW + "Bold, underlined, bright yellow!" + ANSI_RESET);
    }
}
