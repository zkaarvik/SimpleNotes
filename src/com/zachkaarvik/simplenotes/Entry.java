package com.zachkaarvik.simplenotes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zach on 28/01/14.
 */
public class Entry implements Parcelable {
    private long id;
    private String name;
    private String content;

    public Entry(long id, String name, String content){
        this.id = id;
        this.name = name;
        this.content = content;
    }
    public Entry(long id, String name){
        this.id = id;
        this.name = name;
        this.content = "";
    }
    public Entry(){
        this.id = -1;
    }
    public Entry(Parcel in){
        readFromParcel(in);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Entry makeShell() {
        return new Entry(this.getId(), this.getName(), "");

    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (id != entry.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(content);
    }

    public void readFromParcel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        content = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Entry createFromParcel(Parcel in) {
                    return new Entry(in);
                }
                public Entry[] newArray(int size) {
                    return new Entry[size];
                }
            };
}
