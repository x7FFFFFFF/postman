/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.my.treeexample.model;

/**
 *
 * @author thienlh
 */
public class Department {
    String depID;
    String depName;

    public Department(String depID, String depName) {
        this.depID = depID;
        this.depName = depName;
    }

    public String getDepID() {
        return depID;
    }

    public void setDepID(String depID) {
        this.depID = depID;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }
    
    @Override
    public String toString()    {
        return depID + "-" + depName;
    }
}
