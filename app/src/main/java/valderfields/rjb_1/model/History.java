package valderfields.rjb_1.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 11650 on 2017/6/24.
 */

public class History {

    private String id;
    private String name;
    private String tags;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("tags",tags);
        map.put("time",time);
        return map;
    }
}
