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
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.impl.MultiPartWriter;

import de.phoenix.util.UploadHelper;

public class Core {

    public static File download(String name) {

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(MultiPartWriter.class);
        Client client = Client.create(cc);
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

    public static void upload(String path) {

        // current directory + filename
        File file = new File(path);
        // String author = System.getProperty("user.name");

        if (!file.exists()) {
            System.out.println("File doesn't exist.");
            return;
        }

        // ClientConfig cc = new DefaultClientConfig();
        // cc.getClasses().add(MultiPartWriter.class);
        Client client = Client.create();
        //WebResource wr = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest").path("/submission").path("/upload").path(author);
        WebResource resource = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest").path("/submission").path("/submit");
        
        // create file packet
        //FormDataMultiPart multiPart = new FormDataMultiPart();
        //if (file != null) {
          //  multiPart.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        //}

        // Send file to server
        ClientResponse response = UploadHelper.uploadFile(resource, file);
        System.out.println("Hallo hier" + response);
        // ClientResponse cr = wr.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, multiPart);
        System.out.println("Respons: " + response.getClientResponseStatus());
    }

    public static void main(String[] args) {

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(MultiPartWriter.class);
        Client client = Client.create(cc);

        WebResource wr = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest/helloworld");

        System.out.println(wr.get(String.class));

        if (args.length > 0) {
            // "java -jar ... upload file"
            if (args[0].toLowerCase().equals("upload")) {
                upload(args[1]);
                // "java -jar ... download file"
            } else if (args[0].toLowerCase().equals("download")) {
                download(args[1]);
            } else {
                System.out.println("[USAGE]: java -jar ... {upload, download} filepath");
            }
        } else {
            System.out.println("[USAGE]: java -jar ... {upload, download} filepath");
            return;
        }

    }

}
