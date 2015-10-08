package timetableapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import timetableapp.models.table.TableModel;
import lombok.Getter;
import processing.data.Table;
import processing.data.TableRow;

public class DataManager {

    @Getter
    private TableModel tm;

    @Getter
    private Map<String, Building> bl;

    @Getter
    private List<Activity> activities;

    private Map<String,String> names;
    
    private static DataManager instance = new DataManager();

    public static DataManager getInstance() {
        return instance;
    }

    private DataManager() {
            names = new HashMap(){{
                put("KSH","KOHNSTAMMHUIS");
                put("STU","STUDIO HVA");
                put("MLH","MULLER-LULOFSHUIS");
                put("TTH","THEO THIJSSENHUIS");
                put("BPH","BENNO PREMSELAHUIS");
                put("WBH","WIBAUTHUIS");
                put("KMH","KOETSIER-MONTAIGNEHUIS");
            }};
        
    }

    public void createTable(Table data) {
        if (tm == null) {
            tm = new TableModel(data);
        }
    }

    public void createActivities(Table data) throws ParseException {
        if (activities == null) {
            activities = new ArrayList<>();
            SimpleDateFormat dp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (TableRow tr : data.rows()) {
                activities.add(new Activity(tr.getString("Activity"),tr.getString("Course"),
                        dp.parse(tr.getString("Start date") + " " + tr.getString("Start time")),
                        dp.parse(tr.getString("End date") + " " + tr.getString("End time"))));
            }
        }
    }

    public void createMap(Table data) {
        if (bl == null) {
            bl = new HashMap<>();

            for (TableRow r : data.rows()) {
                String locations = r.getString("Location");

                if (locations.contains(",")) {
                    for (String location : locations.split(",")) {
                        addLocation(location);
                    }
                } else {
                    addLocation(locations);
                }
            }
        }
    }
    
    private void addLocation(String location) {
        String[] classRoomInfo = location.trim().split(" ");
        String code = classRoomInfo[0];
        ClassRoom c;
        if (bl.containsKey(code)) {
            c = makeClassRoom(classRoomInfo);
            if (c != null) {
                bl.get(code).addClassRoom(c);
            }
        } else {
            Building b = new Building(code, names.get(code));
            c = makeClassRoom(classRoomInfo);
            if (c != null) {
                b.addClassRoom(c);
            }
            bl.put(code, b);
        }
    }

    private ClassRoom makeClassRoom(String[] classRoomInfo) {
        ClassRoom classRoom = null;
        String[] formatOne = {"KSH", "STU", "MLH", "TTH", "BPH", "WBH"};
        String[] formatTwo = {"KMH"};

        if (Arrays.asList(formatOne).contains(classRoomInfo[0])) {
            int floor = Integer.valueOf(classRoomInfo[1].subSequence(0, 2).toString());
            char letter = classRoomInfo[1].charAt(2);
            int number = Integer.valueOf(classRoomInfo[1].subSequence(3, 5).toString());
            int capacity = Integer.valueOf(classRoomInfo[classRoomInfo.length - 1].substring(1, classRoomInfo[classRoomInfo.length - 1].length() - 1));
            classRoom = new ClassRoom(floor, letter, number, capacity);
        } else if (Arrays.asList(formatTwo).contains(classRoomInfo[0])) {
            int floor = Integer.valueOf(classRoomInfo[1].subSequence(0, 1).toString());
            int number = Integer.valueOf(classRoomInfo[1].subSequence(2, 4).toString());
            int capacity = Integer.valueOf(classRoomInfo[classRoomInfo.length - 1].substring(1, classRoomInfo[classRoomInfo.length - 1].length() - 1));
            classRoom = new ClassRoom(floor, number, capacity);
        }
        //KMH 2.20

        return classRoom;
    }

}
