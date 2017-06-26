$output.java("${configuration.rootPackage}.elasticsearch.model", "${entity.model.type}")##

$output.require("org.springframework.data.elasticsearch.annotations.Document")##

/**
 * entity ${entity.model.type} for Elasticsearch.
 *
 */
@Document(indexName = "${entity.model.var.toLowerCase()}_index", type = "${entity.model.vars}")
public class ${entity.model.type} {
	
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() || $attribute.isSimplePk())
$output.require($attribute)##
    private $attribute.type $attribute.var;
#end
#end
	
	/**
	 * Default constructor.
	 */
	public ${entity.model.type}() {
		super();
	}
	
	public ${entity.model.type}(
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if ($velocityCount == $entity.nonCpkAttributes.list.size())
    $attribute.type $attribute.var
#else
    $attribute.type $attribute.var,
#end
#end
			) {
#foreach ($attribute in $entity.nonCpkAttributes.list)
	    this.$attribute.var = $attribute.var;
#end
	}

#foreach ($attribute in $entity.nonCpkAttributes.list)
public void ${attribute.setter}($attribute.type $attribute.var) {
this.$attribute.var = $attribute.var;
}
#end

	@Override
	public String toString() {
		return "${entity.model.type}{" + 
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if ($velocityCount == 1)
## first time, no starting comma
"$attribute.var='" + $attribute.var + '\'' +
#end
", $attribute.var='" + $attribute.var + '\'' +
#end
				'}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

#foreach ($attribute in $entity.nonCpkAttributes.list)
result = prime * result + (($attribute.var == null) ? 0 : ${attribute.var}.hashCode()); 
#end		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		${entity.model.type} other = (${entity.model.type}) obj;
		
		#foreach ($attribute in $entity.nonCpkAttributes.list)
		if ($attribute.var == null) {
			if (other.${attribute.var} != null) {
				return false;
			}
		} else if (!${attribute.var}.equals(other.${attribute.var})) {
			return false;
		}
		#end		
		
		return true;
	}
}
