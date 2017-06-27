$output.java("${configuration.rootPackage}.jpa.model", "${entity.model.type}_")##
##------------------------------------------------------------------------
## IMPORTANT: This template must be executed after the Entity.e.vm.java one
## because the Entity.e.vm.java also create the metamodel take over if needed.
## By executing this template after Entity.e.vm.java, celerio has the opportunity to
## create the XxxBase_ metamodel instead of the Xxx_ metamodel
##------------------------------------------------------------------------

$output.require("javax.persistence.metamodel.SingularAttribute")##
$output.require("javax.persistence.metamodel.StaticMetamodel")##
$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##

@StaticMetamodel(${output.currentClassWithout_}.class)
#if($entity.isRoot())
public abstract class $output.currentClass {
#else
$output.require("${entity.parent.model.fullType}_")##
public abstract class $output.currentClass extends ${entity.parent.model.type}_ {
#end
#if ($entity.isRoot() && $entity.primaryKey.isComposite())
    // Composite primary key
    public static volatile SingularAttribute<$entity.model.type, $entity.primaryKey.type> $entity.primaryKey.var;
#end
## --------------- Raw attributes (exception the one involved in XtoOneRelation)
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if ($velocityCount == 1)

    // Raw attributes
#end
#if(!$attribute.isInFk() || $attribute.isSimplePk())
$output.require($attribute)##
    public static volatile SingularAttribute<$entity.model.type, $attribute.type> $attribute.var;
#end
#end
##----------------------
#foreach ($manyToOne in $entity.manyToOne.list)
#if ($velocityCount == 1)

    // Many to one
#end
$output.require("${configuration.rootPackage}.jpa.model.${manyToOne.to.type}")##    
    public static volatile SingularAttribute<$entity.model.type, $manyToOne.to.type> $manyToOne.to.var;
#end
#foreach ($oneToOne in $entity.oneToOne.list)
#if ($velocityCount == 1)

    // One to one
#end
$output.require("${configuration.rootPackage}.jpa.model.${oneToOne.to.type}")##    
    public static volatile SingularAttribute<$entity.model.type, $oneToOne.to.type> $oneToOne.to.var;
#end
#foreach ($oneToVirtualOne in $entity.oneToVirtualOne.list)
#if ($velocityCount == 1)

    // One to virtual one
$output.require("$entity.collectionType.metaModelFullType")##    
#end
$output.require("${configuration.rootPackage}.jpa.model.${oneToVirtualOne.to.type}")##    
    public static volatile ${entity.collectionType.metaModelType}<$entity.model.type, $oneToVirtualOne.to.type> $oneToVirtualOne.to.vars;
#end
#foreach ($oneToMany in $entity.oneToMany.list)
#if ($velocityCount == 1)

    // One to many
$output.require("$entity.collectionType.metaModelFullType")##
#end
$output.require("${configuration.rootPackage}.jpa.model.${oneToMany.to.type}")##
    public static volatile ${entity.collectionType.metaModelType}<$entity.model.type, $oneToMany.to.type> $oneToMany.to.vars;
#end
#foreach ($manyToMany in $entity.manyToMany.list)
#if ($velocityCount == 1)

    // Many to many
$output.require("$entity.collectionType.metaModelFullType")##
#end
$output.require("${configuration.rootPackage}.jpa.model.${manyToMany.to.type}")##
    public static volatile ${entity.collectionType.metaModelType}<$entity.model.type, $manyToMany.to.type> $manyToMany.to.vars;
#end
}