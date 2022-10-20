package models

/**
 * @author Ajit kumar
 */

import play.api.libs.json.{Format, Json}

case class Outfit(id: Long, name: String, hasTag: String)

object Outfit {
  implicit val fmt: Format[Outfit] = Json.format[Outfit]
}

