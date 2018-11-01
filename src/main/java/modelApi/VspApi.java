package modelApi;

import com.google.common.base.Charsets;
import model.Vsp;
import org.json.*;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class VspApi {
    HashMap<String, Vsp> vspList;

    public void loadVspList() {
        HashMap<String, Vsp> vspList = new HashMap<>();
        try {
            URL url = Resources.getResource("vsp_list.json");
            String text = Resources.toString(url, Charsets.UTF_8);
            JSONObject obj = new JSONObject(text);
            JSONArray arr = obj.getJSONArray("VSP");
            for (Object o : arr){
                JSONObject vspObj = (JSONObject) o;
                Vsp vsp = new Vsp();
                vsp.setVspName(vspObj.getString("NAME"));
                vsp.setAmdins(getListFromStringArray(vspObj.getJSONArray("ADMIN_IDS")));
                vspList.put(vspObj.getString("NAME"), vsp);
            };
         } catch (IOException e) {
            e.printStackTrace();
        }
        this.vspList = vspList;
    }

    private static List<String> getListFromStringArray(JSONArray jsonArray) {
        List<String> res = new LinkedList<String>();
        for (Object o : jsonArray.toList()) {
            String str = (String) o;
            res.add(str);
        };
        return res;
    };

    public Vsp getVspById(String vspId) {
        return vspList.get(vspId);
    }

}
