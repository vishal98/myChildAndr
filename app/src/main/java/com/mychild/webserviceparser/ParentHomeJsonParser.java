package com.mychild.webserviceparser;

/**
 * Created by Vijay on 3/21/15.
 */
public class ParentHomeJsonParser  {
    public static ParentHomeJsonParser parentHomeJsonParser = null;

    private ParentHomeJsonParser() {

    }

    public static ParentHomeJsonParser getInstance() {
        if (parentHomeJsonParser == null) {
            parentHomeJsonParser = new ParentHomeJsonParser();
        }
        return parentHomeJsonParser;
    }
}
