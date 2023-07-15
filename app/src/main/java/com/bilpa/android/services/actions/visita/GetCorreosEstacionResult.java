package com.bilpa.android.services.actions.visita;


import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetCorreosEstacionResult extends BaseResult {


    @SerializedName("datos")
    public List<EmailData> emails;

    public GetCorreosEstacionResult() {
        this.emails = new ArrayList<>();
    }

    public List<String> getEmails() {
        List<String> result = new ArrayList<>();
        for (EmailData email : emails) {
            result.add(email.email);
        }
        return result;
    }

    @Override
    public String toString() {
        return "GetCorreosEstacionResult{" +
                "emails=" + emails +
                "} " + super.toString();
    }

    public class EmailData implements Serializable {

        private static final long serialVersionUID = -3368330244094349556L;

        public Long id;
        public String email;

        @Override
        public String toString() {
            return "EmailData{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    '}';
        }
    }



}
