package com.colonelfund.colonelfund;

import android.os.Build;
import android.support.annotation.RequiresApi;

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
 * Member Collection Class
 * Needs to be converted from regular java to android compatible for restore and save.
 */
public class MemberCollection {
    private static final String jsonFileName = "./members.json";
    public Map<String,Member> memberMap = null;

    /**
     * Default constructor. Will attempt to read a library file that has been saved.
     */
    public MemberCollection() {
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
        FileWriter fileOut = null;
        JSONObject jsonLibrary = new JSONObject();
        try {
            fileOut = new FileWriter(jsonFileName);
            Iterator<Member> libItr = this.memberMap.values().iterator();
            while (libItr.hasNext())
            {
                Member memberObj = (Member) libItr.next();
                jsonLibrary.put(memberObj.getUserID(), memberObj.toJson());
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
     * Restores a member library from the last saved or opened json library.
     * @return boolean
     */
    public boolean restoreFromFile() {
        boolean restored = false;
        this.memberMap = new HashMap<String,Member>();
        File libraryFile = new File(jsonFileName);
        FileInputStream fileIn = null;
        try {
            if (libraryFile.exists()) {
                System.out.println("Member Library collection found under: " + jsonFileName);
                fileIn = new FileInputStream(jsonFileName);
                String fileString = getFileContent(fileIn);
                JSONObject obj = new JSONObject(new JSONTokener(fileString));
                List<String> keyList = new ArrayList<String>();
                for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                    String key = it.next();
                    keyList.add(key);
                }
                String [] memberIDs = keyList.toArray(new String[keyList.size()]);
                for (int i=0; i < memberIDs.length; i++) {
                    Member memberObj = new Member((JSONObject)obj.getJSONObject(memberIDs[i]));
                    if (memberObj != null)
                        this.memberMap.put(memberObj.getUserID(), memberObj);
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
     * @param aMember
     * @return boolean
     */
    public boolean add(Member aMember){
        if (memberMap.containsKey(aMember.getUserID())){
            System.out.println("Member already exists.");
            return false;
        } else {
            memberMap.put(aMember.getUserID(), aMember);
            return true;
        }
    }
    /**
     * A method to remove the named Member from the library.
     * @param aMember
     * @return boolean
     */
    public boolean remove(String aMember) {
        if (memberMap.containsKey(aMember)) {
            memberMap.remove(aMember);
            return true;
        } else {
            System.out.println("Member does not exist.");
            return false;
        }
    }
    /**
     * Returns the video's description with member aMember.
     * @param aMember
     * @return
     */
    public Member get(String aMember) {
        if (memberMap.containsKey(aMember)) {
            return memberMap.get(aMember);
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
        if (memberMap.size() > 0)
            return memberMap.keySet().toArray(new String[memberMap.size()]);
        else
            return null;
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
     * File in to string
     * @param fis
     * @return
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
