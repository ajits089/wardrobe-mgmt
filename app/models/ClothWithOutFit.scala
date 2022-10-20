package models

/**
 * @author Ajit kumar
 */

import play.api.libs.json.{Format, Json}

case class ClothWithOutFit(clothName: String, category: Seq[String], outfits: Seq[String])

object ClothWithOutFit {
  implicit val fmt: Format[ClothWithOutFit] = Json.format[ClothWithOutFit]
}
