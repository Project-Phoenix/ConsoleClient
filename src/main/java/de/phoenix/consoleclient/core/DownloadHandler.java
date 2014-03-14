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
import org.apache.commons.cli.ParseException;

public class DownloadHandler {

    private Options option;
    private DownloadMenu download;

    public DownloadHandler(String[] args) {
        this.download = new DownloadMenu();
        option = createOption();
        handleOption(args);
    }

    private Options createOption() {

        Options options = new Options();

        options.addOption("d", "download", false, "describes action to do");

        options.addOption("u", "upload", false, "describes action to do");

        options.addOption("c", "current", false, "should return current tasksheet");

        return options;

    }

    private void handleOption(String[] args) {

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(this.option, args);

            if (line.hasOption("download") && line.hasOption("current")) {
                try {
                    download.execute(args);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
