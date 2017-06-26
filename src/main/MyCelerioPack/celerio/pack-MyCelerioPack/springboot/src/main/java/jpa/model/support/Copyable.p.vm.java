$output.generateIf($COPYABLE)##
$output.java("${configuration.rootPackage}.jpa.model.support", "Copyable")##

public interface $output.currentClass<T> {
    /**
     * Return a copy of the current object
     */
    T copy();

    /**
     * Copy the current properties to the given object
     */
    void copyTo(T t);
}