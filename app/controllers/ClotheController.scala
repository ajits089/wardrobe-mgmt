package controllers

import com.github.tototoshi.csv.CSVReader
import models.{ClothRepository, Clothe, OutFitRepository}
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ClotheController @Inject()(clothRepo: ClothRepository, outFitRepo: OutFitRepository, controllerComponents: MessagesControllerComponents)
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(controllerComponents) {


  /**
   * The search Clothe action
   *
   * This is asynchronous, since we are invoking the asynchronous methods on ClotheRepository.
   */

  def search(name: String) = Action.async(parse.empty) { implicit req =>
    // searches with this name in the clothes table and returns cloth with the outfits
    //TODO Why to calculate this early --> A case what if the cloth table does not hold any entry for the cloth name
    // Need to check how this can be called internally from the repo or something better!
    val outfits = outFitRepo.list
    clothRepo.search(name, outfits).map { clothesWithOutFits => Ok(Json.toJson(clothesWithOutFits)) }
  }

  /**
   * The add Clothe action.
   *
   * This is asynchronous, since we're invoking the asynchronous methods on ClotheRepository.
   */
  def addClothe(name: String, category: String) = Action.async { implicit req =>
    //Adds a single entry to the clothes table and returns back the added entry
    clothRepo.create(name, category).map { clothe =>
      Ok(Json.toJson(clothe))
    }
  }

  /**
   * The upload Clothe action.
   * This takes csv file as an inp and stores the nonEmpty values to clothes table
   * This is asynchronous, since we're invoking the asynchronous methods on ClotheRepository.
   */

  def uploadAndAddClothes = Action.async(parse.multipartFormData) { request =>
    request.body
      .file("clothes")
      .map { csv =>
        //Getting the data from the client
        val clientSource = Source.fromFile(csv.ref.toFile)
        //reading the data with the help of csv reader
        val reader = CSVReader.open(clientSource)
        // getting all the rows of csv in one container
        val lines = reader.all() // This is list of list of strings
        //closing the source
        clientSource.close()
        //sending csv entries to services for db create ops
        val fromDB: Future[List[Clothe]] = clothRepo.addClothesFromCsv(lines.drop(1))
        //returning the result
        fromDB.map(clothes => Ok(Json.toJson(clothes)))
        // If no entries were made return the empty json
      }.getOrElse(Future(Ok(Json.toJson("[]")))) //TODO Can this be improved?
  }

}
