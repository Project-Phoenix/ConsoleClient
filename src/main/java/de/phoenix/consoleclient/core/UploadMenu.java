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

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class UploadMenu extends Menu {

    private WebResource wrTask;
    private WebResource wrSubmit;

    public UploadMenu() {
        super();
        wrTask = PhoenixTask.getResource(Core.client, Core.BASE_URL);
        wrSubmit = PhoenixTask.submitResource(Core.client, Core.BASE_URL);
    }

    public String desiredPath() {

        System.out.println("Please enter the path your file is saved in:");
        String path = Core.scanner.nextLine();
        return path;
    }

    /* creates a new config-entry when programm starts first time */
    public String firstStart() throws Exception {

        String workspace;
        Configuration config = new JSONConfiguration("config.json");

        // first start
        if (!config.exists("workspacePath")) {
            System.out.println("It seems to be your first upload task. Please specify where your workspace is saved:");
            workspace = Core.scanner.nextLine();
            config.setString("workspacePath", workspace);
            // !first start, reads out the path of the workspace
        } else {
            workspace = config.getString("workspacePath");
        }

        return workspace;
    }

    public void execute(String[] args) throws Exception {

        String pathWorkspace = firstStart();

        List<String> allTaskSheets = showAllTaskSheets();
        if (allTaskSheets == null)
            return;
        // user selects a tasksheet
        String sheetTitle = userChoice(allTaskSheets);
        if (sheetTitle == null)
            return;

        PhoenixTaskSheet wantedSheet = titleToTaskSheet(sheetTitle);

        List<String> allTasks = showTasks(wantedSheet);
        // user selects a task from tasksheet
        String taskTitle = userChoice(allTasks);
        if (taskTitle == null)
            return;

        String pathOfFile = pathWorkspace + "/" + desiredPath();
        File file = new File(pathOfFile);
        if (!file.exists()) {
            System.out.println("Your file doesn't exist under the following path: " + pathOfFile);
            return;
        }

        // Adds single solution to the text file list
        List<File> textFiles = new ArrayList<File>();
        textFiles.add(new File(pathOfFile));

        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", taskTitle);

        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        System.out.println("Title is " + taskTitle);
        List<PhoenixTask> list = EntityUtil.extractEntityList(post);
        PhoenixTask reqTask = list.get(0);

        PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(file));
        // connects a solution to a task
        post = wrSubmit.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(reqTask, Arrays.asList(sub)));
        System.out.println(post.getStatus());
        if (post.getStatus() != 200)
            throw new Exception("Status is not 200!");

        PhoenixSubmissionResult result = post.getEntity(PhoenixSubmissionResult.class);
        System.out.println(result.getStatus());

        // TODO: irgendwas mit getStatus unterscheiden.

        // evtl halt mehrere Dateien?
    }

}
