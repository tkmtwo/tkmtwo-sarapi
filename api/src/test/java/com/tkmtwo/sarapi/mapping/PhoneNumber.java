package com.tkmtwo.sarapi.mapping;

import com.google.common.base.MoreConditions;
import com.google.common.base.Objects;
import java.io.Serializable;

//import org.apache.commons.lang.StringUtils;


/**
 *
 */
public class PhoneNumber {
  
  private String fullNumber;
  private String countryCode;
  private String areaCode;
  private String localNumber;

  public PhoneNumber(String fn, String cc, String ac, String ln) {
    setFullNumber(fn);
    setCountryCode(cc);
    setAreaCode(ac);
    setLocalNumber(ln);
  }

  public PhoneNumber() { }

  public String getFullNumber() { return fullNumber; }
  public void setFullNumber(String s) { fullNumber = s; }
  
  public String getCountryCode() { return countryCode; }
  public void setCountryCode(String s) { countryCode = s; }
  
  public String getAreaCode() { return areaCode; }
  public void setAreaCode(String s) { areaCode = s; }
  
  public String getLocalNumber() { return localNumber; }
  public void setLocalNumber(String s) { localNumber = s; }
  
  public boolean isBlank() {
    return
      MoreConditions.isBlank(getCountryCode())
      && MoreConditions.isBlank(getAreaCode())
      && MoreConditions.isBlank(getLocalNumber());
  }
      

  public String getPhoneNumber() {
    if (!MoreConditions.isBlank(getFullNumber())) {
      return getFullNumber();
    }
    
    String formattedNumber = getFormattedNumber();
    if (!MoreConditions.isBlank(formattedNumber)) {
      return formattedNumber;
    }
    
    return "###";
  }



  public String getFormattedNumber() {
    if (!MoreConditions.isBlank(getFullNumber())) {
      return getFullNumber();
    }

    StringBuffer sb = new StringBuffer();

    if (!MoreConditions.isBlank(getCountryCode())) {
      sb.append("+")
        .append(getCountryCode());
    }
    if (!MoreConditions.isBlank(getAreaCode())) {
      sb.append(" (")
        .append(getAreaCode())
        .append(")");
    }
    if (!MoreConditions.isBlank(getLocalNumber())) {
      sb.append(" ")
        .append(getLocalNumber());
    }
    return sb.toString();
  }


  
  
  
  
  
  
  
  
  
  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!(o instanceof PhoneNumber)) { return false; }
      
    PhoneNumber impl = (PhoneNumber) o;
    
    return
      Objects.equal(getCountryCode(), impl.getCountryCode())
      && Objects.equal(getAreaCode(), impl.getAreaCode())
      && Objects.equal(getLocalNumber(), impl.getLocalNumber());
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(getCountryCode(), getAreaCode(), getLocalNumber());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("fullNumber", getFullNumber())
      .add("countryCode", getCountryCode())
      .add("areaCode", getAreaCode())
      .add("localNumber", getLocalNumber())
      .toString();
  }
  
  
  
  
}
