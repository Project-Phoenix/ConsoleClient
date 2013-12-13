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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;

public class UploadMenu extends Menu {
    
    private static Scanner scanner = new Scanner(System.in);

    private final static String BASE_URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";

    public UploadMenu() {
        super();
    }

    public String desiredPath() {

        System.out.println("Please enter the path your file is saved in:");

        String path = scanner.nextLine();

        File stats = new File(path);
        if (!stats.exists()) {
            System.out.println("Path doesn't exist.");
            return null;
        }

        return path;
    }
    
    public void execute(String[] args) {

        if (args.length != 1) {
            System.out.println("[USAGE]: java -jar ... upload"); // args[0] = upload
            return;
        }
        
        String path = desiredPath();
        
        Client client = PhoenixClient.create();
        WebResource wr = client.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);
        ClientResponse post = wr.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class);
        
        List<PhoenixTask> tasks = PhoenixTask.fromSendableList(post);
        PhoenixTask task = tasks.get(0);
        
        List<File> fileAttachments = new ArrayList<File>();
        List<File> fileTexts = new ArrayList<File>();
        
        PhoenixSubmission sub = null;
        try {
            sub = new PhoenixSubmission(task, fileAttachments, fileTexts);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        wr = client.resource(BASE_URL).path(PhoenixSubmission.WEB_RESOURCE_ROOT).path(PhoenixSubmission.WEB_RESOURCE_SUBMIT);
        post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, sub);
        PhoenixSubmissionResult res = post.getEntity(PhoenixSubmissionResult.class);
        
        
        
    }

}
