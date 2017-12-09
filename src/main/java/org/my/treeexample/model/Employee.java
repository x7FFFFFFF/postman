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
public class Employee {

    String empID;
    String empName;
    String empEmail;
    String empAddress;

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }
    int salary;

    public Employee(String empID, String empName, int salary) {
        this.empID = empID;
        this.empName = empName;
        this.salary = salary;
    }

    public Employee(String empID, String empName, String empEmail, String empAddress, int salary) {
        this.empID = empID;
        this.empName = empName;
        this.empEmail = empEmail;
        this.empAddress = empAddress;
        this.salary = salary;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
    
    @Override
    public String toString()    {
        return empID + "-" + empName;
    }
}
