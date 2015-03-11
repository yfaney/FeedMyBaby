package com.yfaney.feedmybaby;

/**
 * This class handles table key and items for access XML data easily.
 * @author Faney
 *
 */
public class TableItem {
    String fieldName;
    String fieldTitle;
    int fieldID;
    String fieldContents;
    public TableItem(String fieldName, String fieldTitle, int fieldID, String fieldContents) {
        super();
        this.fieldName = fieldName;
        this.fieldTitle = fieldTitle;
        this.fieldContents = fieldContents;
        this.fieldID = fieldID;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldTitle() {
        return fieldTitle;
    }
    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }
    public int getFieldId() {
        return fieldID;
    }
    public void setFieldId(int fieldID) {
        this.fieldID = fieldID;
    }
    public String getFieldContents() {
        return fieldContents;
    }
    public void setFieldContents(String fieldContents) {
        this.fieldContents = fieldContents;
    }
}