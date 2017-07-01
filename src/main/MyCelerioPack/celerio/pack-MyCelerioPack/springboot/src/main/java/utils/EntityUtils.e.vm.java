$output.java("${configuration.rootPackage}.utils", "${entity.model.type}EntityUtils")##

$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##

public class ${entity.model.type}EntityUtils {

	private ${entity.model.type}EntityUtils() {
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

	public static ${configuration.rootPackage}.elasticsearch.model.${entity.model.type} convertToElasticsearch${entity.model.type} (${entity.model.type} ${entity.model.var}) {
		${configuration.rootPackage}.elasticsearch.model.${entity.model.type} elasticsearch${entity.model.type} = new ${configuration.rootPackage}.elasticsearch.model.${entity.model.type}();
		
#foreach ($attribute in $entity.nonCpkAttributes.list)
		elasticsearch${entity.model.type}.${attribute.setter}(${entity.model.var}.${attribute.getter}());
#end		
		
		return elasticsearch${entity.model.type};
	}
}
