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
	
	// simple attributes
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() || $attribute.isSimplePk())
$output.require($attribute)##
    private $attribute.type $attribute.var;
#end
#end

## --------------- Many to One
#foreach ($manyToOne in $entity.manyToOne.list)
#if ($velocityCount == 1)

    // Many to one
#end
$output.require("${configuration.rootPackage}.elasticsearch.model.${manyToOne.to.type}")##
    private $manyToOne.to.type $manyToOne.to.var;
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
	public ${entity.model.type}($entity.extended.getAttributesListJavaStyle()) {
#foreach ($attribute in $entity.nonCpkAttributes.list)
	#if(!$attribute.isInFk())
	    this.$attribute.var = $attribute.var;
	#end
#end
// Many to one relations
#foreach ($manyToOne in $entity.manyToOne.list)
	this.${manyToOne.to.var} = ${manyToOne.to.var};
#end
	}

#foreach ($attribute in $entity.nonCpkAttributes.list)
	#if(!$attribute.isInFk())
	/**
	 * Getter for $attribute.var.
	 */
public $attribute.type ${attribute.getter}() {
	return this.$attribute.var;
}
	#end
#end

#foreach ($attribute in $entity.nonCpkAttributes.list)
	#if(!$attribute.isInFk())
	/**
	 * Setter for $attribute.var.
	 */
public void ${attribute.setter}($attribute.type $attribute.var) {
this.$attribute.var = $attribute.var;
}
	#end
#end

## --------------- Many to one: getters and setters
#foreach ($relation in $entity.manyToOne.list)
	/**
	 * Getter for $relation.to.var (Many to one attribute).
	 */
	public $relation.to.type ${relation.to.getter}() {
		return $relation.to.var;
	}
	
	/**
	 * setter for $relation.to.var (Many to one attribute).
	 */
	public void ${relation.to.setter}($relation.to.type $relation.to.var) {
        this.$relation.to.var = $relation.to.var;
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
		final int PRIME = 31;
		int result = 1;

## simple attribute		
#foreach ($attribute in $entity.nonCpkAttributes.list)
	#if(!$attribute.isInFk())
		result = PRIME * result + (($attribute.var == null) ? 0 : ${attribute.var}.hashCode());
	#end
#end	

## Many to one relations
#foreach ($manyToOne in $entity.manyToOne.list)
		result = PRIME * result + (($manyToOne.to.var == null) ? 0 : ${manyToOne.to.var}.hashCode());
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
		#if(!$attribute.isInFk())
			#if ($velocityCount == 1)
				    boolean result = Elasticsearch${entity.model.type}EntityUtils.compare${attribute.varUp}(this, other);
			#else	    
				    result = result && Elasticsearch${entity.model.type}EntityUtils.compare${attribute.varUp}(this, other);
			#end	
		#end
	#end

	// Many to one relations
	## generates a comparison method for all Many to one relations
	#foreach ($manyToOne in $entity.manyToOne.list)
		result = result && Elasticsearch${entity.model.type}EntityUtils.compare${manyToOne.to.varUp}(this, other);
	#end

			    return result;
	}
}
