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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.filter.EduFilter;
import de.phoenix.filter.TextFilter;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class DownloadMenu extends Menu {

    private WebResource wrTask;

    public DownloadMenu() {
        super();
        wrTask = PhoenixTask.getResource(Core.client, Core.BASE_URL);
    }

    public String firstStart() throws Exception {

        String path;
        Configuration config = new JSONConfiguration("config.json");
        if (!config.exists("downloadPath")) {
            System.out.println("It seems to be your first start. Please enter where you wanna save your files:");
            path = Core.scanner.nextLine();
            config.setString("downloadPath", path);
        } else {
            path = config.getString("downloadPath");
        }
        return path;
    }

    public void writeInFile(File file, String text) {
        Writer fw;
        Writer bw;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void execute(String[] args) throws Exception {

        // storage location
        String path = firstStart();

        List<String> allTaskSheets = showAllTaskSheets();
        if (allTaskSheets == null)
            return;
        String sheetTitle = userChoice(allTaskSheets);
        if (sheetTitle == null)
            return;

        PhoenixTaskSheet wantedTaskSheet = titleToTask(sheetTitle);

        List<String> allTasks = showTasks(wantedTaskSheet);
        String taskTitle = userChoice(allTasks);
        if (taskTitle == null)
            return;

        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", taskTitle);
        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);

        PhoenixTask reqTitle = EntityUtil.extractEntity(post);
        List<PhoenixText> pattern = reqTitle.getPattern();
        List<PhoenixAttachment> attachment = reqTitle.getAttachments();

        // TODO: Datei erstellen, wos hingeschrieben wird
        // TODO: bei mehreren pattern in Ordner packen
        // TODO: description und pattern in Datei schreiben

        TextFilter t = EduFilter.INSTANCE;
        String description = reqTitle.getDescription();
        if (description.equals("")) {
            System.out.println("Description is empty.");
            return;
        }
        String descrFiltered = t.filter(description);

        File directory = new File(path);

        // task is without any given code or attachment, just text
        if (pattern.isEmpty() && attachment.isEmpty()) {

            File file = new File(directory, taskTitle + ".java");

            if (file.exists()) {
                System.out.println("File already exists.");
                return;
            }

            // if file doesn't exist, create this file and write the
            // description as a comment in it
            writeInFile(file, "/*" + descrFiltered + "*/");

            // task has one pattern hence one class to submit, no attachments
        } else if (pattern.size() == 1 && attachment.isEmpty()) {

            File file = new File(directory, pattern.get(0).getFullname());

            if (file.exists()) {
                System.out.println("File already exists.");
                return;
            }
            writeInFile(file, "/*" + descrFiltered + "*/" + pattern.get(0).getText());

            // task has at least two patterns or some attachments
        } else {

            File dir = new File(directory, taskTitle);
            if (dir.exists()) {
                System.out.println("Directory already exists.");
                return;
            }
            dir.mkdir();

            if (!pattern.isEmpty()) {
                // writes each class in a file in the directory
                for (PhoenixText clazz : pattern) {

                    File file = new File(directory + "/" + taskTitle, clazz.getFullname());
                    writeInFile(file, "/*" + description + "*/\n" + clazz.getText());

                }
            }
            
            if(!attachment.isEmpty()) {
                
                if(pattern.isEmpty()) {
                    File file = new File(directory + "/" + taskTitle, taskTitle + ".java");
                    writeInFile(file, descrFiltered);
                }
                    
                for(PhoenixAttachment picture : attachment) {
                    
                    File file = new File(directory + "/" + taskTitle, picture.getFullname());
                    byte[] content = picture.getContent();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(content);
                    fos.close();
                }
            }

        }
    }



}
