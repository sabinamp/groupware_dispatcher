package groupware.dispatcher.service.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class Total   {
    @JsonProperty("amountAfterTax")
    private Float amountAfterTax = null;

    @JsonProperty("currencyCode")
    private String currencyCode = null;

    public Total amountAfterTax(Float amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
        return this;
    }

    /**
     * Get amountAfterTax
     * @return amountAfterTax
     **/
    public Float getAmountAfterTax() {
        return amountAfterTax;
    }

    public void setAmountAfterTax(Float amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
    }

    public Total currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    /**
     * Get currencyCode
     * @return currencyCode
     **/
       public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Total total = (Total) o;
        return Objects.equals(this.amountAfterTax, total.amountAfterTax) &&
                Objects.equals(this.currencyCode, total.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountAfterTax, currencyCode);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Total {\n");

        sb.append("    amountAfterTax: ").append(toIndentedString(amountAfterTax)).append("\n");
        sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
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

