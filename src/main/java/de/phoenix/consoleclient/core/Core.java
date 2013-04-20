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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class Core {
    
    @GET
    @Path("/download/{name}")
    public static File download(@PathParam("name") String name) {

        System.out.println(new File("").getAbsolutePath());
        File f = new File(name);
        return f;

    }
    

    public static void main(String[] args){
//        
//        System.out.println("Hello World");
//        
        Client c = Client.create();
        
        WebResource wr = c.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest/helloworld");
        
        System.out.println(wr.get(String.class));
        
        if(args[0].toLowerCase().equals("upload")){
            
            upload();
            System.out.println(args[1]);
            
        } else if (args[0].toLowerCase().equals("download")) {
            String name = args[1].toString().toLowerCase();
            download(args[2]);
            System.out.println(args[1]);
        } else {
            System.out.println("Please choose download (1) or upload (2): ");
            BufferedReader enter = new BufferedReader(
                    new InputStreamReader(System.in));
            int text;
            try {
                text = enter.read();
                System.out.println("ausgabe" + text);
                if (text == 49){
                    System.out.println("Yej, you choose download :)");
                } else if (text == 50) {
                    System.out.println("now you choose upload, good choice");
                } else {
                    System.out.println("sorry, invalid input");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
  
    }
    
}
