package android.compact.impl;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TaskPayload implements Parcelable, Serializable {
    public String identify;
    public String title;
    public String content;
    public String pkg;
    public String from;
    public String to;
    public String url;
    public String uri;
    public String path;
    public String auth;
    public String ex;
    public long timestamp;
    public int state;

    public TaskPayload() {
        super();
    }

    protected TaskPayload(Parcel in) {
        identify = in.readString();
        title = in.readString();
        content = in.readString();
        pkg = in.readString();
        from = in.readString();
        to = in.readString();
        url = in.readString();
        uri = in.readString();
        path = in.readString();
        auth = in.readString();
        ex = in.readString();
        timestamp = in.readLong();
        state = in.readInt();
    }

    public static final Creator<TaskPayload> CREATOR = new Creator<TaskPayload>() {
        @Override
        public TaskPayload createFromParcel(Parcel in) {
            return new TaskPayload(in);
        }

        @Override
        public TaskPayload[] newArray(int size) {
            return new TaskPayload[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identify);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(pkg);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(url);
        dest.writeString(uri);
        dest.writeString(path);
        dest.writeString(auth);
        dest.writeString(ex);
        dest.writeLong(timestamp);
        dest.writeInt(state);
    }
}
