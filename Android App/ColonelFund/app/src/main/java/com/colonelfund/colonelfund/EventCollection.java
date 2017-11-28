package com.colonelfund.colonelfund;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Event Collection Class
 * Needs to be converted from regular java to android compatible for restore and save.
 */
public class EventCollection {
    private static final String jsonFileName = "./events.json";
    public Map<String,Event> eventMap = null;

    /**
     * Default constructor. Will attempt to read a library file that has been saved.
     */
    public EventCollection() {
        boolean successfulLoad = this.restoreFromFile();
        if (successfulLoad) {
            System.out.println("Library loaded from: " + jsonFileName);
        }
        else {
            System.out.println("Unable to load library from: " + jsonFileName);
            System.out.println("New library created.");
            this.eventMap = new HashMap<String,Event>();
        }
    }

    /**
     * Attempts to take a event library, convert it to a json object, then save it to the
     * provided filename.
     * @return
     */
    public boolean saveJsonLibrary() {
        FileWriter fileOut = null;
        JSONObject jsonLibrary = new JSONObject();
        try {
            fileOut = new FileWriter(jsonFileName);
            Iterator<Event> libItr = this.eventMap.values().iterator();
            while (libItr.hasNext())
            {
                Event eventDesc = (Event) libItr.next();
                jsonLibrary.put(eventDesc.getTitle(), eventDesc.toJson());
            }
            fileOut.write(jsonLibrary.toString());
            System.out.println("Library saved under: " + jsonFileName);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error writing file: " + jsonFileName);

        }
        finally
        {
            if (fileOut != null) {
                try {
                    fileOut.close();
                    return true;
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        }
        return false;
    }
    /**
     * Restores a event library from the last saved or opened json library.
     * @return boolean
     */
    public boolean restoreFromFile() {
        boolean restored = false;
        this.eventMap = new HashMap<String,Event>();
        File libraryFile = new File(jsonFileName);
        FileInputStream fileIn = null;
        try {
            if (libraryFile.exists()) {
                System.out.println("Event Library collection found under: " + jsonFileName);
                fileIn = new FileInputStream(jsonFileName);
                String fileString = getFileContent(fileIn);
                JSONObject obj = new JSONObject(new JSONTokener(fileString));
                List<String> keyList = new ArrayList<String>();
                for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                    String key = it.next();
                    keyList.add(key);
                }
                String [] eventTitles = keyList.toArray(new String[keyList.size()]);
                for (int i=0; i < eventTitles.length; i++) {
                    Event eventDesc = new Event((JSONObject)obj.getJSONObject(eventTitles[i]));
                    if (eventDesc != null)
                        this.eventMap.put(eventDesc.getTitle(), eventDesc);
                }
                restored = true;
            }
            else {
                System.out.println("Error loading library file.");
            }
        } catch (Exception e) {
            System.out.println("Library File Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return restored;
    }
    /**
     * A method to add a new video to the library. True is returned when the request is successful.
     * @param aEvent
     * @return boolean
     */
    public boolean add(Event aEvent){
        if (eventMap.containsKey(aEvent.getTitle())){
            System.out.println("Event already exists.");
            return false;
        } else {
            eventMap.put(aEvent.getTitle(), aEvent);
            return true;
        }
    }
    /**
     * A method to remove the named Event from the library.
     * @param aEvent
     * @return boolean
     */
    public boolean remove(String aEvent) {
        if (eventMap.containsKey(aEvent)) {
            eventMap.remove(aEvent);
            return true;
        } else {
            System.out.println("Title does not exist.");
            return false;
        }
    }
    /**
     * Returns the video's description with title aTitle.
     * @param aEvent
     * @return
     */
    public Event get(String aEvent) {
        if (eventMap.containsKey(aEvent)) {
            return eventMap.get(aEvent);
        } else {
            System.out.println("Title does not exist.");
            return null;
        }
    }
    /**
     * Returns an array of strings, which are all of the titles in the library.
     * @return
     */
    public String[] getTitles() {
        if (eventMap.size() > 0)
            return eventMap.keySet().toArray(new String[eventMap.size()]);
        else
            return null;
    }
    /**
     * Returns a string of all Event.
     * @return
     * @return
     */
    public String toString() {
        String toString = "";
        if (eventMap.size() > 0) {
            Iterator<Event> iter = eventMap.values().iterator();
            while (iter.hasNext()) {
                Event aEvent = (Event) iter.next();
                toString += aEvent.toString();
            }
        }
        return toString;
    }
    /**
     * File in to string
     * @param fis
     * @return
     * @throws IOException
     */
    public static String getFileContent(FileInputStream fis) throws IOException {
        String encoding = "UTF-8";
        try( BufferedReader br = new BufferedReader( new InputStreamReader(fis, encoding ))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
    }
}

