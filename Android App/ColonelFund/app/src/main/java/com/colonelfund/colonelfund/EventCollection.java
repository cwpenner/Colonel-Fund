package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Event Collection Class
 * Needs to be converted from regular java to android compatible for restore and save.
 */
public class EventCollection {
    private static final String jsonFileName = "events.json";
    private final String TAG = "EventListFragment";
    private final String URL_FOR_NAMES = "https://wesll.com/colonelfund/events.php";
    private Map<String, Event> eventMap = null;
    AssetManager am = null;
    AppSingleton appContext;
    Context context;

    /**
     * Default constructor. Will attempt to read a library file that has been saved.
     */
    public EventCollection(Context myContext) {
        this.am = myContext.getAssets();
        this.appContext = new AppSingleton(myContext);
        this.context = myContext;
        boolean successfulLoad = restoreFromFile(context);
        if (successfulLoad) {
            System.out.println("Library loaded from: " + jsonFileName);
        } else {
            System.out.println("Unable to load library from: " + jsonFileName);
            System.out.println("New library created.");
            this.eventMap = new HashMap<String, Event>();
        }
    }

    /**
     * queries remote database for updated members in JSONArray forum
     */
    public void updateFromRemote() {
        String cancel_req_tag = "event_list";
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_NAMES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Event Response: " + response);
                JSONObject outputObj = new JSONObject();
                try {
                    JSONArray jArray = new JSONArray(response);
                    JSONObject jObj;
                    for (int i = 0; i < jArray.length(); i++) {
                        jObj = (JSONObject) jArray.get(i);
                        Event aEvent = new Event(jObj.getString("title"),
                                jObj.getString("associatedMember"),
                                jObj.getString("associatedEmail"),
                                jObj.getString("eventDate"),
                                Double.parseDouble(jObj.getString("fundGoal")),
                                Double.parseDouble(jObj.getString("currentFunds")),
                                jObj.getString("description"),
                                jObj.getString("type"),
                                jObj.getString("imageURL"),
                                jObj.getString("eventTime"),
                                jObj.getString("address"));

                        outputObj.put(jObj.getString("title"), aEvent.toJson());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveJsonLocal(outputObj, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        // Adding request to request queue
        strReq.setTag(appContext);
        appContext.addToRequestQueue(strReq, cancel_req_tag);
    }

    /**
     * Attempts to take a event library, convert it to a json object, then save it to the
     * provided filename.
     *
     * @return boolean
     */
    public boolean saveJsonLocal(JSONObject jObj, Context ctx) {
        FileOutputStream outputStream;
        try {
            outputStream = ctx.openFileOutput(jsonFileName, Context.MODE_PRIVATE);
            outputStream.write(jObj.toString().getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restores a event library from the last saved or opened json library.
     *
     * @return boolean
     */
    public boolean restoreFromFile(Context ctx) {
        boolean restored = false;
        this.eventMap = new HashMap<String, Event>();
        FileInputStream inputStream = null;
        try {
            inputStream = ctx.openFileInput(jsonFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (inputStream != null) {
                System.out.println("Event Library collection found under: " + jsonFileName);
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
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
                String[] eventTitles = keyList.toArray(new String[keyList.size()]);
                for (int i = 0; i < eventTitles.length; i++) {
                    Event eventDesc = new Event((JSONObject) obj.getJSONObject(eventTitles[i]));
                    if (eventDesc != null)
                        this.eventMap.put(eventDesc.getTitle(), eventDesc);
                }
                restored = true;
            } else {
                System.out.println("Error loading library file.");
            }
        } catch (Exception e) {
            System.out.println("Library File Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return restored;
    }

    /**
     * A method to add a new event to the library. True is returned when the request is successful.
     *
     * @param aEvent an event.
     * @return boolean
     */
    public boolean add(Event aEvent) {
        if (eventMap.containsKey(aEvent.getTitle())) {
            System.out.println("Event already exists.");
            return false;
        } else {
            eventMap.put(aEvent.getTitle(), aEvent);
            return true;
        }
    }

    /**
     * A method to remove the named Event from the library.
     *
     * @param aEvent an event.
     * @return boolean
     */
    public boolean remove(String aEvent) {
        if (eventMap.containsKey(aEvent)) {
            eventMap.remove(aEvent);
            return true;
        } else {
            System.out.println("Event does not exist.");
            return false;
        }
    }

    /**
     * Returns the event's description..
     *
     * @param aEvent string of an event.
     * @return an event object.
     */
    public Event get(String aEvent) {
        if (eventMap.containsKey(aEvent)) {
            return eventMap.get(aEvent);
        } else {
            System.out.println("Event does not exist.");
            return null;
        }
    }

    /**
     * Returns a string of all Event.
     *
     * @return string of all events.
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
     * Returns an array of associated events.
     *
     * @return array list of all events.
     */
    public ArrayList<String> getAssociatedEvents(String aMemberId) {
        ArrayList<String> associatedEvents = new ArrayList<String>();
        if (eventMap.size() > 0) {
            Iterator it = eventMap.values().iterator();
            while (it.hasNext()) {
                Event tempEvent = (Event) it.next();
                if (tempEvent.getAssociatedMember().equals(aMemberId)) {
                    associatedEvents.add(tempEvent.getTitle());
                }
            }
            System.out.println(associatedEvents.toString());
            return associatedEvents;
        } else {
            System.out.println("No Events");
            return null;
        }
    }

    /**
     * Gets eventMap values.
     *
     * @return collection of all events.
     */
    public Collection<Event> getEventsList() {
        ArrayList<Event> eventCollection = new ArrayList<>();
        if (eventMap.size() > 0) {
            Iterator it = eventMap.values().iterator();
            while (it.hasNext()) {
                eventCollection.add((Event) it.next());;
            }
            Collections.sort(eventCollection, new Comparator<Event>() {
                public int compare(Event obj1, Event obj2) {
                    return obj1.getEventDate().compareToIgnoreCase(obj2.getEventDate());
                }
            });
            return eventCollection;
        } else {
            System.out.println("No Events");
            return null;
        }
    }

    /**
     * Generates Initials and User Name for Event List.
     *
     * @return eventModel for an eventList
     */
    public ArrayList<EventListModel> generateListData() {
        ArrayList<EventListModel> models = new ArrayList<EventListModel>();
        Iterator<Event> EventItr = eventMap.values().iterator();
        while (EventItr.hasNext()) {
            Event temp = EventItr.next();
            double goalProgress;
            if ((temp.getCurrentFunds() / temp.getFundGoal()) < 1) {
                goalProgress = (temp.getCurrentFunds() / temp.getFundGoal());
            } else {
                goalProgress = 1;
            }
            models.add(new EventListModel(temp.getTitle(), temp.getType(), temp.getAssociatedMember(),
                    temp.getAssociatedEmail(), temp.getEventDate(), goalProgress, temp.getDescription()));
        }
        Collections.sort(models, new Comparator<EventListModel>() {
            public int compare(EventListModel obj1, EventListModel obj2) {
                return obj1.getEventDate().compareToIgnoreCase(obj2.getEventDate());
            }
        });
        return models;
    }

    /**
     * Generates Initials and User Name for Event List.
     *
     * @return eventModel for an eventList
     */
    //toDo: Create Filter That Fits "Top" Criteria
    public ArrayList<EventListModel> generateTop3ListData() {
        ArrayList<EventListModel> models = new ArrayList<EventListModel>();
        Iterator<Event> EventItr = eventMap.values().iterator();
        int i = 0;
        while (EventItr.hasNext() && i < 3) {
            Event temp = EventItr.next();
            double goalProgress;
            if ((temp.getCurrentFunds() / temp.getFundGoal()) < 1) {
                goalProgress = (temp.getCurrentFunds() / temp.getFundGoal());
            } else {
                goalProgress = 1;
            }
            models.add(new EventListModel(temp.getTitle(), temp.getType(), temp.getAssociatedMember(),
                    temp.getAssociatedEmail(), temp.getEventDate(), goalProgress, temp.getDescription()));
            i++;
        }
        Collections.sort(models, new Comparator<EventListModel>() {
            public int compare(EventListModel obj1, EventListModel obj2) {
                return obj1.getEventDate().compareToIgnoreCase(obj2.getEventDate());
            }
        });
        return models;
    }
}