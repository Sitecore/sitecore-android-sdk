package net.sitecore.android.sdk.web;

import android.text.TextUtils;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Contact {

    private static final String ID = "id";
    private static final String RAW_ID = "rawId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String COMPANY = "company";
    private static final String EMAILS = "emails";
    private static final String PHONES = "phones";
    private static final String WEBSITES = "websites";
    private static final String PHOTO = "photo";
    private static final String BIRTHDAY = "birthday";
    private static final String ADDRESSES = "addresses";

    private HashSet<String> mEmails;
    private HashSet<String> mPhones;
    private HashSet<String> mWebsites;
    private HashSet<JSONObject> mAddresses;

    private String mId;
    private String mRawId;
    private String mFirstName;
    private String mLastName;
    private String mCompany;
    private String mBirthday;
    private String mPhoto;

    public Contact() {
        super();

        mEmails = new HashSet<String>();
        mPhones = new HashSet<String>();
        mWebsites = new HashSet<String>();
        mAddresses = new HashSet<JSONObject>();
    }

    public void setId(String id) {
        mId = id;
    }

    public void setRawId(String rawId) {
        mRawId = rawId;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public void addPhone(String phone) {
        mPhones.add(phone);
    }

    public void addEmail(String email) {
        mEmails.add(email);
    }

    public void addAddress(JSONObject address) {
        mAddresses.add(address);
    }

    public void addWebsite(String site) {
        mWebsites.add(site);
    }

    public JSONObject getJSONObject() throws JSONException {
        final JSONObject result = new JSONObject();

        if (!TextUtils.isEmpty(mId)) result.put(ID, mId);
        if (!TextUtils.isEmpty(mRawId)) result.put(RAW_ID, mRawId);
        if (!TextUtils.isEmpty(mFirstName)) result.put(FIRST_NAME, mFirstName);
        if (!TextUtils.isEmpty(mLastName)) result.put(LAST_NAME, mLastName);
        if (!TextUtils.isEmpty(mCompany)) result.put(COMPANY, mCompany);
        if (!TextUtils.isEmpty(mBirthday)) result.put(BIRTHDAY, mBirthday);
        if (!TextUtils.isEmpty(mPhoto)) result.put(PHOTO, mPhoto);

        JSONArray emails = new JSONArray(mEmails);
        result.put(EMAILS, emails);

        JSONArray phones = new JSONArray(mPhones);
        result.put(PHONES, phones);

        JSONArray addresses = new JSONArray(mAddresses);
        result.put(ADDRESSES, addresses);

        JSONArray sites = new JSONArray(mWebsites);
        result.put(WEBSITES, sites);

        return result;
    }

}
