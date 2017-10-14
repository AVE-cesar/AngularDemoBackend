$output.java("${configuration.rootPackage}.utils", "${entity.model.type}EntityUtils")##

$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##

public class ${entity.model.type}EntityUtils {

	private ${entity.model.type}EntityUtils() {
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

## generates a method to convert an entity from the JPA model to the ElasticSearch model
	/**
	 * Converts an entity of type ${entity.model.type} from the JPA model to the ElasticSearch one.
	 */
	public static ${configuration.rootPackage}.elasticsearch.model.${entity.model.type} convertToElasticsearch${entity.model.type} (${entity.model.type} ${entity.model.var}) {
		${configuration.rootPackage}.elasticsearch.model.${entity.model.type} elasticsearch${entity.model.type} = new ${configuration.rootPackage}.elasticsearch.model.${entity.model.type}();
		
#foreach ($attribute in $entity.nonCpkAttributes.list)
	#if(!$attribute.isInFk())
		elasticsearch${entity.model.type}.${attribute.setter}(${entity.model.var}.${attribute.getter}());
	#end	
#end		

	// Many to one relations
#foreach ($manyToOne in $entity.manyToOne.list)
	elasticsearch${entity.model.type}.${manyToOne.to.setter}(${manyToOne.to.type}EntityUtils.convertToElasticsearch${manyToOne.to.type}(${entity.model.var}.${manyToOne.to.getter}()));
#end

		return elasticsearch${entity.model.type};
	}
}
