package groupware.dispatcher.service.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;

public class ContactInfo   {
    @JsonProperty("companyName")
    private String companyName = null;

    @JsonProperty("address")
    private Address address = null;

    @JsonProperty("phones")
    private List<Phone> phones = null;

    @JsonProperty("email")
    private Email email = null;

    public ContactInfo companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    /**
     * Get companyName
     * @return companyName
     **/
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ContactInfo address(Address address) {
        this.address = address;
        return this;
    }



    /**
     * Get address
     * @return address
     **/
   public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ContactInfo phones(List<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public ContactInfo addPhonesItem(Phone phonesItem) {
        if (this.phones == null) {
            this.phones = new ArrayList<Phone>();
        }
        this.phones.add(phonesItem);
        return this;
    }

    /**
     * Get phones
     * @return phones
     **/
   public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public ContactInfo emails(Email email) {
        this.email = email;
        return this;
    }



    /**
     * Get emails
     * @return emails
     **/
   public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactInfo contactInfo = (ContactInfo) o;
        return Objects.equals(this.companyName, contactInfo.companyName) &&
                Objects.equals(this.address, contactInfo.address) &&
                Objects.equals(this.phones, contactInfo.phones) &&
                Objects.equals(this.email, contactInfo.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, address, phones, email);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ContactInfo {\n");

        sb.append("    companyName: ").append(toIndentedString(companyName)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    phones: ").append(toIndentedString(phones)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
