package com.djkong.newschecker;

// NewsChecker class
public class NewsChecker {
    private int mID;
    private String mTitle;
    private String mDate;
    private String mTime;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private String mURL;
    private String mTargetString;
    private String mCatchedText;


    public NewsChecker(int ID, String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active, String URL, String TargetString){
        mID = ID;
        mTitle = Title;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatNo = RepeatNo;
        mRepeatType = RepeatType;
        mActive = Active;
        mURL = URL;
        mTargetString = TargetString;
    }

    public NewsChecker(String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active, String URL, String TargetString){
        mTitle = Title;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatNo = RepeatNo;
        mRepeatType = RepeatType;
        mActive = Active;
        mURL = URL;
        mTargetString = TargetString;
    }

    public NewsChecker(){}

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String repeatType) {
        mRepeatType = repeatType;
    }

    public String getRepeatNo() {
        return mRepeatNo;
    }

    public void setRepeatNo(String repeatNo) {
        mRepeatNo = repeatNo;
    }

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String repeat) {
        mRepeat = repeat;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        this.mURL = URL;
    }

    public String getTargetString() {
        return mTargetString;
    }

    public void setTargetString(String TargetString) {
        this.mTargetString = TargetString;
    }

    public String getCatchedText() {
        return mCatchedText;
    }

    public void setCatchedText(String CatchedText) {
        this.mCatchedText = CatchedText;
    }


}
