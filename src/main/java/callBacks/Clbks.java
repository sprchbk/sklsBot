package callBacks;

import inlineMessages.States;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Clbks {
    public States getStt() {
        return stt;
    }

    States stt = null;

    public String getCat() {
        return cat;
    }

    String cat = null;
    String calback ="";
    Map<String,States> claStates = Arrays.stream(new Object[][]{
            {"setStatesInOffices", States.inOffice},
            {"setStatesNotInOffices", States.notinOffice},
            {"setCatVSP", States.setCat},
            {"setCatProducts", States.setCat},
            {"setCatAnother", States.setCat},
            {"setMail", States.setAnswChannel},
            {"setContact", States.setAnswChannel},
            {"setPhone", States.setAnswChannel}
    }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (States) kv[1]));


    public Clbks(String clbk){
        if(claStates.containsKey(clbk)) {
            this.stt = claStates.get(clbk);
            this.cat = clbk;
        }
    }
}
