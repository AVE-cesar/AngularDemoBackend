$output.java("${configuration.rootPackage}.jpa.repository", "${entity.model.type}JpaRepository")##

#if ($entity.hasUniqueBigIntegerAttribute())
$output.require("java.math.BigInteger")##
#end
#if ($entity.hasUniqueDateAttribute() || $entity.root.hasDatePk())
$output.require("java.util.Date")##
#end
$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require($entity.root.primaryKey)##
#foreach ($enumAttribute in $entity.uniqueEnumAttributes.list)
$output.require($enumAttribute)##
#end
$output.require("org.springframework.data.jpa.repository.*")##

/**
 * $entity repository.
 * 
 * @author generated by Celerio
 *
 */
public interface $output.currentClass extends JpaRepository<$entity.model.type, $entity.root.primaryKey.type> {

## --------------- One to One
#foreach ($oneToOne in $entity.oneToOne.list)
$output.require("java.util.List")##
$output.require("${configuration.rootPackage}.jpa.model.$oneToOne.to.type")##
	/**
	 * Find by $oneToOne.to.varUp (One To One relation).
	 */
	List<$entity.model.type> findBy${oneToOne.to.varUp}($oneToOne.to.type $oneToOne.to.var);
	
#end	
	
## --------------- Many to One
#foreach ($manyToOne in $entity.manyToOne.list)
$output.require("java.util.List")##
$output.require("${configuration.rootPackage}.jpa.model.$manyToOne.to.type")##
	/**
	 * Find by $manyToOne.to.varUp (Many To One relation).
	 */
	List<$entity.model.type> findBy${manyToOne.to.varUp}($manyToOne.to.type $manyToOne.to.var);
	
#end

#if ($entity.model.type == "Book")
$output.require("org.springframework.data.repository.query.Param")##
$output.require("java.math.BigDecimal")##
$output.require("java.util.List")##
	@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:title) OR LOWER(b.description) LIKE LOWER(:description) OR b.price = :price")
	public List<Book> search(@Param("title") String title, @Param("description") String description, @Param("price") BigDecimal price);
#end

## dedicated method for system entities
#if ($entity.model.type == "AppParameter")
    /**
     * Find by domain and key.
     */
	// FIXME this code is hard coded !
    AppParameter findByDomainAndKey(String domain, String key);	
#end
#if ($entity.model.type == "AppUser")
    /**
     * Find by login.
     */
	// Needed for authentication mechanism.
	public AppUser findByLogin(String login);
#end
}