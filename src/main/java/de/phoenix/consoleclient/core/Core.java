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

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;


public class Core {
    
    public static File download(String name) {
        
        Client client = Client.create();
        WebResource wr = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest/" + name);

        // access requested file
        File file = wr.get(File.class);
        if (!file.exists()) {
            System.out.println("File doesn't exist.");
            return null;            
        }
        // if file exists
        return file;
    }

    public static void upload(String name) {
        
        // current directory + filename
        File file = new File(System.getProperty("user.dir").concat("/").concat(name));
        String author = System.getProperty("user.name");

        Client client = Client.create();
        WebResource wr = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest").path("/submission").path("/upload").path(author);

        // create file packet
        FormDataMultiPart multiPart = new FormDataMultiPart();
        if (file != null) {
            multiPart.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        }

        // Send file to server
        ClientResponse cr = wr.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, multiPart);
        System.out.println("Response: " + cr.getClientResponseStatus());
    }

    

    public static void main(String[] args) {
        
        Client c = Client.create();

        WebResource wr = c.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest/helloworld");

        System.out.println(wr.get(String.class));
        
        // "java -jar ... upload file"
        if (args[0].toLowerCase().equals("upload")) {
            upload(args[2]);
            System.out.println(args[1]);
        // "java -jar ... download file"
        } else if (args[0].toLowerCase().equals("download")) {
            download(args[2]);
            System.out.println(args[1]);
        } else {
            System.out.println("Please choose download (1) or upload (2): ");
            BufferedReader enter = new BufferedReader(new InputStreamReader(System.in));
            int text;
            try {
                text = enter.read();
                System.out.println("ausgabe" + text);
                // ASCII 1 = 49
                if (text == 49) {
                    System.out.println("Yej, you choose download :)");
                // ASCII 2 = 50
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
