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



public class Core {
    

//    public static File download(String name) {
//
//        ClientConfig cc = new DefaultClientConfig();
//        cc.getClasses().add(MultiPartWriter.class);
//        Client client = Client.create(cc);
//        WebResource wr = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest/" + name);
//
//        // access requested file
//        File file = wr.get(File.class);
//        if (!file.exists()) {
//            System.out.println("File doesn't exist.");
//            return null;
//        }
//        // if file exists
//        return file;
//    }
//
//    public static void upload(String path) {
//
//        // current directory + filename
//        File file = new File(path);
//        // String author = System.getProperty("user.name");
//
//        if (!file.exists()) {
//            System.out.println("File doesn't exist.");
//            return;
//        }
//
//        ClientConfig cc = new DefaultClientConfig();
//        cc.getClasses().add(MultiPartWriter.class);
//        Client client = Client.create(cc);
//        WebResource resource = client.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest").path("/submission").path("/submit");
//       
//        // Send file to server
//        ClientResponse response = UploadHelper.uploadFile(resource, file);
//        System.out.println("Response: " + response.getClientResponseStatus());
//    }

    public static void main(String[] args){
        
        //MenuHandler Instance
        MenuHandler menuHandler = new MenuHandler();
        
        //register menus
        menuHandler.register("upload", new UploadMenu());
        menuHandler.register("download", new DownloadMenu());
        menuHandler.register("login", new LoginMenu());
        
        menuHandler.execute(args);
        
        
//
//        ClientConfig cc = new DefaultClientConfig();
//        cc.getClasses().add(MultiPartWriter.class);
        
     
        
        

//        if (args.length > 0) {
//            // "java -jar ... upload file"
//            if (args[0].toLowerCase().equals("upload")) {
//                upload(args[1]);
//                // "java -jar ... download file"
//            } else if (args[0].toLowerCase().equals("download")) {
//                download(args[1]);
//            } else {
//                System.out.println("[USAGE]: java -jar ... [upload, download} filepath");
//            }
//        } else {
//            System.out.println("[USAGE]: java -jar ... [upload, download} filepath");
//            return;
//        }

    }

}
