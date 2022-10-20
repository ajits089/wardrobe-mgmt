package models

/**
 * @author Ajit kumar
 */

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OutFitRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  // We want the JdbcProfile for this provider
  protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.

  import dbConfig._
  import profile.api._

  class OutFitTable(tag: Tag) extends Table[Outfit](tag, "outfits") {
    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def name = column[String]("name")

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the outfit object.
     *
     * In this case, we are simply passing the id, name and hasTag parameters to the outfit case classes
     * apply and unapply methods.
     */
    def hasTag = column[String]("outfit_tag")

    def * = (id, name, hasTag) <> ((Outfit.apply _).tupled, Outfit.unapply)
  }

  private val outfits = TableQuery[OutFitTable]

  /**
   * Create an outfit with the given name and tag.
   *
   * This is an asynchronous operation, it will return a future of the created outfit, which can be used to obtain the
   * id for that outfit.
   */
  def create(name: String, hasTag: String): Future[Outfit] = db.run {
    // We create a projection of just the name and category columns, since we're not inserting a value for the id column
    (outfits.map(p => (p.name, p.hasTag))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning outfits.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((createdOutFits, id) => Outfit(id, createdOutFits._1, createdOutFits._2))
      // And finally, insert the outfit into the database
      ) += (name.trim, hasTag.trim)
  }

  /**
   * Get a Outfit with the given name.
   *
   * This is an asynchronous operation, it will return a future of outfits with same name.
   */
  def get(name: String): Future[Seq[Outfit]] = db.run {
    outfits.filter(_.name === name).distinct.result
  }

  /**
   * List all the outfits in the database.
   */
  def list: Future[Seq[Outfit]] = db.run {
    outfits.result
  }

}
