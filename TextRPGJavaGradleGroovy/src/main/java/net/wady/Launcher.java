package net.wady;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        System.out.println("RUNNING FROM LAUNCHER, NOT MAIN");
        Main.main(args);
    }


}

