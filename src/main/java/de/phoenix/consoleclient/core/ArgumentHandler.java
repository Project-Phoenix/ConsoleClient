/*
 * Copyright (C) 2014 Project-Phoenix
 * 
 * This file is part of ConsoleClient.
 * 
 * ConsoleClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * ConsoleClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ConsoleClient.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.consoleclient.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class ArgumentHandler {

    private Options option;
    private DownloadHandler downloadHandler;

    public ArgumentHandler(String[] args) {
        option = createOption();
        downloadHandler = new DownloadHandler(args);
    }

    private Options createOption() {

        System.out.println("Nurpf bei Optionen irgendwo");

        Options options = new Options();

        options.addOption("d", "download", false, "describes action to do");

        options.addOption("u", "upload", false, "describes action to do");

        options.addOption("c", "current", false, "should return current tasksheet");

        return options;

    }

    private void handleOption(String[] args) throws Exception {

        CommandLineParser parser = new DefaultParser();

        CommandLine line = parser.parse(this.option, args);

        if (line.hasOption("download") && line.hasOption("current")) {
            // TODO: aktuellstes Blatt ausgeben bzw den String basteln wie Download ihn haben will
        } else if (line.hasOption("download")) {
            downloadHandler.execute(args);
        } else {
            System.out.println("line hat die option nicht?!");
        }
        
    }

    public void execute(String[] args) throws Exception {

        handleOption(args);
        System.out.println("im ArgumentHandler . . . . . . .");

    }

}
