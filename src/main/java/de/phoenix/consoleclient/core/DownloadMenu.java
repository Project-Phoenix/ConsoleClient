/*
 * Copyright (C) 2013 Project-Phoenix
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

import java.io.File;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.impl.MultiPartWriter;

public class DownloadMenu extends Menu {
    
    private final static String BASE_URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";

    public DownloadMenu() {
        super();
    }

    public void execute(String[] args) {

        String adress = "";
        if (args[1].isEmpty()) {
            System.out.println("[USAGE]: java -jar ... download filepath");
            return;

        } else {
            adress = args[1];

            ClientConfig cc = new DefaultClientConfig();
            cc.getClasses().add(MultiPartWriter.class);
            Client client = Client.create(cc);
            WebResource wr = client.resource(BASE_URL + adress);

            // access requested file
            File file = wr.get(File.class);
            if (!file.exists()) {
                System.out.println("File doesn't exist, please check the entered adress.");
            }
            // if file exists
            // TODO: save file to eclipse

        }

    }

}
