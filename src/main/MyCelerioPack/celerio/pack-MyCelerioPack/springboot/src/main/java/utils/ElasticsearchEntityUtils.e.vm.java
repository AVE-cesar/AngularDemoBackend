$output.java("${configuration.rootPackage}.utils", "Elasticsearch${entity.model.type}EntityUtils")##

$output.require("${configuration.rootPackage}.elasticsearch.model.${entity.model.type}")##

public class Elasticsearch${entity.model.type}EntityUtils {

	private Elasticsearch${entity.model.type}EntityUtils() {
		super();
	}

	## generates a comparison method for each simple attribute
	#foreach ($attribute in $entity.nonCpkAttributes.list)
		#if(!$attribute.isInFk())
			/**
		 * Compares the attribute ${attribute.var} for two entity of type: ${entity.model.type}.
		 * 
		 * @return true if the two entity's attribute are equals; otherwise false.
		 */
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
	#end

	## generates a comparison method for all Many to one relations
	#foreach ($manyToOne in $entity.manyToOne.list)
	public static boolean compare${manyToOne.to.varUp} (${entity.model.type} ${entity.model.var}, ${entity.model.type} other) {
		if (${entity.model.var}.${manyToOne.to.getter}() == null) {
			if (other.${manyToOne.to.getter}() != null) {
				return false;
			}
		} else if (!${entity.model.var}.${manyToOne.to.getter}().equals(other.${manyToOne.to.getter}())) {
			return false;
		}

		return true;
	}
	#end
}
