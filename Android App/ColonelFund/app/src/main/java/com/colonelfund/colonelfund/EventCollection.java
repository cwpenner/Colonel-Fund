package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * Event Collection Class
 * Needs to be converted from regular java to android compatible for restore and save.
 */
public class EventCollection {
    private static final String jsonFileName = "events.json";
    public Map<String,Event> eventMap = null;
    AssetManager am = null;
    /**
     * Default constructor. Will attempt to read a library file that has been saved.
     */
    public EventCollection(Context myContext) {
        am = myContext.getAssets();
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
        //toDo not operational
        return false;
    }
    /**
     * Restores a event library from the last saved or opened json library.
     * @return boolean
     */
    public boolean restoreFromFile() {
        boolean restored = false;
        this.eventMap = new HashMap<String,Event>();
        InputStream is = null;
        try {
            is = am.open(jsonFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!is.equals(null)) {
                System.out.println("Event Library collection found under: " + jsonFileName);
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
                String result = total.toString();
                JSONObject obj = new JSONObject(result);
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
            if (is != null) {
                try {
                    is.close();
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
     * Returns an array of strings for associated events.
     * @return
     */
    public ArrayList<String> getAssociatedEvents(String aMemberId) {
        ArrayList<String> associatedEvents = new ArrayList<String>();
        if (eventMap.size() > 0) {
            Iterator it = eventMap.values().iterator();
            while (it.hasNext()) {
                Event tempEvent = (Event)it.next();
                if (tempEvent.getAssociatedMember().equals(aMemberId)) {
                    associatedEvents.add(tempEvent.getTitle());
                }
            }
            System.out.println(associatedEvents.toString());
            return associatedEvents;
        }
        else {
            System.out.println("No Events");
            return null;
        }
    }
}