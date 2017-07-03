$output.java("${configuration.rootPackage}.utils", "Elasticsearch${entity.model.type}EntityUtils")##

$output.require("${configuration.rootPackage}.elasticsearch.model.${entity.model.type}")##

public class Elasticsearch${entity.model.type}EntityUtils {

	private Elasticsearch${entity.model.type}EntityUtils() {
		super();
	}

#foreach ($attribute in $entity.attributes.list)
	public static boolean compare${attribute.varUp} (${entity.model.type} ${entity.model.var}, ${entity.model.type} other) {
		if (${entity.model.var}.${attribute.getter}() == null) {
			if (other.${attribute.getter}() != null) {
				return false;
			}
		} else if (!${entity.model.var}.${attribute.getter}().equals(other.${attribute.getter}())) {
			return false;
		}

		return true;
	}
#end
}
