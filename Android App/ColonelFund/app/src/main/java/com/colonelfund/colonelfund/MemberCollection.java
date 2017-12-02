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
 * Member Collection Class
 * Needs to be converted from regular java to android compatible for restore and save.
 */
public class MemberCollection {
    private static final String jsonFileName = "members.json";
    public Map<String,Member> memberMap = null;
    AssetManager am = null;

    /**
     * Default constructor. Will attempt to read a library file that has been saved.
     */
    public MemberCollection(Context myContext) {
        am = myContext.getAssets();
        boolean successfulLoad = this.restoreFromFile();
        if (successfulLoad) {
            System.out.println("Library loaded from: " + jsonFileName);
        }
        else {
            System.out.println("Unable to load library from: " + jsonFileName);
            System.out.println("New library created.");
            this.memberMap = new HashMap<String,Member>();
        }
    }

    /**
     * Attempts to take a member library, convert it to a json object, then save it to the
     * provided filename.
     * @return
     */
    public boolean saveJsonLibrary() {
        //toDo not operational
        return false;
    }
    /**
     * Restores a member library from the last saved or opened json library.
     * @return boolean
     */
    public boolean restoreFromFile() {
        boolean restored = false;
        this.memberMap = new HashMap<String, Member>();
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
                String[] eventTitles = keyList.toArray(new String[keyList.size()]);
                for (int i = 0; i < eventTitles.length; i++) {
                    Member memberDesc = new Member((JSONObject) obj.getJSONObject(eventTitles[i]));
                    if (memberDesc != null)
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
     * @param aMember
     * @return boolean
     */
    public boolean add(Member aMember){
        if (memberMap.containsKey(aMember.getUserID().toLowerCase())){
            System.out.println("Member already exists.");
            return false;
        } else {
            memberMap.put(aMember.getUserID().toLowerCase(), aMember);
            return true;
        }
    }
    /**
     * A method to remove the named Member from the library.
     * @param aMember
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
     * @param aMember
     * @return
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
     * @return
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
     * @return
     * @return
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
     * A method to check user login
     * @param aMemberID
     * @param aMemberPassword
     * @return boolean
     */
    public boolean checkLogin(String aMemberID, String aMemberPassword) {
        if (memberMap.containsKey(aMemberID.toLowerCase())) {
            if (get(aMemberID.toLowerCase()).getPassword().equals(aMemberPassword)) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Invalid.");
            return false;
        }
    }
}
