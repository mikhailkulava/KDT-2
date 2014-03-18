package com.sigmaukraine.trn.playerDetails;

/**
 * Created by mkulava on 14.02.14.
 */
public class PlayerDetails {
    int FrequentPlayerLevel;
    boolean AcceptsEmail;
    boolean AcceptsChat;
    String Phone1;
    boolean AcceptsIdentityVerification;
    String Phone2;
    String SecurityPrompt;
    String NickName;
    String HeardAboutUs;
    String LastName;
    boolean IsRewardAbuser;
    boolean AddressVerified;
    String Region;
    String Address3;
    String Address2;
    String Address1;
    String TaxCategory;
    Boolean AcceptsSMS;
    String FirstName;
    String MobilePhone;
    String Currency;
    String SecurityWord;
    String IdentificationManualVerified;
    int PreferredCommunicationChannel;
    int IdentificationVerified;
    String Country;
    String BirthDate;
    String City;
    String TrackingCampaign;
    String PostalCode;
    String BirthName;
    String MiddleName;
    String BirthPlace;
    String Email;
    int Newsletter;
    boolean AcceptsRewards;
    String PreferredLanguage;
    String Username;
    int Gender;
    String IdentificationNumber;
    String MothersName;
    boolean AcceptsPromotionalSMS;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerDetails that = (PlayerDetails) o;

        if (AcceptsChat != that.AcceptsChat) return false;
        if (AcceptsEmail != that.AcceptsEmail) return false;
        if (AcceptsIdentityVerification != that.AcceptsIdentityVerification) return false;
        if (AcceptsPromotionalSMS != that.AcceptsPromotionalSMS) return false;
        if (AcceptsRewards != that.AcceptsRewards) return false;
        if (AddressVerified != that.AddressVerified) return false;
        if (FrequentPlayerLevel != that.FrequentPlayerLevel) return false;
        if (Gender != that.Gender) return false;
        if (IdentificationVerified != that.IdentificationVerified) return false;
        if (IsRewardAbuser != that.IsRewardAbuser) return false;
        if (Newsletter != that.Newsletter) return false;
        if (PreferredCommunicationChannel != that.PreferredCommunicationChannel) return false;
        if (AcceptsSMS != null ? !AcceptsSMS.equals(that.AcceptsSMS) : that.AcceptsSMS != null) return false;
        if (Address1 != null ? !Address1.equals(that.Address1) : that.Address1 != null) return false;
        if (Address2 != null ? !Address2.equals(that.Address2) : that.Address2 != null) return false;
        if (Address3 != null ? !Address3.equals(that.Address3) : that.Address3 != null) return false;
        if (BirthDate != null ? !BirthDate.equals(that.BirthDate) : that.BirthDate != null) return false;
        if (BirthName != null ? !BirthName.equals(that.BirthName) : that.BirthName != null) return false;
        if (BirthPlace != null ? !BirthPlace.equals(that.BirthPlace) : that.BirthPlace != null) return false;
        if (City != null ? !City.equals(that.City) : that.City != null) return false;
        if (Country != null ? !Country.equals(that.Country) : that.Country != null) return false;
        if (Currency != null ? !Currency.equals(that.Currency) : that.Currency != null) return false;
        if (Email != null ? !Email.equals(that.Email) : that.Email != null) return false;
        if (FirstName != null ? !FirstName.equals(that.FirstName) : that.FirstName != null) return false;
        if (HeardAboutUs != null ? !HeardAboutUs.equals(that.HeardAboutUs) : that.HeardAboutUs != null) return false;
        if (IdentificationManualVerified != null ? !IdentificationManualVerified.equals(that.IdentificationManualVerified) : that.IdentificationManualVerified != null)
            return false;
        if (IdentificationNumber != null ? !IdentificationNumber.equals(that.IdentificationNumber) : that.IdentificationNumber != null)
            return false;
        if (LastName != null ? !LastName.equals(that.LastName) : that.LastName != null) return false;
        if (MiddleName != null ? !MiddleName.equals(that.MiddleName) : that.MiddleName != null) return false;
        if (MobilePhone != null ? !MobilePhone.equals(that.MobilePhone) : that.MobilePhone != null) return false;
        if (MothersName != null ? !MothersName.equals(that.MothersName) : that.MothersName != null) return false;
        if (NickName != null ? !NickName.equals(that.NickName) : that.NickName != null) return false;
        if (Phone1 != null ? !Phone1.equals(that.Phone1) : that.Phone1 != null) return false;
        if (Phone2 != null ? !Phone2.equals(that.Phone2) : that.Phone2 != null) return false;
        if (PostalCode != null ? !PostalCode.equals(that.PostalCode) : that.PostalCode != null) return false;
        if (PreferredLanguage != null ? !PreferredLanguage.equals(that.PreferredLanguage) : that.PreferredLanguage != null)
            return false;
        if (Region != null ? !Region.equals(that.Region) : that.Region != null) return false;
        if (SecurityPrompt != null ? !SecurityPrompt.equals(that.SecurityPrompt) : that.SecurityPrompt != null)
            return false;
        if (SecurityWord != null ? !SecurityWord.equals(that.SecurityWord) : that.SecurityWord != null) return false;
        if (TaxCategory != null ? !TaxCategory.equals(that.TaxCategory) : that.TaxCategory != null) return false;
        if (TrackingCampaign != null ? !TrackingCampaign.equals(that.TrackingCampaign) : that.TrackingCampaign != null)
            return false;
        if (Username != null ? !Username.equals(that.Username) : that.Username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = FrequentPlayerLevel;
        result = 31 * result + (AcceptsEmail ? 1 : 0);
        result = 31 * result + (AcceptsChat ? 1 : 0);
        result = 31 * result + (Phone1 != null ? Phone1.hashCode() : 0);
        result = 31 * result + (AcceptsIdentityVerification ? 1 : 0);
        result = 31 * result + (Phone2 != null ? Phone2.hashCode() : 0);
        result = 31 * result + (SecurityPrompt != null ? SecurityPrompt.hashCode() : 0);
        result = 31 * result + (NickName != null ? NickName.hashCode() : 0);
        result = 31 * result + (HeardAboutUs != null ? HeardAboutUs.hashCode() : 0);
        result = 31 * result + (LastName != null ? LastName.hashCode() : 0);
        result = 31 * result + (IsRewardAbuser ? 1 : 0);
        result = 31 * result + (AddressVerified ? 1 : 0);
        result = 31 * result + (Region != null ? Region.hashCode() : 0);
        result = 31 * result + (Address3 != null ? Address3.hashCode() : 0);
        result = 31 * result + (Address2 != null ? Address2.hashCode() : 0);
        result = 31 * result + (Address1 != null ? Address1.hashCode() : 0);
        result = 31 * result + (TaxCategory != null ? TaxCategory.hashCode() : 0);
        result = 31 * result + (AcceptsSMS != null ? AcceptsSMS.hashCode() : 0);
        result = 31 * result + (FirstName != null ? FirstName.hashCode() : 0);
        result = 31 * result + (MobilePhone != null ? MobilePhone.hashCode() : 0);
        result = 31 * result + (Currency != null ? Currency.hashCode() : 0);
        result = 31 * result + (SecurityWord != null ? SecurityWord.hashCode() : 0);
        result = 31 * result + (IdentificationManualVerified != null ? IdentificationManualVerified.hashCode() : 0);
        result = 31 * result + PreferredCommunicationChannel;
        result = 31 * result + IdentificationVerified;
        result = 31 * result + (Country != null ? Country.hashCode() : 0);
        result = 31 * result + (BirthDate != null ? BirthDate.hashCode() : 0);
        result = 31 * result + (City != null ? City.hashCode() : 0);
        result = 31 * result + (TrackingCampaign != null ? TrackingCampaign.hashCode() : 0);
        result = 31 * result + (PostalCode != null ? PostalCode.hashCode() : 0);
        result = 31 * result + (BirthName != null ? BirthName.hashCode() : 0);
        result = 31 * result + (MiddleName != null ? MiddleName.hashCode() : 0);
        result = 31 * result + (BirthPlace != null ? BirthPlace.hashCode() : 0);
        result = 31 * result + (Email != null ? Email.hashCode() : 0);
        result = 31 * result + Newsletter;
        result = 31 * result + (AcceptsRewards ? 1 : 0);
        result = 31 * result + (PreferredLanguage != null ? PreferredLanguage.hashCode() : 0);
        result = 31 * result + (Username != null ? Username.hashCode() : 0);
        result = 31 * result + Gender;
        result = 31 * result + (IdentificationNumber != null ? IdentificationNumber.hashCode() : 0);
        result = 31 * result + (MothersName != null ? MothersName.hashCode() : 0);
        result = 31 * result + (AcceptsPromotionalSMS ? 1 : 0);
        return result;
    }

    public int getFrequentPlayerLevel() {
        return FrequentPlayerLevel;
    }

    public void setFrequentPlayerLevel(int frequentPlayerLevel) {
        FrequentPlayerLevel = frequentPlayerLevel;
    }

    public boolean isAcceptsEmail() {
        return AcceptsEmail;
    }

    public void setAcceptsEmail(boolean acceptsEmail) {
        AcceptsEmail = acceptsEmail;
    }

    public boolean isAcceptsChat() {
        return AcceptsChat;
    }

    public void setAcceptsChat(boolean acceptsChat) {
        AcceptsChat = acceptsChat;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public boolean isAcceptsIdentityVerification() {
        return AcceptsIdentityVerification;
    }

    public void setAcceptsIdentityVerification(boolean acceptsIdentityVerification) {
        AcceptsIdentityVerification = acceptsIdentityVerification;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getSecurityPrompt() {
        return SecurityPrompt;
    }

    public void setSecurityPrompt(String securityPrompt) {
        SecurityPrompt = securityPrompt;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeardAboutUs() {
        return HeardAboutUs;
    }

    public void setHeardAboutUs(String heardAboutUs) {
        HeardAboutUs = heardAboutUs;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public boolean isRewardAbuser() {
        return IsRewardAbuser;
    }

    public void setRewardAbuser(boolean isRewardAbuser) {
        IsRewardAbuser = isRewardAbuser;
    }

    public boolean isAddressVerified() {
        return AddressVerified;
    }

    public void setAddressVerified(boolean addressVerified) {
        AddressVerified = addressVerified;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getAddress3() {
        return Address3;
    }

    public void setAddress3(String address3) {
        Address3 = address3;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getTaxCategory() {
        return TaxCategory;
    }

    public void setTaxCategory(String taxCategory) {
        TaxCategory = taxCategory;
    }

    public Boolean getAcceptsSMS() {
        return AcceptsSMS;
    }

    public void setAcceptsSMS(Boolean acceptsSMS) {
        AcceptsSMS = acceptsSMS;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getSecurityWord() {
        return SecurityWord;
    }

    public void setSecurityWord(String securityWord) {
        SecurityWord = securityWord;
    }

    public String getIdentificationManualVerified() {
        return IdentificationManualVerified;
    }

    public void setIdentificationManualVerified(String identificationManualVerified) {
        IdentificationManualVerified = identificationManualVerified;
    }

    public int getPreferredCommunicationChannel() {
        return PreferredCommunicationChannel;
    }

    public void setPreferredCommunicationChannel(int preferredCommunicationChannel) {
        PreferredCommunicationChannel = preferredCommunicationChannel;
    }

    public int getIdentificationVerified() {
        return IdentificationVerified;
    }

    public void setIdentificationVerified(int identificationVerified) {
        IdentificationVerified = identificationVerified;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getTrackingCampaign() {
        return TrackingCampaign;
    }

    public void setTrackingCampaign(String trackingCampaign) {
        TrackingCampaign = trackingCampaign;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getBirthName() {
        return BirthName;
    }

    public void setBirthName(String birthName) {
        BirthName = birthName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getBirthPlace() {
        return BirthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        BirthPlace = birthPlace;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getNewsletter() {
        return Newsletter;
    }

    public void setNewsletter(int newsletter) {
        Newsletter = newsletter;
    }

    public boolean isAcceptsRewards() {
        return AcceptsRewards;
    }

    public void setAcceptsRewards(boolean acceptsRewards) {
        AcceptsRewards = acceptsRewards;
    }

    public String getPreferredLanguage() {
        return PreferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        PreferredLanguage = preferredLanguage;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getIdentificationNumber() {
        return IdentificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        IdentificationNumber = identificationNumber;
    }

    public String getMothersName() {
        return MothersName;
    }

    public void setMothersName(String mothersName) {
        MothersName = mothersName;
    }

    public boolean isAcceptsPromotionalSMS() {
        return AcceptsPromotionalSMS;
    }

    public void setAcceptsPromotionalSMS(boolean acceptsPromotionalSMS) {
        AcceptsPromotionalSMS = acceptsPromotionalSMS;
    }
}
