
package eu.csaware.stix2.observables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import eu.csaware.stix2.common.CyberObservableCore;
import eu.csaware.stix2.common.Dictionary;
import eu.csaware.stix2.common.Stix2Type;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * mac-addr
 * <p>
 * The MAC Address Object represents a single Media Access Control (MAC) address.
 */
public class MacAddr extends CyberObservableCore {

    /**
     * Specifies one or more mac addresses expressed using CIDR notation.
     * (Required)
     */
    @SerializedName("value")
    @Expose
    @Pattern(regexp = "^([0-9a-f]{2}[:]){5}([0-9a-f]{2})$")
    @NotNull
    private String value;

    /**
     * No args constructor for use in serialization
     */
    public MacAddr() {
    }

    public MacAddr(String value, Dictionary extensions) {
        super(extensions);
        this.value = value;
    }

    /**
     * Specifies one or more mac addresses expressed using CIDR notation.
     * (Required)
     */
    public String getValue() {
        return value;
    }

    /**
     * Specifies one or more mac addresses expressed using CIDR notation.
     * (Required)
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MacAddr.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        int baseLength = sb.length();
        String superString = super.toString();
        if (superString != null) {
            int contentStart = superString.indexOf('[');
            int contentEnd = superString.lastIndexOf(']');
            if ((contentStart >= 0) && (contentEnd > contentStart)) {
                sb.append(superString, (contentStart + 1), contentEnd);
            } else {
                sb.append(superString);
            }
        }
        if (sb.length() > baseLength) {
            sb.append(',');
        }
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null) ? "<null>" : this.value));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.value == null) ? 0 : this.value.hashCode()));
        result = ((result * 31) + super.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MacAddr) == false) {
            return false;
        }
        MacAddr rhs = ((MacAddr) other);
        return (super.equals(rhs) && ((this.value == rhs.value) || ((this.value != null) && this.value.equals(rhs.value))));
    }

}
