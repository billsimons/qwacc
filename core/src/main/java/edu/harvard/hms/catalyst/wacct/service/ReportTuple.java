package edu.harvard.hms.catalyst.wacct.service;

/**
 * @author Bill Simons
 */
public class ReportTuple {
    private String context;
    private String name;

    public ReportTuple(String context, String name) {
        this.context = context;
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
