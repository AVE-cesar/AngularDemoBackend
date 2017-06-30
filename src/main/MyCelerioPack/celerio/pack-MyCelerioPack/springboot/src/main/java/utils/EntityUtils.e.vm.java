$output.java("${configuration.rootPackage}.utils", "${entity.model.type}EntityUtils")##

$output.require("java.math.BigDecimal")##

$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##

public class ${entity.model.type}EntityUtils {

	private ${entity.model.type}EntityUtils() {
		super();
	}
	
	public static ${entity.model.type} createNew${entity.model.type}(String id) {
		${entity.model.type} ${entity.model.var} = new ${entity.model.type}();
// FIXME à terminer		
/*		
#foreach ($attribute in $entity.nonCpkAttributes.list)
		${entity.model.var}.${attribute.setter}("Toto");
#end		
*/
		return ${entity.model.var};
	}
	
	public static ${configuration.rootPackage}.elasticsearch.model.${entity.model.type} createNewElasticsearch${entity.model.type}(String id) {
		${configuration.rootPackage}.elasticsearch.model.${entity.model.type} ${entity.model.var} = new ${configuration.rootPackage}.elasticsearch.model.${entity.model.type}();
		
		
		return ${entity.model.var};
	}
	
	public static ${configuration.rootPackage}.elasticsearch.model.${entity.model.type} convertToElasticsearch${entity.model.type} (${entity.model.type} ${entity.model.var}) {
		${configuration.rootPackage}.elasticsearch.model.${entity.model.type} elasticsearch${entity.model.type} = new ${configuration.rootPackage}.elasticsearch.model.${entity.model.type}();
		
#foreach ($attribute in $entity.nonCpkAttributes.list)
		elasticsearch${entity.model.type}.${attribute.setter}(${entity.model.var}.${attribute.getter}());
#end		
		
		return elasticsearch${entity.model.type};
	}
}
