$output.javaTest("${configuration.rootPackage}.utils", "${entity.model.type}EntityTestUtils")##

$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require("io.github.benas.randombeans.EnhancedRandomBuilder")##
$output.require("io.github.benas.randombeans.api.EnhancedRandom")##

public class ${entity.model.type}EntityTestUtils {

	private ${entity.model.type}EntityTestUtils() {
		super();
	}
	
	public static ${entity.model.type} createNew${entity.model.type}() {
		EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
    		return enhancedRandom.nextObject(${entity.model.type}.class);
	}
	
	public static ${entity.model.type} createNew${entity.model.type}($entity.primaryKey.type id) {
		${entity.model.type} ${entity.model.var} = new ${entity.model.type}();
		
#foreach ($attribute in $entity.allAttributes.list)
#if ($attribute.isInPk())
		// pk
		${entity.model.var}.${attribute.setter}(id);
#else
#if ("Integer" == $attribute.type)	
		${entity.model.var}.${attribute.setter}(new Integer(1));
#elseif ("BigDecimal" == $attribute.type)
$output.require("java.math.BigDecimal")##
		${entity.model.var}.${attribute.setter}(new BigDecimal(1));		
#elseif ("Date" == $attribute.type)
$output.require("java.util.Date")##
		${entity.model.var}.${attribute.setter}(new Date());
#elseif ("Double" == $attribute.type)
		${entity.model.var}.${attribute.setter}(new Double(1));
#elseif ("String" == $attribute.type)
		${entity.model.var}.${attribute.setter}("Todo");
#else
		// FIXME: $attribute.type	not implemented !
		${entity.model.var}.${attribute.setter}(null);
#end
#end		
#end		

		return ${entity.model.var};
	}
	
	public static ${configuration.rootPackage}.elasticsearch.model.${entity.model.type} createNewElasticsearch${entity.model.type}(String id) {
		${configuration.rootPackage}.elasticsearch.model.${entity.model.type} ${entity.model.var} = new ${configuration.rootPackage}.elasticsearch.model.${entity.model.type}();
		
		
		return ${entity.model.var};
	}
}
