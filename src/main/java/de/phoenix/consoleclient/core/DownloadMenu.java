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
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

import de.phoenix.rs.entity.PhoenixTask;

public class DownloadMenu extends Menu {

    private final static String BASE_URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";

    public DownloadMenu() {
        super();
    }

    public void writeInFile(File file, String path) {
        // TODO: in Datei schreiben?!
    }

    public void firstStart() {

        File desiredPath = new File("desiredPath");
        System.out.println("Please enter the path, you wanna save your files in");

        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        scanner.close();

        writeInFile(desiredPath, path);

    }

    /* shows all available files */
    public void showAll() {
        
        System.out.println("Jopjopjopjop");

        Client client = Client.create();
        client.addFilter(new LoggingFilter());
        WebResource wr = client.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETALL_TITLES);
        ClientResponse cresp = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
System.out.println(cresp);
//        List<String> tasks = RSLists.fromSendableStringList(cresp);
        
//        GenericType<List<String>> genericString = new GenericType<List<String>>() {
//        };

        List<String> taskTitles = cresp.getEntity(new GenericType<List<String>>() {
        });

        if (taskTitles.isEmpty()) {
            System.out.println("There aren't any assignments to download!");
        } else {
            System.out.println("There are the following assignments to download: \n");
            for (int i = 0; i < taskTitles.size(); i++) {
                System.out.println(taskTitles.get(i) + "\n");
            }
        }
       
    }

    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("[USAGE]: java -jar ... download"); // args[0] =
                                                                   // download
            return;
        }

   
        showAll();

        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        Client client = Client.create();
        WebResource wr = client.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);
        // TODO: kilians post übernehmen irgendwie.

        GenericType<List<PhoenixTask>> ptask = new GenericType<List<PhoenixTask>>() {
        };
        
        // access requested file
        File file = wr.get(File.class);
        if (!file.exists()) {
            System.out.println("File doesn't exist, please check the entered address.");
        }
        // if file exists
        // TODO: save file to eclipse

    }

}
