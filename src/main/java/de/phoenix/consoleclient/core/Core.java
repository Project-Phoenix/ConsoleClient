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

import java.util.Scanner;

import com.sun.jersey.api.client.Client;

import de.phoenix.rs.PhoenixClient;
import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class Core {
    public static Client client;
    public static Scanner scanner;
    public static String BASE_URL;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        client = PhoenixClient.create();
//        BASE_URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";
        
        Configuration config;
        try {
            config = new JSONConfiguration("config.json");
        } catch (Exception e) {
            System.err.println("An error has occured while retrieving the serverURL");
            e.printStackTrace();
            return;
        }
        BASE_URL = config.getString("webserviceURL");
   
        MenuController menuController = new MenuController(args);

        if (args.length == 0) {
            String[] arguments = new String[1];
            arguments[0] = " ";
            menuController.execute(arguments);
        } else {
            menuController.execute(args);
        }
        scanner.close();
    }

}