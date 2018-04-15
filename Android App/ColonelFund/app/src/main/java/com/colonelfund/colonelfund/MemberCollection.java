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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Member Collection Class
 *
 * gets collections of members from remote database, converts those member objects
 * from JSON objects to a Map<String,Member> and writes those members for local
 * storage in JSON forum.
 */
public class MemberCollection {
    private static final String jsonFileName = "members.json";
    private final String TAG = "MemberListFragment";
    private final String URL_FOR_NAMES = "https://wesll.com/colonelfund/members.php";
    public Map<String, Member> memberMap = null;
    AssetManager am = null;
    AppSingleton appContext;
    Context context;

    /**
     * Default Constructor. will attempt to read a library file that has been saved
     *      locally and takes in Context for later use.
     *
     * @param myContext for activity.
     */
    public MemberCollection(Context myContext) {
        this.am = myContext.getAssets();
        this.appContext = new AppSingleton(myContext);
        this.context = myContext;
        boolean successfulLoad = restoreFromFile(context);
        if (successfulLoad) {
            System.out.println("Library loaded from: " + jsonFileName);
        } else {
            System.out.println("Unable to load library from: " + jsonFileName);
            System.out.println("New library created.");
            this.memberMap = new HashMap<String, Member>();
        }
    }

    /**
     *  queries remote database for updated members in JSONArray forum.
     */
    public void updateFromRemote() {
        String cancel_req_tag = "member_list";
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_NAMES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Member Response: " + response);
                JSONObject outputObj = new JSONObject();
                try {
                    JSONArray jArray = new JSONArray(response);
                    JSONObject jObj;
                    for (int i = 0; i < jArray.length(); i++) {
                        jObj = (JSONObject) jArray.get(i);
                        Member aMember = new Member(jObj.getString("userID"), fixName(jObj.getString("firstName")), fixName(jObj.getString("lastName")), jObj.getString("emailAddress"), jObj.getString("phoneNumber"), jObj.getString("username"), jObj.getString("profilePicURL"), jObj.getString("facebookID"), jObj.getString("googleID"), jObj.getString("firebaseID"));
                        outputObj.put(jObj.getString("userID"), aMember.toJson());
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
     * Capitalizes the first letters of a members name.
     *
     * @param name a members name
     * @return returns the fixed string.
     */
    public String fixName(String name) {
        return (name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    /**
     * Saves json to local file.
     *
     * @param jObj of library
     * @param ctx of activity.
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
     * Restores member JSON objects from local storage.
     *
     * @param ctx application context for permission to access local storage
     * @return true/false for success in reading from local storage.
     */
    public boolean restoreFromFile(Context ctx) {
        boolean restored = false;
        this.memberMap = new HashMap<String, Member>();
        FileInputStream inputStream = null;
        try {
            inputStream = ctx.openFileInput(jsonFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (inputStream != null) {
                System.out.println("Member collection found under: " + jsonFileName);
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
                String[] memberList = keyList.toArray(new String[keyList.size()]);
                for (int i = 0; i < memberList.length; i++) {
                    Member memberDesc = new Member((JSONObject) obj.getJSONObject(memberList[i]));
                    this.memberMap.put(memberDesc.getUserID().toLowerCase(), memberDesc);
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
     * A method to add a new video to the library. True is returned when the request is successful.
     *
     * @param aMember for a user.
     * @return boolean
     */
    public boolean add(Member aMember) {
        if (memberMap.containsKey(aMember.getUserID().toLowerCase())) {
            System.out.println("Member already exists.");
            return false;
        } else {
            memberMap.put(aMember.getUserID().toLowerCase(), aMember);
            return true;
        }
    }

    /**
     * A method to remove the named Member from the library.
     *
     * @param aMember for a user.
     * @return boolean
     */
    public boolean remove(String aMember) {
        if (memberMap.containsKey(aMember.toLowerCase())) {
            memberMap.remove(aMember.toLowerCase());
            return true;
        } else {
            System.out.println("Member does not exist.");
            return false;
        }
    }

    /**
     * Returns the member with member aMember.
     *
     * @param aMember for a user
     * @return boolean
     */
    public Member get(String aMember) {
        if (memberMap.containsKey(aMember.toLowerCase())) {
            return memberMap.get(aMember.toLowerCase());
        } else {
            System.out.println("Member does not exist.");
            return null;
        }
    }

    /**
     * Returns an array of strings, which are all of the members in the library.
     *
     * @return array of strings of users.
     */
    public String[] getMembers() {
        if (memberMap.size() > 0) {
            ArrayList<String> memberArray = new ArrayList<String>();
            Iterator<Member> iter = memberMap.values().iterator();
            while (iter.hasNext()) {
                Member aMember = (Member) iter.next();
                memberArray.add(aMember.getUserID());
            }
            return memberArray.toArray(new String[0]);
        } else {
            return null;
        }
    }

    /**
     * Returns a string of all Members.
     *
     * @return string of all users.
     */
    public String toString() {
        String toString = "";
        if (memberMap.size() > 0) {
            Iterator<Member> iter = memberMap.values().iterator();
            while (iter.hasNext()) {
                Member aMember = (Member) iter.next();
                toString += aMember.toString();
            }
        }
        return toString;
    }

    /**
     * Gets member map values.
     *
     * @return collection of members.
     */
    public Collection<Member> getMembersList() {
        return memberMap.values();
    }

    /**
     * Generates Initials and User Name for member list.
     *
     * @return array list of models for member list
     */
    public ArrayList<MemberListModel> generateListData() {
        ArrayList<MemberListModel> models = new ArrayList<MemberListModel>();
        Iterator<Member> memberIter = memberMap.values().iterator();
        while (memberIter.hasNext()) {
            Member temp = memberIter.next();
            String firstName = temp.getFirstName();
            String lastName = temp.getLastName();
            models.add(new MemberListModel(firstName.substring(0, 1) + lastName.substring(0, 1),
                    temp.getUserID(), firstName, lastName, temp.getEmailAddress()));
        }
        return models;
    }

    /**
     * Generates Initials and User Name for member list.
     *
     * @return array list of models for member list
     */
    public ArrayList<MemberListModel> generateTop3ListData() {
        ArrayList<MemberListModel> models = new ArrayList<MemberListModel>();
        Iterator<Member> memberIter = memberMap.values().iterator();
        int i = 0;
        while (memberIter.hasNext() && i < 3) {
            Member temp = memberIter.next();
            String firstName = temp.getFirstName();
            String lastName = temp.getLastName();
            models.add(new MemberListModel(firstName.substring(0, 1) + lastName.substring(0, 1),
                    temp.getUserID(), firstName, lastName, temp.getEmailAddress()));
            i++;
        }
        return models;
    }
}