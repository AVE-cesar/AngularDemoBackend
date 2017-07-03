$output.java("${configuration.rootPackage}.elasticsearch.model", "${entity.model.type}")##

$output.require("com.google.common.base.MoreObjects")##
$output.require("org.springframework.data.elasticsearch.annotations.Document")##
$output.require("${configuration.rootPackage}.utils.Elasticsearch${entity.model.type}EntityUtils")##

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
	
	/**
	 * Constructor with all attributes.
	 */
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
	/**
	 * Getter for $attribute.var.
	 */
public $attribute.type ${attribute.getter}() {
	return this.$attribute.var;
}
#end

#foreach ($attribute in $entity.nonCpkAttributes.list)
	/**
	 * Setter for $attribute.var.
	 */
public void ${attribute.setter}($attribute.type $attribute.var) {
this.$attribute.var = $attribute.var;
}
#end

	@Override
	public String toString() {
    	return #if ($entity.hasParent())super.toString() + #{end}MoreObjects.toStringHelper(this) //
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() || $attribute.isSimplePk())
#if(!$attribute.isPassword())
     .add("${attribute.var}", ${attribute.var}) //
#end       
#end
#end
        .toString();
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
		#if ($velocityCount == 1)
			    boolean result = Elasticsearch${entity.model.type}EntityUtils.compare${attribute.varUp}(this, other);
		#else	    
			    result = result && Elasticsearch${entity.model.type}EntityUtils.compare${attribute.varUp}(this, other);
		#end	    
		#end

			    return result;
	}
}
