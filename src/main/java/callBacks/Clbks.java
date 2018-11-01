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
            {"setPhone", States.setAnswChannel},
            {"setBad", States.gotFeedBack},
            {"setGood", States.gotFeedBack},
            {"setThank", States.gotFeedBack},
            {"closeTicket", States.getFeedBack}
    }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (States) kv[1]));

    public Clbks(String clbk){
        if(claStates.containsKey(clbk)) {
            stt = claStates.get(clbk);
            if(stt == States.setCat) {
                switch (clbk) {
                    case "setCatVSP":
                        cat = "Качество услуг отделения Сбербанка";
                        break;
                    case "setCatProducts":
                        cat = "Продукты Сбербанка";
                        break;
                    case "setCatAnother":
                        cat = "Другое";
                        break;
                    default:
                        cat = "undefined";
                        break;
                }
            }else
                cat = clbk;
        }
    }
}
