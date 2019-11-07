
package eu.csaware.stix2.observables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import eu.csaware.stix2.common.CyberObservableCore;
import eu.csaware.stix2.common.Dictionary;
import eu.csaware.stix2.common.Stix2Type;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * directory
 * <p>
 * The Directory Object represents the properties common to a file system directory.
 */
public class Directory extends CyberObservableCore {

    /**
     * Specifies the path, as originally observed, to the directory on the file system.
     * (Required)
     */
    @SerializedName("path")
    @Expose
    @NotNull
    private String path;
    /**
     * Specifies the observed encoding for the path.
     */
    @SerializedName("path_enc")
    @Expose
    @Pattern(regexp = "^[a-zA-Z0-9/\\.+_:-]{2,250}$")
    private String pathEnc;
    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    @SerializedName("created")
    @Expose
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]|60)(\\.[0-9]+)?Z$")
    private String created;
    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    @SerializedName("modified")
    @Expose
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]|60)(\\.[0-9]+)?Z$")
    private String modified;
    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    @SerializedName("accessed")
    @Expose
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]|60)(\\.[0-9]+)?Z$")
    private String accessed;
    /**
     * Specifies a list of references to other File and/or Directory Objects contained within the directory.
     */
    @SerializedName("contains_refs")
    @Expose
    @Size(min = 1)
    @Valid
    private List<String> containsRefs = new ArrayList<String>();

    /**
     * No args constructor for use in serialization
     */
    public Directory() {
    }

    public Directory(String path, String pathEnc, String created, String modified, String accessed, List<String> containsRefs, Dictionary extensions) {
        super(extensions);
        this.path = path;
        this.pathEnc = pathEnc;
        this.created = created;
        this.modified = modified;
        this.accessed = accessed;
        this.containsRefs = containsRefs;
    }

    /**
     * Specifies the path, as originally observed, to the directory on the file system.
     * (Required)
     */
    public String getPath() {
        return path;
    }

    /**
     * Specifies the path, as originally observed, to the directory on the file system.
     * (Required)
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Specifies the observed encoding for the path.
     */
    public String getPathEnc() {
        return pathEnc;
    }

    /**
     * Specifies the observed encoding for the path.
     */
    public void setPathEnc(String pathEnc) {
        this.pathEnc = pathEnc;
    }

    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    public String getCreated() {
        return created;
    }

    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    public String getModified() {
        return modified;
    }

    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    public String getAccessed() {
        return accessed;
    }

    /**
     * timestamp
     * <p>
     * Represents timestamps across the CTI specifications. The format is an RFC3339 timestamp, with a required timezone specification of 'Z'.
     */
    public void setAccessed(String accessed) {
        this.accessed = accessed;
    }

    /**
     * Specifies a list of references to other File and/or Directory Objects contained within the directory.
     */
    public List<String> getContainsRefs() {
        return containsRefs;
    }

    /**
     * Specifies a list of references to other File and/or Directory Objects contained within the directory.
     */
    public void setContainsRefs(List<String> containsRefs) {
        this.containsRefs = containsRefs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Directory.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("path");
        sb.append('=');
        sb.append(((this.path == null) ? "<null>" : this.path));
        sb.append(',');
        sb.append("pathEnc");
        sb.append('=');
        sb.append(((this.pathEnc == null) ? "<null>" : this.pathEnc));
        sb.append(',');
        sb.append("created");
        sb.append('=');
        sb.append(((this.created == null) ? "<null>" : this.created));
        sb.append(',');
        sb.append("modified");
        sb.append('=');
        sb.append(((this.modified == null) ? "<null>" : this.modified));
        sb.append(',');
        sb.append("accessed");
        sb.append('=');
        sb.append(((this.accessed == null) ? "<null>" : this.accessed));
        sb.append(',');
        sb.append("containsRefs");
        sb.append('=');
        sb.append(((this.containsRefs == null) ? "<null>" : this.containsRefs));
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
        result = ((result * 31) + ((this.path == null) ? 0 : this.path.hashCode()));
        result = ((result * 31) + ((this.containsRefs == null) ? 0 : this.containsRefs.hashCode()));
        result = ((result * 31) + ((this.created == null) ? 0 : this.created.hashCode()));
        result = ((result * 31) + ((this.pathEnc == null) ? 0 : this.pathEnc.hashCode()));
        result = ((result * 31) + ((this.modified == null) ? 0 : this.modified.hashCode()));
        result = ((result * 31) + ((this.accessed == null) ? 0 : this.accessed.hashCode()));
        result = ((result * 31) + super.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Directory) == false) {
            return false;
        }
        Directory rhs = ((Directory) other);
        return (((((((super.equals(rhs) && ((this.path == rhs.path) || ((this.path != null) && this.path.equals(rhs.path)))) && ((this.containsRefs == rhs.containsRefs) || ((this.containsRefs != null) && this.containsRefs.equals(rhs.containsRefs)))) && ((this.created == rhs.created) || ((this.created != null) && this.created.equals(rhs.created)))) && ((this.pathEnc == rhs.pathEnc) || ((this.pathEnc != null) && this.pathEnc.equals(rhs.pathEnc)))) && ((this.modified == rhs.modified) || ((this.modified != null) && this.modified.equals(rhs.modified))))) && ((this.accessed == rhs.accessed) || ((this.accessed != null) && this.accessed.equals(rhs.accessed))));
    }

}
