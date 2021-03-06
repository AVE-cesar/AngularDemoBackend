$output.java("${configuration.rootPackage}.jpa.model", "${entity.model.type}")##

$output.require("com.google.common.base.MoreObjects")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityUtils")##

#if($entity.hasComment())
/**
 * $entity.comment
 */
#end
#if ($output.isAbstract())
## ==============================================
## /**
$output.createMetaModelTakeOver($entity.model,
"$output.headerComment##
package $entity.model.packageName;

import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(${entity.model.type}.class)
public class ${entity.model.type}_ extends ${entity.model.type}Base_ {
}
")
## */
## ==============================================
$output.dynamicAnnotation("javax.persistence.MappedSuperclass")
#end
#foreach ($annotation in $entity.jpa.annotations)
$annotation
#end
#foreach ($annotation in $entity.search.annotations)
$annotation
#end
#foreach ($annotation in $entity.custom.annotations)
$annotation
#end
#if($entity.isRoot())
$output.require("java.io.Serializable")##
public#if ($output.isAbstract()) abstract#{end} class ${output.currentClass}${entity.spaceAndExtendsStatement} implements Serializable {
#else
$output.require("${configuration.rootPackage}.jpa.model.${entity.parent.model.type}")##
public#if ($output.isAbstract()) abstract#{end} class $output.currentClass extends $entity.parent.model.type {
#end

	private static final long serialVersionUID = 1L;

#if ($entity.isRoot() && $entity.primaryKey.isComposite())

    // Composite primary key
    private $entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}();
#end
## --------------- Raw attributes (exception the one involved in XtoOneRelation)
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if ($velocityCount == 1)

    // Raw attributes
#end
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
$output.require("${configuration.rootPackage}.jpa.model.${manyToOne.to.type}")##
    private $manyToOne.to.type $manyToOne.to.var;
#end
## --------------- One to One
#foreach ($oneToOne in $entity.oneToOne.list)
#if ($velocityCount == 1)

    // One to one
#end
$output.require("${configuration.rootPackage}.jpa.model.${oneToOne.to.type}")##
    private $oneToOne.to.type $oneToOne.to.var;
#end
## --------------- One to Virtual One
#foreach ($oneToVirtualOne in $entity.oneToVirtualOne.list)
#if ($velocityCount == 1)

    // One to virtual one
#end
$output.require($entity.collectionType.fullType)##
$output.require($entity.collectionType.implementationFullType)##
$output.require("${configuration.rootPackage}.jpa.model.${oneToVirtualOne.to.type}")##
    private ${entity.collectionType.type}<$oneToVirtualOne.to.type> $oneToVirtualOne.to.vars = new ${entity.collectionType.implementationType}<>();
#end
## --------------- One to many
#foreach ($oneToMany in $entity.oneToMany.list)
#if ($velocityCount == 1)

    // One to many
#end
$output.require($entity.collectionType.fullType)##
$output.require($entity.collectionType.implementationFullType)##
$output.require("${configuration.rootPackage}.jpa.model.${$oneToMany.to.type}")##
    private ${entity.collectionType.type}<$oneToMany.to.type> $oneToMany.to.vars = new ${entity.collectionType.implementationType}<>();
#end
## --------------- Many to many
#foreach ($manyToMany in $entity.manyToMany.list)
#if ($velocityCount == 1)

    // Many to many
#end
$output.require($entity.collectionType.fullType)##
$output.require($entity.collectionType.implementationFullType)##
$output.require("${configuration.rootPackage}.jpa.model.${manyToMany.to.type}")##
    private ${entity.collectionType.type}<$manyToMany.to.type> $manyToMany.to.vars = new ${entity.collectionType.implementationType}<>();
#end

#if ($entity.isRoot() && $entity.primaryKey.isComposite())

    // -----------------------
    // Composite Primary Key
    // -----------------------

    /**
     * Returns the composite primary key.
     */
    @Override
    $output.dynamicAnnotation("javax.persistence.EmbeddedId")
#if($entity.entityConfig.hasTrueIndexed())
    $output.dynamicAnnotation("org.hibernate.search.annotations.DocumentId")
    @FieldBridge(impl = ${entity.primaryKey.type}Bridge.class)
$output.require("org.hibernate.search.annotations.FieldBridge")##
#end
    public $entity.primaryKey.type ${entity.primaryKey.getter}() {
        return $entity.primaryKey.var;
    }

    /**
     * Set the composite primary key.
     * @param $entity.root.primaryKey.var the composite primary key.
     */
    @Override
    public void ${entity.primaryKey.setter}($entity.primaryKey.type $entity.primaryKey.var) {
        this.$entity.primaryKey.var = $entity.primaryKey.var;
    }

    public ${output.currentRootClass} ${entity.primaryKey.with}($entity.primaryKey.type $entity.primaryKey.var) {
        ${entity.primaryKey.setter}($entity.primaryKey.var);
        return ${output.currentRootCast}this;
    }

    /**
     * Tells whether or not this instance has a non empty composite primary key set.
     * @return true if a non empty primary key is set, false otherwise
     */
    @Override
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public boolean ${entity.primaryKey.has}() {
        return ${entity.primaryKey.getter}() != null && ${entity.primaryKey.getter}().areFieldsSet();
    }
#end
#if ($entity.isAccount())
$output.require("$entity.collectionType.fullType")##
$output.require("$entity.collectionType.implementationFullType")##

    // -------------------------------
    // Role names support
    // -------------------------------

#if ($entity.accountAttributes.isRoleRelationSet())
#set ($roleRelation = $entity.accountAttributes.roleRelation)##
    /**
     * Returns the granted authorities for this user. You may override
     * this method to provide your own custom authorities.
     */
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public ${entity.collectionType.type}<String> getRoleNames() {
        ${entity.collectionType.type}<String> roleNames = new ${entity.collectionType.implementationType}<>();

        for ($roleRelation.to.type $roleRelation.to.var : ${roleRelation.to.getters}()) {
            roleNames.add(${roleRelation.to.var}.${roleRelation.toEntity.roleAttributes.roleName.getter}());
        }

        return roleNames;
    }
#else
    /**
     * Default implementation returns hard coded granted authorities for this account (i.e. "ROLE_USER" and "ROLE_ADMIN").
     * TODO: You should override this method to provide your own custom authorities using your own logic.
     * Or you can follow Celerio Account Table convention. Please refer to Celerio Documentation.
     */
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public ${entity.collectionType.type}<String> getRoleNames() {
        ${entity.collectionType.type}<String> roleNames = new ${entity.collectionType.implementationType}<>();
        if ("user".equalsIgnoreCase(${entity.accountAttributes.username.getter}())) {
            roleNames.add("ROLE_USER");
        } else if ("admin".equalsIgnoreCase(${entity.accountAttributes.username.getter}())) {
            roleNames.add("ROLE_USER");
            roleNames.add("ROLE_ADMIN");
        }

        log.warning("Returning hard coded role names. TODO: get the real role names");
        return roleNames;
    }
#end
#end
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() || $attribute.isSimplePk())
    // -- [${attribute.var}] ------------------------

#if($attribute.hasComment())$attribute.javadoc#end
#foreach ($annotation in $attribute.custom.annotations)
    $annotation
#end
#foreach ($annotation in $attribute.validation.annotations)
    $annotation
#end
#set ($generatedValueMissing = true)
#set ($isIdField = false)
#foreach ($annotation in $attribute.jpa.annotations)
	#if ($annotation.length() >= 15 && $annotation.substring(0,15) == "@GeneratedValue")
		#set ($generatedValueMissing = false)
	#end	
	#if ($annotation == "@Id")
		#set ($isIdField = true)
	#end
	$annotation
#end
## Tips ORACLE: auto generated id
#if ($isIdField == true && $generatedValueMissing == true)
	$output.require("javax.persistence.GeneratedValue")##
	$output.require("javax.persistence.GenerationType")##
	@GeneratedValue(strategy = GenerationType.AUTO)
#end
#foreach ($annotation in $attribute.formatter.annotations)
    $annotation
#end
#foreach ($annotation in $attribute.search.annotations)
    $annotation
#end
    public $attribute.type ${attribute.getter}() {
        return $attribute.var;
    }
#if ($attribute.isSetterAccessibilityPublic())

    public void ${attribute.setter}($attribute.type $attribute.var) {
        this.$attribute.var = $attribute.var;
    }
#else

    private void ${attribute.setter}($attribute.type $attribute.var) {
        this.$attribute.var = $attribute.var;
    }
#end
#end
#end
## --------------- Many to one
#foreach ($relation in $entity.manyToOne.list)
#if ($velocityCount == 1)

    // -----------------------------------------------------------------
    // Many to One support
    // -----------------------------------------------------------------
#end

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: $relation.toString()
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

#foreach ($annotation in $relation.validation.annotations)
    $annotation
#end
#foreach ($annotation in $relation.jpa.annotations)
    $annotation
#end
$output.require("org.hibernate.annotations.NotFound")##
$output.require("org.hibernate.annotations.NotFoundAction")##
	@NotFound(action = NotFoundAction.IGNORE)
    public $relation.to.type ${relation.to.getter}() {
        return $relation.to.var;
    }

    /**
     * Set the {@link #$relation.to.var} without adding this $relation.from.type instance on the passed {@link #${relation.to.var}}
#if($relation.hasInverse())
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by {@link $relation.to.type}
#end
     */
    public void ${relation.to.setter}($relation.to.type $relation.to.var) {
        this.$relation.to.var = $relation.to.var;
#if (!$relation.isIntermediate())        
#foreach ($attributePair in $relation.attributePairs)
#if ($attributePair.fromAttribute.isInCpk() && $attributePair.toAttribute.isInCpk())
         ${identifiableProperty.getter}().${attributePair.fromAttribute.setter}($relation.to.var != null ? ${relation.to.var}.${identifiableProperty.getter}().${attributePair.toAttribute.getter}() : null);
#end
#end
#end
    }
#end
##---------------- One to one
#foreach ($relation in $entity.oneToOne.list)
#if ($velocityCount == 1)

    // -----------------------------------------------------------------
    // One to one
    // -----------------------------------------------------------------
#end

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // #{if}($relation.isInverse())Inverse#{else}Owner#{end} side of one-to-one relation: $relation.toString()
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
## TODO : add @NotNull annotation
#foreach ($annotation in $relation.validation.annotations)
    $annotation
#end
#foreach ($annotation in $relation.jpa.annotations)
    $annotation
#end
$output.require("org.hibernate.annotations.NotFound")##
$output.require("org.hibernate.annotations.NotFoundAction")##
	@NotFound(action = NotFoundAction.IGNORE)
    public $relation.to.type ${relation.to.getter}() {
        return $relation.to.var;
    }

#if($relation.isInverse())
## Inverse ONE TO ONE SETTER (analog to a one to many)
    public void ${relation.to.setter}($relation.to.type $relation.to.var) {
        this.$relation.to.var = $relation.to.var;

        if (this.$relation.to.var != null) {
            this.${relation.to.var}.${relation.inverse.to.setter}(${output.currentRootCast}this);
        }
    }
#else
## FORWARD ONE TO ONE SETTER
    public void ${relation.to.setter}($relation.to.type $relation.to.var) {
        this.$relation.to.var = $relation.to.var;
#if (!$relation.isIntermediate())        
#foreach ($attributePair in $relation.attributePairs)
#if ($attributePair.fromAttribute.isInCpk() && $attributePair.toAttribute.isInCpk())
        ${identifiableProperty.getter}().${attributePair.fromAttribute.setter}($relation.to.var != null ? ${relation.to.var}.${identifiableProperty.getter}().${attributePair.toAttribute.getter}() : null);
#end
#end
#end
    }
#end
#end
##---------------- One to virtual one
#foreach ($relation in $entity.oneToVirtualOne.list)
#if ($velocityCount == 1)

    // -----------------------------------------------------------------
    // One to virtual one
    // -----------------------------------------------------------------
#end
    /**
     * Helper method to set the unique ${relation.to.var} pointing to this instance.
     * Preserve referential integrity by updating the passed {@link $relation.to.type}
     *
     * @param  $relation.to.var
     */
    public void ${relation.to.setter}($relation.to.type $relation.to.var) {
        // remove current value from distant bean
        int i = 0;
        for ($relation.to.type other : ${relation.to.getters}()) {
            other.${relation.from.setter}(null);
            if (++i > 1) {
                throw new IllegalStateException("virtual one to one contract broken!");
            }
        }

        if (i > 0) {
            // clear our ref to current distant bean
            ${relation.to.getters}().clear();
        }

        // add the passed bean
        if ($relation.to.var != null) {
            if (${relation.to.getters}().add($relation.to.var)) {
                ${relation.to.var}.${relation.from.setter}(${output.currentRootCast}this);
            }
        }
    }

    /**
     * Helper method to directly get the unique {@link #$relation.to.var} pointing to this instance.
     *
     * @return relation.to.var
     */
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public $relation.to.type ${relation.to.getter}() {
        for ($relation.to.type other : ${relation.to.getters}()) {
            return other;
        }
        return null;
    }

    /**
     * Get the $relation.to.type list which is supposed to contain at most one element (virtual one to one relation).
     * This method is not intended to be used directly. Use instead the helper method {@link ${pound}${relation.to.getter}()}
     *
     * @return the $relation.to.vars list
     */
#foreach ($annotation in $relation.jpa.annotations)
    $annotation
#end
    protected ${entity.collectionType.type}<$relation.to.type> ${relation.to.getters}() {
        return $relation.to.vars;
    }

    /**
     * Set the {@link #$relation.to.vars} list which is supposed to contain at most one element (virtual one to one relation).
     * This method is not intended to be used directly. Use instead the helper method {@link ${pound}${relation.to.setter}($relation.to.type)}
     * which preserve referential integrity at the object level and ease code readability.
     *
     * @param $relation.to.vars list
     */
    protected void ${relation.to.setters}(${entity.collectionType.type}<$relation.to.type> $relation.to.vars) {
        this.$relation.to.vars = $relation.to.vars;
    }
#end
## --------------- One to many
#foreach ($relation in $entity.oneToMany.list)
#if ($velocityCount == 1)

    // -----------------------------------------------------------------
    // One to Many support
    // -----------------------------------------------------------------
#end

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // one to many: $relation.fromEntity.model.var ==> $relation.to.vars
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

#foreach ($annotation in $relation.jpa.annotations)
    $annotation
#end
$output.require("org.hibernate.annotations.NotFound")##
$output.require("org.hibernate.annotations.NotFoundAction")##
	@NotFound(action = NotFoundAction.IGNORE)
    public ${entity.collectionType.type}<$relation.to.type> ${relation.to.getters}() {
        return $relation.to.vars;
    }

    /**
     * Set the {@link $relation.to.type} list.
     * It is recommended to use the helper method {@link ${pound}${relation.to.adder}($relation.to.type)} / {@link ${pound}${relation.to.remover}($relation.to.type)}
     * if you want to preserve referential integrity at the object level.
     *
     * @param $relation.to.vars the list to set
     */
    public void ${relation.to.setters}(${entity.collectionType.type}<$relation.to.type> $relation.to.vars) {
        this.$relation.to.vars = $relation.to.vars;
    }
    

    /**
     * Helper method to add the passed {@link $relation.to.type} to the {@link #$relation.to.vars} list
     * and set this $relation.from.var on the passed $relation.to.var to preserve referential
     * integrity at the object level.
     *
     * @param $relation.to.var the to add
     * @return true if the $relation.to.var could be added to the $relation.to.vars list, false otherwise
     */
    public boolean ${relation.to.adder}($relation.to.type $relation.to.var) {
        if (${relation.to.getters}().add($relation.to.var)) {
            ${relation.to.var}.${relation.from.setter}(${output.currentRootCast}this);
            return true;
        }
        return false;
    }

    /**
     * Helper method to remove the passed {@link $relation.to.type} from the {@link #$relation.to.vars} list and unset
     * this $relation.from.var from the passed $relation.to.var to preserve referential integrity at the object level.
     *
     * @param $relation.to.var the instance to remove
     * @return true if the $relation.to.var could be removed from the $relation.to.vars list, false otherwise
     */
    public boolean ${relation.to.remover}($relation.to.type $relation.to.var) {
        if (${relation.to.getters}().remove($relation.to.var)) {
            ${relation.to.var}.${relation.from.setter}(null);
            return true;
        }
        return false;
    }

#end
#foreach ($relation in $entity.manyToMany.list)
#if ($velocityCount == 1)

    // -----------------------------------------------------------------
    // Many to Many
    // -----------------------------------------------------------------
#end

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // many-to-many: $relation.fromEntity.model.var ==> $relation.to.vars
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /**
     * Returns the {@link #$relation.to.vars} list.
     */
#foreach ($annotation in $relation.jpa.annotations)
#if ("@ManyToMany" == $annotation)
	$output.require("javax.persistence.FetchType")##
	@ManyToMany(fetch = FetchType.EAGER)
#else	
    $annotation
#end    
#end
$output.require("org.hibernate.annotations.NotFound")##
$output.require("org.hibernate.annotations.NotFoundAction")##
	@NotFound(action = NotFoundAction.IGNORE)
    public ${entity.collectionType.type}<$relation.to.type> ${relation.to.getters}() {
        return $relation.to.vars;
    }

    /**
     * Set the {@link #$relation.to.vars} list.
#if ($relation.hasInverse())
     * <p>
     * It is recommended to use the helper method {@link ${pound}${relation.to.adder}($relation.to.type)} / {@link ${pound}${relation.to.remover}($relation.to.type)}
     * if you want to preserve referential integrity at the object level.
#end
     *
     * @param $relation.to.vars the list of $relation.to.type
     */
    public void ${relation.to.setters}(${entity.collectionType.type}<$relation.to.type> $relation.to.vars) {
        this.$relation.to.vars = $relation.to.vars;
    }

#if ($relation.hasInverse())
    /**
     * Helper method to add the passed {@link $relation.to.type} to the {@link #$relation.to.vars} list
     * and add this $relation.from.var to the passed ${relation.to.var}'s {@link #$relation.from.vars} list
     * to preserve referential integrity at the object level.
     */
    public boolean ${relation.to.adder}($relation.to.type $relation.to.var) {
        if (${relation.to.getters}().add(${relation.to.var})) {
            return ${relation.to.var}.${relation.from.getters}().add(${output.currentRootCast}this);
        }
        return false;
    }

    /**
     * Helper method to remove the passed {@link $relation.to.type} from the {@link #$relation.to.vars} list
     * and remove this $relation.from.var from the passed ${relation.to.var}'s {@link #$relation.from.vars} list.
     * to preserve referential integrity at the object level.
     */
    public boolean ${relation.to.remover}($relation.to.type $relation.to.var) {
        if (${relation.to.getters}().remove($relation.to.var)) {
            return ${relation.to.var}.${relation.from.getters}().remove(${output.currentRootCast}this);
        }
        return false;
    }

#else
    /**
     * Helper method to add the passed {@link $relation.to.type} to the {@link #$relation.to.vars} list.
     */
    public boolean ${relation.to.adder}($relation.to.type $relation.to.var) {
        return ${relation.to.getters}().add($relation.to.var);
    }

    /**
     * Helper method to remove the passed {@link $relation.to.type} from the {@link #$relation.to.vars} list.
     */
    public boolean ${relation.to.remover}($relation.to.type $relation.to.var) {
        return ${relation.to.getters}().remove($relation.to.var);
    }
#end

    /**
     * Helper method to determine if the passed {@link $relation.to.type} is present in the {@link #$relation.to.vars} list.
     */
    public boolean ${relation.to.contains}($relation.to.type $relation.to.var) {
        return ${relation.to.getters}() != null && ${relation.to.getters}().contains($relation.to.var);
    }
#end


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
			    boolean result = ${entity.model.type}EntityUtils.compare${attribute.varUp}(this, other);
		#else	    
			    result = result && ${entity.model.type}EntityUtils.compare${attribute.varUp}(this, other);
		#end	
	#end
#end

// Many to one relations
## generates a comparison method for all Many to one relations
#foreach ($manyToOne in $entity.manyToOne.list)
	result = result && ${entity.model.type}EntityUtils.compare${manyToOne.to.varUp}(this, other);
#end

	    return result;
	}

#if($entity.useBusinessKey())

    private volatile int previousHashCode = 0;

    @Override
    public int hashCode() {
        int hashCode = Objects.hashCode(
#foreach($bka in $entity.businessKey)
#if ($bka.isJavaUtilDate())
            DateUtil.toSecondPrecisionDate(${bka.getter}())$project.print($velocityHasNext,", //", ");")
#elseif($bka.isSimpleFk())
## can happen if user configured it => risky, but it is his choice
            ${bka.xToOneRelation.to.getter}().${identifiableProperty.getter}()$project.print($velocityHasNext,", //", ");")
#else
            ${bka.getter}()$project.print($velocityHasNext,", //", ");")
#end
#end

        if (previousHashCode != 0 && previousHashCode != hashCode) {
            log.warning("DEVELOPER: hashCode has changed!." //
                    + "If you encounter this message you should take the time to carefuly review equals/hashCode for: " //
                    + getClass().getCanonicalName());
        }

        previousHashCode = hashCode;
        return hashCode;
    }

#elseif($entity.isRoot())
$output.require("java.util.Objects")##

    @Override
    public int hashCode() {
        return Objects.hash( 
        		$entity.extended.getAttributesList()
        	);
    }

#end
#foreach ($bundle in $entity.attributeBundles)
#if ($velocityCount == 1)
    // -----------------------
    // Localization shortcuts
    // -----------------------
$output.require("com.jaxio.jpa.querybyexample.LocaleHolder")##
#end

    /**
     * Locale aware getter for fields whose column's name starts
     * with $bundle.base and ends with _xx where xx is a language code.
     */
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public $bundle.type ${bundle.getter}() {
        String language = LocaleHolder.getLocale().getLanguage().toLowerCase();
#foreach($attribute in $bundle.attributes)
        if (language.equals("$attribute.columnNameLanguage")) {
            return ${attribute.getter}();
        }
#end
        throw new IllegalStateException("Could not find the proper getter for current locale's language " + language);
    }

#end

    /**
     * Construct a readable string representation for this ${output.currentClass} instance.
     * @see java.lang.Object${pound}toString()
     */
    @Override
    public String toString() {
        return #if ($entity.hasParent())super.toString() + #{end}MoreObjects.toStringHelper(this) //
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() || $attribute.isSimplePk())
	## avoid the password to be visible
	#if(!$attribute.isPassword())
         .add("${attribute.var}", ${attribute.getter}()) //
    #end       
#end
#end
## one to one relation
#foreach ($relation in $entity.oneToOne.list)
	.add("$relation.to.var", ${relation.to.getter}() != null ? ${relation.to.getter}().toString() : "null") //
#end
## --------------- Many to one
#foreach ($relation in $entity.manyToOne.list)
	.add("$relation.to.var", ${relation.to.getter}() != null ? ${relation.to.getter}().toString() : "null") //
#end
            .toString();
    }
#if ($configuration.has($COPYABLE))

    /**
     * Return a copy of the current object
     */
    @Override
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public $entity.model.type copy() {
        ${entity.model.type} ${entity.model.var} = new ${entity.model.type}();
        copyTo(${entity.model.var});
        return ${entity.model.var};
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    $output.dynamicAnnotation("javax.persistence.Transient")
    $output.dynamicAnnotation("javax.xml.bind.annotation.XmlTransient")
    public void copyTo($entity.model.type $entity.model.var) {
#if ($entity.isRoot() && $entity.primaryKey.isComposite())
		if (${entity.primaryKey.getter}() != null) {
			${entity.model.var}.${entity.primaryKey.setter}(${entity.primaryKey.getter}().copy());
		}
#end
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if ($attribute.isSetterAccessibilityPublic())
        ${entity.model.var}.${attribute.setter}(${attribute.getter}());
#end
#end
#foreach ($xToOne in $entity.xToOne.list)
        if (${xToOne.to.var} != null) {
            ${entity.model.var}.${xToOne.to.setter}(new ${xToOne.to.type}().${entity.primaryKey.with}(${xToOne.to.var}.${identifiableProperty.getter}()));
        }
#end
    }
#end
#if($entity.auditEntityAttributes.hasCreationAttributes())
$output.require($Audit, "AuditContextHolder")##

    $output.dynamicAnnotation("javax.persistence.PrePersist")
    protected void prePersist() {
        if (AuditContextHolder.audit()) {
#if($entity.auditEntityAttributes.isCreationAuthorSet())
            ${entity.auditEntityAttributes.creationAuthor.setter}(AuditContextHolder.username());
#end
#if($entity.auditEntityAttributes.isCreationDateSet())
            ${entity.auditEntityAttributes.creationDate.setter}(new ${entity.auditEntityAttributes.creationDate.type}());
#end
        }
    }
#end
#if($entity.auditEntityAttributes.hasLastModificationAttributes())
$output.require($Audit, "AuditContextHolder")##

    $output.dynamicAnnotation("javax.persistence.PreUpdate")
    protected void preUpdate() {
        if (AuditContextHolder.audit()) {
#if($entity.auditEntityAttributes.isLastModificationAuthorSet())
            ${entity.auditEntityAttributes.lastModificationAuthor.setter}(AuditContextHolder.username());
#end
#if($entity.auditEntityAttributes.isLastModificationDateSet())
            ${entity.auditEntityAttributes.lastModificationDate.setter}(new ${entity.auditEntityAttributes.lastModificationDate.type}());
#end
        }
    }
#end
}