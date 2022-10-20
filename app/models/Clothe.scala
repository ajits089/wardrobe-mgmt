package models

/**
 * @author Ajit kumar
 */

import play.api.libs.json.{Format, Json}

case class Clothe(id: Long, name: String, category: String)

object Clothe {
  implicit val fmt: Format[Clothe] = Json.format[Clothe]
}



