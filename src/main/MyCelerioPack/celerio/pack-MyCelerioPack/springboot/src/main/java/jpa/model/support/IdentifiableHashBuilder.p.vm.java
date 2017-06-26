$output.java("${configuration.rootPackage}.jpa.model.support", "IdentifiableHashBuilder")##

$output.require("java.io.Serializable")##
$output.require("java.util.logging.Logger")
$output.require("com.jaxio.jpa.querybyexample.Identifiable")##

/**
 * The first time the {@link ${pound}hash(Logger, Identifiable)} is called, we check if the primary key is present or not.
 * <ul>
 * <li>If yes: we use it to get the hash</li>
 * <li>If no: we use a VMID during the entire life of this instance even if later on this instance is assigned a primary key.</li>
 * </ul>
 */
public class $output.currentClass implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object technicalId;

    public int hash(Logger log, Identifiable<?> identifiable) {
        if (technicalId == null) {
            if (identifiable.${identifiableProperty.iser}()) {
                technicalId = identifiable.${identifiableProperty.getter}();
            } else {
                technicalId = new java.rmi.dgc.VMID();
                log.warning("DEVELOPER: hashCode is not safe." //
                        + "If you encounter this message you should take the time to carefuly " //
                        + "review the equals/hashCode methods for: " + identifiable.getClass().getCanonicalName() //
                        + " You may consider using a business key.");
            }
        }
        return technicalId.hashCode();
    }
}
