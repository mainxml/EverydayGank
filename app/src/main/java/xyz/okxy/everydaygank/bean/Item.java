package xyz.okxy.everydaygank.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

    @SerializedName("_id")
    public String mId;

    @SerializedName("desc")
    public String mDesc;

    @SerializedName("images")
    public List<String> mImages;

    //@SerializedName("createdAt")
    //public String mCreatedAt;

    @SerializedName("publishedAt")
    public String mPublishedAt;

    //@SerializedName("source")
    //public String mSource;

    @SerializedName("type")
    public String mType;

    @SerializedName("url")
    public String mUrl;

    @SerializedName("who")
    public String mWho;

}
