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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.filter.EduFilter;
import de.phoenix.filter.TextFilter;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectEntity;

public class DownloadMenu extends Menu {

    private WebResource wrTask;

    public DownloadMenu() {
        super();
        wrTask = PhoenixTask.getResource(Core.client, Core.BASE_URL);
    }

    public String firststart() throws Exception {

        String path;
        File f = new File("C:/Users/Tabea/Desktop/Phoenix/target/downloadPath.txt");
        if (!f.exists()) {
            PrintWriter pw = new PrintWriter(new FileWriter("downloadPath.txt"));
            System.out.println("It seems to be your first start. Please enter where you wanna save your files:");
            path = Core.scanner.nextLine();
            f.createNewFile();
            pw.write(path);
            pw.close();
        } else {
            BufferedReader br = new BufferedReader(new FileReader("downloadPath.txt"));
            path = br.readLine();
            br.close();
        }
        return path;
    }

    public void execute(String[] args) throws Exception {

        // storage location
        String path = firststart();

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

        File dir = new File(path);
        File file = new File(dir, taskTitle + ".java");
        if (!file.exists()) {
            System.out.println("BUILT!");
        } else {
            System.out.println("File already exists.");
            return;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File couldn't be created.");
            e.printStackTrace();
        }

        // TODO: attachements? bilder?
        TextFilter t = EduFilter.INSTANCE;
        String description = reqTitle.getDescription();
        String descrFiltered = t.filter(description);
        Writer fw = new FileWriter(file);
        Writer bw = new BufferedWriter(fw);

        List<PhoenixText> pattern = reqTitle.getPattern();

        if (pattern.isEmpty()) {
            bw.write(descrFiltered);
            bw.close();
        }

        for (PhoenixText hans : pattern) {
            try {
                bw.write(hans.getText() + "\n");
                bw.write(descrFiltered);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
