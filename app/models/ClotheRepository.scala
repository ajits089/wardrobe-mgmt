package models

/**
 * @author Ajit kumar
 */

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClothRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  // We want the JdbcProfile for this provider
  protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.

  import dbConfig._
  import profile.api._

  class ClotheTable(tag: Tag) extends Table[Clothe](tag, "clothes") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def name = column[String]("name")

    /** The category column */
    def category = column[String]("category")

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Clothe object.
     *
     * In this case, we are simply passing the id, name and category parameters to the Clothe case classes
     * apply and unapply methods.
     */
    def * = (id, name, category) <> ((Clothe.apply _).tupled, Clothe.unapply)
  }

  /**
   * The starting point for all queries on the people table.
   */
  private val clothes: TableQuery[ClotheTable] = TableQuery[ClotheTable]

  /**
   * Create a Clothe with the given name and category.
   *
   * This is an asynchronous operation, it will return a future of the created clothe, which can be used to obtain the
   * id for that clothe.
   */
  def create(name: String, category: String): Future[Clothe] = db.run {
    // We create a projection of just the name and category columns, since we're not inserting a value for the id column
    (clothes.map(p => (p.name, p.category))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning clothes.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((createdClothe, id) => Clothe(id, createdClothe._1, createdClothe._2))
      // And finally, insert the clothe into the database
      ) += (name.trim, category.trim)
  }

  /**
   * searches a cloth by it's name and returns the cloths present in the cloth table
   * and also adds outfits where this cloth is tagged
   */

  def search(name: String, outfits: Future[Seq[Outfit]]): Future[ClothWithOutFit] = {
    get(name).flatMap { matchedClothes =>
      val ofsWithClotheName: Future[Seq[Outfit]] = outfits.map(ofs => ofs.filter(of => of.hasTag == name))
      val categories: Future[ClothWithOutFit] =
        ofsWithClotheName.map(ofs => ClothWithOutFit(name, matchedClothes.map(_.category).distinct, ofs.map(_.name).distinct))
      categories
    }
  }

  /**
   * Create a Clothes with the list of list of given names and categories.
   *
   * This is an asynchronous operation, it will return a future of list of the created clothes, which can be used to send
   * along with the response body.
   */

  def addClothesFromCsv(entries: List[List[String]]): Future[List[Clothe]] =
  //TODO Remove the warning by adding a condition which filters list for having only two elements i.e, name, category
  //It would fail on the following inputs: List(_), Nil
    Future.sequence(entries.filter(_.nonEmpty).map { case List(name, cat) => create(name, cat) })

  /**
   * Get a Clothe with the given name.
   *
   * This is an asynchronous operation, it will return a future of clothes with same name.
   */
  def get(name: String): Future[Seq[Clothe]] = db.run {
    clothes.filter(_.name === name).distinct.result
  }

  /**
   * List all the clothes in the database.
   */
  def list: Future[Seq[Clothe]] = db.run {
    clothes.result
  }

}
