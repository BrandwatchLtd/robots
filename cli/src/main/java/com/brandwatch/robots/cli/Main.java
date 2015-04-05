package com.brandwatch.robots.cli;

/*
 * #%L
 * Robots (command-line interface)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Brandwatch nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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
