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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectEntity;

public class UploadMenu extends Menu {

    private static Scanner scanner = new Scanner(System.in);

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

    public void execute(String[] args) throws Exception {

        showAll("upload");

        String title = scanner.nextLine();
        String pathOfFile = desiredPath();
        File file = new File(pathOfFile);

        // Empty attachment list
        // List<File> attachments = new ArrayList<File>();

        // Add single solution to the text file list
        List<File> textFiles = new ArrayList<File>();
        textFiles.add(new File(pathOfFile));

        Client client = PhoenixClient.create();
        WebResource wr = PhoenixTask.getResource(client, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", title);
        // WebResource wr = client.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        System.out.println("Title is " + title);
        List<PhoenixTask> list = EntityUtil.extractEntityList(post);
        PhoenixTask reqTask = list.get(0);
        
        //wr = client.resource(BASE_URL).path(PhoenixSubmission.WEB_RESOURCE_ROOT).path(PhoenixSubmission.WEB_RESOURCE_SUBMIT);
        PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(),Arrays.asList(file));
        wr = PhoenixTask.submitResource(client, BASE_URL);
        post= wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(reqTask, sub));
        //PhoenixSubmission sub = new PhoenixSubmission(reqTask, attachments, textFiles);
        //ClientResponse post2 = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, sub);
        System.out.println(post.getStatus());
        if (post.getStatus() != 200)
            throw new Exception("Status is not 200!");
        
        PhoenixSubmissionResult result = post.getEntity(PhoenixSubmissionResult.class);
        System.out.println(result.getStatusText());

        // Next: SubmissionResult ausgeben und dann schauen, dass Nutzer angeben
        // muss, wo seine Datei liegt, die hochgeladen werden soll und dann evtl
        // halt mehrere Dateien
    }

    /*
     * hab irgendwo eine Datei liegen. die soll hochgeladen werden. möglichst
     * noch passend zur Aufgabe. und möglichst auch noch nicht nur eine Datei
     * sondern wenns sein muss auch mehrere.
     */

}
