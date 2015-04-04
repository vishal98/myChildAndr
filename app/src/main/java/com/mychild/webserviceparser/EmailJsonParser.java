package com.mychild.webserviceparser;

/**
 * Created by Sandeep on 03-04-2015.
 */
public class EmailJsonParser {
    public static EmailJsonParser emailJsonParser = null;

    private EmailJsonParser() {
    }

    public static EmailJsonParser getInstance() {
        if (emailJsonParser == null) {
            emailJsonParser = new EmailJsonParser();
        }
        return emailJsonParser;
    }
}
