package com.brandwatch.robots.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.PrintStream;

public class Main {

    private final PrintStream out;
    private final PrintStream err;
    private String[] args;

    public Main(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public void configure(String... args) {
        this.args = args;
    }

    public static void main(String[] args) {
        Main main = new Main(System.out, System.err);
        main.configure(args);
        main.run();
     }

    public void run() {

        final Arguments arguments = new Arguments();
        final JCommander commander = new JCommander(arguments);
        commander.setProgramName("robots");

        try {
            commander.parse(args);
        } catch (ParameterException e) {
            err.println(e.getMessage());
            err.println("Try `--help` for more information.");
            return;
        }

        if (arguments.isHelpRequested()) {
            StringBuilder builder = new StringBuilder();
            commander.usage(builder);
            out.print(builder.toString());
            return;
        }

        Command command = new Command(arguments);

        for (Result result : command.getResults()) {
            out.printf("%s: %s%n", result.getResource().toString(),
                    result.isAllowed() ? "allowed" : "disallowed");
        }

    }
}
