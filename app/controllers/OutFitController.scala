package controllers

/**
 * @author Ajit kumar
 */

import models.OutFitRepository
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class OutFitController @Inject()(outFitRepo: OutFitRepository, controllerComponents: MessagesControllerComponents)
                                (implicit ec: ExecutionContext) extends MessagesAbstractController(controllerComponents) {
  /**
   * The add OutFit action.
   *
   * This is asynchronous, since we're invoking the asynchronous methods on OutfitRepository.
   */
  def addOutFit(name: String, tag: String) = Action.async { implicit req =>
    //Adds a single entry to the Outfits table and returns back the added entry
    outFitRepo.create(name, tag).map { outfit =>
      Ok(Json.toJson(outfit))
    }
  }
}
