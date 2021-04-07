package com.yomplex.tests.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserContentVersion {

    public String useremail;
    public String phonenumber;
    public String userid;
    public String algebraversion;
    public String geometryversion;
    public String calculus1version;
    public String calculus2version;
    public String otherversion;
    public String updatedtime;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getAlgebraversion() {
        return algebraversion;
    }

    public void setAlgebraversion(String algebraversion) {
        this.algebraversion = algebraversion;
    }

    public String getGeometryversion() {
        return geometryversion;
    }

    public void setGeometryversion(String geometryversion) {
        this.geometryversion = geometryversion;
    }

    public String getCalculus1version() {
        return calculus1version;
    }

    public void setCalculus1version(String calculus1version) {
        this.calculus1version = calculus1version;
    }

    public String getCalculus2version() {
        return calculus2version;
    }

    public void setCalculus2version(String calculus2version) {
        this.calculus2version = calculus2version;
    }

    public String getOtherversion() {
        return otherversion;
    }

    public void setOtherversion(String otherversion) {
        this.otherversion = otherversion;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }
}
