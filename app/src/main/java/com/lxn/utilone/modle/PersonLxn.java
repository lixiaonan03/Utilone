package com.lxn.utilone.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
  *  @author lixiaonan
  *  功能描述: 存储序列化类测试的
  *  时 间： 2022/2/23 5:03 PM
  */
public class PersonLxn implements Parcelable {
    public String name;
    public int age;

    public PersonLxn(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected PersonLxn(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<PersonLxn> CREATOR = new Creator<PersonLxn>() {
        @Override
        public PersonLxn createFromParcel(Parcel in) {
            return new PersonLxn(in);
        }

        @Override
        public PersonLxn[] newArray(int size) {
            return new PersonLxn[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }
}
