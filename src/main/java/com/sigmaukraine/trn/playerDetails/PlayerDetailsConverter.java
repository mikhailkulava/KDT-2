package com.sigmaukraine.trn.playerDetails;

import com.sigmaukraine.trn.playerDetails.PlayerDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by mkulava on 14.02.14.
 */
public class PlayerDetailsConverter {
    public static PlayerDetails convert(Map<String, String> details){
        PlayerDetails playerDetails = new PlayerDetails();
        playerDetails.setFrequentPlayerLevel(Integer.parseInt(details.get("FrequentPlayerLevel")));
        playerDetails.setAcceptsEmail(toBool(details.get("AcceptsEmail")));
        playerDetails.setAcceptsChat(toBool(details.get("AcceptsChat")));
        playerDetails.setPhone1(toNull(details.get("Phone1")));
        playerDetails.setAcceptsIdentityVerification(toBool(details.get("AcceptsIdentityVerification")));
        playerDetails.setPhone2(toNull(details.get("Phone2")));
        playerDetails.setSecurityPrompt(toNull(details.get("SecurityPrompt")));
        playerDetails.setNickName(toNull(details.get("NickName")));
        playerDetails.setHeardAboutUs(toNull(details.get("HeardAboutUs")));
        playerDetails.setLastName(toNull(details.get("LastName")));
        playerDetails.setRewardAbuser(toBool(details.get("IsRewardAbuser")));
        playerDetails.setAddressVerified(toBool(details.get("AddressVerified")));
        playerDetails.setRegion(toNull(details.get("Region")));
        playerDetails.setAddress3(toNull(details.get("Address3")));
        playerDetails.setAddress2(toNull(details.get("Address2")));
        playerDetails.setAddress1(toNull(details.get("Address1")));
        playerDetails.setTaxCategory(toNull(details.get("TaxCategory")));
        playerDetails.setAcceptsSMS(toBool(details.get("AcceptsSMS")));
        playerDetails.setFirstName(toNull(details.get("FirstName")));
        playerDetails.setMobilePhone(toNull(details.get("MobilePhone")));
        playerDetails.setCurrency(toNull(details.get("Currency")));
        playerDetails.setSecurityWord(toNull(details.get("SecurityWord")));
        playerDetails.setIdentificationManualVerified(toNull(details.get("IdentificationManualVerified")));
        playerDetails.setPreferredCommunicationChannel(Integer.parseInt(details.get("PreferredCommunicationChannel")));
        playerDetails.setIdentificationVerified(Integer.parseInt(details.get("IdentificationVerified")));
        playerDetails.setCountry(toNull(details.get("Country")));
        playerDetails.setBirthDate(correctDateFormat(details.get("BirthDate")));
        playerDetails.setCity(toNull(details.get("City")));
        playerDetails.setTrackingCampaign(toNull(details.get("TrackingCampaign")));
        playerDetails.setPostalCode(toNull(details.get("PostalCode")));
        playerDetails.setBirthName(toNull(details.get("BirthName")));
        playerDetails.setMiddleName(toNull(details.get("MiddleName")));
        playerDetails.setBirthPlace(toNull(details.get("BirthPlace")));
        playerDetails.setEmail(toNull(details.get("Email")));
        playerDetails.setNewsletter(Integer.parseInt(details.get("Newsletter")));
        playerDetails.setAcceptsRewards(toBool(details.get("AcceptsRewards")));
        playerDetails.setPreferredLanguage(toNull(details.get("PreferredLanguage")));
        playerDetails.setUsername(toNull(details.get("Username")));
        playerDetails.setGender(Integer.parseInt(details.get("Gender")));
        playerDetails.setIdentificationNumber(toNull(details.get("IdentificationNumber")));
        playerDetails.setMothersName(toNull(details.get("MothersName")));
        playerDetails.setAcceptsPromotionalSMS(toBool(details.get("AcceptsPromotionalSMS")));
        return playerDetails;
    }

    private static boolean toBool(String str){
        return str.equals("1") || str.equals("true") ? true : false;
    }

    private static String toNull(String str){
        return str = str == (null) ? str = "" : str;
    }

    private static String correctDateFormat(String date){
        final String OLD_FORMAT = "yyyy-mm-dd hh:mm:ss.s";
        final String NEW_FORMAT = "yyyy/MM/dd";
        date = "1986-09-28 14:59:40.0";
        String newDateString = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLD_FORMAT);
        try{
            Date d = simpleDateFormat.parse(date);
            simpleDateFormat.applyPattern(NEW_FORMAT);
            newDateString = simpleDateFormat.format(d);
        } catch (ParseException e){
            e.printStackTrace();
        } finally{
            return newDateString;
        }

    }

}
