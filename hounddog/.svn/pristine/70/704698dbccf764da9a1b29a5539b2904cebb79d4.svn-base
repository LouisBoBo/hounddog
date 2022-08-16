package com.slxk.hounddog.mvp.model.entity;

//列表项类
public class TreeData {
    private int familyCount;//当前组织下级个数
    private String familyName; //组织名称
    private String familySid; // family_sid
    private Boolean hasChild; //是否有下级
    private int Level; //层级
    private boolean isClick; //是否点击

    public int getFamilyCount() {
        return familyCount;
    }

    public void setFamilyCount(int familyCount) {
        this.familyCount = familyCount;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilySid() {
        return familySid;
    }

    public void setFamilySid(String familySid) {
        this.familySid = familySid;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    /**
     * @param name     组织名称
     * @param code     当前的ID
     * @param Level    层级
     * @param hasChild 是否可以展开
     */
    public TreeData(String name, String code, int Level, Boolean hasChild, int count) {
        this.familyName = name;
        this.familySid = code;
        this.Level = Level;
        this.hasChild = hasChild;
        this.familyCount = count;
    }


    @Override
    public String toString() {
        return "TreeData{" +
                "familyName='" + familyName + '\'' +
                ", familySid='" + familySid + '\'' +
                ", hasChild=" + hasChild +
                ", Level=" + Level +
                ", isClick=" + isClick +
                ", familyCount=" + familyCount +
                '}';
    }
}
